/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocv_proba;

import java.util.*;
import java.io.*;

/**
 *
 * @author Yura
 */
public class NeuralNetwork {

    private String name;
    private List<Layer> layers;
    private Layer input;
    private Layer output;

    public NeuralNetwork(String name) {
        this.name = name;
        layers = new ArrayList<Layer>();
    }

    public void addLayer(Layer layer) {
        layers.add(layer);

        if (layers.size() == 1) {
            input = layer;
        }

        if (layers.size() > 1) {
            Layer previousLayer = layers.get(layers.size() - 2);
            previousLayer.setNextLayer(layer);
        }

        output = layers.get(layers.size() - 1);
    }

    public void setInputs(double[] inputs) {
        if (input != null) {

            int biasCount = input.hasBias() ? 1 : 0;

            if (input.getNeurons().size() - biasCount != inputs.length) {
                throw new IllegalArgumentException("The number of inputs must equal the number of neurons in the input layer");
            } else {
                List<Neuron> neurons = input.getNeurons();
                for (int i = biasCount; i < neurons.size(); i++) {
                    neurons.get(i).setOutput(inputs[i - biasCount]);
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public double[] getOutput() {

        double[] outputs = new double[output.getNeurons().size()];

        for (int i = 1; i < layers.size(); i++) {
            Layer layer = layers.get(i);
            layer.feedForward();
        }

        int i = 0;
        for (Neuron neuron : output.getNeurons()) {
            outputs[i] = neuron.getOutput();
            i++;
        }

        return outputs;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public void reset() {
        for (Layer layer : layers) {
            for (Neuron neuron : layer.getNeurons()) {
                for (Synapse synapse : neuron.getInputs()) {
                    synapse.setWeight((Math.random() * 1) - 0.5);
                }
            }
        }
    }

    public double[] getWeights() {

        List<Double> weights = new ArrayList<Double>();

        for (Layer layer : layers) {

            for (Neuron neuron : layer.getNeurons()) {

                for (Synapse synapse : neuron.getInputs()) {
                    weights.add(synapse.getWeight());
                }
            }
        }

        double[] allWeights = new double[weights.size()];

        int i = 0;
        for (Double weight : weights) {
            allWeights[i] = weight;
            i++;
        }

        return allWeights;
    }

    public void copyWeightsFrom(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner scan = new Scanner(file);
        String[] numbers = scan.nextLine().split(" ");
        double[] w = new double[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            w[i] = Double.parseDouble(numbers[i]);
        }
        int k = 0;
        for (Layer layer : layers) {

            for (Neuron neuron : layer.getNeurons()) {

                for (Synapse synapse : neuron.getInputs()) {
                    synapse.setWeight(w[k]);
                    k++;
                }
            }
        }
    }

    public void persist() throws IOException {
        File file = new File("E:\\Image for NN\\weight1.txt");
        FileWriter fwrt = new FileWriter(file);
        double[] w = getWeights();
        for (int i = 0; i < w.length; i++) {
            fwrt.append(Double.toString(w[i]) + " ");
        }
        fwrt.close();
    }
}
