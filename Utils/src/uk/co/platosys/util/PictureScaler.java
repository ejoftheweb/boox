/*
 * PictureScaler.java
 *
 * Created on 04 September 2007, 13:29
 *
 * This code is copyright PLATOSYS,
 * to the extent that copyright subsists.
 *
 *
 */

package uk.co.platosys.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This is a utility class to scale an image to a specific size.
 * it preserves aspect ratio and scales a rectangular image so that its longest side is 
 * outputSize pixels long, regardless of the size of the input image. Its normal use is for reducing the size of images uploaded to a website.
 *
 * It works by default with .bmp, .jpeg and .png files
 *
 * @author edward
 */
public class PictureScaler {
private static Logger logger = Logger.getLogger("PictureScaler");
    /**
     * Creates a new instance of PictureScaler
     */
    public PictureScaler() {
    }

    /**
     * 
     * @param image a Buffered Image
     * @param outputSize the required largest output dimension in pixels
     * @throws java.lang.Exception 
     * @return a scaled buffered image
     */
    private static BufferedImage scale(BufferedImage image, int outputSize) throws Exception {
        try{
        int height=new Integer(image.getHeight());
        int width=image.getWidth();
        Integer maxSize=new Integer(Math.max(height, width));
        Integer outputSiz=new Integer(outputSize);
        float scaleFactor = outputSiz.floatValue()/maxSize.floatValue();
        int newHeight = Math.round(height*scaleFactor);
        int newWidth = Math.round(width*scaleFactor);
        //klujj to deal with disappeareing png type in ImageIO.
        int type=1;
        if (image.getType()!=0){
            type=image.getType();
        }
        BufferedImage bufim2 = new BufferedImage(newWidth, newHeight, type);
        Graphics2D g2d = (Graphics2D) bufim2.getGraphics();
        g2d.scale(scaleFactor, scaleFactor);
        g2d.drawImage(image, 0, 0, null);
        return bufim2;
        }catch(Exception x){
            logger.log("Bufim scale problem", x);
            throw x;
        }
    }
    
    /**
     * generates a BufferedImage scaled to a new size
     * @param file The image file to be rescaled
     * @param outputSize The largest dimension of the output image in pixels
     * @throws java.lang.Exception if things go wrong
     * 
     * @return a BufferedImage scaled to the new size
     */
    public static BufferedImage scale(File file, int outputSize) throws Exception {
       BufferedImage bufferedImage = null;
       try{
          
           
           
           bufferedImage=ImageIO.read(file);


           if(bufferedImage==null){
               throw new Exception("PS couldn't read file, no reader registered for this file-type");
           }
           if (bufferedImage.getType()==0){

           }
           logger.log("PS read file:"+file.getName()+", type is: "+bufferedImage.getType());
       }catch(Exception ioex){
           logger.log("PS problem reading file", ioex);
           throw ioex;
       }
       return scale(bufferedImage, outputSize);
    }
    
    /**
     * creates a new file containing an image scaled to a new size and optionally deletes the input file.
     * @param inputFile the source image file
     * @param outputFile the file to put the output file to
     * @param outputSize the size of the longest dimension of the output file in pixels
     * @param eraseInput if true, the input file is deleted
     * @throws java.lang.Exception if things go wrong
     * @return true if successful
     */
    public static boolean scale (File inputFile, File outputFile, int outputSize, boolean eraseInput) throws Exception {
        BufferedImage image = scale(inputFile, outputSize);
        try{
            ImageIO.write(image, "jpg", outputFile);
            if (eraseInput){
                try{
                    inputFile.delete();
                }catch(Exception ex){
                    return false;
                }
            }
            return true;
        }catch(Exception e){
            logger.log("problem writing output file");
            return false;
        }
    }
    

    /**
     * creates a new file containing an image scaled to a new size
     * @param inputFile the source image file
     * @param outputFile the file to put the output file to
     * @param outputSize the size of the longest dimension of the output file in pixels
     * @throws java.lang.Exception if things go wrong
     * @return true if successful
     */
    public static boolean scale (File inputFile, File outputFile, int outputSize) throws Exception {
         return scale(inputFile, outputFile, outputSize, false);
    }
}
    

       