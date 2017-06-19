package com.lichao.opencv;

import java.awt.BorderLayout;
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

/**
 * 图片宽高像素读写
 * @author LiChao
 *
 */
public class ImagePanel extends JComponent implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JButton processBtn;
	private BufferedImage image;
	
	public ImagePanel(BufferedImage image) {
		this.image = image;
	}
	
	public JButton initButton() {
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
		WhiteImageFilter bif = new WhiteImageFilter();
		bif.process(this.image);
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
			ImagePanel imagePanel = new ImagePanel(image);
			frame.getContentPane().add(imagePanel, BorderLayout.CENTER);
			frame.getContentPane().add(imagePanel.initButton(), BorderLayout.SOUTH);
			frame.setSize(600, 600);
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
