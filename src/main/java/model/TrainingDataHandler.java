package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TrainingDataHandler {

    private ImageHandler imageHandler = new ImageHandler();

    public List<List<Integer>> fitDataToResolution(List<List<Integer>> trainingData) {
        List<List<Integer>> results = new ArrayList<>();

        for (List<Integer> trainingImage : trainingData) {
            List<List<Integer>> image = splitImage(trainingImage.subList(1, trainingImage.size()));
            image = imageHandler.fitToResolution(image);
            BufferedImage bufferedImage = imageHandler.scaleImage(image);
            image = imageHandler.bufferedImageToList(bufferedImage, false);
            List<Integer> scaledImage = mergeImage(image);
            List<Integer> trainingImageScaled = new ArrayList<>();
            trainingImageScaled.add(trainingImage.get(0));
            trainingImageScaled.addAll(scaledImage);
            results.add(trainingImageScaled);
        }
        return results;
    }

    private List<List<Integer>> splitImage(List<Integer> img) {
        List<List<Integer>> splittedImage = new ArrayList<>();

        for (int i = 0; i < Main.Constants.TRAINING_DATA_SIZE; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < Main.Constants.TRAINING_DATA_SIZE; j++) {
                row.add(img.get(i * Main.Constants.TRAINING_DATA_SIZE + j));
            }
            splittedImage.add(row);
        }
        return splittedImage;
    }

    private List<Integer> mergeImage(List<List<Integer>> img) {
        List<Integer> mergedImage = new ArrayList<>();
        for (List<Integer> row : img) {
            mergedImage.addAll(row);
        }
        return mergedImage;
    }
}
