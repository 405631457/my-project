
import java.awt.Image;

import javax.swing.ImageIcon;

public class BackgroundImage {

	public int x = 0, y = 0;
	public int ox = 0, oy = 0;
	public java.net.URL imgURL = BackgroundImage.class.getResource("Resource/center.jpg");
	public Image img = new ImageIcon(imgURL).getImage();
}