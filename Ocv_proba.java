/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocv_proba;

import com.googlecode.javacpp.Loader;
import java.io.*;
import java.util.Scanner;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import java.util.ArrayList;
import java.awt.*;

public class Ocv_proba {

    public static IplImage loadImage;
    public static int tem;

    public static boolean checkArray(ArrayList<CvPoint> arr, IplImage img, int x, int y) {
        boolean flag = true;
        if ((x >= img.asCvMat().cols()) || (y >= img.asCvMat().rows()) || x < 0 || y < 0) {
            flag = false;
            return flag;
        }
        for (int i = 0; i < arr.size(); i++) {
            if ((x == arr.get(i).x()) && (y == arr.get(i).y())) {
                flag = false;
            }
        }
        return flag;
    }

    public static ArrayList<CvRect> selectAllSymbol(IplImage img) {
        int bias = 0;
        CvRect temp;
        CvPoint pt;
        ArrayList<CvRect> arr = new ArrayList<CvRect>();
        while (bias < img.width()) {
            pt = findPoint(img, bias);
            if (pt == null) {
                break;
            }
            temp = selectSymbol(img, pt);
            arr.add(temp);
            bias = temp.x() + temp.width() + 1;
        }
        return arr;
    }

    public static CvRect selectSymbol(IplImage img, CvPoint findPoint) {
        ArrayList<CvPoint> arr = new ArrayList<CvPoint>();
        arr.add(findPoint);
        int i = 0;
        CvPoint currentPoint;
        do {
            currentPoint = arr.get(i);
            if ((checkArray(arr, img, currentPoint.x(), currentPoint.y() - 1)) && (cvGet2D(img.asCvMat(), currentPoint.y() - 1, currentPoint.x()).getVal(0) == 0)) {
                arr.add(cvPoint(currentPoint.x(), currentPoint.y() - 1));
            }
            if ((checkArray(arr, img, currentPoint.x(), currentPoint.y() + 1)) && (cvGet2D(img.asCvMat(), currentPoint.y() + 1, currentPoint.x()).getVal(0) == 0)) {
                arr.add(cvPoint(currentPoint.x(), currentPoint.y() + 1));
            }
            if ((checkArray(arr, img, currentPoint.x() - 1, currentPoint.y())) && (cvGet2D(img.asCvMat(), currentPoint.y(), currentPoint.x() - 1).getVal(0) == 0)) {
                arr.add(cvPoint(currentPoint.x() - 1, currentPoint.y()));
            }
            if ((checkArray(arr, img, currentPoint.x() + 1, currentPoint.y())) && (cvGet2D(img.asCvMat(), currentPoint.y(), currentPoint.x() + 1).getVal(0) == 0)) {
                arr.add(cvPoint(currentPoint.x() + 1, currentPoint.y()));
            }
            i++;
        } while (i < arr.size());
        //створюємо прямокутник по знайденим точкам
        int minX, maxX, minY, maxY;
        minX = maxX = arr.get(0).x();
        minY = maxY = arr.get(0).y();
        for (int j = 1; j < arr.size(); j++) {
            if (arr.get(j).x() < minX) {
                minX = arr.get(j).x();
            }
            if (arr.get(j).y() < minY) {
                minY = arr.get(j).y();
            }
            if (arr.get(j).x() > maxX) {
                maxX = arr.get(j).x();
            }
            if (arr.get(j).y() > maxY) {
                maxY = arr.get(j).y();
            }
        }
        if (maxX - minX == 0) {
            maxX = minX + 1;
        }
        if (maxY - minY == 0) {
            maxY = minY + 1;
        }
        return cvRect(minX, minY, maxX - minX + 1, maxY - minY);
    }

    public static CvPoint findPoint(IplImage img, int x_bias) {
        int top, bot;
        for (int j = x_bias; j < img.asCvMat().cols(); j++) {
            top = 0;
            bot = img.asCvMat().rows();
            
            top = (int) (img.height() * 0.4);
            bot = (int) (img.height() * 0.6 );
            for (int i = top; i <= bot; i++) {
                if (cvGet2D(img.asCvMat(), i, j).getVal(0) == 0) {
                    return cvPoint(j, i);
                }
            }
        }
        return null;
    }

