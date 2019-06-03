package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.ImageHandler;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable
{
    static public final int CANVAS_HEIGHT = 200;

    public final static int CANVAS_WIDTH = 200;

    static public final int CANVAS_HEIGHT_SCALED = 28;

    public final static int CANVAS_WIDTH_SCALED = 28;

    @FXML
    private Canvas canvas;

    @FXML
    private Button buttonClearCanvas;

    @FXML
    private Button buttonTest;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        clearCanvas();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            gc.setFill(Color.BLACK);
            gc.beginPath();
            gc.setLineWidth(0.3);
            gc.moveTo(event.getX(), event.getY());
            gc.stroke();
        });
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            gc.setFill(Color.BLACK);
            gc.lineTo(event.getX(), event.getY());
            gc.setLineWidth(0.3);
            gc.stroke();
        });
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
        });

        buttonClearCanvas.setOnAction(event -> clearCanvas());

        buttonTest.setOnAction(event -> getPixels());
    }

    private void clearCanvas()
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    private void getPixels()
    {
        ImageHandler imageHandler = new ImageHandler();
        BufferedImage scaledImage = imageHandler.getScaledImageFromCanvas(canvas);
        //TODO scaledImage. PixelReader()
        //scaledImage.getRgb()
    }
}
