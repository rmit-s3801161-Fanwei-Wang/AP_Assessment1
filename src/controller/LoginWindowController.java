package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.UniLinkDatabase;

import java.io.IOException;

public class LoginWindowController{
    @FXML private TextField loginInput;
    @FXML private Label errorMessageLabel;
    @FXML private Button loginButton;

    private static String currentUserID;

    public static String getCurrentUserID() {
        return currentUserID;
    }


    @FXML
    private void initialize() {
        errorMessageLabel.setText("");

        //allowed user press enter to login
        loginInput.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                loginButton.fire();
            }

        });
    }


    @FXML
    public void login(ActionEvent event) throws IOException {
        if(isCorrectUsername()){
            String fileAddress = "/view/main_window_view.fxml";

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fileAddress));
            Parent mainViewParent = loader.load();

            Scene mainWindowViewScene = new Scene(mainViewParent);

            //access the controller and call a method
            MainWindowController controller = loader.getController();

            //pass the  username to MainWindowController
            controller.getCurrentUserIDLabel().setText(loginInput.getText());
            currentUserID = loginInput.getText();

            //get Window
            Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
            mainWindow.setScene(mainWindowViewScene);
            mainWindow.show();

        }
    }

    @FXML
    private void closeSystem(ActionEvent event) {
        UniLinkDatabase.update();
        Platform.exit();
        System.exit(0);
    }

    private  boolean isCorrectUsername(){
        int count = 0;
        String errorMessage = "";

        if(!(loginInput.getText().startsWith("s") || loginInput.getText().startsWith("S"))){
            errorMessage += "Username should start with 'S' or 's'. \n";
            errorMessageLabel.setText(errorMessage);
            count++;
        }
        if(!(loginInput.getText().length() >= 2 && loginInput.getText().length() <= 8 && isNumeric(loginInput.getText().substring(1)))){
            errorMessage += "Username should has at least 1 up to 7 integer. \n";
            errorMessageLabel.setText(errorMessage);
            count++;
        }

        if(count != 0)
            return false;

        errorMessageLabel.setText("Oops....");
        return true;
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
