package com.omega.config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerException;

public class BotConfigWriter extends AbstractConfigWriter<BotConfig> {

    public BotConfigWriter(BotConfig config) {
        super(config);
    }

    @Override
    protected void save(Document document)
            throws TransformerException {
        Element rootEle = document.createElement("Config");

        appendEntry(rootEle, "BotToken", config.getBotToken());
        appendEntry(rootEle, "ClientId", config.getClientId());

        document.appendChild(rootEle);
    }
}
