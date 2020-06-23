package controller;

import exception.InvalidReplyException;
import exception.JoinAgainException;
import exception.NoReplyException;
import exception.PostIsCloseException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.util.ArrayList;

public class PostListViewCellController extends ListCell<Post> {
    @FXML
    private ImageView postImageView;
    @FXML
    private Button replyButton;
    @FXML
    private Button moreDetailsButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Label postIDLabel;
    @FXML
    private Label creatorIDLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label label01;
    @FXML
    private Label label00;
    @FXML
    private Label label02;
    @FXML
    private Label label10;
    @FXML
    private Label label11;
    @FXML
    private Label label12;
    @FXML
    private Label label03;
    @FXML
    private Label label04;
    @FXML
    private Label label13;
    @FXML
    private Label label14;
    @FXML
    private HBox hbox;
    private String currentUserID;
    private FXMLLoader mlLoader;

    //Constructor
    public PostListViewCellController(String currentUserID) {
        this.currentUserID = currentUserID;
    }

    //initialize setting
    @FXML
    public void initialize() {
        //initialize the all label as empty
        titleLabel.setText("");
        postIDLabel.setText("");
        creatorIDLabel.setText("");
        statusLabel.setText("");
        descriptionLabel.setText("");

        label00.setText("");
        label01.setText("");
        label02.setText("");
        label03.setText("");
        label04.setText("");
        label10.setText("");
        label11.setText("");
        label12.setText("");
        label13.setText("");
        label14.setText("");


    }

    @Override
    protected void updateItem(Post post, boolean empty) {
        super.updateItem(post, empty);
        setText(null);
        setGraphic(null);
        if (empty || post == null) {
            setText(null);
            setGraphic(null);
        } else {
            //set the controller of each cell
            mlLoader = new FXMLLoader(getClass().getResource("/view/post_listView_cell.fxml"));
            mlLoader.setController(this);
            try {
                mlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //set common details of post
            setCommonDetail(post);
            //The more Details Button can be only used by current user is the creator of this post
            if (post.getCreatorId().equalsIgnoreCase(currentUserID)) {
                moreDetailsButton.setOnAction(e -> detailDisplay(post, e));
                replyButton.setOnAction(e -> Util.AlertBox("UniLink", "\nYou are the creator of this post!\nYou can't reply it."));

            } else {
                moreDetailsButton.setOnAction(e -> Util.AlertBox("UniLink", "\nYou are not the creator of this post!\nYou can't see the details."));
                replyButton.setOnAction(e -> {
//                    if (handleReply(post))
                    handleReply(post);
                    //update this cell if (handelReply(post)) == true
                    updateItem(post, empty);
                });
            }

            //set other details according the types of post
            if (post instanceof Event) {
                setEventDetail((Event) post);
            } else if (post instanceof Sale) {
                setSaleDetail(post);
            } else if (post instanceof Job) {
                setJobDetail((Job) post);
            }
            setText(null);
            setGraphic(hbox);
        }
    }

    private void setJobDetail(Job post) {
        hbox.setStyle("-fx-background-color: LIGHTYELLOW ");
        label00.setText("Proposed price:");
        label01.setText("Lowest offer:");

        label10.setText("$" + post.getProposedPrice());
        label11.setText("$" + post.getLowestOffer());
    }

    private void setSaleDetail(Post post) {
        hbox.setStyle("-fx-background-color: LIGHTPINK ");
        label00.setText("Highest offer:");
        label01.setText("Minimum raise:");

        label10.setText("$" + ((Sale) post).getHighestOffer());
        label11.setText("$" + ((Sale) post).getMinRaise());

        //Check information about current user to decide show Asking price or not
        if (currentUserID.equalsIgnoreCase(post.getCreatorId())) {
            label02.setText("Asking price:");
            label12.setText("$" + ((Sale) post).getAskingPrice());
        }
    }

    private void setEventDetail(Event post) {
        hbox.setStyle("-fx-background-color: LIGHTCYAN ");
        label00.setText("Venue:");
        label01.setText("Date:");
        label02.setText("Capacity:");
        label03.setText("Attendee count:");

        label10.setText(post.getVenue());
        label11.setText(post.getDate());
        label12.setText(Integer.toString(post.getCapacity()));
        label13.setText(Integer.toString(post.getAttendeeCount()));
    }

    private void detailDisplay(Post post, ActionEvent e) {
        String fileAddress = "/view/post_detail_view.fxml";
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fileAddress));
        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Scene scene = new Scene(parent);
        //access the controller and call a method
        PostDetailViewController controller = loader.getController();
        //pass the  username to MainWindowController
        controller.initData(post, getReplies(post.getId()));

        //get Window
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    private void setCommonDetail(Post post) {
        titleLabel.setText(post.getTitle());
        postIDLabel.setText(post.getId());
        creatorIDLabel.setText(post.getCreatorId());
        statusLabel.setText(post.getStatus());
        descriptionLabel.setText(post.getDescription());
        postImageView.setImage(post.getPhoto());
    }

    private boolean handleReply(Post post) {
        //user cannot reply a closed post
        if (post.getStatus().equalsIgnoreCase("CLOSED")) {
            try {
                throw new PostIsCloseException("\nReply denied!\nThis post is closed.");
            } catch (PostIsCloseException e) {
                Util.AlertBox("UniLink", e.getMessage());
            }
            return false;
        }
        if (post instanceof Event) {
            for (Reply reply : getReplies(post.getId())) {
                if (reply.getResponderId().equalsIgnoreCase(currentUserID)) {
                    try {
                        throw new JoinAgainException("\nReply denied!\nYou have already joint this event.");
                    } catch (JoinAgainException e) {
                        Util.AlertBox("UniLink", e.getMessage());
                    }

                    return false;
                }
            }
            if (((Event) post).getAttendeeCount() >= ((Event) post).getCapacity()) {
                Util.AlertBox("UniLink", "\nReply denied!\nThis event is full.");
                return false;
            }
            //handle this reply of event
            Reply reply = new Reply(post.getId(), 1, currentUserID);
            try {
                if (post.handleReply(reply)) {
                    Util.AlertBox("UniLink", "\nEvent registration accepted!");
                    return true;
                }
            } catch (InvalidReplyException e) {
                e.printStackTrace();
            }
        } else if (post instanceof Sale || post instanceof Job) {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("UniLink");
            window.setMinWidth(250);
            String fileAddress = "/view/value_input_dialog.fxml";
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fileAddress));
            Parent parent = null;
            try {
                parent = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(parent);
            // access the controller and pass the current user id by call a method
            ValueInputDialogController controller = loader.getController();
            controller.initData(post);
            window.setScene(scene);
            window.showAndWait();
            return true;
        }
        return false;
    }

    //Get all replies of this post
    private ArrayList<Reply> getReplies(String postID) {
        ArrayList<Reply> replies = new ArrayList<>();
        for (Reply reply : UniLinkDatabase.getReplies()) {
            if (reply.getPostId().equalsIgnoreCase(postID))
                replies.add(reply);
        }
        if (replies == null)
            try {
                throw new NoReplyException("There is no reply in this post");
            } catch (NoReplyException e) {
                e.printStackTrace();
            }

        return replies;
    }
}
