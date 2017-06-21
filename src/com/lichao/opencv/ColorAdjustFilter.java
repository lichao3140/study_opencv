package com.lichao.opencv;

import java.awt.image.BufferedImage;

/**
 * 色彩调节
 * @author LiChao
 *
 */
public class ColorAdjustFilter extends AbstractImageOptionFilter{
	
	private float minValue;
	private float maxValue;
	private float defaultMinValue;
	private float defaultMaxValue;
	private int[] lut;
	private float bvalue;
	private boolean minmax;
	
	public ColorAdjustFilter() {
		defaultMinValue = 0;
		defaultMaxValue	= 255;
		minValue = 0;
		maxValue = 255;
		bvalue = 20;
		minmax = true;
	}
	
	@Override
	public BufferedImage process(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels = new int[width * height];
		getRGB(image, 0, 0, width, height, pixels);
		int index = 0;
		if(minmax) {
			setupMinMaxLut();
		}
		else {
			setupBrightnessLut();
		}
		for (int row = 0; row < height; row++) {//列
			for (int col = 0; col < width; col++) {//行
				index = row * width + col;
				int pixel = pixels[index];//读像素
				int ta = (pixel >> 24) & 0xff;//透明通道 Alpha
				int tr = (pixel >> 16) & 0xff;//红色通道 red
				int tg = (pixel >> 8) & 0xff;//绿色通道 green
				int tb = (pixel & 0xff);//蓝色通道 blue
				
				// 使用查找表
				pixels[index] = (ta << 24 | lut[tr] << 16 | lut[tg] << 8 | lut[tb]);
			}
		}
		setRGB(image, 0, 0, width, height, pixels);
		return image;
	}
	
	public void setupMinMaxLut() {
		float min = defaultMinValue + minValue*(defaultMaxValue-defaultMinValue)/255;
		float max = defaultMinValue + maxValue*(defaultMaxValue-defaultMinValue)/255;
		System.out.println("min : " + min + ", max : " + max);
		lut = new int[256];
		for(int i=0; i<256; i++) {
			float v1 = (i - min);
			float delta = max - min;
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
	
	/**
	 * 调节亮度
	 */
	public void setupBrightnessLut() {
		float center = defaultMinValue + (defaultMaxValue-defaultMinValue)*((256-bvalue)/256);
		float min = defaultMinValue + minValue*(defaultMaxValue-defaultMinValue)/255;
		float max = defaultMinValue + maxValue*(defaultMaxValue-defaultMinValue)/255;
		float delta= max-min;
		min = center - delta/2.0f;
		max = center + delta/2.0f;

		System.out.println("min : " + min + ", max : " + max);
		lut = new int[256];
		for(int i=0; i<256; i++) {
			float v1 = (i - min);
			float delta2 = max - min;
			int v = (int)((v1 / delta2) * 256);
			if(v < 0) {
				v = 0;
			}
			if(v > 255) {
				v = 255;
			}
			lut[i] = v;
		}
	}

	public float getMinValue() {
		return minValue;
	}

	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}

	public float getDefaultMinValue() {
		return defaultMinValue;
	}

	public void setDefaultMinValue(float defaultMinValue) {
		this.defaultMinValue = defaultMinValue;
	}

	public float getDefaultMaxValue() {
		return defaultMaxValue;
	}

	public void setDefaultMaxValue(float defaultMaxValue) {
		this.defaultMaxValue = defaultMaxValue;
	}

	public float getBvalue() {
		return bvalue;
	}

	public void setBvalue(float bvalue) {
		this.bvalue = bvalue;
	}

	public boolean isMinmax() {
		return minmax;
	}

	public void setMinmax(boolean minmax) {
		this.minmax = minmax;
	}
	
}
