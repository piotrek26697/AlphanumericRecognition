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

    static public class Constants {
        public static final int TRAINING_DATA_SIZE = 28;

        static public final int CANVAS_HEIGHT = 150;

        public final static int CANVAS_WIDTH = 150;

        static public final int CANVAS_HEIGHT_SCALED = 28;

        public final static int CANVAS_WIDTH_SCALED = 28;

        static public final double ERROR = 0.05;

        static public final String SOURCE = "src/main/resources/learningDataParsed.csv";
    }
}