    public static double checkPixels(IplImage img) {
        IplImage dest = cvCreateImage(cvGetSize(img), IPL_DEPTH_8U, 1);
        cvCvtColor(img, dest, CV_RGB2GRAY);
        cvThreshold(dest, dest, cvAvg(dest, null).val(0), 255, CV_THRESH_BINARY);
        //cvShowImage("dest"+tem,dest);
        tem++;
        int whiteCount = 0, blackCount = 0;
        for (int i = 0; i < dest.asCvMat().rows(); i++) {
            for (int j = 0; j < dest.asCvMat().cols(); j++) {
                if (cvGet2D(dest.asCvMat(), i, j).getVal(0) == 255) {
                    whiteCount++;
                } else {
                    blackCount++;
                }
            }
        }
        //cvShowImage("dest"+tem,dest);
        tem++;
        return (double) whiteCount / blackCount;
    }

    public static IplImage aditionalCut(IplImage img) {
        boolean flag = true;
        int count = 0;
        for (int i = img.asCvMat().rows() / 2; i < img.asCvMat().rows(); i++) {
            count = 0;
            for (int j = 0; j < img.asCvMat().cols(); j++) {
                if ((cvGet2D(img.asCvMat(), i, j).getVal(0) == 0) && (flag == true)) {
                    count++;
                    flag = false;
                }
                if (cvGet2D(img.asCvMat(), i, j).getVal(0) == 255) {
                    flag = true;
                }
            }
            if (count < 4) {
                cvSetImageROI(img, cvRect(0, 0, img.width(), i));
                IplImage outImage = cvCreateImage(cvGetSize(img), img.depth(), img.nChannels());
                cvCopy(img, outImage);
                cvResetImageROI(img);
                img = cvCloneImage(outImage);
                cvReleaseImage(outImage);
            }
        }

        for (int i = img.asCvMat().rows() / 2; i >= 0; i--) {
            count = 0;
            for (int j = 0; j < img.asCvMat().cols(); j++) {
                if ((cvGet2D(img.asCvMat(), i, j).getVal(0) == 0) && (flag == true)) {
                    count++;
                    flag = false;
                }
                if (cvGet2D(img.asCvMat(), i, j).getVal(0) == 255) {
                    flag = true;
                }
            }
            if (count < 4) {
                cvSetImageROI(img, cvRect(0, i, img.width(), img.height() - i));
                IplImage outImage = cvCreateImage(cvGetSize(img), img.depth(), img.nChannels());
                cvCopy(img, outImage);
                cvResetImageROI(img);
                img = cvCloneImage(outImage);
                cvReleaseImage(outImage);
            }
        }
        return img;
    }

