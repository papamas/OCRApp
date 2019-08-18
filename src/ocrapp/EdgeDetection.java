/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ocrapp;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;

/**
 *
 * @author papamas
 */
public class EdgeDetection {
    
    static{ 
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main( String[] args ) throws Exception{      
        Mat src = Imgcodecs.imread("sample/receipt.jpg");
        Mat src_gray = new Mat();
        Mat output = new Mat();
        
        //convert the image to grayscale and flip the foreground
        //and background to ensure foreground is now "white" and
        //the background is "black"
        
        Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
        Imgcodecs.imwrite("src_gray.jpg", src_gray);
        Core.bitwise_not(src_gray, src_gray);
        
        //hreshold the image, setting all foreground pixels to
        //255 and all background pixels to 0
        Imgproc.threshold(src_gray,
                output,
                0,
                255,
                THRESH_BINARY | Imgproc.THRESH_OTSU);
        
        Imgcodecs.imwrite("threshold_output.jpg", output);

        Mat points = Mat.zeros(output.size(),output.type());  
        Core.findNonZero(output, points);   

        MatOfPoint mpoints = new MatOfPoint(points);    
        MatOfPoint2f points2f = new MatOfPoint2f(mpoints.toArray());
        RotatedRect box = Imgproc.minAreaRect(points2f);

        Mat src_squares = src.clone();
        Mat rot_mat = Imgproc.getRotationMatrix2D(box.center, box.angle, 1);
        Mat rotated = new Mat(); 
        Imgproc.warpAffine(src_squares, rotated, rot_mat, src_squares.size(), Imgproc.INTER_CUBIC);
        Imgcodecs.imwrite("inclined_text_squares_rotated.jpg",rotated);    
    }
    
    private static Mat DeeskewImage(Mat src, double angle) {
        
        Point center = new Point(src.width()/2, src.height()/2);
        Mat rotImage = Imgproc.getRotationMatrix2D(center, angle, 1.0);
        //1.0 means 100 % scale
        Size size = new Size(src.width(), src.height());
        Imgproc.warpAffine(src, src, rotImage, size, Imgproc.INTER_LINEAR + Imgproc.CV_WARP_FILL_OUTLIERS);
        return src;
    }
}
