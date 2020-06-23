package model;

import exception.InvalidReplyException;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;

public abstract class Post {
    private String id; // Uniquely identifies each post
    private String title; // Summarise the post
    private String description; // Provides more information about the post.
    private String creatorId; // Is the id of the student who created the post
    private String status; // Indicates whether the post is open to receive
    // replies or closed and no longer accepts replies
    // GUI various
    private Image photo;
    private String photoPath = "Default_Image.png";

    private ArrayList<Reply> replies = new ArrayList<Reply>();

    public Post(String title, String description, String creatorId) {
        this.id = idGenerator();
        this.title = title;
        this.description = description;
        this.creatorId = creatorId;
        this.status = "OPEN";
        // set Default image here
        photo = new Image(photoPath);

    }

    public Post(String title, String description, String creatorId,
                String photoPath) {
        this.id = idGenerator();
        this.title = title;
        this.description = description;
        this.creatorId = creatorId;
        this.status = "OPEN";
        this.photoPath = photoPath;
        this.photo = new Image(photoPath);
    }

    public Post(String id, String title, String description, String creatorId,
                String status, String photoPath) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creatorId = creatorId;
        this.status = status;
        this.photoPath = photoPath;
        this.photo = new Image(photoPath);


    }

    // get and set methods
    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
        this.photo = new Image(photoPath);
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Reply> getReplies() {
        return replies;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static int getUniqueID() {
        return 0;
    }

    public static void setUniqueID(int uniqueID) {

    }

    // get the information about a post
    public String getPostDetails() {
        return String.format("%-20s", "\nID:") + id
                + String.format("%-20s", "\nTitle:") + title
                + String.format("%-20s", "\nDescription:") + description
                + String.format("%-20s", "\nCreator ID:") + creatorId
                + String.format("%-20s", "\nStatue:") + status;
    }

    // abstract method
    public abstract boolean handleReply(Reply reply) throws InvalidReplyException;

    public abstract String getReplyDetails();

    // Generate unique ID
    public abstract String idGenerator();

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(id);
        ret.append("\t");
        ret.append(title);
        ret.append("\t");
        ret.append(description);
        ret.append("\t");
        ret.append(creatorId);
        ret.append("\t");
        ret.append(status);
        ret.append("\t");
        ret.append(photoPath);
        ret.append("\t");
        return ret.toString();
    }
}
