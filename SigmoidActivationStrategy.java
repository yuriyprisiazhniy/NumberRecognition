/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocv_proba;

/**
 *
 * @author Yura
 */
public class SigmoidActivationStrategy implements ActivationStrategy {
    public double activate(double weightedSum) {
        return 1.0 / (1 + Math.exp(-1.0 * weightedSum));
    }

    public double derivative(double weightedSum) {
        return activate(weightedSum) * (1.0 - activate(weightedSum));
    }

    public SigmoidActivationStrategy copy() {
        return new SigmoidActivationStrategy();
    }
}
