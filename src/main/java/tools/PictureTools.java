package tools;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class PictureTools {

	public static BufferedImage makeImageTranslucent(BufferedImage source, double alpha) {
		BufferedImage target = new BufferedImage(source.getWidth(), source.getHeight(), 3);

		Graphics2D g = target.createGraphics();

		g.setComposite(AlphaComposite.getInstance(3, (float) alpha));

		g.drawImage(source, null, 0, 0);

		g.dispose();

		return target;
	}
}
