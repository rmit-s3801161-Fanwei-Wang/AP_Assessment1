package model;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

import java.text.DecimalFormat;
import java.util.*;

public class Sale extends Post {
    private static int uniqueID = 0;
    private double askingPrice; // The price at which the post creator wants to sell the item
    private double highestOffer; // The highest price offered among all replies from students interested to buy the item
    private double minRaise; // the minimum increase amount of a new offer price than highest offer.
    private Map<String, Double> offerHistory = new HashMap<String, Double>();  //offer history data store

    public Sale(String id, String title, String description, String creatorId, String status, String photoPath, double askingPrice, double minRaise) {
        super(id, title, description, creatorId, status, photoPath);
        this.askingPrice = askingPrice;
        this.highestOffer = 0;
        this.minRaise = minRaise;
    }

    public Sale(String title, String description, String creatorId, String photoPath, double askingPrice, double minRaise) {
        super(title, description, creatorId,photoPath);
        this.askingPrice = askingPrice;
        highestOffer = 0;
        this.minRaise = minRaise;
    }
    public Sale(String title, String description, String creatorId, double askingPrice, double minRaise) {
        super(title, description, creatorId);
        this.askingPrice = askingPrice;
        highestOffer = 0;
        this.minRaise = minRaise;
    }


    @Override
    public String getPostDetails() {
        if (highestOffer <= 0) {
            return super.getPostDetails()
                    + String.format("%-20s", "\nMinimum raise:") + "$" + minRaise
                    + String.format("%-20s", "\nHighest offer:") + "NO OFFER";
        }
        return super.getPostDetails()
                + String.format("%-20s", "\nMinimum raise:") + "$" + minRaise
                + String.format("%-20s", "\nHighest offer:") + "$" + highestOffer;
    }

    @Override
    public boolean handleReply(Reply reply) {
        if ((reply.getResponderId()).equals(getCreatorId()) || reply.getValue() - highestOffer < minRaise) {
            return false;
        }
        UniLinkDatabase.getReplies().add(reply);
//        getReplies().add(reply);

        if (reply.getValue() >= askingPrice) {
            setStatus("CLOSED");
        }

        setHighestOffer(reply.getValue());

//        offerHistory.put(reply.getResponderId(), reply.getValue());

        return true;
    }

    @Override
    public String getReplyDetails() {
        if (offerHistory.isEmpty()) {
            return getPostDetails() + "\n" + String.format("%-20s", "\nAsking price:") + "$" + askingPrice + "\nOffer History:\tEmpty";
        } else {
            return getPostDetails() + "\n" + String.format("%-20s", "\nAsking price:") + "$" + askingPrice + "\n" + sortedOfferHistory();
        }
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
                if (Double.valueOf(oh[i][1]) < Double.valueOf(oh[j][1])) {
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
        String str = "SAL" + String.format("%03d", ++uniqueID);
        return str;
    }

    //Acesseor and getter
    public double getHighestOffer() {
        return highestOffer;
    }

    public double getMinRaise() {
        return minRaise;
    }

    public void setAskingPrice(double askingPrice) {
        this.askingPrice = askingPrice;
    }

    public void setMinRaise(double minRaise) {
        this.minRaise = minRaise;
    }

    public double getAskingPrice() {
        return askingPrice;
    }

    public void setHighestOffer(double highestOffer) {
        this.highestOffer = highestOffer;
    }

    public static int getUniqueID() {
        return uniqueID;
    }

    public static void setUniqueID(int uniqueID) {
        Sale.uniqueID = uniqueID;
    }

    public double moneyFormat(double a) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        return Double.valueOf(decimalFormat.format(a));
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(askingPrice);
        ret.append("\t");
        ret.append(minRaise);

        return super.toString() + ret.toString();
    }
}

