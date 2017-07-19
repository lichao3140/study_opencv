package com.lichao.opencv;

import java.awt.image.BufferedImage;

/**
 * 临近点插值(不抗锯齿)
 * @author LiChao
 *
 */
public class NearestInterpolationFilter extends AbstractImageOptionFilter{

	private float xscale;
	private float yscale;
	
	public NearestInterpolationFilter() {
		xscale = 0.75f;//x放大多少倍
		yscale = 0.75f;//y放大多少倍
	}
	
	public float getXscale() {
		return xscale;
	}


	public void setXscale(float xscale) {
		this.xscale = xscale;
	}


	public float getYscale() {
		return yscale;
	}


	public void setYscale(float yscale) {
		this.yscale = yscale;
	}


	@Override
	public BufferedImage process(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels = new int[width * height];
		getRGB(image, 0, 0, width, height, pixels);
		
		int nw = (int)(width* xscale);
		int nh = (int)(height* yscale);
		BufferedImage dest = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
		int[] output = new int[nw*nh];

		int index = 0;
		int index2 = 0;
		for(int row=0; row<nh; row++) {
			for(int col=0; col<nw; col++) {
				float oldRow = row / yscale;
				float oldcol = col / xscale;
				
				int oldRow2 = (int)Math.floor(oldRow + 0.5);
				int oldcol2 = (int)Math.floor(oldcol + 0.5);
				
				if(oldRow2 >= height) {
					oldRow2 = height - 1;
				}
				if(oldcol2 >= width) {
					oldcol2 = width - 1;
				}
				index = row*nw + col;
				index2 = oldRow2 * width + oldcol2;
				output[index] = pixels[index2];
			}
		}
		
		setRGB(dest, 0, 0, nw, nh, output);
		return dest;
	}

}
