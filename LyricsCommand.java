package com.omega.command.impl;

import com.omega.command.AbstractCommand;
import com.omega.command.Parameter;
import com.omega.command.Command;
import com.omega.command.Signature;
import com.omega.util.SenderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Command(name = "lyrics")
public class LyricsCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(LyricsCommand.class);

    class  SongStruct {
        public String author;
        public String songName;
        public String lyrics;
        public String url;
    }

    public LyricsCommand(IUser by, IMessage message)
    {
        super(by,message);
    }

    @Signature(help = "Find the lyrics of a song")
    public void lyricsCommand(@Parameter(name = "songName") String songName) throws IOException {
        SongStruct songStruct = searchLyrics(songName);
        String msg = "Auteur: " + songStruct.author + " - Titre: " + songStruct.author + "\nLyrics:\n```" + songStruct.lyrics + "```";
        if (msg.length() > 2000)
        {
            SenderUtil.sendPrivateMessage(by, "Lien du lyrics: " + songStruct.url);
        }
        else
        {
            SenderUtil.sendPrivateMessage(by, msg);
        }
    }

    private SongStruct searchLyrics(String songName) throws IOException {
        String baseUrl = "http://search.azlyrics.com/search.php?q=";
        String encoded = URLEncoder.encode(songName);
        String finalUrl = baseUrl + encoded;
        String regex = "<td class=\\\\\\\"text-left visitedlyr\\\\\\\">(\\\\s*\\\\w*.*)<a href=\\\\\\\"(?<link>\\\\s*\\\\w*.*)\\\\\\\" target=\\\\\\\"_blank\\\\\\\"><b>(?<songName>\\\\s*\\\\w*.*?)<\\\\/b><\\\\/a>.*<b>(?<author>\\\\s*\\\\w*.*?)<\\\\/b><br>";

        String srcOne = getSource(new URL(finalUrl));

        SongStruct songStruct = null;
        if (srcOne.contains("no results"))
        {
            return songStruct;
        }
        else
        {

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(srcOne);
            if (matcher.find())
            {
                String url = matcher.group("link");
                songStruct = new SongStruct();
                songStruct.author = matcher.group("author");
                songStruct.songName = matcher.group("songName");
                songStruct.url = url;

                String srcLyrics = getSource(new URL(url));
                Pattern lyricsPattern = Pattern.compile("Sorry about that. -->(.*)", Pattern.DOTALL);
                Matcher lyricsMatcher = lyricsPattern.matcher(srcLyrics);

                if (lyricsMatcher.find())
                {
                    String text = lyricsMatcher.group(1);
                    text = text.substring(0, text.indexOf("</div>"));
                    text = text.replaceAll("\\[[^\\[]*\\]", "");
                    songStruct.lyrics = text;
                }
            }
        }
        return songStruct;
    }

    private String getSource(URL url) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

        String result = "";
        String s = null;
        while ((s = reader.readLine()) != null)
            result += s;

        return result;
    }

}