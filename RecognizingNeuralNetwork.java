/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocv_proba;

import java.io.IOException;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

/**
 *
 * @author Yura
 */
public class RecognizingNeuralNetwork {

    final static int INPUTSIZE = 120;

    public static NeuralNetwork createNN() {
        NeuralNetwork neuralNetwork = new NeuralNetwork("Digit Recognizing Neural Network");

        Neuron inputBias = new Neuron(new LinearActivationStrategy());
        inputBias.setOutput(1);

        Layer inputLayer = new Layer(null, inputBias);

        for (int i = 0; i < INPUTSIZE; i++) {
            Neuron neuron = new Neuron(new SigmoidActivationStrategy());
            neuron.setOutput(0);
            inputLayer.addNeuron(neuron);
        }

        Neuron hiddenBias = new Neuron(new LinearActivationStrategy());
        hiddenBias.setOutput(1);

        Layer hiddenLayer = new Layer(inputLayer, hiddenBias);

        long numberOfHiddenNeurons = Math.round((2.0 / 3.0) * INPUTSIZE + 10);

        for (int i = 0; i < numberOfHiddenNeurons; i++) {
            Neuron neuron = new Neuron(new SigmoidActivationStrategy());
            neuron.setOutput(0);
            hiddenLayer.addNeuron(neuron);
        }

        Layer outputLayer = new Layer(hiddenLayer);


        for (int i = 0; i < 21; i++) {
            Neuron neuron = new Neuron(new SigmoidActivationStrategy());
            neuron.setOutput(0);
            outputLayer.addNeuron(neuron);
        }

        neuralNetwork.addLayer(inputLayer);
        neuralNetwork.addLayer(hiddenLayer);
        neuralNetwork.addLayer(outputLayer);
        return neuralNetwork;
    }

    public static void teachNN(NeuralNetwork nn) throws IOException {
        Backpropagator backpropagator = new Backpropagator(nn, 0.1, 0.9, 0);
        backpropagator.train(0.005);
        nn.persist();
    }

    public static char recognize(IplImage image, NeuralNetwork nn, boolean digit) throws IOException {
        char[] symbols = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'E', 'H', 'K', 'M', 'O', 'P', 'T', 'X'};
        double[] inputVector = new double[image.width() * image.height()];
        cvThreshold(image, image, 20, 255, CV_THRESH_BINARY);
        for (int i = 0; i < image.asCvMat().rows(); i++) {
            for (int j = 0; j < image.asCvMat().cols(); j++) {
                if (cvGet2D(image.asCvMat(), i, j).getVal(0) != 255) {
                    inputVector[i * image.asCvMat().cols() + j] = 1;
                } else {
                    inputVector[i * image.asCvMat().cols() + j] = 0;
                }
            }
        }
        nn.setInputs(inputVector);
        double[] outputVector = nn.getOutput();
        double max = outputVector[0];
        char recognizeSymbol = symbols[0];
        if (digit) {
            for (int i = 1; i <= 9; i++) {
                if (outputVector[i] > max) {
                    max = outputVector[i];
                    recognizeSymbol = symbols[i];
                }
            }
        } else {
            for (int i = 10; i < outputVector.length; i++) {
                if (outputVector[i] > max) {
                    max = outputVector[i];
                    recognizeSymbol = symbols[i];
                }
            }
        }
        return recognizeSymbol;

    }
}
