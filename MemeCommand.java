package com.omega.command.impl;

import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Signature;
import com.omega.util.SenderUtil;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;


@Command(name = "meme")
public class MemeCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(LyricsCommand.class);

    public MemeCommand(IUser by, IMessage message)
    {
        super(by,message);
    }

    @Signature(help = "Send a random meme")
    public void memeCommand() throws IOException, ParseException {
        String url = "https://api.imgflip.com/get_memes";
        String data = getSource(new URL((url)));

        JSONParser parser = new JSONParser();
        String obj = ((JSONObject)parser.parse(data)).get("data").toString();
        JSONObject datas = (JSONObject)parser.parse(obj);
        JSONArray array = (JSONArray)datas.get("memes");

        List<String> memes = new Vector<String>();
        for(int i = 0; i < array.size(); i++)
        {
            JSONObject tmpObj = (JSONObject)array.get(i);
            memes.add(tmpObj.get("url").toString());
        }

        int randomNumber = ThreadLocalRandom.current().nextInt(0, memes.size() - 1);
        String image = memes.get(randomNumber);

        // TODO : Send Image
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
