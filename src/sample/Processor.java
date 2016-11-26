package sample;

import java.awt.image.BufferedImage;

/**
 * Created by Lukado on 23. 11. 2016.
 */
class Processor {
    private String energyImageFilePath = "img/energy.png";
    private String seamImageFilePath = "img/seam.png";
    private String outputImageFilePath = "img/result.png";
    private boolean nope;
    private int X;

    Processor(String rawImageFilePath, int N) {
        SeamCarving sc = new SeamCarving();
        ImageLoader il = new ImageLoader();
        BufferedImage rawImage = il.readImage(rawImageFilePath);

        BufferedImage grayedOut = sc.grayOut(rawImage);
        BufferedImage gradientImage = sc.gradientFilter(grayedOut);
        il.writeImage(gradientImage, energyImageFilePath, "jpg");

        BufferedImage energyImage = il.readImage(energyImageFilePath);
        BufferedImage enlargeEnergyImg = sc.enlargeEnergy(energyImage);

        double[][] cumulativeEnergyArray = sc.getCumulativeEnergyArray(enlargeEnergyImg);
        rawImage = il.readImage(rawImageFilePath);

        BufferedImage removePathImg = rawImage;
        int paths[][] = new int[N][rawImage.getHeight()];
        nope = true;
        for (int n = 0; n < N; ++n){
            int[] path  = sc.findPath(cumulativeEnergyArray);
            if (path.length == 1) {
                nope = false;
                X = n;
                break;
            }
            cumulativeEnergyArray = sc.removePathEnergyArray(cumulativeEnergyArray, path);
            removePathImg = sc.removePathFromImage(removePathImg, path);
            paths = sc.writePaths(path, paths, n);
        }
        if (nope) {
            BufferedImage seamsPathImg = sc.writePathsToImage(grayedOut, removePathImg, paths);
            il.writeImage(seamsPathImg, seamImageFilePath, "jpg");
        }
        il.writeImage(removePathImg, outputImageFilePath, "jpg");
    }

    int getIterNum(){ return X;}

    boolean getNope(){
        return nope;
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
