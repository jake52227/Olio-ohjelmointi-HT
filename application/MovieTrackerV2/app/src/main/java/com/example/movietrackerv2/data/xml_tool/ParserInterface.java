package com.example.movietrackerv2.data.xml_tool;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public interface ParserInterface {
    NodeList parseToNodeList(Document doc, String tag);
}
