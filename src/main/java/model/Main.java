package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/mainScreen.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Alphanumeric recognition");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    static public void main(String[] args)
    {
        launch(args);
    }
}
