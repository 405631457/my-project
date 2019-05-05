
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

public class StartPanel extends JPanel {
	private Image image = null;

	public StartPanel() {
		super();
		this.setOpaque(false);// 設定圖片透明
	}

	public Image getImage() { // 取得image元件
		return image;
	}

	public void setImage(Image image) { // 設置圖片來源
		this.image = image;
	}

	/*
	 * 繪製背景圖
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null) {
			g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
		}
	}
}