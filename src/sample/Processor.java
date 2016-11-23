package sample;
import java.awt.image.BufferedImage;

/**
 * Created by Lukado on 23. 11. 2016.
 */
public class Processor {

    public Processor() {
        SeamCarving sc = new SeamCarving();
        ImageLoader il = new ImageLoader();
        int N = 500;

        String rawImageFilePath="img/weareliq.png";
        String energyImageFilePath="img/energy_weareliq.png";
        String seamImageFilePath="img/seam_weareliq.png";
        String outputImageFilePath="img/result_weareliq.png";

        BufferedImage rawImage = il.readImage(rawImageFilePath);

        BufferedImage grayedOut = sc.grayOut(rawImage);
        il.writeImage(grayedOut, seamImageFilePath, "png");
        BufferedImage gradientImage = sc.gradientFilter(grayedOut);
        il.writeImage(gradientImage,energyImageFilePath , "jpg");
//
//        BufferedImage energyImage = il.readImage(energyImageFilePath);
//        BufferedImage enlargeEnergyImg = sc.enlargeEnergy(energyImage);
//        double[][] cumulativeEnergyArray = sc.getCumulativeEnergyArray(enlargeEnergyImg);
//        rawImage = il.readImage(rawImageFilePath);
//
//        double[][] new_cumulativeEnergyArray = cumulativeEnergyArray;
//        BufferedImage removePathImg = rawImage;
//        for (int n = 0; n < N; ++n){
//            int[] path  = sc.findPath(new_cumulativeEnergyArray);
//            new_cumulativeEnergyArray = sc.removePathEnergyArray(new_cumulativeEnergyArray, path);
//            removePathImg = sc.removePathFromImage(removePathImg, path);
//        }
//
//        il.writeImage(removePathImg, outputImageFilePath, "jpg");
    }
}
