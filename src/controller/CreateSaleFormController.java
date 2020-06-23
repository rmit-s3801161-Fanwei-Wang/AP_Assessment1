package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Event;
import model.Post;
import model.Sale;
import model.UniLinkDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CreateSaleFormController {
    @FXML public TextField saleName;
    @FXML public TextField askingPrice;
    @FXML public TextField minRaise;
    @FXML public TextArea saleDescription;
    @FXML public ImageView imageView;
    @FXML public Label fileName;
    @FXML public Label errorLabel;

    private String currentUserID;
    private FileChooser fileChooser;
    private File filePath = new File("Default_Image.png");
    private final int MAX_DESCRIPTION = 80;

    public void initialize() {
        //set default image before user choose one
        imageView.setImage(new Image(filePath.getName()));
    }

    // pass current user id to this window
    public void initData(String currentUserID) {
        this.currentUserID = currentUserID;
    }

    @FXML
    public void chooseImageFromComputer(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image");

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
            fileName.setText(filePath.getName());
        }
    }

    @FXML
    public void createNewSale(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (saleReadyToCreate()) {

            try {
                // save image to image directory
                String imageDir = String.valueOf(filePath);
                String saveDir = "images" + File.separator + filePath.getName();
                Util.saveImageFromDir(imageDir, saveDir);

                Post post = new Sale(saleName.getText(),
                        saleDescription.getText(),
                        currentUserID,
                        filePath.getName(),
                        Double.parseDouble(askingPrice.getText()),
                        Double.parseDouble(minRaise.getText()));

                // add created post into post list
                UniLinkDatabase.getPosts().put(post.getId(), post);
                stage.close();
            } catch (IllegalArgumentException | FileNotFoundException e) {
                errorLabel.setText(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean saleReadyToCreate() {
        StringBuilder errorMessage = new StringBuilder("");

        if (saleName.getText().length() <= 0)
            errorMessage.append("Please input sale name.\n");

        try{
            if(Double.parseDouble(askingPrice.getText()) <= 0)
                errorMessage.append("Asking price should be larger than 0.\n");
        }catch (NumberFormatException e){
            errorMessage.append("Asking price should be a decimal\n");
        }

        try{
            if(Double.parseDouble(minRaise.getText()) <= 0)
                errorMessage.append("Minimal raise should be larger than 0.\n");
        }catch (NumberFormatException e){
            errorMessage.append("Minimal raise should be a decimal\n");
        }

        if (saleDescription.getText().length() > MAX_DESCRIPTION)
            errorMessage.append("Event description is too long.\n");

        //set error message to the error label
        errorLabel.setText(errorMessage.toString());

        if (errorMessage.length() == 0)
            return true;
        else
            return false;

    }


}
