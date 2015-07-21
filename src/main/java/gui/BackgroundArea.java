package gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JTextArea;
import tools.PictureTools;

public class BackgroundArea extends JTextArea {
	private static final long serialVersionUID = 3839434695121001754L;
	protected BufferedImage bgImage;

	public BackgroundArea(BufferedImage image) {
		
		image = PictureTools.makeImageTranslucent(image, 0.075D);
		 
		this.bgImage = image;
		
		setOpaque(false);
		
	}

	public void paint(Graphics g) {
		
		g.drawImage(this.bgImage, 20, 0, this);
		super.paint(g);
	}

}