/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocv_proba;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import java.util.Random;

/**
 *
 * @author Yura
 */
public class TemplateForNeuronNetwork {

    public static int number;

    public static void rotation(IplImage image, String name) {

        CvPoint2D32f center = cvPoint2D32f(image.width() / 2, image.height() / 2);
        double angle;
        double scale = 1;
        number = -1;
        for (int k = 0; k <= 6; k++) {
            number++;
            angle = -15 + 5 * k;
            CvMat rot_mat = cvCreateMat(2, 3, CV_32FC1);
            cv2DRotationMatrix(center, angle, scale, rot_mat);
            IplImage result = cvCloneImage(image);
            cvWarpAffine(image, result, rot_mat, CV_WARP_FILL_OUTLIERS, cvScalar(255, 255, 255, 0));
            cvSaveImage("E:\\Image for NN\\" + name + "\\" + name + "_" + number + ".jpg", result);
            cvReleaseMat(rot_mat);
            cvReleaseImage(result);
        }
    }

    public static void cut(String name) {
        for (int i = 0; i <= 6; i++) {
            number++;
            IplImage image = cvLoadImage("E:\\Image for NN\\" + name + "\\" + name + "_" + i + ".jpg");
            IplImage forSize = cvCreateImage(cvSize(45, 57), image.depth(), image.nChannels());
            cvSetImageROI(image, cvRect(5, 0, image.width() - 5, image.height()));
            IplImage tmp = cvCreateImage(cvGetSize(image), image.depth(), image.nChannels());
            cvCopy(image, tmp);
            cvResize(tmp, forSize, 1);
            cvSaveImage("E:\\Image for NN\\" + name + "\\" + name + "_" + number + ".jpg", forSize);
            cvResetImageROI(image);
            number++;
            cvSetImageROI(image, cvRect(0, 5, image.width(), image.height() - 5));
            tmp = cvCreateImage(cvGetSize(image), image.depth(), image.nChannels());
            cvCopy(image, tmp);
            cvResize(tmp, forSize, 1);
            cvSaveImage("E:\\Image for NN\\" + name + "\\" + name + "_" + number + ".jpg", forSize);
            cvResetImageROI(image);
            number++;
            cvSetImageROI(image, cvRect(0, 0, image.width() - 5, image.height()));
            tmp = cvCreateImage(cvGetSize(image), image.depth(), image.nChannels());
            cvCopy(image, tmp);
            cvResize(tmp, forSize, 1);
            cvSaveImage("E:\\Image for NN\\" + name + "\\" + name + "_" + number + ".jpg", forSize);
            cvResetImageROI(image);
            number++;
            cvSetImageROI(image, cvRect(0, 0, image.width(), image.height() - 5));
            tmp = cvCreateImage(cvGetSize(image), image.depth(), image.nChannels());
            cvCopy(image, tmp);
            cvResize(tmp, forSize, 1);
            cvSaveImage("E:\\Image for NN\\" + name + "\\" + name + "_" + number + ".jpg", forSize);
            cvResetImageROI(image);
            cvReleaseImage(tmp);
            cvReleaseImage(forSize);
            cvReleaseImage(image);
        }

    }

    public static void erodeDilate(String name) {
        for (int i = 0; i <= 34; i++) {
            number++;
            IplImage image = cvLoadImage("E:\\Image for NN\\" + name + "\\" + name + "_" + i + ".jpg");
            IplImage image1 = cvCloneImage(image);
            IplConvKernel kern = cvCreateStructuringElementEx(5, 5, 3, 3, CV_SHAPE_RECT, null);
            cvErode(image, image1, kern, CV_C);
            cvSaveImage("E:\\Image for NN\\" + name + "\\" + name + "_" + number + ".jpg", image1);
            number++;
            cvDilate(image, image1, kern, CV_C);
            cvSaveImage("E:\\Image for NN\\" + name + "\\" + name + "_" + number + ".jpg", image1);

            cvReleaseImage(image);
            cvReleaseImage(image1);
        }
    }

