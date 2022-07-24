package com.example.movietrackerv2.data.xml_tool;

import org.w3c.dom.Document;

public interface LoaderInterface {
    Document readToDocument(String url);
}
