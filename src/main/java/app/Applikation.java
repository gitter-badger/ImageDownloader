package app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import gui.GUI;
public class Applikation {

	public static void main(String[] args) throws MalformedURLException, IOException {
		//new GameDing(ImageIO.read(Applikation.class.getResource("/bg.png")));
		new GUI();

	}

}
class GameDing extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GameDing(BufferedImage image) {
		this.setSize(1000, 1000);
		this.setVisible(true);
		JLabel label = new JLabel();  
		label.setIcon(new ImageIcon(image));
		this.getContentPane().add(label);
	}
	
}