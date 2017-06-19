package com.lichao.opencv;

import java.awt.image.BufferedImage;

/**
 * 像素的运算
 * @author LiChao
 *
 */
public class ImageMathFilter extends AbstractImageOptionFilter{

	private int type;
	private int value;
	
	public ImageMathFilter() {
		this.type = 0;
		this.value = 20;
	}

	@Override
	public BufferedImage process(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels = new int[width * height];
		getRGB(image, 0, 0, width, height, pixels);
		int index = 0;
		for (int row = 0; row < height; row++) {//列
			for (int col = 0; col < width; col++) {//行
				index = row * width + col;
				int pixel = pixels[index];//读像素
				int ta = (pixel >> 24) & 0xff;//透明通道 Alpha
				int tr = (pixel >> 16) & 0xff;//红色通道 red
				int tg = (pixel >> 8) & 0xff;//绿色通道 green
				int tb = (pixel & 0xff);//蓝色通道 blue
				
				tr = imageMath(tr);
				tg = imageMath(tg);
				tb = imageMath(tb);
				
				pixels[index] = (ta << 24 | tr << 16 | tg << 8 | tb);
			}
		}
		setRGB(image, 0, 0, width, height, pixels);
		return image;
	}

	/**
	 * 像素运算处理
	 * @param tr
	 * @return
	 */
	private int imageMath(int tr) {
		int result = 0;
		double scale = (255 / Math.log(255));
		if (type == 0) {
			result = tr + value;
		} else if (type == 1) {
			result = tr - value;
		} else if (type == 2) {
			result = tr * value;
		} else if (type == 3) {
			result = tr / value;
		} else if (type == 4) {
			result = (int) (Math.log(tr) * scale);
		} else if (type == 5) {
			result = (int) Math.sqrt(tr);
		} else if (type == 6) {
			result = (tr * tr) / 255;
		}
		return (result < 0 ? 0 : (result > 255 ? 255 : result));
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
