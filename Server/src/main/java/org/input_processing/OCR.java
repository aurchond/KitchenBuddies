package org.input_processing;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Core;

import java.io.File;

public class OCR {
    public void OCR() {

    }

    public void TestOCRBasic(){
        System.out.println("Welcome to OpenCV " + Core.VERSION);

        Tesseract tess = new Tesseract();
        try {
            System.out.println("Testing OCR");

            // TODO: Get relative path
            tess.setDatapath("C:\\Users\\12049\\Documents\\Capstone\\KitchenBuddies\\Server\\res\\tess_data");
            String text = tess.doOCR(new File("C:\\Users\\12049\\Documents\\Capstone\\KitchenBuddies\\Server\\res\\images\\base_test.png"));

            System.out.println(text);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    }

    public void TestOCRAdvanced(){
        System.out.println("Welcome to OpenCV " + Core.VERSION);

        Tesseract tess = new Tesseract();
        try {
            System.out.println("Testing OCR");

            // TODO: Get relative path
            tess.setDatapath("C:\\Users\\12049\\Documents\\Capstone\\KitchenBuddies\\Server\\res\\tess_data");
            String text = tess.doOCR(new File("C:\\Users\\12049\\Documents\\Capstone\\KitchenBuddies\\Server\\res\\images\\carrot_cake.png"));

            System.out.println(text);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    }
}
