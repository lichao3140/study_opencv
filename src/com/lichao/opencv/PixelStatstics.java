package com.lichao.opencv;

import java.awt.image.BufferedImage;

/**
 * 灰度图片读取
 * @author LiChao
 *
 */
public class PixelStatstics extends AbstractImageOptionFilter{

	private int min;//最小值
	private int max;//最大值
	private double means;//均值
	private double sd;//方差为0,则为空白图像
	
	public PixelStatstics() {
		this.min = 255;
		this.max = 0;
		this.means = 0;
		this.sd = 0;
	}
	
	@Override
	public BufferedImage process(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels = new int[width * height];
		getRGB(image, 0, 0, width, height, pixels);
		int index = 0;
		double sum = 0;
		for (int row = 0; row < height; row++) {//列
			for (int col = 0; col < width; col++) {//行
				index = row * width + col;
				int pixel = pixels[index];//读像素
				//int ta = (pixel >> 24) & 0xff;//透明通道 Alpha
				int tr = (pixel >> 16) & 0xff;//红色通道 red
				//int tg = (pixel >> 8) & 0xff;//绿色通道 green
				//int tb = (pixel & 0xff);//蓝色通道 blue
				
				min = Math.min(min, tr);
				max = Math.max(max, tr);
				sum += tr;
			}
		}
		means = sum / (width * height);
		System.out.println("min:" + min);
		System.out.println("max:" + max);
		System.out.println("means:" + means);
		
		sum = 0;
		//求方差
		for (int row = 0; row < height; row++) {//列
			for (int col = 0; col < width; col++) {//行
				index = row * width + col;
				int pixel = pixels[index];//读像素
				int tr = (pixel >> 16) & 0xff;//红色通道 red
				sum += Math.pow(tr - means, 2);
			}
		}
		double len = width * height;
		sd = Math.sqrt(sum / len);
		System.out.println("sd:" + sd);
		
		//灰色图片二值化
		for (int row = 0; row < height; row++) {//列
			for (int col = 0; col < width; col++) {//行
				index = row * width + col;
				int pixel = pixels[index];//读像素
				int tr = (pixel >> 16) & 0xff;//红色通道 red
				if (tr > means) {
					tr = 255;
				} else {
					tr = 0;
				}
				pixels[index] = (255 << 24 | tr << 16 | tr << 8 | tr);
			}
		}
		
		setRGB(image, 0, 0, width, height, pixels);
		return image;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public double getMeans() {
		return means;
	}

	public void setMeans(double means) {
		this.means = means;
	}

	public double getSd() {
		return sd;
	}

	public void setSd(double sd) {
		this.sd = sd;
	}

}