    public static IplImage cutNumberVertical(IplImage img, int kl) { //обрізаєм вертикальні краї right= true - правий край
        IplImage dest = cvCreateImage(cvGetSize(img), IPL_DEPTH_8U, 1);
        cvCvtColor(img, dest, CV_RGB2GRAY);
        if (kl == 0)
        cvThreshold(dest, dest, 120, 255, CV_THRESH_BINARY);
        else 
        cvThreshold(dest, dest, cvAvg(dest,null).getVal(0), 255, CV_THRESH_BINARY);
        //cvShowImage("proba", dest);
        int blackCount;
        //ліва
        for (int j = 0; j < dest.asCvMat().cols(); j++) {
            blackCount = 0;
            for (int i = 0; i < dest.asCvMat().rows(); i++) {
                if (cvGet2D(dest.asCvMat(), i, j).getVal(0) != 255) {
                    blackCount++;
                }
            }
            if (blackCount >= 0.75 * dest.height()) {
                cvSetImageROI(dest, cvRect(1, 0, dest.width() - 1, dest.height()));
                IplImage outImage = cvCreateImage(cvGetSize(dest), dest.depth(), dest.nChannels());
                cvCopy(dest, outImage);
                cvResetImageROI(dest);
                dest = cvCloneImage(outImage);
                cvReleaseImage(outImage);
            }
        }
        //права
        for (int j = dest.asCvMat().cols() - 1; j >= 0; j--) {
            blackCount = 0;
            for (int i = 0; i < dest.asCvMat().rows(); i++) {
                if (cvGet2D(dest.asCvMat(), i, j).getVal(0) != 255) {
                    blackCount++;
                }
            }
            if (blackCount >= 0.75 * dest.height()) {
                cvSetImageROI(dest, cvRect(0, 0, dest.width() - 1, dest.height()));
                IplImage outImage = cvCreateImage(cvGetSize(dest), dest.depth(), dest.nChannels());
                cvCopy(dest, outImage);
                cvResetImageROI(dest);
                dest = cvCloneImage(outImage);
                cvReleaseImage(outImage);
            }
        }
        //нижня
        for (int i = dest.asCvMat().rows() / 2; i < dest.asCvMat().rows(); i++) {
            blackCount = 0;
            for (int j = 0; j < dest.asCvMat().cols(); j++) {
                if (cvGet2D(dest.asCvMat(), i, j).getVal(0) != 255) {
                    blackCount++;
                }
            }
            if (blackCount >= 0.75 * dest.width()) {
                cvSetImageROI(dest, cvRect(0, 0, dest.width(), i - 1));
                IplImage outImage = cvCreateImage(cvGetSize(dest), dest.depth(), dest.nChannels());
                cvCopy(dest, outImage);
                cvResetImageROI(dest);
                dest = cvCloneImage(outImage);
                cvReleaseImage(outImage);
                break;
            }
        }
        //верхня    
        for (int i = dest.asCvMat().rows() / 2; i >= 0; i--) {
            blackCount = 0;
            for (int j = 0; j < dest.asCvMat().cols(); j++) {
                if (cvGet2D(dest.asCvMat(), i, j).getVal(0) != 255) {
                    blackCount++;
                }
            }
            if (blackCount >= 0.75 * dest.width()) {
                cvSetImageROI(dest, cvRect(0, i + 1, dest.width(), dest.height() - i - 1));
                IplImage outImage = cvCreateImage(cvGetSize(dest), dest.depth(), dest.nChannels());
                cvCopy(dest, outImage);
                cvResetImageROI(dest);
                dest = cvCloneImage(outImage);
                cvReleaseImage(outImage);
                break;
            }
        }
        return dest;
    }

    public static char recognizeSymbol(IplImage symbol, int flag) {
        char[] char1 = {'A', 'B', 'C', 'E', 'H', 'K', 'M', 'O', 'P', 'T', 'X', 'I'};
        char[] char2 = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        char[] letters;
        if (flag > 1 && flag < 6) {
            letters = char2;
        } else {
            letters = char1;
        }
        int min = templateMatching(symbol, letters[0], flag);
        char result = letters[0];
        int k;
        for (int i = 1; i < letters.length; i++) {
            k = templateMatching(symbol, letters[i], flag);
            if (k < min) {
                min = k;
                result = letters[i];
            }
        }
        return result;
    }

    

