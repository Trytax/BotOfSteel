package com.omega.config;

import java.io.File;

public class BotConfig extends BaseConfig<BotConfig> {

    private static final String FILE_NAME = "settings.xml";

    private String botToken;
    private String clientId;

    private BotConfig() {
        super();
    }

    public static BotConfig getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    protected File provideDirectory() {
        return new File(".");
    }

    @Override
    protected String provideConfigFileName() {
        return FILE_NAME;
    }

    @Override
    protected AbstractConfigReader createConfigReader(BotConfig config) {
        return new BotConfigReader(config);
    }

    @Override
    protected AbstractConfigWriter createConfigWriter(BotConfig config) {
        return new BotConfigWriter(config);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append("botToken = ").append(botToken).append('\n')
                .append("clientId = ").append(clientId);

        return sb.toString();
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    private static class SingletonHolder {

        private static final BotConfig INSTANCE = new BotConfig();
    }
}
