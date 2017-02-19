package com.omega.command.impl;

import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Parameter;
import com.omega.command.Signature;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Command(name = "rand")
public class RandomCommand extends AbstractCommand {

    public RandomCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Pick a random integer in the given range")
    public void randomCommand(@Parameter(name = "min") Integer min, @Parameter(name = "max") Integer max) throws RateLimitException, DiscordException, MissingPermissionsException {
        int randomNumber = ThreadLocalRandom.current().nextInt(min, max + 1);
        message.reply("The random number is " + randomNumber);
    }

    @Signature(help = "Pick a random 64 bits integer in the given range")
    public void randomCommand(@Parameter(name = "min") Long min, @Parameter(name = "max") Long max) throws RateLimitException, DiscordException, MissingPermissionsException {
        long randomNumber = ThreadLocalRandom.current().nextLong(min, max + 1);
        message.reply("The random number is " + randomNumber);
    }

    @Signature(help = "Pick a random floating point in the given range")
    public void randomCommand(@Parameter(name = "min") Float min, @Parameter(name = "max") Float max) throws RateLimitException, DiscordException, MissingPermissionsException {
        float randomNumber = ThreadLocalRandom.current().nextFloat() * (max - min) + min;
        message.reply("The random number is " + randomNumber);
    }

    @Signature(help = "Pick a random 64 bits floating point in the given range")
    public void randomCommand(@Parameter(name = "min") Double min, @Parameter(name = "max") Double max) throws RateLimitException, DiscordException, MissingPermissionsException {
        double randomNumber = ThreadLocalRandom.current().nextDouble(min, max + 1);
        message.reply("The random number is " + randomNumber);
    }
}
