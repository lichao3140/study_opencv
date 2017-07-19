package com.lichao.opencv;

import java.awt.image.BufferedImage;

/**
 * 图像灰度
 * @author LiChao
 *
 */
public class GrayFilter extends AbstractImageOptionFilter {

	public final static int MEANS_RGB = 1;
	public final static int MIN_MAX_RGB = 2;
	public final static int WEIGHT_RGB = 3;
	public final static double MEANS_WEIGHT = 1.0 / 3.0;

	private int method;
	public GrayFilter() {
		method = MEANS_RGB;
	}
	
	public int getMethod() {
		return method;
	}

	public void setMethod(int method) {
		this.method = method;
	}
	
	@Override
	public BufferedImage process(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels = new int[width * height];
		getRGB(image, 0, 0, width, height, pixels);
		
		BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int[] output = new int[width*height];
		
		int index = 0;
		int r=0,g=0,b=0, gray=0;
		for(int row=0; row<height; row++) {
			for(int col=0; col<width; col++) {
				index = row*width + col;
				r = (pixels[index]>>16)&0xff;
				g = (pixels[index]>>8)&0xff;
				b = pixels[index]&0xff;
				if(method == MEANS_RGB) {
					gray = (int)((r+g+b) * MEANS_WEIGHT);
				}
				else if(method == MIN_MAX_RGB) {
					int max = Math.max(Math.max(r, g), b);
					int min = Math.min(Math.min(r, g), b);
					gray = (int)((max + min) / 2.0);
				}
				else if(method == MIN_MAX_RGB) {
					gray = (int)(0.299 * r + 0.587 * g+ 0.114 * b);
				}
				output[index] = (0xff << 24) | ((gray&0xff) << 16) | ((gray&0xff) << 8) | (gray&0xff);
			}
		}
		
		setRGB(dest, 0, 0, width, height, output);
		return dest;
	}

}
