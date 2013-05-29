package ui;

import java.awt.Graphics;
import java.awt.Image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Paints a sheep on a Panel
 * 
 * @author Matts Devriendt      <matts.devriendt@gmail.com>
 */
public class ImagePanel extends JPanel {
	
	private Random r = new Random();
	private JLabel lblImage;
	
	public ImagePanel(String fileName) {
		this.init(fileName);
	}
	
	private void init(String fileName) {
		InputStream stream = this.getClass().getResourceAsStream(fileName);
		
		Image icon;
		try {
			icon = ImageIO.read(stream);
			
			lblImage = new JLabel(new ImageIcon(icon));
			
			this.add(lblImage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}