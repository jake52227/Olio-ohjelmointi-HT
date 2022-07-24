package com.example.movietrackerv2.data.initializer;

import android.util.Log;

import com.example.movietrackerv2.data.component.Actor;
import com.example.movietrackerv2.data.component.Director;
import com.example.movietrackerv2.data.component.Movie;
import com.example.movietrackerv2.data.collection.MovieList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// the purpose of this class is to provide methods for initializing a MovieList object with values
public class MovieListInitializer {

     // initializes the given MovieList object with values extracted from the given NodeLists.
    public void initialize(MovieList list, NodeList scheduleList, NodeList eventsList) {

        if (scheduleList == null) {
            Log.e(null, "Schedule NodeList is null at MovieListInitializer");
            return;
        }
        if (eventsList == null) {
            Log.e(null, "Events NodeList is null at MovieListInitializer");
            return;
        }

        goThroughScheduleList(list, scheduleList);
        goThroughEventList(list, eventsList);

        // set date of last update to current date.
        list.setLastUpdate(new SimpleDateFormat("dd.MM").format(new Date()));
    }


    // goes through the nodes of the NodeList, extracts information of interest and adds a new object to the list
    private void goThroughScheduleList(MovieList list, NodeList scheduleList) {
        // go through list
        for (int i = 0; i < scheduleList.getLength(); i++) {
            Node node = scheduleList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                // extract elements of interest
                if (element.getElementsByTagName("EventType").item(0).getTextContent().equals("Movie")) {
                    String title  = element.getElementsByTagName("OriginalTitle").item(0).getTextContent();
                    String genres = element.getElementsByTagName("Genres").item(0).getTextContent();

                    // add new movie to the list if there isn't one with the same title already
                    if (!list.hasTitle(title))
                        list.addMovie(new Movie(title, genres));
                }
            }
        }
    }


    // goes through the values of eventList to extract information about actors and directors of a movie and add the information to a Movie object
    private void goThroughEventList(MovieList list, NodeList eventList) {
        for (int i = 0; i < eventList.getLength(); i++) {
            Node node = eventList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element   = (Element) node;
                String movieTitle = element.getElementsByTagName("OriginalTitle").item(0).getTextContent();
                Movie movie       = list.getMovieByTitle(movieTitle); // find movie from list by title
                if (movie == null) // no such movie in list, continue
                    continue;
                movie.setActors(extractActors(element));        // set actors for movie using extractActors method
                movie.setDirectors(extractDirectors(element));  // set directors for movie using extractDirectors method
            }
        }
    }

    // takes the given element and extracts actor information from it. Returns a list of Actor objects representing these actors
    private ArrayList<Actor> extractActors(Element element) {
        ArrayList<Actor> actorsList = new ArrayList<Actor>();
        NodeList nl = element.getElementsByTagName("Actor");

        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element1 = (Element) node;
                String firstName = element1.getElementsByTagName("FirstName").item(0).getTextContent();
                String lastName  = element1.getElementsByTagName("LastName").item(0).getTextContent();
                actorsList.add(new Actor(firstName, lastName));
            }
        }
        return actorsList;
    }

    // takes the given element and extracts director information from it. Returns a list of Director objects representing these directors
    private ArrayList<Director> extractDirectors(Element element) {
        ArrayList<Director> directorsList = new ArrayList<Director>();
        NodeList nl = element.getElementsByTagName("Directors");

        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element1 = (Element) node;
                NodeList directorsInfo = ((Element) node).getElementsByTagName("Director");
                for (int j = 0; j < directorsInfo.getLength(); j++) {
                    String firstName = element1.getElementsByTagName("FirstName").item(0).getTextContent();
                    String lastName  = element1.getElementsByTagName("LastName").item(0).getTextContent();
                    directorsList.add(new Director(firstName, lastName));
                }
            }
        }
        return directorsList;
    }
}
