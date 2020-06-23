package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PostDetailViewController {
    @FXML private Label titleLabel;
    @FXML private Label errorLabel;
    @FXML private Button uploadImageButton;
    @FXML private ImageView imageView;
    @FXML private TableView tableView;
    @FXML private GridPane gridPane;
    private Post post;
    private boolean editable;
    private File filePath;
    private Label status;
    private TextField title;
    private TextField description;
    private TextField venue;
    private DatePicker datePicker;
    private TextField capacity;
    private TextField minRaise;
    private TextField askPrice;
    private TextField proposedPrice;
    private ObservableList<Reply> replyObservableList;
    private TableColumn<Reply, String> responderColumn;






    public void initData(Post post, ArrayList<Reply> replies) {
        //initialize date
        this.post = post;
        imageView.setImage(new Image(post.getPhotoPath()));
        filePath = new File(post.getPhotoPath());
        titleLabel.setText("Current User: "+ LoginWindowController.getCurrentUserID());

        //set grid pane
        setGridPane(post);

        replyObservableList = FXCollections.observableList(replies);
        if (post instanceof Event) {
            //set date pattern to set default date in datePicker
            String pattern = "dd/MM/yyyy";
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
            //Create Label, text field, and date picker to display and modify information about the post
            setGripPaneOfEvent((Event) post, replies, dateTimeFormatter);
            //Set participant column
            setEventReplyTable();
        } else if (post instanceof Sale || post instanceof Job) {

            //set responder column
            TableColumn<Reply, String> responderColumn = new TableColumn<>("Responder");
            responderColumn.setMinWidth(300);
            responderColumn.setCellValueFactory(new PropertyValueFactory<Reply, String>("responderId"));

            //set offer column
            TableColumn<Reply, String> offerColumn = new TableColumn<>("Offer");
            offerColumn.setMinWidth(300);
            offerColumn.setCellValueFactory(new PropertyValueFactory<Reply, String>("value"));

            //set default sort type of offerColumn
            if (post instanceof Sale) {
                setGridPaneOfSale((Sale) post);

                //set default sort type of offerColumn
                offerColumn.setSortType(TableColumn.SortType.DESCENDING);
            } else {
                setGridPaneOfJob((Job) post);
                //set default sort type of offerColumn
                offerColumn.setSortType(TableColumn.SortType.ASCENDING);
            }
            tableView.setItems(replyObservableList);
            tableView.getColumns().addAll(responderColumn, offerColumn);
            tableView.getSortOrder().add(offerColumn);
        }
        //check if the post is editable
        //set post information disable to change if the post has some replies
        checkEditable(replies);
    }

    private void setEventReplyTable() {
        responderColumn = new TableColumn<>("Participants");
        responderColumn.setMinWidth(300);
        responderColumn.setCellValueFactory(new PropertyValueFactory<Reply, String>("responderId"));

        tableView.setItems(replyObservableList);
        tableView.getColumns().add(responderColumn);
    }

    private void checkEditable(ArrayList<Reply> replies) {
        if (replies.size() == 0)
            editable = true;
        else {
            editable = false;
            setToUneditable();
        }
    }

    private void setGridPaneOfJob(Job post) {
        Label proposedPriceLabel = new Label("Proposed price");
        proposedPrice = new TextField(String.valueOf(post.getProposedPrice()));

        GridPane.setConstraints(proposedPriceLabel, 0, 5);
        GridPane.setConstraints(proposedPrice, 1, 5);

        gridPane.getChildren().addAll(
                proposedPriceLabel, proposedPrice
        );
    }

    private void setGridPaneOfSale(Sale post) {
        Label minRaiseLabel = new Label("Minimum Raise");
        minRaise = new TextField(String.valueOf(post.getMinRaise()));
        Label askPriceLabel = new Label("Asking Price");
        askPrice = new TextField(String.valueOf(post.getAskingPrice()));

        //set rest items
        GridPane.setConstraints(minRaiseLabel, 0, 5);
        GridPane.setConstraints(minRaise, 1, 5);
        GridPane.setConstraints(askPriceLabel, 0, 6);
        GridPane.setConstraints(askPrice, 1, 6);

        gridPane.getChildren().addAll(
                minRaiseLabel, minRaise,
                askPriceLabel, askPrice
        );
    }

    private void setGripPaneOfEvent(Event post, ArrayList<Reply> replies, DateTimeFormatter dateTimeFormatter) {
        Label venueLabel = new Label("Venue");
        venue = new TextField(post.getVenue());
        Label dateLabel = new Label("Date");
        datePicker = new DatePicker();
        datePicker.setValue(LocalDate.parse(post.getDate(), dateTimeFormatter));
        Label capacityLabel = new Label("Capacity");
        capacity = new TextField(String.valueOf(post.getCapacity()));
        Label attendeeCountLabel = new Label("Attendee Count");
        Label attendeeCount = new Label(String.valueOf(replies.size()));

        //set rest items
        GridPane.setConstraints(venueLabel, 0, 5);
        GridPane.setConstraints(venue, 1, 5);
        GridPane.setConstraints(dateLabel, 0, 6);
        GridPane.setConstraints(datePicker, 1, 6);
        GridPane.setConstraints(capacityLabel, 0, 7);
        GridPane.setConstraints(capacity, 1, 7);
        GridPane.setConstraints(attendeeCountLabel, 0, 8);
        GridPane.setConstraints(attendeeCount, 1, 8);

        //add rest items into gridPane
        gridPane.getChildren().addAll(
                venueLabel, venue,
                dateLabel, datePicker,
                capacityLabel, capacity,
                attendeeCountLabel, attendeeCount
        );
    }

    private void setGridPane(Post post) {
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        //Set editable information of this post
        Label postIDLabel = new Label("Post ID");
        Label postID = new Label(post.getId());
        Label titleLabel = new Label("Title");
        title = new TextField(post.getTitle());
        Label creatorIDLabel = new Label("Creator ID");
        Label creator = new Label(post.getCreatorId());
        Label statusLabel = new Label("Status");
        status = new Label(post.getStatus());
        Label descriptionLabel = new Label("Description");
        description = new TextField(post.getDescription());

        //set the position of items in gripPane
        GridPane.setConstraints(postIDLabel, 0, 0);
        GridPane.setConstraints(postID, 1, 0);
        GridPane.setConstraints(titleLabel, 0, 1);
        GridPane.setConstraints(title, 1, 1);
        GridPane.setConstraints(creatorIDLabel, 0, 2);
        GridPane.setConstraints(creator, 1, 2);
        GridPane.setConstraints(statusLabel, 0, 3);
        GridPane.setConstraints(status, 1, 3);
        GridPane.setConstraints(descriptionLabel, 0, 4);
        GridPane.setConstraints(description, 1, 4);

        //add items into gridPane
        gridPane.getChildren().addAll(postIDLabel, postID,
                titleLabel, title,
                creatorIDLabel, creator,
                statusLabel, status,
                descriptionLabel, description);
    }

    private void setToUneditable() {
        title.setDisable(true);
        description.setDisable(true);
        imageView.setDisable(true);
        uploadImageButton.setDisable(true);
        if (post instanceof Event) {
            venue.setDisable(true);
            datePicker.setDisable(true);
            capacity.setDisable(true);
        } else if (post instanceof Sale) {
            minRaise.setDisable(true);
            askPrice.setDisable(true);
        } else if (post instanceof Job) {
            proposedPrice.setDisable(true);
        }


    }

    @FXML
    public void uploadImage(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        // Set to user's directory or go to the default C drive if cannot access
        String userDirectoryString = System.getProperty("user.home");
        File userDirectory = new File(userDirectoryString);

        if (!userDirectory.canRead())
            userDirectory = new File("c:/");

        fileChooser.setInitialDirectory(userDirectory);

        // Set restrict file chooser only can choose *.png *.jpg *.svg
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "All Images", "*.jpg", "*.png", "*.svg","*.jpeg"));

        this.filePath = fileChooser.showOpenDialog(stage);

        if (filePath != null && filePath.isFile()) {
            imageView.setImage(new Image(filePath.toURI().toString()));
        }
    }


    public void closePost(ActionEvent event) {
        status.setText("CLOSED");
        post.setStatus("CLOSED");
    }

    public void deletePost(ActionEvent event) {
        if (editable) {
            //remove post data
            UniLinkDatabase.getPosts().remove(post.getId());

            if(post instanceof Event)
                UniLinkDatabase.getEvents().remove(post.getId());
            else if(post instanceof Sale)
                UniLinkDatabase.getSales().remove(post.getId());
            else if(post instanceof Job)
                UniLinkDatabase.getJobs().remove(post.getId());

            //remove replies data
            UniLinkDatabase.getReplies().removeIf(reply -> reply.getPostId().equals(post.getId()));

            Util.AlertBox("UniLink", "Post delete successful!");

            //back to main window
            try {
                backToMainWindow(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            Util.AlertBox("UniLink", "\nSomeone has replied your post.\nYou can't delete this post.");
    }

    public void savePost(ActionEvent event) throws IOException {
        if (!editable) {
            Util.AlertBox("UniLink", "\nSomeone has replied your post.\nYou can't modify it.\n [Nothing will change]");
            return;
        }
        if (postReadyToCreate()) {
            UniLinkDatabase.getPosts().get(post.getId()).setTitle(title.getText());
            UniLinkDatabase.getPosts().get(post.getId()).setDescription(description.getText());
            UniLinkDatabase.getPosts().get(post.getId()).setStatus(status.getText());
            if (post instanceof Event) {
                //set date pattern
                String pattern = "dd/MM/yyyy";
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
                // Change values in this post
                ((Event) (UniLinkDatabase.getPosts().get(post.getId()))).setVenue(venue.getText());
                ((Event) (UniLinkDatabase.getPosts().get(post.getId()))).setDate(dateTimeFormatter.format(datePicker.getValue()));
                ((Event) (UniLinkDatabase.getPosts().get(post.getId()))).setCapacity(Integer.parseInt(capacity.getText()));
            } else if (post instanceof Sale) {
                ((Sale) (UniLinkDatabase.getPosts().get(post.getId()))).setMinRaise(Double.parseDouble(minRaise.getText()));
                ((Sale) (UniLinkDatabase.getPosts().get(post.getId()))).setAskingPrice(Double.parseDouble(minRaise.getText()));

            } else if (post instanceof Job) {
                ((Job) (UniLinkDatabase.getPosts().get(post.getId()))).setProposedPrice(Double.parseDouble(proposedPrice.getText()));
            }

            if (!filePath.getName().equals(post.getPhotoPath())) {
                UniLinkDatabase.getPosts().get(post.getId()).setPhotoPath(filePath.getName());
                // save image to image directory
                String imageDir = String.valueOf(filePath);
                String saveDir = "images" + File.separator + filePath.getName();
                Util.saveImageFromDir(imageDir, saveDir);
            }
            Util.AlertBox("UniLink", "\nYour modify is saved");
        }
    }

    private boolean postReadyToCreate() {
        StringBuilder errorMessage = new StringBuilder();

        if (title.getText().length() <= 0)
            errorMessage.append("Please input event name.\n");

        int MAX_DESCRIPTION = 80;
        if (description.getText().length() > MAX_DESCRIPTION)
            errorMessage.append("Event description is too long.\n");


        if (post instanceof Event) {
            eventReadyToCreate(errorMessage);
        } else if (post instanceof Sale) {
            saleReadyToCreate(errorMessage);
        } else if (post instanceof Job) {
            jobReadyToCreate(errorMessage);
        }

        errorLabel.setText(errorMessage.toString());

        return errorMessage.length() == 0;
    }


    private void eventReadyToCreate(StringBuilder errorMessage) {

        if (venue.getText().length() <= 0)
            errorMessage.append("Please input event venue.\n");

        try {
            if (Integer.parseInt(capacity.getText()) <= 0)
                errorMessage.append("Invalid capacity.\n");
        } catch (NumberFormatException e) {
            errorMessage.append("Capacity should be an integer\n");
        }

        try {
            if (datePicker.getValue().isBefore(LocalDate.now()))
                errorMessage.append("Invalid datetime.\n");
        } catch (NullPointerException e) {
            errorMessage.append("You should choose a datetime.\n");
        }

    }

    private void saleReadyToCreate(StringBuilder errorMessage) {
        try {
            if (Double.parseDouble(askPrice.getText()) <= 0)
                errorMessage.append("Asking price should be larger than 0.\n");
        } catch (NumberFormatException e) {
            errorMessage.append("Asking price should be a decimal\n");
        }

        try {
            if (Double.parseDouble(minRaise.getText()) <= 0)
                errorMessage.append("Minimal raise should be larger than 0.\n");
        } catch (NumberFormatException e) {
            errorMessage.append("Minimal raise should be a decimal\n");
        }
    }

    private void jobReadyToCreate(StringBuilder errorMessage) {
        try {
            if (Double.parseDouble(proposedPrice.getText()) <= 0)
                errorMessage.append("Asking price should be larger than 0.\n");
        } catch (NumberFormatException e) {
            errorMessage.append("Asking price should be a decimal\n");
        }

    }

    public void backToMainWindow(ActionEvent event) throws IOException {
        String fileAddress = "/view/main_window_view.fxml";

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fileAddress));
        Parent mainViewParent = loader.load();

        Scene mainWindowViewScene = new Scene(mainViewParent);

        //access the controller and call a method
        MainWindowController controller = loader.getController();

        //pass the  username to MainWindowController
        controller.getCurrentUserIDLabel().setText(LoginWindowController.getCurrentUserID());

        //get Window
        Stage mainWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        mainWindow.setScene(mainWindowViewScene);
        mainWindow.show();
    }
}
