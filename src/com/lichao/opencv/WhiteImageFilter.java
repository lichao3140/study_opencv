package com.lichao.opencv;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * 图像美白优化
 * @author LiChao
 *
 */
public class WhiteImageFilter extends AbstractImageOptionFilter{

	private double beta;
	
	public WhiteImageFilter() {
		this.beta = 1.2;
	}
	@Override
	public BufferedImage process(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels = new int[width * height];
		getRGB(image, 0, 0, width, height, pixels);
		int index = 0;
		int[] lut = new int[256];//LUT查找表
		for(int i=0; i<256; i++) {
			lut[i] = imageMath(i);
		}
		for (int row = 0; row < height; row++) {//列
			for (int col = 0; col < width; col++) {//行
				index = row * width + col;
				int pixel = pixels[index];//读像素
				int ta = (pixel >> 24) & 0xff;//透明通道 Alpha
				int tr = (pixel >> 16) & 0xff;//红色通道 red
				int tg = (pixel >> 8) & 0xff;//绿色通道 green
				int tb = (pixel & 0xff);//蓝色通道 blue
				
				//转换成ycbcr显示
//				double[] ycbcr = ColorSpaceUtil.rgb2YCbCr(tr, tg, tb);
//				int[] rgb = ColorSpaceUtil.YCbCr2rgb(ycbcr[0], ycbcr[1], ycbcr[2]);
//				tr = rgb[0];
//				tg = rgb[1];
//				tb = rgb[2];
				
				//hsb转换
//				float[] hsbvals = new float[3];
//				Color.RGBtoHSB(tr, tg, tb, hsbvals);
//				int color = Color.HSBtoRGB(hsbvals[0], hsbvals[1], hsbvals[2]);
//				Color c = new Color(color);
//				tr = c.getRed();
//				tg = c.getGreen();
//				tb = c.getBlue();
				
				tr = lut[tr];
				tg = lut[tg];
				tb = lut[tb];
				
				pixels[index] = (ta << 24 | tr << 16 | tg << 8 | tb);
			}
		}
		setRGB(image, 0, 0, width, height, pixels);
		return image;
	}

	private int imageMath(int pixel) {
		double scale = 255 / (Math.log(255 * (this.beta - 1) + 1) / Math.log(this.beta));
		double p1 = Math.log(pixel * (this.beta - 1) + 1);
		double np = p1 / Math.log(this.beta);
		return (int)(np * scale);
	}
	
	public double getBeta() {
		return beta;
	}
	
	public void setBeta(double beta) {
		this.beta = beta;
	}
	
}
