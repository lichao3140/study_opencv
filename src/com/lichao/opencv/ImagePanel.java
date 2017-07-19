package com.lichao.opencv;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 图片宽高像素读写
 * @author LiChao
 *
 */
public class ImagePanel extends JComponent implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JButton processBtn;
	private BufferedImage image;
	private BufferedImage resultImage;
	private int[] histdata;
	
	public ImagePanel(BufferedImage image) {
		this.image = image;
	}
	
	public JButton getButton() {
		processBtn = new JButton("process");
		processBtn.addActionListener(this);
		return processBtn;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		if (image != null) {
			//将图片显示出来，位置从起始位置开始，宽和高不变
			g2d.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		}
		if(resultImage != null) {
			g2d.drawImage(resultImage, image.getWidth() + 10, 0, resultImage.getWidth(), resultImage.getHeight(), null);
		}
		renderHistogram(g2d);
	}
	
	private void renderHistogram(Graphics2D g2d) {
		if (histdata != null) {
			int xstart = image.getWidth() + 50;
			int ystart = image.getHeight();

			g2d.setPaint(Color.BLACK);//直方图坐标绘制
			g2d.drawLine(xstart, 0, xstart, ystart);// Y Axis
			g2d.drawLine(xstart, ystart, xstart + 256, ystart); // X Axis

			// find max value
			int max = -1;
			int min = 0;
			for (int i = 0; i < histdata.length; i++) {
				max = Math.max(max, histdata[i]);
			}

			float delta = max - min;
			g2d.setPaint(Color.GREEN);
			for (int i = 0; i < histdata.length; i++) {
				float v1 = histdata[i] - min;
				int value = (int) ((v1 / delta) * 256);
				g2d.drawLine(xstart + i + 1, ystart, xstart + i + 1, ystart - value); // X
																						// Axis
			}

		}
	}

	public void process() {
		//灰度图片读取
//		PixelStatstics ps = new PixelStatstics();
//		ps.process(this.image);
		
		//图片像素运算
//		ImageMathFilter imf = new ImageMathFilter();
//		imf.setType(2);
//		imf.setValue(2);
//		imf.process(this.image);
		
		//美白与亮度提升	
//		WhiteImageFilter bif = new WhiteImageFilter();
//		bif.process(this.image);
		
		//灰度图像变彩色
//		File f = new File("F:\\opencv\\shejie.jpg");
//		try {
//			ColorFillFilter cff = new ColorFillFilter();
//			BufferedImage card = ImageIO.read(f);
//			cff.setColorCard(card);
//			cff.process(this.image);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		//RGB色彩空间
//		RGBColorFilter rgb = new RGBColorFilter();
//		rgb.setChannelIndex(3);
//		rgb.process(this.image);
		
		//亮度调节
//		ColorAdjustFilter caf = new ColorAdjustFilter();
//		caf.setMinmax(false);
//		caf.setMinValue(80);
//		caf.process(this.image);
		
		//调整图像对比度
//		ContrastAdjustFilter caf = new ContrastAdjustFilter();
//		caf.setContrast(2.8f);
//		caf.process(this.image);
		
		//直方图
//		HistogramFilter hf = new HistogramFilter();
//		hf.process(this.image);
//		histdata = hf.getHistData();
//		for (int i = 0; i < histdata.length; i++) {
//			System.out.println("gray value " + i + " :" + histdata[i]);
//		}
		
		//直方图  均衡化
//		HistgramBalanceFilter hbf = new HistgramBalanceFilter();
//		resultImage = hbf.process(this.image);
		
		//临近点插值
//		NearestInterpolationFilter ntf = new NearestInterpolationFilter();
//		resultImage = ntf.process(this.image);
		
		//双线性插值
//		BilineInterpolationFilter bif = new BilineInterpolationFilter();
//		resultImage = bif.process(this.image);
		
		//图像旋转
//		RotateImageFilter rif = new RotateImageFilter();
//		resultImage = rif.process(this.image);
		
		//图片翻转和平移
//		FlipTranslateFilter ftf = new FlipTranslateFilter();
//		resultImage = ftf.process(this.image);
		
		//图像灰度
//		GrayFilter gf = new GrayFilter();
//		resultImage = gf.process(this.image);
		
		//图像二值化
//		BinaryFilter bf = new BinaryFilter();
//		resultImage = bf.process(this.image);
		
		SinCityFilter scf = new SinCityFilter();
		resultImage = scf.process(this.image);
	}
	
	public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel dstCM) {
		if (dstCM == null)
			dstCM = src.getColorModel();
		return new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()),
				dstCM.isAlphaPremultiplied(), null);
	}
	
	public int[] getRGB (BufferedImage image, int x, int y, int width, int height, int[] pixels) {
		int type = image.getType();
		if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB)
			return (int[]) image.getRaster().getDataElements(x, y, width, height, pixels);
		return image.getRGB(x, y, width, height, pixels, 0, width);
	}
	
	public void setRGB (BufferedImage image, int x, int y, int width, int height, int[] pixels) {
		int type = image.getType();
		if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB)
			image.getRaster().setDataElements(x, y, width, height, pixels);
		else
			image.setRGB(x, y, width, height, pixels, 0, width);
	}

	public static void main(String[] args) {
		File file = new File("F:\\opencv\\gril.jpg");
		try {
			BufferedImage image = ImageIO.read(file);
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			ImagePanel imagePanel = new ImagePanel(image);
			frame.getContentPane().add(imagePanel, BorderLayout.CENTER);
			JPanel flowPanel = new JPanel();
			flowPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			flowPanel.add(imagePanel.getButton());
			frame.getContentPane().add(flowPanel, BorderLayout.SOUTH);
			frame.setSize(1000, 600);
			frame.setTitle("演示");
			frame.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == processBtn) {
			this.process();
			this.repaint();
		}
	}

}
