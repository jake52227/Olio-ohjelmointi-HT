package com.example.movietrackerv2.data.api.finnkino;

import com.example.movietrackerv2.data.xml_tool.ParserInterface;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

// the purpose of this class is to parse Finnkino-related document objects into NodeLists
public class FinnkinoDocumentParser {

    private ParserInterface parser;
    private final String theatreTag = "TheatreArea";
    private final String scheduleTag = "Show";
    private final String eventsTag = "Event";

    public FinnkinoDocumentParser(ParserInterface parser) {
        this.parser = parser;
    }

    // takes a document which holds Finnkino's theatres information. Parses that to a NodeList and returns the list.
    public NodeList parseTheatreInfo(Document document) {
        return parser.parseToNodeList(document, theatreTag);
    }

    public NodeList parseScheduleInfo(Document document) {
        return parser.parseToNodeList(document, scheduleTag);
    }

    public NodeList parseEventsInfo(Document document) {
        return parser.parseToNodeList(document, eventsTag);
    }
}