    public static boolean aroundWhite(IplImage img, int x, int y) {
        boolean result = false;
        if ((cvGet2D(img.asCvMat(), x, y).getVal(0) == 255) && (x + 1 < img.height()) && (x - 1 >= 0) && (y + 1 < img.width()) && (y - 1 >= 0)) {
            if (cvGet2D(img.asCvMat(), x, y + 1).getVal(0) == 0) {
                result = true;
                return result;
            }
            if (cvGet2D(img.asCvMat(), x, y - 1).getVal(0) == 0) {
                result = true;
                return result;
            }
            if (cvGet2D(img.asCvMat(), x + 1, y).getVal(0) == 0) {
                result = true;
                return result;
            }
            if (cvGet2D(img.asCvMat(), x - 1, y).getVal(0) == 0) {
                result = true;
                return result;
            }
            if (cvGet2D(img.asCvMat(), x + 1, y + 1).getVal(0) == 0) {
                result = true;
                return result;
            }
            if (cvGet2D(img.asCvMat(), x + 1, y - 1).getVal(0) == 0) {
                result = true;
                return result;
            }
            if (cvGet2D(img.asCvMat(), x - 1, y - 1).getVal(0) == 0) {
                result = true;
                return result;
            }
            if (cvGet2D(img.asCvMat(), x - 1, y + 1).getVal(0) == 0) {
                result = true;
                return result;
            }
        }
        return result;
    }

    public static boolean aroundBlack(IplImage img, int x, int y) {
        boolean result = false;
        if ((cvGet2D(img.asCvMat(), x, y).getVal(0) == 0) && (x + 1 < img.height()) && (x - 1 >= 0) && (y + 1 < img.width()) && (y - 1 >= 0)) {
            if (cvGet2D(img.asCvMat(), x, y + 1).getVal(0) == 255) {
                result = true;
                return result;
            }
            if (cvGet2D(img.asCvMat(), x, y - 1).getVal(0) == 255) {
                result = true;
                return result;
            }
            if (cvGet2D(img.asCvMat(), x + 1, y).getVal(0) == 255) {
                result = true;
                return result;
            }
            if (cvGet2D(img.asCvMat(), x - 1, y).getVal(0) == 255) {
                result = true;
                return result;
            }
            if (cvGet2D(img.asCvMat(), x + 1, y + 1).getVal(0) == 255) {
                result = true;
                return result;
            }
            if (cvGet2D(img.asCvMat(), x + 1, y - 1).getVal(0) == 255) {
                result = true;
                return result;
            }
            if (cvGet2D(img.asCvMat(), x - 1, y - 1).getVal(0) == 255) {
                result = true;
                return result;
            }
            if (cvGet2D(img.asCvMat(), x - 1, y + 1).getVal(0) == 255) {
                result = true;
                return result;
            }
        }
        return result;
    }

    public static void noise(String name) {
        Random rand;
        for (int l = 0; l <= 104; l++) {
            number++;
            IplImage image = cvLoadImage("E:\\Image for NN\\" + name + "\\" + name + "_" + l + ".jpg");
            for (int i = 0; i < image.asCvMat().rows(); i++) {
                for (int j = 0; j < image.asCvMat().cols(); j++) {
                    if (aroundWhite(image, i, j)) {
                        rand = new Random();
                        if (rand.nextInt(9) > 5) {
                            cvGet2D(image.asCvMat(), i, j).setVal(0, 0);
                        }
                    }
                    if (aroundBlack(image, i, j)) {
                        rand = new Random();
                        if (rand.nextInt(9) > 7) {
                            cvGet2D(image.asCvMat(), i, j).setVal(0, 255);
                        }
                    }
                }
            }
            cvSaveImage("E:\\Image for NN\\" + name + "\\" + name + "_" + number + ".jpg", image);
            cvReleaseImage(image);
        }
    }

    public static double[] getInVector(IplImage img) {
        double[] x = new double[img.width() * img.height()];
        for (int i = 0; i < img.asCvMat().rows(); i++) {
            for (int j = 0; j < img.asCvMat().cols(); j++) {
                if (cvGet2D(img.asCvMat(), i, j).getVal(0) != 255) {
                    x[i * img.asCvMat().cols() + j] = 1;
                } else {
                    x[i * img.asCvMat().cols() + j] = 0;
                }
            }
        }
        return x;
    }

    public static double[] getOutVector(char a, char[] arr) {
        int k = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == a) {
                k = i;
            }
        }
        double[] x = new double[22/*23*/];
        for (int i = 0; i < x.length; i++) {
            if (k == i) {
                x[i] = 1;
            } else {
                x[i] = -1;
            }
        }
        return x;
    }
}
