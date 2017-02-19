package com.omega;

import com.omega.command.CommandManager;
import com.omega.config.BotConfig;
import com.omega.config.ConfigurationManager;
import com.omega.listener.MessageListener;
import com.omega.listener.StateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactoryConfigurationException;
import java.io.IOException;

public class BotManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(BotManager.class);

    private ClientBuilder clientBuilder;
    private IDiscordClient client;
    private IUser applicationOwner;

    private BotManager() {
    }

    public static BotManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init() throws Exception {
        try {
            LOGGER.info("Load configuration file");
            ConfigurationManager.getInstance().load();
            LOGGER.debug("Config {}", BotConfig.getInstance().toString());
        } catch (ParserConfigurationException | XPathFactoryConfigurationException | SAXException | IOException | XPathExpressionException e) {
            throw new Exception("Unable to load bot configuration", e);
        }

        clientBuilder = new ClientBuilder();
        clientBuilder.withToken(BotConfig.getInstance().getBotToken());
    }

    public void connect() {
        try {
            client = clientBuilder.login();
            EventDispatcher dispatcher = client.getDispatcher();
            dispatcher.registerListener(new StateListener());
            dispatcher.registerListener(new MessageListener(client));
            dispatcher.registerListener(CommandManager.getInstance());
            dispatcher.registerListener(this);
        } catch (DiscordException e) {
            LOGGER.error("Unable to connect the bot", e);
        }
    }

    @EventSubscriber
    public void postConnect(ReadyEvent event) {
        try {
            this.applicationOwner = getClient().getApplicationOwner();
            LOGGER.debug("Application owner set : {}", applicationOwner.getName());
        } catch (DiscordException e) {
            LOGGER.warn("Unable to get application owner, retrying");
            postConnect(event);
        }
    }

    public IDiscordClient getClient() {
        return client;
    }

    public IUser getApplicationOwner() {
        return applicationOwner;
    }

    private static class SingletonHolder {
        private static BotManager INSTANCE = new BotManager();
    }
}
