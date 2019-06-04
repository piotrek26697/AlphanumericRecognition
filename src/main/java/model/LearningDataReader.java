package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LearningDataReader
{
    private final int CHUNK = 115000;

    public List<List<Integer>> getDataFromCSV(String source, NeuralNetManager neuralNetManager)
    {
        List<List<Integer>> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(source)))
        {
            String line;

            for (int i = 0; i < CHUNK; i++)
            {

                if ((line = reader.readLine()) != null)
                {

                    String[] values = line.split(",");
                    Integer[] intValues = Arrays.stream(values)
                            .mapToInt(Integer::parseInt)
                            .boxed()
                            .toArray(Integer[]::new);
                    data.add(Arrays.asList(intValues));
                }
                for (int k = 0; k < 3; k++)
                    line = reader.readLine();

                /*for (int d = 0; d < 740; d++)
                    line = reader.readLine();*/


                /*int k = 0;
                for (int x = 0; x < 28; x++)
                {
                    for (int j = 0; j < 28; j++)
                    {
                        System.out.print(intValues[k] + "\t");
                        k++;
                    }
                    System.out.println("Poprawne litera to: " + intValues[0]);
                }*/

                //iterator++;
            }

        } catch (FileNotFoundException e)
        {
            System.out.println("File not found");
            e.printStackTrace();
            return null;
        } catch (IOException e)
        {
            System.out.println("File found");
            e.printStackTrace();
            return null;
        }
        return data;
    }
}
