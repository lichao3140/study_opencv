package com.lichao.opencv;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * 图像二值化
 * @author LiChao
 *
 */
public class BinaryFilter extends GrayFilter {

	@Override
	public BufferedImage process(BufferedImage src) {
		BufferedImage grayImage = super.process(src);
		int width = grayImage.getWidth();
		int height = grayImage.getHeight();
		int[] pixels = new int[width * height];
		getRGB(grayImage, 0, 0, width, height, pixels);
		
		BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int[] output = new int[width*height];
		// calculate means
		int means = 0;
		long sum = 0;
		int index = 0;
		int r=0,g=0,b=0, gray=0;
		
		for(int row=0; row<height; row++) {
			for(int col=0; col<width; col++) {
				index = row*width + col;
				r = (pixels[index]>>16)&0xff;
				sum += r;
			}
		}
		
		// threshold for binary image
		means = (int)(sum / pixels.length);
		System.out.println("binary threshold : " + means);
		for(int row=0; row<height; row++) {
			for(int col=0; col<width; col++) {
				index = row*width + col;
				r = (pixels[index]>>16)&0xff;
				if(r >= means) {
					output[index] = Color.WHITE.getRGB();
				}
				else
				{
					output[index] = Color.BLACK.getRGB();
				}
			}
		}
		
		setRGB(dest, 0, 0, width, height, output);
		return dest;
	}	
}
