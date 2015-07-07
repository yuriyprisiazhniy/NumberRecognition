/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocv_proba;

/**
 *
 * @author Yura
 */
public interface ActivationStrategy {
    double activate(double weightedSum);
    double derivative(double weightedSum);
    ActivationStrategy copy();
}
