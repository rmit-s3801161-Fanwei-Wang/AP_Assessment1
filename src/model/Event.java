package model;
import exception.InvalidReplyException;

import java.util.ArrayList;

public class Event extends Post {
    private static int uniqueID = 0; //the number of id be used in idGenerator
    private String venue; // Is the location of the event
    private String date; // The date when the event happens
    private int capacity; // the maximum number of attendees of the event
    private int attendeeCount; // the current number of participants in the event.
    private ArrayList<String> attendeeList = new ArrayList<String>();


    public Event(String title, String description, String creatorId, String venue, String date, int capacity) {
        super(title, description, creatorId);
        this.venue = venue;
        this.date = date;
        this.capacity = capacity;
        this.attendeeCount = 0;
    }
    public Event(String title, String description, String creatorId, String photoPath, String venue, String date, int capacity) {
        super(title, description, creatorId,photoPath);
        this.venue = venue;
        this.date = date;
        this.capacity = capacity;
        this.attendeeCount = 0;
    }

    public Event(String id, String title, String description, String creatorId, String status, String photoPath, String venue, String date, int capacity) {
        super(id, title, description, creatorId, status, photoPath);
        this.venue = venue;
        this.date = date;
        this.capacity = capacity;
        this.attendeeCount = 0;
    }

    @Override
    public String getPostDetails() {
        return super.getPostDetails()
                + String.format("%-20s", "\nVenue:") + venue
                + String.format("%-20s", "\nData:") + date
                + String.format("%-20s", "\nCapacity:") + capacity
                + String.format("%-20s", "\nAttendees:") + attendeeCount;
    }

    @Override
    public boolean handleReply(Reply reply) throws InvalidReplyException {
        if ((reply.getResponderId()).equals(getCreatorId()) || reply.getValue() != 1 || attendeeCount >= capacity || attendeeList.contains(reply.getResponderId()) || getStatus().equals("CLOSED")) {
            throw new InvalidReplyException("This is not a valid reply");
        }

//        getReplies().add(reply);
        UniLinkDatabase.getReplies().add(reply);
//        attendeeList.add(reply.getResponderId());
        attendeeCount++;

        if (attendeeCount >= capacity) {
            setStatus("CLOSED");
        }

        return true;
    }
    @Override
    public String getReplyDetails() {
        if (attendeeList.isEmpty()) {
            return getPostDetails() + String.format("%-20s", "\nAttendee list:") + "Empty";
        } else {
            return getPostDetails() + String.format("%-20s", "\nAttendee list:") + attendeeList;
        }
    }

    @Override
    public String idGenerator() {
        String str = "EVE" + String.format("%03d", ++uniqueID);
        return str;
    }


    //Accessor and mutator
    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDate() {
        return date;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getAttendeeCount() {
        return attendeeCount;
    }

    public static int getUniqueID() {
        return uniqueID;
    }

    public static void setUniqueID(int uniqueID) {
        Event.uniqueID = uniqueID;
    }

    public ArrayList<String> getAttendeeList() {
        return attendeeList;
    }

    public void increaseAttendeeCount(){
        attendeeCount++;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(venue);
        ret.append("\t");
        ret.append(date);
        ret.append("\t");
        ret.append(capacity);
        return super.toString()+ret.toString();
    }
}