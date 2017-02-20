package com.omega.command.impl;


import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Parameter;
import com.omega.command.Signature;
import com.omega.util.SenderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command(name = "say")
public class SayCommand extends AbstractCommand {

    public  static final Logger LOGGER = LoggerFactory.getLogger(SayCommand.class);

    public SayCommand(IUser by, IMessage message)
    {
        super(by,message);
    }

    @Signature(help = "Say what the user input")
    public void sayCommand(@Parameter(name = "text") String text)
    {
        // TODO : Send message in the user's channel
        SenderUtil.sendMessage();
    }

}
