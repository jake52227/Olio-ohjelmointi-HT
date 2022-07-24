package com.example.movietrackerv2.data.xml_tool;

import android.util.Log;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DocumentLoader implements LoaderInterface {

    // attempts to read content from the given url to a document. On success returns the document, else returns null
    @Override
    public Document readToDocument(String url) {
        Document doc = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.parse(url);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            Log.e(null, "error at readToDocument XmlLoader");
            e.printStackTrace();
        }
        System.out.println("fetching document from: " + url);
        return doc;
    }

}
