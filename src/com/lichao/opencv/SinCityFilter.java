package com.lichao.opencv;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * 邪恶之城--灰度美白效果
 * @author LiChao
 *
 */
public class SinCityFilter extends AbstractImageOptionFilter {

	private Color referColor;
	private int threshold;
	
	public SinCityFilter() {
		referColor = new Color(120,99, 213);
		threshold = 300;
	}

	public Color getReferColor() {
		return referColor;
	}

	public void setReferColor(Color referColor) {
		this.referColor = referColor;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
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
		int r=0,g=0,b=0,gray=0;
		int gr=0,gg=0,gb=0;
		for(int row=0; row<height; row++) {
			for(int col=0; col<width; col++) {
				index = row*width + col;
				r = (pixels[index]>>16)&0xff;
				g = (pixels[index]>>8)&0xff;
				b = pixels[index]&0xff;
				double distance = calculateDis(r, g, b);
				if(distance >= threshold) {
					output[index] = (0xff << 24) | ((r&0xff) << 16) | ((g&0xff) << 8) | (b&0xff);
				}
				else
				{
					gray = (int)(0.299 * r + 0.587 * g+ 0.114 * b);
					double rate = distance / threshold;
					gr = (int)(r*rate + (1-rate)*gray);
					gg = (int)(g*rate + (1-rate)*gray);
					gb = (int)(b*rate + (1-rate)*gray);
					output[index] = (0xff << 24 | clamp(gr) << 16 | clamp(gg) << 8 | clamp(gb)); 
				}
				
			}
		}
		
		setRGB(dest, 0, 0, width, height, output);
		return dest;
	}

	private double calculateDis(int r, int g, int b) {
		int dr = r - referColor.getRed();
		int dg = g - referColor.getGreen();
		int db = b - referColor.getBlue();
		double distance = Math.sqrt(dr*dr + dg*dg + db*db);
		return distance;
	}	
}
