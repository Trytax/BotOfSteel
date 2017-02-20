package com.omega.command.impl;

import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Signature;
import com.omega.util.SenderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

@Command(name = "cat")
public class CatCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(LyricsCommand.class);

    public CatCommand(IUser by, IMessage message)
    {
        super(by,message);
    }

    @Signature(help = "Send a picture of an awesome cat")
    public void catCommand() throws IOException, ParseException {
        String src = getSource(new URL("http://random.cat/meow"));

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(src);
        JSONObject jsonObject = (JSONObject)obj;

        String file = jsonObject.get("file").toString().replace("\\","");

        // TODO : Send image

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
