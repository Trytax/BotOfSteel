package com.omega.command;

import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public abstract class AbstractCommand {

    protected IUser by;
    protected IMessage message;

    public AbstractCommand(IUser by, IMessage message) {
        this.by = by;
        this.message = message;
    }
}
