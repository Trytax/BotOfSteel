package com.omega.command.impl;

import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Parameter;
import com.omega.command.Signature;
import com.omega.util.SenderUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

@Command(name = "compile")
public class CompileCommand extends AbstractCommand {

    public  static final Logger LOGGER = LoggerFactory.getLogger(CompileCommand.class);

    public  CompileCommand(IUser by, IMessage message)
    {
        super(by,message);
    }

    @Signature(help = "Compile a code with a specified language")
    public  void compileCommand(@Parameter(name = "code") String code, @Parameter(name = "language") String language) throws IOException, ParseException {
        String baseUrl = "https://compile-public-low.remoteinterview.io/compile";
        int lang = ConvertLanguage(language.toLowerCase());
        if (lang == Integer.MAX_VALUE)
            SenderUtil.reply(message, "Invalid language !");
        else
        {
            String param = "language=" + lang + "&code=" + URLEncoder.encode(code.replaceAll("`","")) + "&stdin=&name=Guest+KLT";
            URL url = new URL(baseUrl);
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(param);
            wr.flush();
            wr.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = reader.readLine()) != null)
                response.append(inputLine);
            reader.close();

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject)parser.parse(response.toString());

            String output = obj.get("output").toString();
            String errors = obj.get("errors").toString();

            SenderUtil.reply(message, "Output: ```" + output + "```\nErrors: ```" + errors + "```");
        }
    }

    private int ConvertLanguage(String language)
    {
        switch (language)
        {
            case "c#":
                return 10;
            case "c":
                return 7;
            case "c++":
                return 7;
            case "clojure":
                return 2;
            case "java":
                return 8;
            case "go":
                return 6;
            case "js":
                return 4;
            case "php":
                return 3;
            case "python":
                return 0;
            case "python3":
                return 15;
            case "swift":
                return 16;
            case "ruby":
                return 1;
            case "scala":
                return 5;
            case "vb.net":
                return 9;
            case "objective-c":
                return 12;
            case "perl":
                return 14;
            case "erlang":
                return 17;
            case "elixir":
                return 18;
            case "bash":
                return 11;
            case "powershell":
                return 19;
            case "shell":
                return -2;
            default:
                return Integer.MAX_VALUE;
        }
    }

}
