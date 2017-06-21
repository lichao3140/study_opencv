package com.lichao.opencv;

import java.awt.image.BufferedImage;

/**
 * 调整图像对比度
 * @author LiChao
 *
 */
public class ContrastAdjustFilter extends AbstractImageOptionFilter {
	
	private float minValue;
	private float maxValue;
	private float defaultMinValue;
	private float defaultMaxValue;
	private float contrast;
	private int[] lut;

	public ContrastAdjustFilter() {
		defaultMinValue = 0;
		defaultMaxValue = 255;
		minValue = 0;
		maxValue = 255;
		contrast = 1.2f;
	}

	public void setContrast(float bvalue) {
		this.contrast = bvalue;
	}
	
	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}

	@Override
	public BufferedImage process(BufferedImage src) {
		int width = src.getWidth();
		int height = src.getHeight();
		int[] pixels = new int[width * height];
		getRGB(src, 0, 0, width, height, pixels);
		int index = 0;
		// setupMinMaxLut();
		// 计算均值
		double redsum = 0;
		double greensum = 0;
		double bluesum = 0;
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				index = row * width + col;
				int pixel = pixels[index];
				int ta = (pixel >> 24) & 0xff; // Alpha
				int tr = (pixel >> 16) & 0xff; // red
				int tg = (pixel >> 8) & 0xff; // green
				int tb = (pixel) & 0xff; // blue
				
				redsum += tr;
				greensum += tg;
				bluesum += tb;
				
				// 使用查找表
				// pixels[index] = (ta << 24 | lut[tr] << 16 | lut[tg] << 8 | lut[tb]);
			}
		}
		double count = (width * height);
		double rm = redsum / count ;
		double gm = greensum / count;
		double bm = bluesum / count;
		// adjust contrast
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				index = row * width + col;
				int pixel = pixels[index];
				int ta = (pixel >> 24) & 0xff; // Alpha
				int tr = (pixel >> 16) & 0xff; // red
				int tg = (pixel >> 8) & 0xff; // green
				int tb = (pixel) & 0xff; // blue
				
				double pr = tr - rm;
				double pg = tg - gm;
				double pb = tb - bm;
				
				tr = (int)(pr*contrast + rm);
				tg = (int)(pg*contrast + gm);
				tb = (int)(pb*contrast + bm);

				pixels[index] = (ta << 24 | clamp(tr) << 16 | clamp(tg) << 8 | clamp(tb));
			}
		}
		setRGB(src, 0, 0, width, height, pixels);
		return src;
	}
	

	public void setupMinMaxLut() {

		double min = defaultMinValue + minValue*(defaultMaxValue-defaultMinValue)/255;
		double max = defaultMinValue + maxValue*(defaultMaxValue-defaultMinValue)/255;
		double slope;
		double center = min + (max-min)/2.0;
		double range = defaultMaxValue-defaultMinValue;
		double mid = 128;
		if (contrast<=mid)
			slope = contrast/mid;
		else
			slope = mid/(256-contrast);
		if (slope>0.0) {
			min = center-(0.5*range)/slope;
			max = center+(0.5*range)/slope;
		}

		System.out.println("min : " + min + ", max : " + max);
		lut = new int[256];
		for(int i=0; i<256; i++) {
			double v1 = (i - min);
			double delta = max - min;
			int v = (int)((v1 / delta) * 256);
			if(v < 0) {
				v = 0;
			}
			if(v > 255) {
				v = 255;
			}
			lut[i] = v;
		}
	}

}
