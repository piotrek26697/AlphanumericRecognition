package model;

import controllers.MainScreenController;
import org.encog.engine.network.activation.ActivationFunction;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

import java.util.List;

public class NeuralNetManager
{
    private BasicNetwork network;

    private MLTrain trainer;

    private double error = 0.01;

    private final int hiddenLayersCount;

    private final int hiddenNeuronCount;

    private final int inputNeuronCount;

    private final int outputNeuronCount;

    public final static int CHUNK = 500;

    public NeuralNetManager()
    {
        hiddenLayersCount = 2;
        hiddenNeuronCount = 32;
        inputNeuronCount = MainScreenController.CANVAS_HEIGHT_SCALED * MainScreenController.CANVAS_WIDTH_SCALED;
        outputNeuronCount = 26;

        ActivationFunction activationFunction = new ActivationSigmoid();

        network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, false, inputNeuronCount));
        for (int i = 0; i < hiddenLayersCount; i++)
            network.addLayer(new BasicLayer(activationFunction, false, hiddenNeuronCount));

        network.addLayer(new BasicLayer(activationFunction, false, outputNeuronCount));
        network.getStructure().finalizeStructure();
        network.reset();
    }

    public void prepareDataSet(List<List<Integer>> list)
    {
        double[][] input = new double[CHUNK][inputNeuronCount];
        double[][] idealOutput = new double[CHUNK][outputNeuronCount];

        int iterator = 0;
        for (List<Integer> integers : list)
        {
            input[iterator] = integers.subList(1, integers.size()).stream()
                    .mapToDouble(i -> i)
                    .toArray();
            for (int i = 0; i < outputNeuronCount; i++)
                idealOutput[iterator][i] = 0.0;
            idealOutput[iterator][integers.get(0)] = 1.0;
            iterator++;

        }
        trainNeuralNetwork(new BasicMLDataSet(input, idealOutput));
    }

    private void trainNeuralNetwork(MLDataSet trainingSet)
    {
        MLTrain trainer = new Backpropagation(network, trainingSet);

        int epoch = 1;
        do
        {
            trainer.iteration();
            System.out.println("Epoch #" + epoch + " Error: " + trainer.getError());
            epoch++;
        } while (trainer.getError() > this.error);
    }

    public double[] recognizeLetter(List<Integer> pixels)
    {
        double[] result = new double[outputNeuronCount];
        double[] pixelArray = pixels.stream().mapToDouble(i -> i).toArray();

        network.compute(pixelArray, result);

        return result;
    }

    public void setError(double error)
    {
        this.error = error;
    }
}
