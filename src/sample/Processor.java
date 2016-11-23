package sample;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * Created by Lukado on 23. 11. 2016.
 */
public class Processor {

    public static void Processor(String args[]) {
        SeamCarving sc = new SeamCarving();
        ImageLoader il = new ImageLoader();
        int N = 50;	// number of seams want to remove

        // set file path
        String rawImageFilePath="img/weareliq.png";
        String energyImageFilePath="img/energy_weareliq.png";
        String outputImageFilePath="img/result_weareliq.png";

        // reading the input image
        System.out.println("Reading input image...");
        BufferedImage rawImage = il.readImage(rawImageFilePath);
        System.out.println("Successfully Read Image: "+rawImageFilePath);

        // convert color image to gray image
        System.out.println("\nConverting the image to Gray colors.");
        BufferedImage grayedOut = sc.grayOut(rawImage);
        System.out.println("Successful...");

        // calculate energy map
        System.out.println("\nGetting energy image.");
        BufferedImage gradientImage = sc.gradientFilter(grayedOut);
        System.out.println("Successful...");

        // write energy map
        System.out.println("\nWriting energy image to filesystems.");
        il.writeImage(gradientImage,energyImageFilePath , "jpg");
        System.out.println("Successfully Wrote Image To: "+energyImageFilePath);

        // read energy map
        System.out.println("Reading input image...");
        BufferedImage energyImage = il.readImage(energyImageFilePath);
        System.out.println("Successfully Read Image: "+energyImageFilePath);

        // enlarge energy map
        System.out.println("\nGetting enlarge energy image.");
        BufferedImage enlargeEnergyImg = sc.enlargeEnergy(energyImage);
        System.out.println("Successful...");

        // get cumulative energy map
        System.out.println("\nGetting cumulative energy image.");
        double[][] cumulativeEnergyArray = sc.getCumulativeEnergyArray(enlargeEnergyImg);
        System.out.println("Successful...");

        // iteratively doing seam carving
        rawImage = il.readImage(rawImageFilePath);
        double[][] new_cumulativeEnergyArray = cumulativeEnergyArray;
        BufferedImage removePathImg = rawImage;
        for (int n = 0; n < N; ++n){
            System.out.println("\nFinding the path.");
            int[] path  = sc.findPath(new_cumulativeEnergyArray);
            System.out.println("Successful...");

            System.out.println("\nRemoving the path in energy array.");
            new_cumulativeEnergyArray = sc.removePathEnergyArray(new_cumulativeEnergyArray, path);
            System.out.println("Successful...");

            System.out.println("\nRemoving the path.");
            removePathImg = sc.removePathFromImage(removePathImg, path);
            System.out.println("Successful...");
        }

        // save result
        System.out.println("\nWriting average image to filesystems.");
        il.writeImage(removePathImg,outputImageFilePath , "jpg");
        System.out.println("Successfully Wrote Image To: "+outputImageFilePath);

        // display result
        display(removePathImg);
//        System.exit(0);
    }


    /**
     * This method display an image from the input buffered image
     * @return void --> display the image in the frame
     */
    public static void display(BufferedImage img){
        JFrame frame = new JFrame("Title of the window :)");
        JLabel label = new JLabel(new ImageIcon(img));
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}
