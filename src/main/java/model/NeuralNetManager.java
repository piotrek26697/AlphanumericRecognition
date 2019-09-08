package model;

import org.encog.engine.network.activation.ActivationFunction;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.arrayutil.NormalizeArray;

import java.util.List;

public class NeuralNetManager {
    private final int NORMALIZATION_LOW = -1;

    private final int NORMALIZATION_HIGH = 1;

    private BasicNetwork network;

    private double error;

    private final int hiddenNeuronCount;

    private final int inputNeuronCount;

    private final int outputNeuronCount;

    public NeuralNetManager() {
        hiddenNeuronCount = 32;
        inputNeuronCount = Main.Constants.CANVAS_HEIGHT_SCALED * Main.Constants.CANVAS_WIDTH_SCALED;
        outputNeuronCount = 26;

        ActivationFunction activationFunction = new ActivationTANH();

        network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, true, inputNeuronCount));

        network.addLayer(new BasicLayer(activationFunction, true, hiddenNeuronCount));

        network.addLayer(new BasicLayer(activationFunction, false, outputNeuronCount));
        network.getStructure().finalizeStructure();
        network.reset();
    }

    private double[] normalize(double[] tab) {
        NormalizeArray norm = new NormalizeArray();
        norm.setNormalizedLow(NORMALIZATION_LOW);
        norm.setNormalizedHigh(NORMALIZATION_HIGH);

        return norm.process(tab);
    }

    public MLDataSet prepareDataSet(List<List<Integer>> list) {
        double[][] input = new double[list.size()][inputNeuronCount];
        double[][] idealOutput = new double[list.size()][outputNeuronCount];

        int iterator = 0;
        for (List<Integer> integers : list) {
            input[iterator] = normalize(integers.subList(1, integers.size()).stream()
                    .mapToDouble(x -> x)
                    .toArray());

            for (int i = 0; i < outputNeuronCount; i++)
                idealOutput[iterator][i] = NORMALIZATION_LOW;
            idealOutput[iterator][integers.get(0)] = NORMALIZATION_HIGH;

            iterator++;

        }
//        ---------------------------------------------------------
//            for (int i = 0; i < MainScreenController.CANVAS_HEIGHT_SCALED; i++) {
//                for (int j = 0; j < MainScreenController.CANVAS_WIDTH_SCALED; j++) {
//                    System.out.printf("%.2f\t", input[2500][i * MainScreenController.CANVAS_WIDTH_SCALED + j]);
//                }
//                System.out.println();
//            }
//        ---------------------------------------------------------

        return new BasicMLDataSet(input, idealOutput);
    }

    public void trainNeuralNetwork(MLDataSet trainingSet) {
        MLTrain trainer = new ResilientPropagation(network, trainingSet);

        int epoch = 1;
        do {
            trainer.iteration();
            System.out.println("Epoch #" + epoch + " Error: " + trainer.getError());
            epoch++;
        } while (trainer.getError() > this.error);
    }

    public double[] recognizeLetter(List<List<Integer>> pixels) {
        double[] result = new double[outputNeuronCount];
        double[] pixelArray = new double[pixels.size() * pixels.get(0).size()];

        int iterator = 0;
        for (List<Integer> row : pixels) {
            for (Integer pixel : row) {
                pixelArray[iterator] = (double) pixel;
                iterator++;
            }
        }

        network.compute(normalize(pixelArray), result);

        return result;
    }

    public void setError(double error) {
        this.error = error;
    }
}
