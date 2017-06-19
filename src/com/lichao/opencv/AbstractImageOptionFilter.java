package com.lichao.opencv;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public abstract class AbstractImageOptionFilter {

	public abstract BufferedImage process(BufferedImage image);
	
	public static BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel dstCM) {
		if (dstCM == null)
			dstCM = src.getColorModel();
		return new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()),
				dstCM.isAlphaPremultiplied(), null);
	}
	
	public static int[] getRGB (BufferedImage image, int x, int y, int width, int height, int[] pixels) {
		int type = image.getType();
		if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB)
			return (int[]) image.getRaster().getDataElements(x, y, width, height, pixels);
		return image.getRGB(x, y, width, height, pixels, 0, width);
	}
	
	public static void setRGB (BufferedImage image, int x, int y, int width, int height, int[] pixels) {
		int type = image.getType();
		if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB)
			image.getRaster().setDataElements(x, y, width, height, pixels);
		else
			image.setRGB(x, y, width, height, pixels, 0, width);
	}
}
