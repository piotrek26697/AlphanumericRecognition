package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LearningDataReader {

    public List<List<Integer>> getDataFromCSV(String source) {
        List<List<Integer>> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Integer[] intValues = Arrays.stream(values)
                        .mapToInt(Integer::parseInt)
                        .boxed()
                        .toArray(Integer[]::new);
                data.add(Arrays.asList(intValues));
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            System.out.println("File found");
            e.printStackTrace();
            return null;
        }
        return data;
    }
}