    public static int templateMatching(IplImage image, char a, int flag) {
        String s;
        if (flag > 1 && flag < 6) {
            s = "E:\\Simbols\\Numbers\\";
        } else {
            s = "E:\\Simbols\\";
        }
        File file = new File(s + a + ".txt");//45x57
        int n = image.asCvMat().rows();
        int m = image.asCvMat().cols();
        int[][] mat = new int[n][m];

        /*int[][] mat1 = new int[n][m];
         ProcessingTemplate pt = new ProcessingTemplate(n,m);
         for(int i=0;i<n;i++)
         for(int j=0;j<m;j++)
         {
         mat1[i][j]=0;
         if (cvGet2D(image.asCvMat(), i, j).getVal(0) == 0) mat1[i][j] = 1;
         }
         pt.processing(mat1);*/
        int sum = 0;
        try {
            Scanner scan = new Scanner(file);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    mat[i][j] = scan.nextInt();
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (cvGet2D(image.asCvMat(), i, j).getVal(0) == 0) {
                    sum += mat[i][j];
                }
                if ((cvGet2D(image.asCvMat(), i, j).getVal(0) == 255) && (mat[i][j] == 1)) {
                    sum++;/*= mat1[i][j]*/;
                }
            }
        }
        return sum;
    }

    public static ArrayList<IplImage> getFinalSymbols(IplImage img, int kl) {
        double old_rate = 0;
        ArrayList<IplImage> finalSymbols = new ArrayList<IplImage>();
        IplImage img1 = cvCreateImage(cvGetSize(img), IPL_DEPTH_8U, 1);
        CvSeq contour1 = new CvSeq(), contourLow = new CvSeq();
        CvMemStorage storage = CvMemStorage.create();
        cvCvtColor(img, img1, CV_RGB2GRAY);
        cvSmooth(img1, img1, CV_GAUSSIAN, 3, 3, 0, 0);
        cvCanny(img1, img1, 50, 100, 3);
        cvFindContours(img1, storage, contour1, Loader.sizeof(CvContour.class),
                CV_RETR_LIST, CV_CHAIN_APPROX_TC89_L1, cvPoint(0, 0));

        contourLow = cvApproxPoly(contour1, Loader.sizeof(CvContour.class), storage, CV_POLY_APPROX_DP, 1, 1);
        CvRect rect;
        for (; contourLow != null; contourLow = contourLow.h_next()) {
            rect = cvBoundingRect(contourLow, 0);
            double rate = (double) rect.width() / rect.height();
            double square = rect.height() * rect.width();
            if ((rate >= 2.4) && (rate < 6) && square > 990) {
                cvSetImageROI(img, rect);
                IplImage outImage = cvCreateImage(cvSize(rect.width(), rect.height()), img.depth(), img.nChannels());
                cvCopy(img, outImage);
                cvResetImageROI(img);
                double pixels = checkPixels(outImage);
                int jump = jump(outImage);
                //if (pixels > 1.8 && pixels < 3.5) {
                if (jump >= 9 && jump <= 18 && pixels > 1 && pixels < 1.8 && rate>old_rate) {
                    old_rate = rate;
                    IplImage tmp = aditionalCut(cutNumberVertical(outImage,kl));
                    IplImage tmp1 = cvCreateImage(cvGetSize(tmp), tmp.depth(), tmp.nChannels());
                    cvCopy(tmp, tmp1);
                    ArrayList<CvRect> array = new ArrayList<CvRect>();
                    array = selectAllSymbol(tmp);
                    int max_area = array.get(0).width() * array.get(0).height();
                    for (int i = 1; i < array.size(); i++) {
                        if (array.get(i).width() * array.get(i).height() > max_area) {
                            max_area = array.get(i).width() * array.get(i).height();
                        }
                    }
                    for (int i = 0; i < array.size(); i++) {
                        cvSetImageROI(tmp, array.get(i));
                        if ((double) array.get(i).width() * array.get(i).height() / max_area > 0.48) {

                            IplImage tmp2 = cvCreateImage(cvGetSize(tmp), tmp.depth(), tmp.nChannels());
                            cvCopy(tmp, tmp2);
                            finalSymbols.add(tmp2);


                        }
                    }
                }

            }

        }
        return finalSymbols;
    }

    public static int jump(IplImage image) {
        IplImage dest = cvCreateImage(cvGetSize(image), IPL_DEPTH_8U, 1);
        cvCvtColor(image, dest, CV_RGB2GRAY);
        cvThreshold(dest, dest, 120, 255, CV_THRESH_BINARY);
        int count = 0;
        boolean jump = true;//чорна полоса
        int i = (int) (image.height() / 2);
        for (int j = 0; j < dest.asCvMat().cols(); j++) {
            if ((cvGet2D(dest.asCvMat(), i, j).getVal(0) == 255) && (jump == true)) {
                count++;
                jump = false;
            }
            if ((cvGet2D(dest.asCvMat(), i, j).getVal(0) == 0) && (jump == false)) {
                jump = true;
            }
        }
        return count;
    }

    public static void main(String[] args) throws FileNotFoundException {
        NewJFrame a = new NewJFrame();
         a.setVisible(true);
        
    }
}