package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.UniLinkDatabase;

public class MainGUI  extends Application {
    public static void main(String[] args) {

        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        UniLinkDatabase.connectDB();

        Parent root = FXMLLoader.load(getClass().getResource("/view/login_window_view.fxml"));

        stage.setTitle("UniLink System");
        stage.setScene(new Scene(root,800  ,800));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        UniLinkDatabase.update();
    }
}
