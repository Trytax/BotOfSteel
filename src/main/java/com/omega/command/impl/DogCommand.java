package com.omega.command.impl;


import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Command(name = "dog")
public class DogCommand extends AbstractCommand {

    public static final Logger LOGGER = LoggerFactory.getLogger(DogCommand.class);

    public DogCommand(IUser by, IMessage message)
    {
        super(by,message);
    }

    @Signature(help = "Send a picture of an awesome dog")
    public void dogCommand() throws IOException {
        String url = "http://random.dog/";
        String source  =getSource(new URL(url));
        String regex = "<img src='(?<link>.*?)'><\\\\/img>";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        if (matcher.find())
        {
            String finalUrl = url + matcher.group("link");
            // TODO : Download Image and Send
        }
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
