package com.lichao.opencv;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * 直方图数据
 * @author LiChao
 *
 */
public class HistogramFilter extends AbstractImageOptionFilter{

	private int[] histData;

	public HistogramFilter() {
		histData = new int[256];
	}

	public int[] getHistData() {
		return histData;
	}

	public void setHistData(int[] histData) {
		this.histData = histData;
	}
	
	@Override
	public BufferedImage process(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels = new int[width * height];
		getRGB(image, 0, 0, width, height, pixels);
		int index = 0;
		Arrays.fill(histData, 0);
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				index = row * width + col;
				int pixel = pixels[index];
				int tr = (pixel >> 16) & 0xff; // red
				histData[tr]++;
			}
		}
		
		return image;
	}

}
