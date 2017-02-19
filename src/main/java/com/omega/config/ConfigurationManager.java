package com.omega.config;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactoryConfigurationException;
import java.io.IOException;

public class ConfigurationManager {

    private ConfigurationManager() {
    }

    public static ConfigurationManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void load() throws ParserConfigurationException, XPathFactoryConfigurationException, SAXException, XPathExpressionException, IOException {
        BotConfig.getInstance().read();
    }

    private static class SingletonHolder {
        private static final ConfigurationManager INSTANCE = new ConfigurationManager();
    }
}
