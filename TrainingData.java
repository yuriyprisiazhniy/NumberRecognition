/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocv_proba;

import java.util.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

/**
 *
 * @author Yura
 */
public class TrainingData {

    Map<Character, List<IplImage>> labelImageMap;
    char[] symbols = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'E', 'H', 'K', 'M', 'O', 'P', 'T', 'X'/*,'I'*/};
    IplImage image;

    public TrainingData(String path) {
        labelImageMap = new HashMap<Character, List<IplImage>>();
        for (char currentSymbol : symbols) 
            for(int i=0;i<=104;i++)
        {
            image = cvLoadImage(path + "\\" + currentSymbol +"\\"+currentSymbol+"_"+ i+".jpg");
            cvThreshold(image, image, 20, 255, CV_THRESH_BINARY);
            if (labelImageMap.containsKey(currentSymbol)) {
                labelImageMap.get(currentSymbol).add(image);
            } else {
                labelImageMap.put(currentSymbol, new ArrayList<IplImage>());
                labelImageMap.get(currentSymbol).add(image);
            }

        }
    }

    private double[] randomImageConvert(char label) {
        Random rand = new Random();
        List<IplImage> images = labelImageMap.get(label);
        IplImage resultImage = images.get(rand.nextInt(images.size())); 
        double[] result = new double[image.width() * image.height()];
        for (int i = 0; i < resultImage.asCvMat().rows(); i++) {
            for (int j = 0; j < resultImage.asCvMat().cols(); j++) {
                if (cvGet2D(resultImage.asCvMat(), i, j).getVal(0) != 255) {
                    result[i * resultImage.asCvMat().cols() + j] = 1;
                } else {
                    result[i * resultImage.asCvMat().cols() + j] = 0;
                }
            }
        }
        return result;
    }

    public double[][] getInputs() {
        double[][] inputs = new double[21][image.width() * image.height()];
        for (int i = 0; i < 21; i++) {
            inputs[i] = randomImageConvert(symbols[i]);
        }
        return inputs;
    }

    public double[][] getOutputs() {
        double[][] outputs = new double[21][21];
        for (int i = 0; i < 21; i++) {
            outputs[i] = getOutVector(i);
        }
        return outputs;
    }

    private double[] getOutVector(int k) {
        double[] result = new double[21];
        for (int i = 0; i < 21; i++) {
            result[i] = 0;
        }
        result[k] = 1;
        return result;
    }
}
