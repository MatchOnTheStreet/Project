package com.cse403.matchonthestreet;

import android.location.Location;

import java.util.*;

/**
 * Created by hopecrandall on 2/11/16.
 */
public class ListViewFilteredSearchData implements Comparable<ListViewFilteredSearchData>{

    // List of query strings
    protected List<String> queryStrings;

    // List of filters
    protected Date startTime;
    protected Date endTime;
    protected Location centralLocation;
    protected int searchRadius; // in miles
    protected Date timeStamp;

    public ListViewFilteredSearchData
            (String queryString, Date startTime, Date endTime, Location centralLocation, int searchRadius) {
        this.queryStrings = splitQueryString(queryString);
        this.startTime = startTime;
        this.endTime = endTime;
        this.centralLocation = centralLocation;
        this.searchRadius = searchRadius;
        this.timeStamp = new Date(); // current time
    }


    /* Compares this search to the comparedSearch:
    *  -Returns 0 if the searches are equal
    *  -Returns -1 if this search is narrower than the comparedSearch
    *  -Returns 1 if this search is wider than the comparedSearch */
    public int compareTo(ListViewFilteredSearchData comparedSearch) {
        boolean centralLocationIsChanged = locationIsEqual(comparedSearch);
        int timeChange = this.compareTimeInterval(comparedSearch);
        int queryStringChange = this.compareQueryStrings(comparedSearch);
        int searchRadiusChange = this.compareSearchRadius(comparedSearch);

        boolean searchIsEqual = timeChange == 0 && queryStringChange == 0
                                && searchRadiusChange == 0 && !centralLocationIsChanged;

        boolean searchIsWidened = timeChange == 1 || queryStringChange == 1 || searchRadiusChange > 1
                                  || centralLocationIsChanged;

        if (searchIsEqual) {
            return 0;
        } else if (searchIsWidened) {
            return 1;
        } else { // search is narrowed
            return -1;
        }
    }

    /* Compares the time interval of this search to the time interval of
    *  the passed in search:
    *  -Returns 0 if the time intervals are the same
    *  -Returns -1 if this time interval is within the time interval of comparedSearch
    *  -Returns 1 if this search has a time interval that contains any time that is outside the
    *   time interval of comparedSearch */
    private int compareTimeInterval(ListViewFilteredSearchData comparedSearch) {
        return 0;
    }


    /* Compares this list of query strings to the list of query strings in
    *  comparedSearch:
    *  -Returns 0 if the lists contain exactly the same strings
    *  -Returns -1 if this list contains the same strings as compared Search
    *   plus additional strings
    *  -Returns 1 if this list contains fewer strings than comparedSearch, or if not all
    *   the strings in comparedSearch are in this list  */
    private int compareQueryStrings(ListViewFilteredSearchData comparedSearch) {
        return 0;
    }

    /* Compares this search radius with the search radius of comparedSearch
    *  -Returns 0 if they are equal
    *  -Returns a negative number if this radius is smaller
    *  -Returns a positive number if this radius is bigger
    *  */
    private int compareSearchRadius(ListViewFilteredSearchData comparedSearch) {
        return this.searchRadius - comparedSearch.searchRadius;
    }

    private boolean locationIsEqual(ListViewFilteredSearchData comparedSearch) {
        return this.centralLocation.equals(comparedSearch.centralLocation);
    }

    /* Splits the whole query string into individual words (by spaces) and
    *  returns a list of them */
    private List<String> splitQueryString(String queryString) {
        return null;
    }

}
