package com.omega.audio;

import com.omega.guild.GuildContext;
import com.omega.guild.GuildManager;
import com.omega.util.SenderUtil;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.audio.IAudioProvider;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.audio.AudioPlayer;
import sx.blah.discord.util.audio.providers.AudioInputStreamProvider;

import javax.sound.sampled.AudioInputStream;
import java.io.File;

public class AudioPlayerManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AudioPlayerManager.class);

    private final com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager audioPlayerManager;

    private AudioPlayerManager() {
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    public static AudioPlayerManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public GuildAudioPlayer get(IGuild guild) {
        GuildContext guildContext = GuildManager.getInstance().getContext(guild);
        GuildAudioPlayer audioPlayer = guildContext.getAudioPlayer();
        if (audioPlayer == null) {
            audioPlayer = new GuildAudioPlayer(audioPlayerManager, guild);
            guildContext.setAudioPlayer(audioPlayer);

            guild.getAudioManager().setAudioProvider(audioPlayer.getAudioProvider());

            addPlayerListeners(guild.getClient(), audioPlayer);
        }

        return audioPlayer;
    }

    private void addPlayerListeners(IDiscordClient client, GuildAudioPlayer audioPlayer) {
        audioPlayer.addListener(new AudioEventAdapter() {
            @Override
            public void onTrackStart(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player, AudioTrack track) {
                SenderUtil.sendMessage(audioPlayer.getGuild().getChannels().get(2), "Playing track " + track.getInfo().title);
            }

            @Override
            public void onTrackStuck(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player, AudioTrack track, long thresholdMs) {
                SenderUtil.sendMessage(audioPlayer.getGuild().getChannels().get(2), "Track is stuck, skipping to next track");
            }

            @Override
            public void onPlayerPause(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player) {
                SenderUtil.sendMessage(audioPlayer.getGuild().getChannels().get(2), "Audio player paused");
            }

            @Override
            public void onPlayerResume(com.sedmelluq.discord.lavaplayer.player.AudioPlayer player) {
                SenderUtil.sendMessage(audioPlayer.getGuild().getChannels().get(2), "Audio player resumed");
            }
        });
    }

    public static class Tracks {
        public static class Metadata {
            public static final String NAME = "title";
        }
    }

    public static class Track extends AudioPlayer.Track {

        private String title;
        private File audioFile;
        private String source;

        public Track(IAudioProvider provider) {
            super(provider);
        }

        public Track(AudioInputStreamProvider provider) {
            super(provider);
        }

        public Track(AudioInputStream stream) {
            super(stream);
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public File getAudioFile() {
            return audioFile;
        }

        public void setAudioFile(File audioFile) {
            this.audioFile = audioFile;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }

    private static class SingletonHolder {
        private static final AudioPlayerManager INSTANCE = new AudioPlayerManager();
    }
}
