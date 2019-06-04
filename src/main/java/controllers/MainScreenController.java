package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.ImageHandler;
import model.LearningDataReader;
import model.NeuralNetManager;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable
{
    static public final int CANVAS_HEIGHT = 150;

    public final static int CANVAS_WIDTH = 150;

    static public final int CANVAS_HEIGHT_SCALED = 28;

    public final static int CANVAS_WIDTH_SCALED = 28;

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
    public void initialize(URL location, ResourceBundle resources)
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        clearCanvas();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            gc.beginPath();
            gc.setLineWidth(3);
            gc.setStroke(Color.WHITE);
            gc.moveTo(event.getX(), event.getY());
            gc.stroke();
        });
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            gc.lineTo(event.getX(), event.getY());
            gc.setLineWidth(3);
            gc.setStroke(Color.WHITE);
            gc.stroke();
        });
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
        });

        buttonClearCanvas.setOnAction(event -> clearCanvas());

        buttonLearn.setOnAction(event -> train());

        buttonRecognize.setOnAction(event -> recognizeLetter());
    }

    private void clearCanvas()
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    private List<Integer> getPixels()
    {
        ImageHandler imageHandler = new ImageHandler();
        BufferedImage scaledImage = imageHandler.getScaledImageFromCanvas(canvas);
        Integer[] pixels = new Integer[CANVAS_WIDTH_SCALED * CANVAS_HEIGHT_SCALED];
        int iterator = 0;
        for (int y = 0; y < CANVAS_HEIGHT_SCALED; y++)
        {
            for (int x = 0; x < CANVAS_WIDTH_SCALED; x++)
            {
                int rgb = scaledImage.getRGB(x, y) & 0xffffff;
                int red = (rgb >> 16) & 0xff;
                int green = (rgb >> 8) & 0xff;
                int blue = rgb & 0xff;
                pixels[iterator] = ((red + green + blue) / 3);
                iterator++;
            }
        }
        return Arrays.asList(pixels);
    }

    private void train() //TODO make it better
    {
        String source = "src/main/resources/learningData.csv";
        neuralNetManager.setError(0.1);
        LearningDataReader learner = new LearningDataReader();
        learner.getDataFromCSV(source, neuralNetManager);
        System.out.println("Learning finished");
    }

    private void recognizeLetter()
    {
        List<Integer> pixels = getPixels();


        int k = 0;
        for (int i = 0; i < 28; i++)
        {
            for (int j = 0; j < 28; j++)
            {
                System.out.print(pixels.get(k) + "\t");
                k++;
            }
            System.out.println();
        }


        double[] result = neuralNetManager.recognizeLetter(pixels);
        char letter = 'A';
        for (int i = 0; i < 26; i++)
        {
            System.out.print(letter + ": " + result[i] + "\t");
            letter++;
        }
        System.out.println();
    }
}
