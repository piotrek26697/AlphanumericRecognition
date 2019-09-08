package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.*;
import org.encog.ml.data.MLDataSet;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {

    @FXML
    private Canvas canvas;

    @FXML
    private Button buttonClearCanvas;

    @FXML
    private Button buttonLearn;

    @FXML
    private Button buttonRecognize;

    private NeuralNetManager neuralNetManager = new NeuralNetManager();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        clearCanvas();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            gc.beginPath();
            gc.setLineWidth(6);
            gc.setStroke(Color.WHITE);
            gc.moveTo(event.getX(), event.getY());
            gc.stroke();
        });
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            gc.lineTo(event.getX(), event.getY());
            gc.setLineWidth(6);
            gc.setStroke(Color.WHITE);
            gc.stroke();
        });
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
        });

        buttonClearCanvas.setOnAction(event -> clearCanvas());

        buttonLearn.setOnAction(event -> train());

        buttonRecognize.setOnAction(event -> recognizeLetter());
    }

    private void clearCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    private List<List<Integer>> getPixels() throws EmptyCanvasException {
        ImageHandler imageHandler = new ImageHandler();
        BufferedImage imageFromCanvas = imageHandler.getScaledImageFromCanvas(canvas);
        List<List<Integer>> pixelList = imageHandler.bufferedImageToList(imageFromCanvas, false);
        pixelList = imageHandler.fitToResolution(pixelList);
        BufferedImage scaledImage = imageHandler.scaleImage(pixelList);
        return imageHandler.bufferedImageToList(scaledImage, true);
    }

    private void train() {
        TrainingDataHandler trainingDataHandler = new TrainingDataHandler();

        neuralNetManager.setError(Main.Constants.ERROR);
        LearningDataReader learner = new LearningDataReader();
        List<List<Integer>> list = learner.getDataFromCSV(Main.Constants.SOURCE);
        list = trainingDataHandler.fitDataToResolution(list);

//        for(int k =5000; k<6000; k++) {
//            List<Integer> letterData = list.get(k);
//            for (int i = 0; i < Main.Constants.CANVAS_HEIGHT_SCALED; i++) {
//                for (int j = 0; j < Main.Constants.CANVAS_WIDTH_SCALED; j++) {
//                    System.out.print(letterData.get(i * Main.Constants.CANVAS_WIDTH_SCALED + j) + "\t");
//                }
//                System.out.println();
//            }
//            System.out.println("-------------------------------------------------------------------");
//        }

        MLDataSet trainingSet = neuralNetManager.prepareDataSet(list);
        neuralNetManager.trainNeuralNetwork(trainingSet);
        System.out.println("Learning finished");
    }

    private void recognizeLetter() {
        try {
            List<List<Integer>> pixels = getPixels();

            for (List<Integer> row : pixels) {
                for (Integer pixel : row) {
                    System.out.print(pixel + "\t");
                }
                System.out.println();
            }
            System.out.println("--------------------------");

            double[] result = neuralNetManager.recognizeLetter(pixels);
            char letter = 'A';
            double max = -1;
            char maxLetter = letter;
            for (int i = 0; i < 26; i++) {
                System.out.print(letter + ": " + result[i] + "\t");
                if (result[i] > max) {
                    max = result[i];
                    maxLetter = letter;
                }
                letter++;
            }
            System.out.println("\n\nHighest letter: " + maxLetter);
        } catch (EmptyCanvasException ex) {
            System.out.println("Canvas is empty");
        }
    }
}
