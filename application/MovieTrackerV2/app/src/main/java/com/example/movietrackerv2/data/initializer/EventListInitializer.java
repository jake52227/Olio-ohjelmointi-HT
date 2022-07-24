package com.example.movietrackerv2.data.initializer;

import android.util.Log;

import com.example.movietrackerv2.data.component.MovieEvent;
import com.example.movietrackerv2.data.collection.MovieEventList;
import com.example.movietrackerv2.data.utility.TimeMethodsInterface;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// the purpose of this class is to provide methods for initializing a MovieEventList object with values
public class EventListInitializer {

    private TimeMethodsInterface timeUtil;

    public EventListInitializer(TimeMethodsInterface timeUtil) {
        this.timeUtil = timeUtil;
    }

    // initializes the given MovieEventList object with values extracted from the given NodeList. Uses TimeMethodsInterface to format dates and times.
    public void initialize(MovieEventList eventList, NodeList nl) {

        String movieTitle;
        String[] theatreNameAndArea;
        String startDate;
        String endDate;

        // make sure list is clear
        if (eventList != null) {
            if (!eventList.getList().isEmpty()) {
                eventList.getList().clear();
            }
        } else {
            return;
        }
        if (nl == null)
            return;

        // go through list
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                // extract the info from the document elements by tags
                if (element.getElementsByTagName("EventType").item(0).getTextContent().equals("Movie")) {
                    try {
                        movieTitle          = element.getElementsByTagName("OriginalTitle").item(0).getTextContent();
                        theatreNameAndArea  = element.getElementsByTagName("Theatre").item(0).getTextContent().split(",");
                        startDate           = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                        endDate             = element.getElementsByTagName("dttmShowEnd").item(0).getTextContent();
                    } catch (IndexOutOfBoundsException e) {
                        Log.e(null, "Index out of bounds at EventListInitializer element data extraction");
                        return;
                    } catch (NullPointerException e) {
                        Log.e(null, "null pointer exception at EventListInitializer element data extraction");
                        return;
                    }

                    String[] startTimeAndDate = timeUtil.formatTimeAndDate(startDate);

                    String start = null;
                    if (startTimeAndDate != null)
                        start = startTimeAndDate[0];

                    String showDate = null;
                    if (startTimeAndDate != null)
                        showDate = startTimeAndDate[1];

                    if (eventList.getDate() == null)
                        eventList.setDate(showDate);

                    String end = null;
                    try {
                        end = timeUtil.formatTimeAndDate(endDate)[0];
                    } catch (NullPointerException e) {
                        Log.e(null, "Null pointer exception at EventListInitializer 'endDate' string parsing");
                    }

                    eventList.addEvent(new MovieEvent(movieTitle, theatreNameAndArea[0], start, end, showDate ));
                }
            }
        }
    }
}


