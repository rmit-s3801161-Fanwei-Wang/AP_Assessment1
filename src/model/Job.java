package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Job extends Post {
    private static int uniqueID = 0;
    private double proposedPrice; // the maximum amount of money the job post creator willing to paid for the job
    private double lowestOffer; // the lowest amount offered among all replies from students interested in taking this job
    private Map<String, Double> offerHistory = new HashMap<String, Double>();


    public Job(String id, String title, String description, String creatorId, String status, String photoPath, double proposedPrice) {
        super(id, title, description, creatorId, status, photoPath);
        this.proposedPrice = proposedPrice;
        this.lowestOffer = 0.00;
    }

    public Job(String title, String description, String creatorId, String photoPath, double proposedPrice) {
        super(title, description, creatorId, photoPath);
        this.proposedPrice = proposedPrice;
        lowestOffer = 0.00;
    }

    public Job(String title, String description, String creatorId, double proposedPrice) {
        super(title, description, creatorId);
        this.proposedPrice = proposedPrice;
        lowestOffer = 0.00;
    }

    @Override
    public String getPostDetails() {
        return super.getPostDetails()
                + String.format("%-20s", "\nProposed price:") + "$" + proposedPrice
                + String.format("%-20s", "\nLowest Offer:") + "$" + lowestOffer;
    }

    @Override
    public boolean handleReply(Reply reply) {
        if ((reply.getResponderId()).equals(getCreatorId()) || ((lowestOffer == 0) && reply.getValue() > proposedPrice) || ((lowestOffer != 0) && reply.getValue() > lowestOffer)) {
            return false;
        }
        UniLinkDatabase.getReplies().add(reply);
//        getReplies().add(reply);
        setLowestOffer(reply.getValue());
//        offerHistory.put(reply.getResponderId(), reply.getValue());

        return true;

    }

    @Override
    public String getReplyDetails() {
        if (offerHistory.isEmpty()) {
            return getPostDetails() + "\nOffer History:\t Empty";
        }
        return getPostDetails() + "\n" + sortedOfferHistory();
    }

    private String sortedOfferHistory() {
        String sortDescend = "";
        String[][] oh = new String[offerHistory.size()][2];

        int index = 0;
        for (String i : offerHistory.keySet()) {
            oh[index][0] = i;
            oh[index][1] = offerHistory.get(i).toString();
            index++;
        }

        for (int i = 0; i < oh.length; i++) {
            for (int j = i + 1; j < oh.length; j++) {
                if (Double.valueOf(oh[i][1]) > Double.valueOf(oh[j][1])) {
                    String[] temp = {oh[j][0], oh[j][1]};
                    oh[j][0] = oh[i][0];
                    oh[j][1] = oh[i][1];
                    oh[i][0] = temp[0];
                    oh[i][1] = temp[1];
                }
            }
        }

        for (int i = 0; i < oh.length; i++) {
            sortDescend += "\n" + oh[i][0] + ":\t$" + oh[i][1];
        }

        return "-- Offer History --" + sortDescend;
    }


    @Override
    public String idGenerator() {
        String str = "JOB" + String.format("%03d", ++uniqueID);
        return str;
    }

    //Accessor and mutator

    public void setLowestOffer(double lowestOffer) {
        this.lowestOffer = lowestOffer;
    }

    public double getProposedPrice() {
        return proposedPrice;
    }

    public double getLowestOffer() {
        return lowestOffer;
    }

    public static int getUniqueID() {
        return uniqueID;
    }

    public void setProposedPrice(double proposedPrice) {
        this.proposedPrice = proposedPrice;
    }

    public static void setUniqueID(int uniqueID) {
        Job.uniqueID = uniqueID;
    }


    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(proposedPrice);
        return super.toString()+ ret.toString();
    }
}
