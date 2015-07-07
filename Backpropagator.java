/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocv_proba;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Yura
 */
public class Backpropagator {

    private NeuralNetwork neuralNetwork;
    private double learningRate;
    private double momentum;
    private double characteristicTime;
    private double currentEpoch;

    public Backpropagator(NeuralNetwork neuralNetwork, double learningRate, double momentum, double characteristicTime) {
        this.neuralNetwork = neuralNetwork;
        this.learningRate = learningRate;
        this.momentum = momentum;
        this.characteristicTime = characteristicTime;
    }

    public void train(double errorThreshold) {

        TrainingData trainingData = new TrainingData("E:\\Image for NN");
        double error;
        int epoch = 1;
        do {
            error = backpropagate(trainingData.getInputs(), trainingData.getOutputs());
            System.out.println("Error for epoch " + epoch + ": " + error);
            epoch++;
            currentEpoch = epoch;
        } while (error > errorThreshold);
    }

    public double backpropagate(double[][] inputs, double[][] expectedOutputs) {

        double error = 0;
        double[] output;
        Map<Synapse, Double> synapseNeuronDeltaMap = new HashMap<Synapse, Double>();

        for (int i = 0; i < inputs.length; i++) {

            double[] input = inputs[i];
            double[] expectedOutput = expectedOutputs[i];

            List<Layer> layers = neuralNetwork.getLayers();

            neuralNetwork.setInputs(input);
            output = neuralNetwork.getOutput();


            for (int j = layers.size() - 1; j > 0; j--) {
                Layer layer = layers.get(j);

                for (int k = 0; k < layer.getNeurons().size(); k++) {
                    Neuron neuron = layer.getNeurons().get(k);
                    double neuronError = 0;

                    if (layer.isOutputLayer()) {

                        neuronError = neuron.getDerivative() * (output[k] - expectedOutput[k]);
                    } else {
                        neuronError = neuron.getDerivative();

                        double sum = 0;
                        List<Neuron> downstreamNeurons = layer.getNextLayer().getNeurons();
                        for (Neuron downstreamNeuron : downstreamNeurons) {

                            int l = 0;
                            boolean found = false;
                            while (l < downstreamNeuron.getInputs().size() && !found) {
                                Synapse synapse = downstreamNeuron.getInputs().get(l);

                                if (synapse.getSourceNeuron() == neuron) {
                                    sum += (synapse.getWeight() * downstreamNeuron.getError());
                                    found = true;
                                }

                                l++;
                            }
                        }

                        neuronError *= sum;
                    }

                    neuron.setError(neuronError);
                }
            }


            for (int j = layers.size() - 1; j > 0; j--) {
                Layer layer = layers.get(j);

                for (Neuron neuron : layer.getNeurons()) {

                    for (Synapse synapse : neuron.getInputs()) {

                        double newLearningRate = characteristicTime > 0 ? learningRate / (1 + (currentEpoch / characteristicTime)) : learningRate;
                        double delta = newLearningRate * neuron.getError() * synapse.getSourceNeuron().getOutput();

                        if (synapseNeuronDeltaMap.get(synapse) != null) {
                            double previousDelta = synapseNeuronDeltaMap.get(synapse);
                            delta += momentum * previousDelta;
                        }

                        synapseNeuronDeltaMap.put(synapse, delta);
                        synapse.setWeight(synapse.getWeight() - delta);
                    }
                }
            }

            output = neuralNetwork.getOutput();
            error += error(output, expectedOutput);

        }

        return error;
    }

    public double error(double[] actual, double[] expected) {

        if (actual.length != expected.length) {
            throw new IllegalArgumentException("The lengths of the actual and expected value arrays must be equal");
        }

        double sum = 0;

        for (int i = 0; i < expected.length; i++) {
            sum += Math.pow(expected[i] - actual[i], 2);
        }

        return sum / 2;
    }
}
