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
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.utils.Converters;

/**
 *
 * @author papamas
 */
public class OCRApp {   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
         System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //load the image and compute the ratio of the old height
        Mat src = Imgcodecs.imread("sample/receipt.jpg");
        
        // convert the image to grayscale, blur it, and find edges
        //in the image
        Mat gray = new Mat();
        Mat edged = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(gray, gray, new Size(5,5), 0);
        
        // for the canny edge detection algorithm,
        // play with these to see different results
        int threshold1 = 75;
        int threshold2 = 200;
        
        Imgproc.Canny(gray,edged ,threshold1, threshold2);
        //HighGui.imshow("Image", src);
        //HighGui.imshow("Edged", edged);
        //HighGui.waitKey();
        //HighGui.destroyAllWindows();
        Imgcodecs.imwrite("edged.jpg", edged);
        System.out.println("STEP 1: Edge Detection");
                
        List<MatOfPoint> contoursList = new ArrayList<>();
        
        Imgproc.findContours(edged, 
                contoursList,
                new Mat(),
                Imgproc.RETR_LIST,
                Imgproc.CHAIN_APPROX_SIMPLE,
                new Point(0,0));
       
        // Draw all the contours such that they are filled in.
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        Mat contourImg = new Mat(src.size(), src.type());
        
        for (int i = 0; i < contoursList.size(); i++) {
            System.out.println("STEP 2: Find contours of paper");
             //Convert contours from MatOfPoint to MatOfPoint2f
            MatOfPoint2f contour2f = new MatOfPoint2f(contoursList.get(i).toArray());
            //Processing on mMOP2f1 which is in type MatOfPoint2f
            double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
            if (approxDistance > 1) {
                //Find Polygons
                Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);
                 //Convert back to MatOfPoint
                MatOfPoint points = new MatOfPoint(approxCurve.toArray());
                Rect rect = Imgproc.boundingRect(points);
                 
                if (points.total() == 4 &&
                        Math.abs(Imgproc.contourArea(points)) > 1000 && 
                        Imgproc.isContourConvex(points))
                {
                    System.out.println( rect.tl().toString());
                            
                    Imgproc.drawContours(contourImg ,
                            contoursList,
                            i,
                            new Scalar(255, 255, 255),
                            -1);
                    
                       
                   
                }
            }           
        }
        
        Imgcodecs.imwrite("outline.jpg", contourImg); // DEBUG
       
       
       
        
        
        
    }
    
    
    
}
