package com.omega.config;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

public abstract class AbstractConfigReader<T extends BaseConfig> {

    protected final T config;
    protected final XPathFactory pathFactory;

    protected AbstractConfigReader(T config) {
        this.config = config;
        this.pathFactory = XPathFactory.newInstance();
    }

    public final void read() throws ParserConfigurationException, IOException, SAXException,
            XPathFactoryConfigurationException, XPathExpressionException {
        if (!config.getConfigFile().exists()) {
            throw new FileNotFoundException(
                    "Configuration file " + config.getConfigFile().getAbsolutePath() + " not found");
        }
        if (!config.getConfigFile().isFile()) {
            throw new FileNotFoundException(
                    "Configuration file " + config.getConfigFile().getAbsolutePath() +
                            " should not be a directory");
        }

        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(config.getConfigFile());
        read(document);
    }

    protected abstract void read(Document document)
            throws XPathFactoryConfigurationException, XPathExpressionException;

    protected String readString(Document document, String expression) throws XPathExpressionException {
        return (String) pathFactory.newXPath().compile(expression).evaluate(document, XPathConstants.STRING);
    }

    protected boolean readBoolean(Document document, String expression) throws XPathExpressionException {
        return (boolean) pathFactory.newXPath().compile(expression).evaluate(document, XPathConstants.BOOLEAN);
    }

    protected NodeList getNodes(Document document, String expression) throws XPathExpressionException {
        return (NodeList) pathFactory.newXPath().compile(expression).evaluate(document, XPathConstants.NODESET);
    }
}