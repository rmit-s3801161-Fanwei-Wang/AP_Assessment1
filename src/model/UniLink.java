package model;

import exception.InvalidReplyException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;


public class UniLink {
    Scanner sc = new Scanner(System.in);
    ArrayList<Post> posts = new ArrayList<>(); // Store all post information
    String username;  //User who log in this system

    //menu part
    public void start() throws InvalidReplyException {

        int option = -1;
        do {
            System.out.print("\n** UniLink System **\n1.\tLog in\n2.\tQuit");
            System.out.print("\nEnter your choice: ");
            option = checkInt();
            if (option == 1 || option == 2) {
                switch (option) {
                    case 1:
                        studentMenu();
                        break;
                    case 2:
                        System.out.println("Thank you for using this system. ");
                        option = -1;
                        break;
                }
            } else {
                System.out.println("Invalid input!");
                option = 0;
            }
        } while (option != -1);
        sc.close();
    }

    private void studentMenu() throws InvalidReplyException {
        boolean invalid = false;
        System.out.print("\nEnter username: ");
        username = checkUsername();
        System.out.print("\nWelcome " + username + " !");
        int option = -1;
        do {
            System.out.print(
                    "\n** Student Menu **" +
                            "\n1.\tNew Event Post" +
                            "\n2.\tNew Sale Post" +
                            "\n3.\tNew Job Post" +
                            "\n4.\tReply To Post" +
                            "\n5.\tDisplay My Posts" +
                            "\n6.\tDisplay All Posts" +
                            "\n7.\tClose Post" +
                            "\n8.\tDelete Post" +
                            "\n9.\tLog Out" +
                            "\nEnter your choice: ");
            option = checkInt();
            if (option >= 1 && option <= 9) {
                switch (option) {
                    case 1:
                        newEventPost();
                        invalid = true;
                        break;
                    case 2:
                        newSalePost();
                        invalid = true;
                        break;
                    case 3:
                        newJobPost();
                        invalid = true;
                        break;
                    case 4:
                        replyPost();
                        invalid = true;
                        break;
                    case 5:
                        displayMyPost();
                        invalid = true;
                        break;
                    case 6:
                        displayAllPost();
                        invalid = true;
                        break;
                    case 7:
                        closePost();
                        invalid = true;
                        break;
                    case 8:
                        deletePost();
                        invalid = true;
                        break;
                    case 9:
                        invalid = false;
                        break;
                }
            } else {
                System.out.println("Invalid input!");
                invalid = true;
            }
        } while (invalid);
    }



    //implement functions include in student menu
    private void newEventPost() {
        System.out.println("Enter details of the event below: ");
        Post newEventPost = new Event(newTitle(), newDescription(), newCreatorID(), newVenue(), newDate(), newCapacity());
        posts.add(newEventPost);
        System.out.println("Success! You event has been created with ID: " + newEventPost.getId());
    }

    //Create new Sale post
    private void newSalePost() {
        System.out.println("Enter details of the sale below: ");
        Post newSalePost = new Sale(newTitle(), newDescription(), newCreatorID(), newAskingPrice(), newMinRaise());
        posts.add(newSalePost);
        System.out.println("Success! You sale has been created with ID: " + newSalePost.getId());
    }

    //Create new Job post
    private void newJobPost() {
        System.out.println("Enter details of the job below: ");
        Post newJobPost = new Job(newTitle(), newDescription(), newCreatorID(), newProposedPrice());
        posts.add(newJobPost);
        System.out.println("Success! You job has been created with ID: " + newJobPost.getId());
    }

