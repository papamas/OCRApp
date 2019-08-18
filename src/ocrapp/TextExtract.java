/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ocrapp;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author papamas
 */
public class TextExtract {
    
    static{ 
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    
    public static void main(String[] args) {
        Mat Main = Imgcodecs.imread("sample/SKKP.png");
        Mat rgb = new Mat();

        Imgproc.pyrDown(Main, rgb);

        Mat small = new Mat();

        Imgproc.cvtColor(rgb, small, Imgproc.COLOR_RGB2GRAY);

        Mat grad = new Mat();

        Mat morphKernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3,3));

        Imgproc.morphologyEx(small, grad, Imgproc.MORPH_GRADIENT , morphKernel);

        Mat bw = new Mat();

        Imgproc.threshold(grad, bw, 0.0, 255.0, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

        Mat connected = new Mat();

        morphKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(9,1));

        Imgproc.morphologyEx(bw, connected, Imgproc.MORPH_CLOSE  , morphKernel);


        Mat mask = Mat.zeros(bw.size(), CvType.CV_8UC1);

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        Mat hierarchy = new Mat();

        Imgproc.findContours(connected, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));

        for(int idx = 0; idx < contours.size(); idx++)
        {
            Rect rect = Imgproc.boundingRect(contours.get(idx));

            Mat maskROI = new Mat(mask, rect);

            maskROI.setTo(new Scalar(0, 0, 0));
            Imgproc.drawContours(mask, contours, idx, new Scalar(255, 255, 255), Core.FILLED);

            double r = (double)Core.countNonZero(maskROI)/(rect.width*rect.height);

            if (r > .45 && (rect.height > 8 && rect.width > 8))
            {
                Imgproc.rectangle(rgb, rect.br() , new Point( rect.br().x-rect.width ,rect.br().y-rect.height),  new Scalar(0, 255, 0));
            }

            String outputfile = "rectangle-text.png";
            Imgcodecs.imwrite(outputfile,rgb);
            
        }   
    }
    
}
