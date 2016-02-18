package com.cse403.matchonthestreet;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Iris on 2/7/16.
 *
 * Represents a sporting Event.
 */
public class Event implements Parcelable {

    // The title of the event
    public String title;

    // Where the event is held.
    protected Location location;

    // The time of the event
    protected Date time;

    // A description of the event.
    protected String description;

    // A list of accounts who have said they will be attending the event.
    protected List<Account> attending;

    // The time the event was created
    protected Date timeStamp;

    public Event(String title, Location location, Date time, String description) {
        this.title = title;
        this.location = location;
        this.time = time;
        this.description = description;
        this.attending = null;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isAttendedBy(Account account) {
        return true;
    }

    public boolean isAfter(Date time) {
        return this.time.after(time) || this.time.equals(time);
    }

    public boolean isBefore(Date time) {
        return this.time.before(time);
    }

    public boolean isCloser(Location centralLocation, Event e) {
        return true;
    }

    public boolean wasCreatedAfter(Date timeStamp) {
        return true;
    }

    public boolean containsAllStrings(List<String> strings) {
        return true;
    }

    private boolean containsString(String s) {
        return true;
    }

    public boolean meetsFilterAndSearchCriteria(ListViewFilteredSearchData currentSearch) {
        return this.isAfter(currentSearch.startTime)
                && this.isBefore(currentSearch.endTime)
                && this.containsAllStrings(currentSearch.queryStrings);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(title);
        parcel.writeString(description);
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        parcel.writeDouble(lat);
        parcel.writeDouble(lon);
        parcel.writeString(time.toString());

    }

    private Event(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        double lat = in.readDouble();
        double lon = in.readDouble();
        Location location = new Location("created in parcel");
        location.setLatitude(lat);
        location.setLongitude(lon);
        this.location = location;
        String dateString = in.readString();
        this.time = new Date(); //TODO: convert the string to a date

        this.attending = null;
    }

    public static final Parcelable.Creator<Event> CREATOR
            = new Parcelable.Creator<Event>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
