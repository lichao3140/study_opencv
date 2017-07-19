package com.lichao.opencv;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * 图片翻转和平移
 * @author LiChao
 *
 */
public class FlipTranslateFilter extends AbstractImageOptionFilter {

	public final static int H_FLIP = 1;
	public final static int V_FLIP = 2;
	public final static int TRANSLATE = 3;
	private int xoffset;
	private int yoffset;
	private int type;
	
	public FlipTranslateFilter() {
		type = V_FLIP;
		xoffset = 50;
		yoffset = 50;
	}

	public int getXoffset() {
		return xoffset;
	}

	public void setXoffset(int xoffset) {
		this.xoffset = xoffset;
	}

	public int getYoffset() {
		return yoffset;
	}

	public void setYoffset(int yoffset) {
		this.yoffset = yoffset;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	@Override
	public BufferedImage process(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels = new int[width * height];
		getRGB(image, 0, 0, width, height, pixels);
		int nw = width, nh = height;

		BufferedImage dest = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
		int[] output = new int[nw*nh];
		
		int index = 0, np = 0;
		int index2 = 0;
		for(int row=0; row<nh; row++) {
			for(int col=0; col<nw; col++) {
				index = row*nw + col;				
				if(this.type == H_FLIP) {
					index2 = row * width + (width - col -1);
					np = pixels[index2];
				}
				else if(this.type == V_FLIP) {
					index2 = (height-row-1) * width + col;
					np = pixels[index2];
				}
				else if(this.type == TRANSLATE) {
					int nc = col - xoffset;
					int nr = row - yoffset;
					if(nc <0 || nr < 0) {
						np = Color.GRAY.getRGB();
					}
					else {
						index2 = nr * width + nc;
						np = pixels[index2];
					}
				}
				output[index] = np;
			}
		}
		
		setRGB(dest, 0, 0, nw, nh, output);
		return dest;
	}

}
