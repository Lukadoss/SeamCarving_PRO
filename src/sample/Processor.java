package sample;

import java.awt.image.BufferedImage;

/**
 * Created by Lukado on 23. 11. 2016.
 */
public class Processor {
    private String energyImageFilePath = "img/energy.png";
    private String seamImageFilePath = "img/seam.png";
    private String outputImageFilePath = "img/result.png";

    public Processor(String rawImageFilePath, int N) {
        SeamCarving sc = new SeamCarving();
        ImageLoader il = new ImageLoader();

        BufferedImage rawImage = il.readImage(rawImageFilePath);

        BufferedImage grayedOut = sc.grayOut(rawImage);
        BufferedImage gradientImage = sc.gradientFilter(grayedOut);
        il.writeImage(gradientImage, energyImageFilePath, "jpg");

        BufferedImage energyImage = il.readImage(energyImageFilePath);
        BufferedImage enlargeEnergyImg = sc.enlargeEnergy(energyImage);
        il.writeImage(enlargeEnergyImg, seamImageFilePath, "jpg");

        double[][] cumulativeEnergyArray = sc.getCumulativeEnergyArray(enlargeEnergyImg);
        rawImage = il.readImage(rawImageFilePath);

        double[][] new_cumulativeEnergyArray = cumulativeEnergyArray;
        BufferedImage removePathImg = rawImage;
        for (int n = 0; n < N; ++n){
            int[] path  = sc.findPath(new_cumulativeEnergyArray);
            new_cumulativeEnergyArray = sc.removePathEnergyArray(new_cumulativeEnergyArray, path);
            removePathImg = sc.removePathFromImage(removePathImg, path);
        }

        il.writeImage(removePathImg, outputImageFilePath, "jpg");
    }

    String getEnergyImg(){
        return energyImageFilePath;
    }

    String getSeamImg(){
        return seamImageFilePath;
    }

    String getOutputImg(){
        return outputImageFilePath;
    }
}
