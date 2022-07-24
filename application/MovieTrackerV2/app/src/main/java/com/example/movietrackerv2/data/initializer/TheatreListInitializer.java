package com.example.movietrackerv2.data.initializer;

import android.util.Log;

import com.example.movietrackerv2.data.component.Theatre;
import com.example.movietrackerv2.data.collection.TheatreList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// the purpose of this class is to provide methods for initializing an existing TheatreList object with values
public class TheatreListInitializer {
    // initializes the given TheatreList object with values extracted from the given NodeList
    public void initialize(TheatreList list, NodeList nl) {
        if (list != null) {
            if (!list.getList().isEmpty()) {
                list.emptyList();
            }
        } else {
            return;
        }

        // go through elements
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                // extract values of interest
                Integer id;
                try {
                    id = Integer.parseInt(element.getElementsByTagName("ID").item(0).getTextContent());
                } catch (NumberFormatException e) {
                    Log.e(null, "NumberFormatException at TheatreListInitializer");
                    id = -1;
                }
                String[] areaAndName = element.getElementsByTagName("Name").item(0).getTextContent().split(": ");
                if (areaAndName.length == 2) {      // some elements only have the area name those are not wanted
                    String area = areaAndName[0];
                    String name = areaAndName[1];
                    // add new object to list if there isn't one with the same name already
                    if (!list.hasTheatreWithName(name))
                        list.addTheatre(new Theatre(area, name, id));
                }
            }
        }
    }
}
