package model;

import controllers.MainScreenController;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageHandler
{
    public BufferedImage getScaledImageFromCanvas(Canvas canvas)
    {
        WritableImage writableImage = new WritableImage(MainScreenController.CANVAS_WIDTH, MainScreenController.CANVAS_HEIGHT);
        canvas.snapshot(null, writableImage);
        Image tmp = SwingFXUtils.fromFXImage(writableImage, null)
                .getScaledInstance(MainScreenController.CANVAS_WIDTH_SCALED,
                        MainScreenController.CANVAS_HEIGHT_SCALED, Image.SCALE_SMOOTH);
        BufferedImage scaledImg = new BufferedImage(MainScreenController.CANVAS_WIDTH_SCALED, MainScreenController.CANVAS_HEIGHT_SCALED, BufferedImage.TYPE_BYTE_GRAY);
        Graphics graphics = scaledImg.getGraphics();
        graphics.drawImage(tmp, 0, 0, null);
        graphics.dispose();
        return scaledImg;
    }
}
