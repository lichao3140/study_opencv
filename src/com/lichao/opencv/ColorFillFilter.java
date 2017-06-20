package com.lichao.opencv;

import java.awt.image.BufferedImage;

/**
 * 灰度图像变成彩色图像
 * @author LiChao
 *
 */
public class ColorFillFilter extends AbstractImageOptionFilter{
	
	private BufferedImage colorCard;
	private int[] lut;

	@Override
	public BufferedImage process(BufferedImage image) {
		setupLookupTable();
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels = new int[width*height];
		getRGB(image, 0, 0, width, height, pixels);
		int index = 0;
		for(int row=0; row<height; row++) {
			for(int col=0; col<width; col++) {
				index = row * width + col;
				int pixel = pixels[index];
				int tr = (pixel >> 16) & 0xff;
				pixels[index] = lut[tr];
			}
		}
		setRGB(image, 0, 0, width, height, pixels);
		return image;
	}

	private void setupLookupTable() {
		int width = colorCard.getWidth();
		int height = colorCard.getHeight();
		int[] pixels = new int[width * height];
		getRGB(colorCard, 0, 0, width, height, pixels);
		int index = 0;
		lut = new int[256];
		int row = height / 2;
		for (int col = 2; col < 258; col++) {//行
			index = row * width + col;
			int pixel = pixels[index];//读像素
			lut[col-2] = pixel;
		}
	}

	public BufferedImage getColorCard() {
		return colorCard;
	}


	public void setColorCard(BufferedImage colorCard) {
		this.colorCard = colorCard;
	}

}
