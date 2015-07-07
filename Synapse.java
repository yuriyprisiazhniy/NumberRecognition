/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocv_proba;

/**
 *
 * @author Yura
 */
public class Synapse {

    private Neuron sourceNeuron;
    private double weight;

    public Synapse(Neuron sourceNeuron, double weight) {
        this.sourceNeuron = sourceNeuron;
        this.weight = weight;
    }

    public Neuron getSourceNeuron() {
        return sourceNeuron;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
