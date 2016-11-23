package sample;

import java.awt.color.ColorSpace;
import java.awt.image.*;

/**
 * Created by Lukado on 23. 11. 2016.
 */
public class SeamCarving {
    /**
     * This method convert the color image into grayscale image
     * @return BufferedImage  --> img
     */
    public BufferedImage grayOut(BufferedImage img) {
        ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace
                .getInstance(ColorSpace.CS_GRAY), null);
        colorConvert.filter(img, img);

        return img;
    }

    /**
     * This method get the energy map of input image
     * @return BufferedImage  --> output_img
     */
    public BufferedImage gradientFilter (BufferedImage img){
        int type = img.getType();
        System.out.println("image type is" + type);
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage temp_img1 = new BufferedImage(width, height, type);
        BufferedImage temp_img2 = new BufferedImage(width, height, type);
        BufferedImage output_img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        float[] matrix_vertical = { -1.0F, 0.0F, 1.0F,
                -1.0F, 0.0F, 1.0F,
                -1.0F, 0.0F, 1.0F,};
        float[] matrix_horizontal = { 1.0F,  1.0F,  1.0F,
                0.0F,  0.0F,  0.0F,
                -1.0F, -1.0F, -1.0F,};
        Kernel kernel_v = new Kernel(3,3,matrix_vertical);
        Kernel kernel_h = new Kernel(3,3,matrix_horizontal);
        ConvolveOp convolve_v = new ConvolveOp(kernel_v);
        ConvolveOp convolve_h = new ConvolveOp(kernel_h);
        convolve_v.filter(img, temp_img1);
        convolve_h.filter(img, temp_img2);

        WritableRaster raster = output_img.getRaster();

        for (int y = 0; y < height; ++y){
            for (int x = 0; x < width; ++x){
                float sum = 0.0f;
                sum = (   Math.abs(temp_img1.getRaster().getSample(x, y, 0))
                        + Math.abs(temp_img2.getRaster().getSample(x, y, 0)) );
                raster.setSample(x, y, 0, Math.round(sum));
            }
        }
        return output_img;
    }

    /**
     * This method remove the path from input image
     * @return BufferedImage  --> removePathImg
     */
    public BufferedImage removePathFromImage(BufferedImage img, int[] path){
        int type = img.getType();
        int width = img.getWidth();
        int height = img.getHeight();
        int band = 3;
        BufferedImage removePathImg = new BufferedImage(width-1, height, type);
        WritableRaster raster = removePathImg.getRaster();

        for (int b = 0; b < band; ++b){
            for (int y = 0; y < height; ++y){
                for (int x = 0; x <= path[y]-2; ++x){
                    double temp = 0.0;
                    temp = img.getRaster().getSample(x, y, b);
                    raster.setSample(x, y, b, Math.round(temp));
                }
                for (int x = path[y]-1; x < width-1; ++x){
                    double temp = 0.0;
                    temp = img.getRaster().getSample(x+1, y, b);
                    raster.setSample(x, y, b, Math.round(temp));
                }
            }
        }
        return removePathImg;
    }
    /**
     * This method remove the path from energy array
     * @return double[][]  --> new_cumulativeEnergyArray
     */
    public double[][] removePathEnergyArray(double[][] cumulativeEnergyArray, int[] path){
        int width = cumulativeEnergyArray[0].length;
        int height = cumulativeEnergyArray.length;
        double[][] new_cumulativeEnergyArray = new double[height][width-1];
        for (int y = 0; y < height; ++y){
            for (int x = 0; x <= path[y]-1; ++x){
                new_cumulativeEnergyArray[y][x] = cumulativeEnergyArray[y][x];
            }
            for (int x = path[y]; x < width-1; ++x){
                new_cumulativeEnergyArray[y][x] = cumulativeEnergyArray[y][x+1];
            }
        }
        return new_cumulativeEnergyArray;
    }

    /**
     * This method calculate the cumulative energy array
     * @return double[][]  --> cumulative_energy_array
     */
    public double[][] getCumulativeEnergyArray (BufferedImage img){
        int width = img.getWidth();
        int height = img.getHeight();
        double[][] cumulative_energy_array = new double[height][width];

        for (int y = 1; y < height; ++y){
            for (int x = 1; x < width-1; ++x){
                cumulative_energy_array[y][x] = (double)img.getRaster().getSample(x,y,0);
            }
        }

        for (int y = 1; y < height; ++y){
            for (int x = 1; x < width-1; ++x){
                double temp = 0.0;
                double tempArray3[] = new double[3];
                tempArray3[0] = cumulative_energy_array[y-1][x-1];
                tempArray3[1] = cumulative_energy_array[y-1][x];
                tempArray3[2] = cumulative_energy_array[y-1][x+1];
                temp = getMinValue(tempArray3) + (double)img.getRaster().getSample(x,y,0);
                cumulative_energy_array[y][x] = temp;
            }
        }
        return cumulative_energy_array;
    }

    /**
     * This method find the minimum cost path from
     * cumulative energy array
     * @return int[]  --> path
     */
    public int[] findPath (double[][] cumulativeEnergyArray){
        int width = cumulativeEnergyArray[0].length;
        int height = cumulativeEnergyArray.length;
        int[] path = new int[height];

        double[] tempArray = new double[width-10];
        int y = height-1;
        for (int x = 5; x < width-5; ++x){
            tempArray[x-5] = cumulativeEnergyArray[y][x];
        }

        int ind_bot = getMinIndex(tempArray)+5;
        System.out.println("\nThe bottom index is: "+ind_bot);
        path[height-1] = ind_bot;

        int ind_temp = 0;
        double[] tempArray2 = new double[3];
        for (int i = height-1; i > 0; --i){
            tempArray2[0] = cumulativeEnergyArray[i-1][path[i]-1];
            tempArray2[1] = cumulativeEnergyArray[i-1][path[i]];
            tempArray2[2] = cumulativeEnergyArray[i-1][path[i]+1];
            ind_temp = getMinIndex(tempArray2);
            path[i-1] = path[i] + ind_temp - 1;
            if (path[i-1] <= 0){
                path[i-1] = 1;
            }
            else if (path[i-1] >= width-1){
                path[i-1] = width-2;
            }
        }
        return path;
    }

    /**
     * This method enlarge the width of energy image
     * to prevent the boundary effect from convolution
     * @return enlarge (width) image --> enlarge_energy_img
     */
    public BufferedImage enlargeEnergy (BufferedImage img){
        int type = img.getType();
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage enlarge_energy_img = new BufferedImage(width+2, height, type);
        WritableRaster raster = enlarge_energy_img.getRaster();
        for (int y = 0; y < height; ++y){
            for (int x = 1; x < width+1; ++x){
                raster.setSample(x, y, 0, img.getRaster().getSample(x-1, y, 0));
            }
        }
        for (int x = 0; x < 10; ++x){
            for (int y = 0; y < height; ++y){
                raster.setSample(x, y, 0, 255);
            }
        }
        for (int x = width+1; x > width-9; --x){
            for (int y = 0; y < height; ++y){
                raster.setSample(x, y, 0, 255);
            }
        }
        return enlarge_energy_img;
    }

    /**
     * This method get the index of min element in input array
     * @return int  --> minIndex
     */
    public static int getMinIndex(double[] numbers){
        double minValue = numbers[0];
        int minIndex = 0;
        for(int i=0;i<numbers.length;i++){
            if(numbers[i] < minValue){
                minValue = numbers[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    /**
     * This method get the min value in input array
     * @return double  --> minValue
     */
    public static double getMinValue(double[] numbers){
        double minValue = numbers[0];
        for(int i=0;i<numbers.length;i++){
            if(numbers[i] < minValue){
                minValue = numbers[i];
            }
        }
        return minValue;
    }
}
