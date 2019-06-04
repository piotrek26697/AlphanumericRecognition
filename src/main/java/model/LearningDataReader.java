package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LearningDataReader
{
    public void getDataFromCSV(String source, NeuralNetManager neuralNetManager)
    {
        //long iterator = 0;

        List<List<Integer>> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(source)))
        {
            String line;

            for (int i = 0; i < 500; i++)
            {
                /*if (iterator % NeuralNetManager.CHUNK == 0 && iterator != 0)
                {
                    neuralNetManager.prepareDataSet(data);
                    data.clear();
                }*/

                line = reader.readLine();

                String[] values = line.split(",");
                Integer[] intValues = Arrays.stream(values)
                        .mapToInt(Integer::parseInt)
                        .boxed()
                        .toArray(Integer[]::new);
                data.add(Arrays.asList(intValues));

                for (int d = 0; d < 740; d++)
                    line = reader.readLine();


                int k = 0;
                for (int x = 0; x < 28; x++)
                {
                    for (int j = 0; j < 28; j++)
                    {
                        System.out.print(intValues[k] + "\t");
                        k++;
                    }
                    System.out.println("Poprawne litera to: " + intValues[0]);
                }

                //iterator++;
            }
            //System.out.println("tu jestem");
            neuralNetManager.prepareDataSet(data);

        } catch (FileNotFoundException e)
        {
            System.out.println("File not found");
            e.printStackTrace();
            return;
        } catch (IOException e)
        {
            System.out.println("File found");
            e.printStackTrace();
            return;
        }
        // return data;
    }
}
