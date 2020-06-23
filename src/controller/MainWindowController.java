package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainWindowController {
    @FXML
    private Label titleLabel;
    @FXML
    private ChoiceBox<String> typeChoiceBox;
    @FXML
    private ChoiceBox<String> statusChoiceBox;
    @FXML
    private ChoiceBox<String> creatorChoiceBox;

    // show post information in a listView
    @FXML
    private ListView<Post> postInfoListView;
    private ObservableList<Post> postInfoObservableList;

    @FXML
    private Label currentUserIDLabel;

    private String typeFilter = "All";
    private String statusFilter = "All";
    private String creatorFilter = "All";
    private FileChooser chooser;
    private final String SPLIT_CODE = "@REPLY@";

    public MainWindowController() {
        postInfoObservableList = FXCollections.observableArrayList(UniLinkDatabase.getPosts().values());
    }

    @FXML
    public void initialize() {
        // set choice box items
        typeChoiceBox.getItems().addAll("All", "Event", "Sale", "Job");
        typeChoiceBox.setValue("All");
        statusChoiceBox.getItems().addAll("All", "Open", "Closed");
        statusChoiceBox.setValue("All");
        creatorChoiceBox.getItems().addAll("All", "My Post");
        creatorChoiceBox.setValue("All");

        // set ListView items
        postInfoListView.setItems(postInfoObservableList);
        postInfoListView.setCellFactory(listView -> new PostListViewCellController(currentUserIDLabel.getText()));

        // turn off selection mode
        postInfoListView.setFocusTraversable(false);

        typeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            typeFilter = newValue;
            filter();
        });

        statusChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            statusFilter = newValue;
            filter();
        });

        creatorChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            creatorFilter = newValue;
            filter();
        });


    }

    private void filter() {
        postInfoListView.setItems(postInfoObservableList.filtered(p -> {
            if (typeFilter.equals("Event") && p instanceof Event) {
                return true;
            } else if (typeFilter.equals("Sale") && p instanceof Sale) {
                return true;
            } else if (typeFilter.equals("Job") && p instanceof Job) {
                return true;
            } else if (typeFilter.equals("All")) {
                return true;
            }
            return false;
        }).filtered(p -> {
            if (statusFilter.equals("Open") && p.getStatus().equalsIgnoreCase("OPEN")) {
                return true;
            } else if (statusFilter.equals("Closed") && p.getStatus().equalsIgnoreCase("CLOSED")) {
                return true;
            } else if (statusFilter.equals("All")) {
                return true;
            }
            return false;
        }).filtered(p -> {
            if (creatorFilter.equals("My Post") && p.getCreatorId().equalsIgnoreCase(currentUserIDLabel.getText())) {
                return true;
            } else if (creatorFilter.equals("All")) {
                return true;
            }
            return false;
        }));
    }

    public Label getCurrentUserIDLabel() {
        return currentUserIDLabel;
    }


    @FXML
    private void displayDeveloperInfo(ActionEvent event) {
        Util.AlertBox("Developer", "Student Name: Fanwei Wang\nStudent ID: S3801161");
    }

    @FXML
    private void quitUniLink(ActionEvent event) {
        UniLinkDatabase.update();
        Platform.exit();
        System.exit(0);

    }

    @FXML
    private void createNewEvent(ActionEvent event) throws IOException {

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("UniLink");
        window.setMinWidth(250);

        String fileAddress = "/view/create_event_form.fxml";
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fileAddress));
        Parent createNewEventParent = loader.load();

        Scene createNewEventScene = new Scene(createNewEventParent);

        // access the controller and pass the current user id by call a method
        CreateEventFormController controller = loader.getController();
        controller.initData(currentUserIDLabel.getText());

        window.setScene(createNewEventScene);
        window.showAndWait();
        postInfoObservableList = FXCollections.observableArrayList(UniLinkDatabase.getPosts().values());
        filter();
    }

    public void createNewSale(ActionEvent event) throws IOException {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("UniLink");
        window.setMinWidth(250);

        String fileAddress = "/view/create_sale_form.fxml";
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fileAddress));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        // access the controller and pass the current user id by call a method
        CreateSaleFormController controller = loader.getController();
        controller.initData(currentUserIDLabel.getText());

        window.setScene(scene);
        window.showAndWait();
        postInfoObservableList = FXCollections.observableArrayList(UniLinkDatabase.getPosts().values());
        filter();
    }

    public void createNewJob(ActionEvent event) throws IOException {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("UniLink");
        window.setMinWidth(250);

        String fileAddress = "/view/create_job_form.fxml";
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fileAddress));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        // access the controller and pass the current user id by call a method
        CreateJobFormController controller = loader.getController();
        controller.initData(currentUserIDLabel.getText());

        window.setScene(scene);
        window.showAndWait();
        postInfoObservableList = FXCollections.observableArrayList(UniLinkDatabase.getPosts().values());
        filter();
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        Util.changeScene(event, "/view/login_window_view.fxml");
        // TODO save Data
    }

    @FXML
    public void exportData(ActionEvent event) {
        FileWriter fileWriter;
        chooser = new FileChooser();
        chooser.setTitle("Choose directory to save data");
        chooser.setInitialFileName("export_data.txt");
        File selectedDirectory = chooser.showSaveDialog(null);
        if (selectedDirectory != null) {
            try {
                fileWriter = new FileWriter(selectedDirectory);

                for (Post post : UniLinkDatabase.getPosts().values()) {
                    fileWriter.write(post.toString() + "\n");
                }
                //use split_code to identify the play to split data
                fileWriter.write(SPLIT_CODE);
                for (Reply reply : UniLinkDatabase.getReplies()) {
                    fileWriter.write(reply.toString() + "\n");
              }
                fileWriter.close();
            } catch (IOException e) {
                Util.AlertBox("UniLink", "\nFile cannot be created, or cannot be opened.");
            }
        } else {
            Util.AlertBox("UniLink", "\nFile is not valid.");
        }


    }

    @FXML
    public void importData(ActionEvent event) {
        chooser = new FileChooser();
        File selectedDirectory = chooser.showOpenDialog(null);
//        System.out.println(selectedDirectory);
        if (selectedDirectory != null) {
            try {
                Scanner sc = new Scanner(selectedDirectory);
                StringBuilder data = new StringBuilder();
                while (sc.hasNext()) {
                    data.append(sc.nextLine() + "\n");
                }

                int count = 0;
                for (String retval : data.toString().split(SPLIT_CODE)) {
                    if (count++ == 0) {
                        importPost(retval);
                    } else
                        importReply(retval);
                }

                postInfoObservableList = FXCollections.observableArrayList(UniLinkDatabase.getPosts().values());
                filter();
            } catch (FileNotFoundException e) {
                Util.AlertBox("UniLink", "\nFile Not Found.");
            }
        }else
            Util.AlertBox("UniLink", "\nFile is not valid.");
    }

    private void importReply(String retval) {
        Scanner sc = new Scanner(retval);

        while(sc.hasNext()){
            String replyLine = sc.nextLine();
            int count = 0;
            Reply reply = new Reply();
            for(String ret: replyLine.split("\\t")){
                if(count == 0){
                    reply.setPostId(ret);
                    count++;
                }else if(count == 1){
                    try {
                        reply.setValue(Double.parseDouble(ret));
                    }catch (NumberFormatException e){
                        reply.setValue(-1);
                    }
                    count++;
                }else if(count ==2){
                    reply.setResponderId(ret);
                }
            }
            if(replyNotExist(reply)) {
                UniLinkDatabase.getReplies().add(reply);
            }
        }
    }

    private boolean replyNotExist(Reply reply) {
        for(Reply re: UniLinkDatabase.getReplies()){
            if(reply.getPostId().equalsIgnoreCase(re.getPostId()) && reply.getResponderId().equalsIgnoreCase(re.getResponderId())){
                return false;
            }
        }
        return true;
    }


    private void importPost(String retval) {
        Scanner sc = new Scanner(retval);
        while (sc.hasNext()){
            String postLine = sc.nextLine();
            ArrayList<String> postArray = new ArrayList<>();
            for(String ret: postLine.split("\\t")){
                postArray.add(ret);
            }

            String url = postArray.get(5);
            File file = new File(url);
            if(!file.exists()){
                postArray.set(5,"Default_Image.png");
            }
            if(postArray.get(0).toUpperCase().contains("EVE")){
                Post post = new Event(postArray.get(0), postArray.get(1), postArray.get(2), postArray.get(3), postArray.get(4), postArray.get(5), postArray.get(6), postArray.get(7), Integer.parseInt(postArray.get(8)));
                UniLinkDatabase.getPosts().put(post.getId(),post);
            }else if (postArray.get(0).toUpperCase().contains("SAL")){
                Post post = new Sale(postArray.get(0), postArray.get(1), postArray.get(2), postArray.get(3), postArray.get(4), postArray.get(5),Double.parseDouble(postArray.get(6)),Double.parseDouble(postArray.get(7)));
                UniLinkDatabase.getPosts().put(post.getId(),post);
            }else if(postArray.get(0).toUpperCase().contains("JOB")){
                Post post = new Job(postArray.get(0), postArray.get(1), postArray.get(2), postArray.get(3), postArray.get(4), postArray.get(5),Double.parseDouble(postArray.get(6)));
                UniLinkDatabase.getPosts().put(post.getId(),post);
            }
        }


    }
}