    //reply post function implement
    private void replyPost() throws InvalidReplyException {
        String postID;
        Post postToReply = null;
        boolean invalid = true;
        boolean notFound = true;
        do {
            System.out.print("Enter post ID or 'Q' to quit: ");
            postID = sc.nextLine();
            if (postID.equalsIgnoreCase("Q")) {
                return;
            }
            for (Post post : posts) {
                if (postID.equalsIgnoreCase(post.getId())) {
                    notFound = false;
                    if (post.getCreatorId().equalsIgnoreCase(username)) {
                        System.out.println("Reply your own post is no valid! ");
                        return;
                    }
                    postToReply = post;
                    invalid = false;
                }
            }
            if (notFound) {
                System.out.println("Invalid post ID! Post not found");
                postID = null;
                invalid = true;
            }
        } while (invalid);

        if (postToReply.getStatus().equals("CLOSED")) {
            System.out.println("Reply denied! This event is closed. ");
            return;
        }
        System.out.print("\nTitle:\t" + postToReply.getTitle());


        //reply in different kind of post
        if (postToReply instanceof Event) {
            System.out.println("\nVenue:\t" + ((Event) postToReply).getVenue() + "\nStatus:\t" + postToReply.getStatus() + "\nDate:\t" + ((Event) postToReply).getDate());
            invalid = true;
            do {
                System.out.print("Enter '1' to join event or 'Q' to quit: ");
                String option = sc.nextLine();

                if (option.equalsIgnoreCase("Q")) {
                    return;
                } else if (option.equals("1")) {
                    Reply reply = new Reply(postToReply.getId(), 1, username);
                    if (postToReply.handleReply(reply)) {
                        System.out.println("Event registration accepted!");
                        invalid = false;
                    }
                    else {
                        if (((Event)postToReply).getAttendeeCount() >= ((Event)postToReply).getCapacity()) {
                            System.out.println("Reply denied! This event is full. ");
                        }

                        if ((((Event)postToReply).getAttendeeList()).contains(reply.getResponderId())) {
                            System.out.println("Reply denied! You have already joint this event.");
                        }

                        if (postToReply.getStatus().equals("CLOSED")) {
                            System.out.println("Reply denied! This event is closed.");
                        }
                    }
                } else {
                    System.out.println("Invalid input!");
                }
            } while (invalid);
        } else if (postToReply instanceof Job) {
            System.out.println("\nProposed price:\t$" + ((Job) postToReply).getProposedPrice() + "\nLowest offer:\t$" + ((Job) postToReply).getLowestOffer());
            invalid = true;
            do {
                System.out.print("Enter you offer or 'Q' to quit: ");
                String option = sc.nextLine();

                if (option.equalsIgnoreCase("Q")) {
                    return;
                } else if (isDouble(option)) {
                    Reply reply = new Reply(postToReply.getId(), Double.parseDouble(option), username);
                    if (postToReply.handleReply(reply)) {
                        System.out.println("Offer accepted!");
                        invalid = false;
                    }
                } else {
                    System.out.println("Invalid in put");
                }
            } while (invalid);

        } else if (postToReply instanceof Sale) {
            System.out.println("\nHighest offer:\t$" + ((Sale) postToReply).getHighestOffer() + "\nMinimum raise:\t$" + ((Sale) postToReply).getMinRaise());
            invalid = true;
            do {
                System.out.print("Enter you offer or 'Q' to quit: ");
                String option = sc.nextLine();

                if (option.equalsIgnoreCase("Q")) {
                    return;
                } else if (isDouble(option)) {
                    Reply reply = new Reply(postToReply.getId(), Double.parseDouble(option), username);
                    if (postToReply.handleReply(reply)) {
                        if ( reply.getValue() < ((Sale) postToReply).getAskingPrice()) {
                            System.out.println("However, you offer is below the asking price.\nThis item is still on sale.");
                        }

                        if (reply.getValue() >= ((Sale) postToReply).getAskingPrice()) {
                            System.out.println("Congratulation! The " + ((Sale) postToReply).getTitle() + " has been sold to you.\nPlease contact the owner " + ((Sale) postToReply).getCreatorId() + " for more details.");
                        }
                        invalid = false;
                    }
                    else {
                        if (reply.getValue() - ((Sale) postToReply).getHighestOffer() < ((Sale) postToReply).getMinRaise()) {
                            System.out.println("Offer not accepted!");
                        }
                    }
                } else {
                    System.out.println("Invalid in put");
                }
            } while (invalid);
        }
    }

    //display my post implement
    private void displayMyPost() {
        boolean unfounded = true;
        for (Post post : posts) {
            if (post.getCreatorId().equalsIgnoreCase(username)) {
                System.out.println(post.getReplyDetails());
                System.out.println("-------------------");
                unfounded = false;
            }
        }
        if (unfounded) {
            System.out.println("You don't have any post of your own!");
        }
    }

    //Display All post implement
    private void displayAllPost() {
        if (posts.size() != 0) {
            for (Post post : posts) {
                System.out.println(post.getPostDetails());
                System.out.println("-------------------");
            }
        } else {
            System.out.println("There don't have any post!");
        }

    }

    //close post function implement
    private void closePost() {
        String postID;
        boolean notFound = true;
        boolean closed = false;

        while (true) {
            System.out.print("Enter post ID or 'Q' to quit: ");
            postID = sc.nextLine();

            if (postID.equalsIgnoreCase("Q")) {
                return;
            }

            for (Post post : posts) {
                if (postID.equalsIgnoreCase(post.getId())) {
                    notFound = false;
                    if (!post.getCreatorId().equalsIgnoreCase(username)) {
                        System.out.println("Request denied! You're not the owner of this post. ");
                        return;
                    }
                    post.setStatus("CLOSED");
                    System.out.println("Close success! Post " + post.getId() + " is close");
                    closed = true;
                    break;
                }
            }
            if (notFound) {
                System.out.println("Invalid post ID! Post not found");
            }
            if (closed) {
                return;
            }
        }
    }

