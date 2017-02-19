package com.omega.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.*;

import java.util.Timer;
import java.util.TimerTask;

public class SenderUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SenderUtil.class);

    public static void reply(IMessage messageToReply, String messageToSend) {
        try {
            messageToReply.reply(messageToSend);
        } catch (RateLimitException e) {
            LOGGER.warn("Rate limit exceeded, will try again in {}ms", e.getRetryDelay());
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    reply(messageToReply, messageToSend);
                }
            }, e.getRetryDelay());
        } catch (MissingPermissionsException e) {
            LOGGER.info("Permissions needed to reply", e);
        } catch (DiscordException e) {
            throw new RuntimeException(e);
        }
    }

    public static void reply(IMessage messageToReply, String message, EmbedObject embedObject) {
        try {
            messageToReply.reply(message, embedObject);
        } catch (RateLimitException e) {
            LOGGER.warn("Rate limit exceeded, will try again in {}ms", e.getRetryDelay());
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    reply(messageToReply, message, embedObject);
                }
            }, e.getRetryDelay());
        } catch (MissingPermissionsException e) {
            LOGGER.info("Permissions needed to reply", e);
        } catch (DiscordException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendPrivateMessage(IUser by, String message) {
        try {
            by.getOrCreatePMChannel().sendMessage(message);
        } catch (RateLimitException e) {
            LOGGER.warn("Rate limit exceeded, will try again in {}ms", e.getRetryDelay());
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    LOGGER.warn("Failed to send message, retry in {}", e.getRetryDelay());
                    sendPrivateMessage(by, message);
                }
            }, e.getRetryDelay());
        } catch (MissingPermissionsException e) {
            LOGGER.info("Permissions needed to send private message", e);
        } catch (DiscordException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendMessage(IChannel channel, String message) {
        try {
            channel.sendMessage(message);
        } catch (RateLimitException e) {
            LOGGER.warn("Rate limit exceeded, will try again in {}ms", e.getRetryDelay());
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    sendMessage(channel, message);
                }
            }, e.getRetryDelay());
        } catch (MissingPermissionsException e) {
            LOGGER.info("Permissions needed send message on channel " + channel.getName(), e);
        } catch (DiscordException e) {
            throw new RuntimeException(e);
        }
    }
}
