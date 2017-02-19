package com.omega.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactoryConfigurationException;
import java.io.File;
import java.io.IOException;

public abstract class BaseConfig<T extends BaseConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseConfig.class);

    public static final String DIRECTORY = "config";

    private final File configDir;
    private final File configFile;
    private final AbstractConfigReader reader;
    private final AbstractConfigWriter writer;

    protected BaseConfig() {
        this.configDir = new File(provideDirectory(), DIRECTORY);
        System.out.println("config directory : " + configDir.getAbsolutePath());
        this.configFile = new File(configDir, provideConfigFileName());

        this.reader = createConfigReader((T) this);
        this.writer = createConfigWriter((T) this);
    }

    protected abstract File provideDirectory();

    protected abstract String provideConfigFileName();

    protected abstract AbstractConfigReader createConfigReader(T config);

    protected abstract AbstractConfigWriter createConfigWriter(T config);

    public File getConfigDirectory() {
        return configDir;
    }

    public File getConfigFile() {
        return configFile;
    }

    public void read() throws IOException, ParserConfigurationException, SAXException,
            XPathExpressionException, XPathFactoryConfigurationException {
        reader.read();
    }

    public void save()
            throws IOException, TransformerException, ParserConfigurationException {
        if (writer != null) {
            writer.save();
        }
    }
}
