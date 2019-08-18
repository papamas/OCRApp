/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ocrapp;
import net.sourceforge.tess4j.*;

import java.io.File;
import net.sourceforge.tess4j.util.LoadLibs;

/**
 *
 * @author papamas
 */
public class Tess4j {
    
   
    
     public static void main(String[] args) {
         
        System.setProperty("jna.debug_load", "true");
        File imageFile = new File("sample/eurotext.pdf");
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        //ITesseract instance = new Tesseract1(); // JNA Direct Mapping
        //instance.setDatapath("tessdata"); // path to tessdata directory
        
        //In case you don't have your own tessdata, let it also be extracted for you
        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        //Set the tessdata path
        instance.setDatapath(tessDataFolder.getAbsolutePath());
        

        try {
            String result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }
    
}
