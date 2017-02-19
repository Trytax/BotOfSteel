package com.omega.config;

import org.w3c.dom.Document;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactoryConfigurationException;

public class BotConfigReader extends AbstractConfigReader<BotConfig> {

    public BotConfigReader(BotConfig config) {
        super(config);
    }

    @Override
    protected void read(Document document)
            throws XPathFactoryConfigurationException, XPathExpressionException {
        config.setBotToken(readString(document, "//BotToken"));
        config.setClientId(readString(document, "//ClientId"));
    }
}
