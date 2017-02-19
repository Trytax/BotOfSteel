package com.omega.audio.grabber.youtubedl;

import com.omega.audio.AudioPlayerManager;
import com.omega.audio.grabber.AudioGrabber;
import com.omega.audio.grabber.GrabberException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.util.audio.providers.FileProvider;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeDLExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(YoutubeDLAudioGrabber.class);

    private static final Pattern PLAYLIST_PATTERN = Pattern.compile("\\[[\\w\\s]+:playlist]");
    private static final Pattern PLAYLIST_PROGRESS_PATTERN = Pattern.compile("\\[download] Downloading video (\\d+) of (\\d+)");
    private static final Pattern DESTINATION_PATTERN = Pattern.compile("\\[(?:ffmpeg|avconv)] Destination: (.*)");
    private static final Pattern CONVERSION_FINISHED_PATTERN = Pattern.compile("Deleting original file (.*)");

    private final String source;
    private final AudioGrabber.Listener listener;

    private boolean playlist;
    private String destination;

    private ExecutorService executor;

    public YoutubeDLExecutor(String source, AudioGrabber.Listener listener) {
        this.source = source;
        this.listener = listener;
    }

    public List<AudioPlayerManager.Track> grab() throws GrabberException {
        List<String> cmd = new ArrayList<>();
        cmd.add("youtube-dl");
        cmd.add("--no-check-certificate");
        cmd.add("--restrict-filenames");
        cmd.add("-x");
        cmd.add("-f");
        cmd.add("\"worstaudio[abr>=96]/[height>=720]\"");
        cmd.add("--audio-format");
        cmd.add("mp3");
        cmd.add("--download-archive");
        cmd.add("\"D:/windows/Downloads/videos/archive.txt\"");

        // Debug only
        cmd.add("-v");
        // cmd.add("-s");
        cmd.add("--max-downloads");
        cmd.add("3");

        cmd.add("-o");
        cmd.add("\"D:/windows/Downloads/videos/%(title)s.%(ext)s\""); // TODO : Audio download directory in configuration
        cmd.add(source);


        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        Process process;
        try {
            process = processBuilder.start();
            listener.onStart();
        } catch (IOException e) {
            throw new GrabberException("Unable to start youtube-dl in command line", e);
        }


        final List<AudioPlayerManager.Track> tracks = new ArrayList<>();

        new StreamLineReader(process.getInputStream()) {
            @Override
            protected void onLine(String line) {
                LOGGER.debug(line);
                if (PLAYLIST_PATTERN.matcher(line).find()) {
                    playlist = true;
                }

                if (playlist) {
                    Matcher progressMatcher = PLAYLIST_PROGRESS_PATTERN.matcher(line);
                    if (progressMatcher.find()) {
                        int current = Integer.valueOf(progressMatcher.group(1));
                        int total = Integer.valueOf(progressMatcher.group(2));

                        listener.onProgress((float) current / total, current, total);
                        return;
                    }
                } else {

                }

                Matcher destinationMatcher = DESTINATION_PATTERN.matcher(line);
                if (destinationMatcher.find()) {
                    destination = destinationMatcher.group(1);
                    return;
                }

                Matcher conversionFinishedMatcher = CONVERSION_FINISHED_PATTERN.matcher(line);
                if (conversionFinishedMatcher.find()) {
                    File destinationFile = new File(destination);
                    String title = destinationFile.getName().split("\\.")[0];
                    try {
                        AudioPlayerManager.Track track = new AudioPlayerManager.Track(new FileProvider(destinationFile));
                        track.setTitle(title);
                        track.setAudioFile(destinationFile);
                        tracks.add(track);
                    } catch (IOException | UnsupportedAudioFileException e) {
                        LOGGER.error("Unable to create track", e);
                    }
                }
            }
        };
//        new StreamLogger(process.getInputStream(), StreamLogger.Level.DEBUG);
        new StreamLogger(process.getErrorStream(), StreamLogger.Level.ERROR);

        try {
            int exitCode = process.waitFor();
            LOGGER.debug("Exit code : {}", exitCode);
        } catch (InterruptedException e) {
            throw new GrabberException("Process interrupted abnormally", e);
        }

        return tracks;
    }
}
