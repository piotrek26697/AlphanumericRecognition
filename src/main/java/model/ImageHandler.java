package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImageHandler {

    public BufferedImage getScaledImageFromCanvas(Canvas canvas) throws EmptyCanvasException {
        WritableImage writableImage = new WritableImage(Main.Constants.CANVAS_WIDTH, Main.Constants.CANVAS_HEIGHT);
        canvas.snapshot(null, writableImage);
        Image tmp = SwingFXUtils.fromFXImage(writableImage, null)
                .getScaledInstance(Main.Constants.CANVAS_WIDTH_SCALED,
                        Main.Constants.CANVAS_HEIGHT_SCALED, Image.SCALE_SMOOTH);
        BufferedImage bufferedImage = new BufferedImage(Main.Constants.CANVAS_WIDTH, Main.Constants.CANVAS_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.drawImage(tmp, 0, 0, null);
        graphics.dispose();

        if (checkIfImageEmpty(bufferedImage))
            throw new EmptyCanvasException();

        return bufferedImage;
    }

    public BufferedImage scaleImage(List<List<Integer>> pixels) {
        BufferedImage bufferedImage = new BufferedImage(pixels.get(0).size(), pixels.size(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < pixels.size(); i++) {
            for (int j = 0; j < pixels.get(i).size(); j++) {
                bufferedImage.setRGB(j, i, pixels.get(i).get(j));
            }
        }

        BufferedImage scaledImage = new BufferedImage(Main.Constants.CANVAS_WIDTH_SCALED, Main.Constants.CANVAS_HEIGHT_SCALED, BufferedImage.TYPE_INT_RGB);
        Image tmpImage = bufferedImage.getScaledInstance(Main.Constants.CANVAS_WIDTH_SCALED, Main.Constants.CANVAS_HEIGHT_SCALED, Image.SCALE_SMOOTH);
        Graphics graphics = scaledImage.getGraphics();
        graphics.drawImage(tmpImage, 0, 0, null);
        graphics.dispose();

        return scaledImage;
    }

    public List<List<Integer>> bufferedImageToList(BufferedImage bufferedImage, boolean greyScale) {
        List<List<Integer>> pixels = new ArrayList<>();
        for (int y = 0; y < Main.Constants.CANVAS_HEIGHT_SCALED; y++) {
            List<Integer> row = new ArrayList<>();

            for (int x = 0; x < Main.Constants.CANVAS_WIDTH_SCALED; x++) {
                if (greyScale) {
                    int rgb = bufferedImage.getRGB(x, y) & 0xffffff;
                    int red = (rgb >> 16) & 0xff;
                    int green = (rgb >> 8) & 0xff;
                    int blue = rgb & 0xff;
                    row.add((red + green + blue) / 3);
                } else {
                    row.add(bufferedImage.getRGB(x, y) & 0xffffff);
                }
            }
            pixels.add(row);
        }

        return pixels;
    }

    public List<List<Integer>> fitToResolution(List<List<Integer>> image) {
        List<List<Integer>> tmp = fitToHeight(image);
        return fitToWidth(tmp);
    }

    private List<List<Integer>> fitToWidth(List<List<Integer>> image) {
        boolean isValue = false;
        List<List<Integer>> newImage = new ArrayList<>();

        Integer[][] newImageTab = new Integer[image.size()][image.get(0).size()];

        for (int i = 0; i < image.get(0).size(); i++) {
            for (int j = 0; j < image.size(); j++) {
                if (image.get(j).get(i) != 0)
                    isValue = true;
            }
            if (isValue) {
                for (int j = 0; j < image.size(); j++) {
                    newImageTab[j][i] = image.get(j).get(i);
                }
            }
            isValue = false;
        }
        for (int i = 0; i < image.size(); i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < image.get(i).size(); j++) {
                if (newImageTab[i][j] != null)
                    row.add(newImageTab[i][j]);
            }
            newImage.add(row);
        }
        return newImage;
    }

    private List<List<Integer>> fitToHeight(List<List<Integer>> image) {
        boolean isValue = false;
        List<List<Integer>> newImage = new ArrayList<>();

        for (int i = 0; i < image.size(); i++) {
            for (int j = 0; j < image.get(i).size(); j++) {
                if (image.get(i).get(j) != 0)
                    isValue = true;
            }
            if (isValue) {
                newImage.add(image.get(i));
            }
            isValue = false;
        }
        return newImage;
    }

    private boolean checkIfImageEmpty(BufferedImage bufferedImage) {
        for (int y = 0; y < Main.Constants.CANVAS_HEIGHT_SCALED; y++) {
            for (int x = 0; x < Main.Constants.CANVAS_WIDTH_SCALED; x++) {
                if ((bufferedImage.getRGB(x, y) & 0xFFFFFF) != 0)
                    return false;
            }
        }
        return true;
    }
}
