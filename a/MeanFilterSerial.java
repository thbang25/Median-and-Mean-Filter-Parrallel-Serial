//2D Mean Filter for Image Smoothing
//Thabang Sambo
//CSC2002S
//04 August 2022
//getting our packages
import java.util.*;
import java.io.*;
import java.awt.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

public class MeanFilterSerial {
   public static void main(String[] args) throws IOException, InterruptedException {//main
   
      if( args.length==0 | args.length==1| args.length==2 | args.length>3){//check if we got arguments
         System.out.println("only 3 arguments please");
         System.exit(0);}
         
      String myfile = args[0];//picture name
      String output = args[1];//new picture
      int windowWidth = Integer.parseInt(args[2]);//window
      
   
      if(windowWidth <= 21 && windowWidth >= 3 && windowWidth % 2 != 0){//check if window width is valid
      
         long speedCount = System.currentTimeMillis(); //method to do the timing measurements
         
         File fileInput = new File(myfile);
         BufferedImage picture = ImageIO.read(fileInput);
        
         int height = picture.getHeight();
         int width = picture.getWidth();
        
         int widthSquare = windowWidth*windowWidth;
        
         Color[] color; //declaring our color array
         color = new Color[widthSquare];
                  
         int centre = (windowWidth-1)/2;
      
         for(int y=0;y<height;y++){
            for(int x=centre;x<width;x++){
               int count = 0;
            
               for(int next=y;next<y+windowWidth;next++){
                  int red = 0, green = 0, blue = 0;
               
                  for (int position = -windowWidth; position <= windowWidth; position++) {
                     int value = Math.min(Math.max(position + x, 0),width - 1);
                     int pixel = picture.getRGB(value,y);
                     red += (float)((pixel & 0x00ff0000) >> 16)
                        / widthSquare;
                     green += (float)((pixel & 0x0000ff00) >>  8)
                        / widthSquare;
                     blue += (float)((pixel & 0x000000ff) >>  0)
                        / widthSquare;
                     count++;}
                 
               
                // Reassemble destination pixel.
                  int Pixel = (0xff000000) |(((int)red) << 16) |(((int)green) <<  8) |(((int)blue) <<  0);   
                  picture.setRGB(x,y,Pixel);}
            }
         }
              
         long stopCount = System.currentTimeMillis();
         System.out.println(Math.abs(speedCount-stopCount)+" seconds(m)");//time taken
         
         ImageIO.write(picture, "jpg", new File(output));

         }
      else{System.out.println("window width is invalid, good bye.");//window filter invalid
         System.exit(0);}
   }
}
