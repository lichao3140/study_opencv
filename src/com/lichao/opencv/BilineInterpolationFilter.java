package com.lichao.opencv;

import java.awt.image.BufferedImage;

/**
 * 双线性插值
 * @author LiChao
 *
 */
public class BilineInterpolationFilter extends AbstractImageOptionFilter{

	private float xscale;
	private float yscale;
	
	public BilineInterpolationFilter() {
		xscale = 1.25f;
		yscale = 1.25f;
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
		
		for(int row=0; row<nh; row++) {
			for(int col=0; col<nw; col++) {
				float oldRow = row / yscale;
				float oldcol = col / xscale;
				index = row*nw + col;
				output[index] = bilineInterpolation(oldRow, oldcol, pixels, width, height);
			}
		}
		
		setRGB(dest, 0, 0, nw, nh, output);
		return dest;
	}
	
	private int bilineInterpolation(float oldRow, float oldCol, int[] pixels, int width, int height) {
		int np = 0;
		int row = (int)Math.floor(oldRow);
		int col = (int)Math.floor(oldCol);
		int rowNext = row + 1;
		int colNext = col + 1;
		if(rowNext >= height) {
			rowNext = height-1;
		}
		if(colNext >= width) {
			colNext = width - 1;
		}
		
		float t = oldCol - col;//X方向
		float u = oldRow - row;//Y方向
		
		int index1 = row*width + col;
		int index2 = row*width + colNext;
		int index3 = rowNext*width + col;
		int index4 = rowNext*width + colNext;
		
		int tr1=0, tr2=0, tr3=0, tr4=0;
		int tg1=0, tg2=0, tg3=0, tg4=0;
		int tb1=0, tb2=0, tb3=0, tb4=0;
		
		// p1
		tr1 = (pixels[index1] >> 16)&0xff;
		tg1 = (pixels[index1] >> 8)&0xff;
		tb1 = pixels[index1]&0xff;
		
		// p2
		tr2 = (pixels[index2] >> 16)&0xff;
		tg2 = (pixels[index2] >> 8)&0xff;
		tb2 = pixels[index2]&0xff;
		
		// p3
		tr3 = (pixels[index3] >> 16)&0xff;
		tg3 = (pixels[index3] >> 8)&0xff;
		tb3 = pixels[index3]&0xff;
		
		// p4
		tr4 = (pixels[index4] >> 16)&0xff;
		tg4 = (pixels[index4] >> 8)&0xff;
		tb4 = pixels[index4]&0xff;
		
		float fr1 = tr1*(1-t) + tr2*t;
		float fg1 = tg1*(1-t) + tg2*t;
		float fb1 = tb1*(1-t) + tb2*t;
		
		float fr2 = tr3*(1-t) + tr4*t;
		float fg2 = tg3*(1-t) + tg4*t;
		float fb2 = tb3*(1-t) + tb4*t;
		
		float fr = fr1*(1-u)+fr2*u;
		float fg = fg1*(1-u)+fg2*u;
		float fb = fb1*(1-u)+fb2*u;		
		
		np = (0xff << 24 | clamp(fr) << 16 | clamp(fg) << 8 | clamp(fb)); 
		
		return np;
	}

	public int clamp(float v) {
		if(v > 255) {
			return 255;
		}
		return (int)v;
	}	
}
