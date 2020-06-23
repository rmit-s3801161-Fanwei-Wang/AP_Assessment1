package model;


import exception.InvalidReplyException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class StartUp extends Application {
    public static void main(String[] args) throws InvalidReplyException {
//        Post e2 = new Event("Play soccer", "4 v 4 game", "S2", "centre field", "05/02/2021", 3);
//        Post e1 = new Event("Watch a movie", "Watch IP MAN 2 ", "S1", "Movie centre", "21/10/2021", 1);
//        Post j = new Job("Moving a house", "Move my house from Carlton to Box hill", "S6", 165.55);
//        Post s = new Sale("Ipad pro 2018 10.5inch", "Brand new ", "S4", 300.05, 20.45);
//
//        try {
//            UniLinkDatabase.connectDB();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//
//
////        UniLinkDatabase.createEventTable();
////        UniLinkDatabase.createSaleTable();
////        UniLinkDatabase.createJobTable();
////        UniLinkDatabase.createIDTable();
////        UniLinkDatabase.createReplyTable();
//        UniLinkDatabase.getPosts().put(e2.getId(), e2);
//        UniLinkDatabase.getPosts().put(e1.getId(), e1);
//        UniLinkDatabase.getPosts().put(j.getId(), j);
//        UniLinkDatabase.getPosts().put(s.getId(), s);
////
//        Reply[] replies = new Reply[5];
//        replies[0] = new Reply(e2.getId(), 1, "S1");
//        replies[1] = new Reply(e2.getId(), 1, "S3");
//        replies[2] = new Reply(j.getId(), 50.65, "S5");
//        replies[3] = new Reply(s.getId(), 150, "S10");
//        replies[4] = new Reply(s.getId(), 299.23, "S123");
//////
//        for(Reply reply: replies){
//            UniLinkDatabase.getReplies().add(reply);
//        }
////        UniLink ul = new UniLink();
////
//
//
////
//        try {
//            UniLinkDatabase.insertRow("EVENT");
//            UniLinkDatabase.insertRow("SALE");
//            UniLinkDatabase.insertRow("JOB");
//            UniLinkDatabase.insertRow("uniqueID");
//            UniLinkDatabase.insertRow("REPLY");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//
//
//
//////
////        e2.handleReply(replies[0]);
////        e2.handleReply(replies[1]);
////        j.handleReply(replies[2]);
////        s.handleReply(replies[3]);
////        s.handleReply(replies[4]);
////
//////        e2.getPhoto();
////        //Start up Unilink System
////        ul.start();
    }

    @Override
    public void start(Stage stage) throws Exception {
    }
}

