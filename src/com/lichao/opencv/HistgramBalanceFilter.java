package com.lichao.opencv;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * 直方图  均衡化
 * @author LiChao
 *
 */
public class HistgramBalanceFilter extends AbstractImageOptionFilter{

	@Override
	public BufferedImage process(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels = new int[width * height];
		int[] output = new int[width * height];
		getRGB(image, 0, 0, width, height, pixels);
		BufferedImage dest = createCompatibleDestImage(image, image.getColorModel());
		int index = 0;
		double[] histData = new double[256];
		Arrays.fill(histData, 0);
		for(int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				index = row * width + col;
				int pixel = pixels[index];
				int tr = (pixel >> 16) & 0xff;  //red
				histData[tr]++;
			}
		}
		
		// 归一化
		double total = pixels.length;
		for(int i = 0; i < histData.length; i++) {
			histData[i] = histData[i] / total;
		}
		
		// setup lookup table
		int[] lut = new int[256];
		for(int i= 0; i < lut.length; i++) {
			double sum = 0.0;
			for(int j = 0; j <= i; j++) {
				sum += histData[j];
			}
			lut[i] = (int)Math.floor(sum * 255 + 0.5);
		}
		
		// 直方图均衡化
		for(int row=0; row<height; row++) {
			for(int col=0; col<width; col++) {
				index = row * width + col;
				int pixel = pixels[index];
				int tr = (pixel >> 16) & 0xff;
				output[index] = (0xff << 24 | lut[tr] << 16 | lut[tr] << 8 | lut[tr]);
			}
		}
				
		setRGB(dest, 0, 0, width, height, output);
		return dest;
	}

}
