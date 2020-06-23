package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Util {
    public static void changeScene(ActionEvent event, String fileAddress) throws IOException {
        Parent root = FXMLLoader.load(Util.class.getResource(fileAddress));
        Scene scene = new Scene(root);

        //get Window
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    public static void AlertBox(String title, String message) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public static void saveImageFromDir(String imageDir, String saveDir) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(imageDir));
        FileOutputStream fileOutputStream = new FileOutputStream(new File(saveDir));

        byte[] b = new byte[1024];

        while (fileInputStream.read(b) != -1) {
            fileOutputStream.write(b);
            fileOutputStream.flush();
        }

        fileInputStream.close();
        fileOutputStream.close();
    }




}
