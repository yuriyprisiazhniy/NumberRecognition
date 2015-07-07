/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocv_proba;

/**
 *
 * @author Yura
 */
public class LinearActivationStrategy implements ActivationStrategy {
    public double activate(double weightedSum) {
        return weightedSum;
    }

    public double derivative(double weightedSum) {
        return 0;
    }

    public ActivationStrategy copy() {
        return new LinearActivationStrategy();
    }
}
