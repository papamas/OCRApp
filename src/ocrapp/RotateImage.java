/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ocrapp;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author papamas
 */
public class RotateImage {
    
    static{ 
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public static void main(String[] args) {
        Mat source = Imgcodecs.imread("sample/receipt.jpg");
        Mat rotMat = new Mat(2, 3, CvType.CV_32FC1);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        Point center = new Point(destination.cols() / 2, destination.rows() / 2);
        rotMat = Imgproc.getRotationMatrix2D(center, 30, 1);
        Imgproc.warpAffine(source, destination, rotMat, destination.size());
        Imgcodecs.imwrite("receipt-rotate.jpg", destination);

    }
    
}
