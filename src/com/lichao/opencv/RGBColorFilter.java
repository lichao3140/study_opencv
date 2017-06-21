package com.lichao.opencv;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * RGB单通道和透明通道获取
 * @author LiChao
 *
 */
public class RGBColorFilter extends AbstractImageOptionFilter{

	private int channelIndex;//1-red;2-green;3-blue
	private double threshold;//半径
	
	public RGBColorFilter() {
		channelIndex = 1;
		threshold = 100;
	}
	
	@Override
	public BufferedImage process(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels = new int[width * height];
		getRGB(image, 0, 0, width, height, pixels);
		int index = 0;
		int cx = width / 2;
		int cy = height / 2;
		BufferedImage mask = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		for (int row = 0; row < height; row++) {//列
			for (int col = 0; col < width; col++) {//行
				index = row * width + col;
				int pixel = pixels[index];//读像素
				int ta = (pixel >> 24) & 0xff;//透明通道 Alpha
				int tr = (pixel >> 16) & 0xff;//红色通道 red
				int tg = (pixel >> 8) & 0xff;//绿色通道 green
				int tb = (pixel & 0xff);//蓝色通道 blue
				
				//分通道显示
				if (channelIndex == 1) {
					tg = tr;
					tb = tr;
				} else if (channelIndex == 2) {
					tr = tg;
					tb = tg;
				} else if (channelIndex == 3) {
					tr = tb;
					tg = tb;
				}
				
				//alpha通道
				if(distance(cx, cy, col, row) > threshold) {
					ta = ta / 2;
					int mp = (ta << 24 | 127 << 16 | 127 << 8 | 127);
					mask.setRGB(col, row, mp);
				} else {
					ta = 0;
					int mp = (ta << 24 | 127 << 16 | 127 << 8 | 127);
					mask.setRGB(col, row, mp);
				}
				
				//pixels[index] = (ta << 24 | tr << 16 | tg << 8 | tb);
			}
		}
		Graphics2D g2d = image.createGraphics();
		g2d.drawImage(mask, 0, 0, width, height, null);
		//setRGB(image, 0, 0, width, height, pixels);
		return image;
	}
	
	/**
	 * 两点之间直线距离
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public double distance(int x1, int y1, int x2, int y2) {
		double dis = Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2);
		return Math.sqrt(dis);
	}

	public int getChannelIndex() {
		return channelIndex;
	}

	public void setChannelIndex(int channelIndex) {
		this.channelIndex = channelIndex;
	}

}
