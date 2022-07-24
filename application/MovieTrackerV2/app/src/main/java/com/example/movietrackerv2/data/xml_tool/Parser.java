package com.example.movietrackerv2.data.xml_tool;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Parser implements ParserInterface {
    // parses the given document to a NodeList by the given tag. Returns the list.
    public NodeList parseToNodeList(Document doc, String tag) {
        doc.getDocumentElement().normalize();
        return doc.getDocumentElement().getElementsByTagName(tag);
    }
}