    //Delete post function implement
    private void deletePost() {
        String postID;
        boolean notFound = true;
        boolean deleted = false;

        while (true) {

            System.out.print("Enter post ID or 'Q' to quit: ");
            postID = sc.nextLine();
            //Quit when user enter "Q" or "q"
            if (postID.equalsIgnoreCase("Q")) {
                return;
            }

            for (Post post : posts) {
                if (postID.equalsIgnoreCase(post.getId())) {
                    notFound = false;
                    if (!post.getCreatorId().equalsIgnoreCase(username)) {
                        System.out.println("Request denied! You're not the owner of this post. ");
                        return;
                    }
                    System.out.println("Succeeded! Post " + post.getId() + " deleted.");
                    getPosts().remove(post);
                    deleted = true;
                    break;
                }
            }
            if (notFound) {
                System.out.println("Invalid post ID! Post not found");
            }

            if (deleted) {
                return;
            }
        }
    }


    //
    //functions above will be reused in upper functions

    public ArrayList<Post> getPosts() {
        return posts;
    }

    //Make sure input is Integer and return an integer
    private int checkInt() {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private boolean isDouble(String a) {
        try {
            Double x = Double.parseDouble(a);
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    //User name should begin with 's', followed by at least one integer, for example s1, up to seven integers, for example s3183028.
    private String checkUsername() {
        boolean invalid = false;
        String username;
        do {
            invalid = false;
            username = sc.nextLine();
            if ((username.startsWith("s") || username.startsWith("S")) && username.length() >= 2 && username.length() <= 8 && isNumeric(username.substring(1)))
                invalid = false;
            else {
                System.out.print("Invalid input! Please try again: ");
                invalid = true;
            }
        } while (invalid);
        return username;
    }

    private String newTitle() {//helps to record post title.
        String name;
        do{
            System.out.print("Name: ");
            name = sc.nextLine();
        }while(name.length() <= 0);
        return name;
    }

    //
    private String newDescription() {//helps to record description of post
        String description;
        do{
            System.out.print("Description: ");
            description = sc.nextLine();
        }while(description.length() <= 0);

        return description;
    }

    private String newCreatorID() {//get user name
        return username;
    }

    private String newVenue() {//records Venue of the post
        String venue;
        do{
            System.out.print("Venue: ");
            venue = sc.nextLine();
        }while(venue.length() <= 0);
        return venue;
    }

    //record date of event also check date is valid or not
    private String newDate() {
        boolean invalid;
        String date;
        do {
            System.out.print("Date: ");

            date = sc.nextLine();
            if (isValidDate(date)) {
                invalid = false;
            } else invalid = true;
        } while (invalid);

        return date;
    }

    private int newCapacity() { //record capacity of event
        int capacity = 0;
        boolean invalid;
        do {
            System.out.print("Capacity: ");
            try {
                capacity = Integer.parseInt(sc.nextLine());
                    if(capacity > 0){
                        invalid = false;
                    }
                    else {
                        invalid = true;
                    }

            } catch (NumberFormatException e) {
                invalid = true;
            }
        } while (invalid);

        return capacity;
    }

    private double newAskingPrice() {// Asking price should be a non-negative double number
        double askingPrice;
        do {
            System.out.print("Asking price: ");
            try {
                askingPrice = Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                askingPrice = -1;
            }
        } while (askingPrice < 0);

        return askingPrice;
    }

    private double newMinRaise() {// record minimum raise of offer
        double minRaise;
        do {
            System.out.print("Minimum raise: ");
            try {
                minRaise = Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                minRaise = -1;
            }
        } while (minRaise < 0);

        return minRaise;
    }

    private double newProposedPrice() {//record proposed price
        double proposedPrice = 0;

        boolean invalid = false;
        do {
            System.out.print("Proposed price: ");
            try {
                proposedPrice = Double.parseDouble(sc.nextLine());
                if(proposedPrice > 0){
                    invalid = false;
                }
                else {
                    invalid = true;
                }
            } catch (NumberFormatException e) {
                invalid = true;
            }
        } while (invalid);

        return proposedPrice;
    }

    private boolean isValidDate(String value) {// Check the date is valid or not
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
        }
        return date != null;
    }

    //after 's' or 'S' ,Check is Int or not
    private boolean isNumeric(String a) {
        try {
            Integer.parseInt(a);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
