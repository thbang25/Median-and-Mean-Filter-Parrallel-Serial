 //2D Median Filter for Image Smoothing
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
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.*;

public class MedianFilterParallel extends RecursiveAction {

   private int[] original;
   private int Initiate;
   private int Area;
   private int[] New;
   private int window; // Processing window size, should be odd.

   public MedianFilterParallel(int[] OriginalPic, int initiate, int area, int[] newPic, int windowWidth ) {
      original = OriginalPic;
      Initiate = initiate;
      Area = area;
      New = newPic;
      window= windowWidth;}


   // Average pixels from source, write results into destination.
   protected void computeDirectly() {
      int sq = window*window;
      int centre = (window- 1) / 2;
      for (int pos = Initiate; pos < Initiate + Area; pos++) {
            // Calculate average.
         float red = 0, green = 0, blue = 0;
         for (int t = -centre; t <= centre; t++) {
            int position = Math.min(Math.max(t + pos, 0), original.length - 1);
            int pixel = original[position];
            red += (float) ((pixel & 0x00ff0000) >> 16);
            green += (float) ((pixel & 0x0000ff00) >> 8);
            blue += (float) ((pixel & 0x000000ff) >> 0);
         }
      
            // Re-assemble destination pixel.
         int Pixel = (0xff000000)| (((int) red) << 16) | (((int) green) << 8) | (((int) blue) << 0);
         New [pos] = Pixel;
      }
   }

   
   protected static int sThreshold = 50000;

   @Override
   protected void compute() {
      if (Area < sThreshold) {
         computeDirectly();
         return;
      }
      int half = (Area-Initiate) / 2;
   
      MedianFilterParallel left = new MedianFilterParallel(original, Initiate, Initiate+half , New, window);
      MedianFilterParallel right = new MedianFilterParallel(original, Initiate+half, Area, New, window);
      invokeAll(left,right);
   }

   // Plumbing follows.
   public static void main(String[] args) throws Exception {
      if( args.length==0 | args.length==1| args.length==2 | args.length>3){//check if we got arguments
         System.out.println("only 3 arguments please");
         System.exit(0);}
   
      String picName = args[0];//picture name
      String output = args[1];//new picture
      int windowWidth = Integer.parseInt(args[2]);//window
      
       
      if(windowWidth > 21 && windowWidth < 3 && windowWidth % 2 == 0){//check if window width is valid
         System.out.println("window width is invalid, good bye.");
         System.exit(0);}
      
      
      String picture = picName;
      File fileInput = new File(picture);
      BufferedImage pic = ImageIO.read(fileInput);
      System.out.println("input picture name: " + picture);
      
         
      int w = pic.getWidth();
      int h = pic.getHeight();
   
      int[] OriginalPic = pic.getRGB(0, 0, w, h, null, 0, w);
      int[] newPic = new int[OriginalPic.length];
   
      MeanFilterParallel fb = new MeanFilterParallel(OriginalPic, 0, OriginalPic.length, newPic ,windowWidth);
   
      ForkJoinPool pool = new ForkJoinPool();
   
      long startTime = System.currentTimeMillis();
      pool.invoke(fb);
      long endTime = System.currentTimeMillis();
   
      System.out.println((endTime - startTime) + " seconds(m).");
   
      BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
      newImage.setRGB(0, 0, w, h, newPic, 0, w);
      
      String NewImage = output;
      File newImageFile = new File(NewImage);
      ImageIO.write(newImage, "jpg", newImageFile);
      
      System.out.println("Output pictureName: " + output);
      
   }
}

   

