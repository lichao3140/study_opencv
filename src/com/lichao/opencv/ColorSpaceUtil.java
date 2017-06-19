package com.lichao.opencv;

/**
 * RGB和YCbCr转换工具
 * @author LiChao
 *
 */
public class ColorSpaceUtil {

	/**
	 * RGB转YCbCr
	 * @param tr
	 * @param tg
	 * @param tb
	 * @return
	 */
	public static double[] rgb2YCbCr(int tr, int tg, int tb) {
		double y = 0.183*tr + 0.614*tg + 0.062*tb;
		double Cb = -0.101*tr - 0.338*tg + 0.439*tb + 128;
		double Cr = 0.439*tr - 0.399*tg + 0.040*tb + 128;
		
		return new double[]{y, Cb, Cr};
	}
	
	/**
	 * YCbCr转RGB
	 * @param Y
	 * @param Cb
	 * @param Cr
	 * @return
	 */
	public static int[] YCbCr2rgb(double Y, double Cb, double Cr) {
		int tr = (int)(1.164*(Y - 16) + 1.793*(Cr-128));
		int tg = (int)(1.164*(Y - 16) - 0.534*(Cr-128) - 0.213*(Cb-128));
		int tb = (int)(1.164*(Y - 16) + 2.115*(Cb-128));
		
		return new int[]{clamp(tr), clamp(tg), clamp(tb)};
	}

	/**
	 * 确保RGB值在0~255之间
	 * @param value
	 * @return
	 */
	public static int clamp(int value) {
		return value < 0 ? 0 : (value > 255 ?  255 : value);
	}
}
