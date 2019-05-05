
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Rule extends JFrame {
	StartPanel rulePanel;
	Image rulebg = null;

	public Rule() {
		rulePanel = new StartPanel();
		java.net.URL imgURL = Rule.class.getResource("Resource/Rule.jpeg");
		rulebg = Toolkit.getDefaultToolkit().getImage(imgURL);

		rulePanel.setImage(rulebg);
		rulePanel.setLayout(new java.awt.BorderLayout());
		// rulePanel.add(contentPanel, BorderLayout.CENTER);

		this.setTitle("遊戲規則");
		this.setLayout(new java.awt.BorderLayout());
		this.add(rulePanel, BorderLayout.CENTER);
		this.setMinimumSize(new java.awt.Dimension(400, 600));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);

		java.net.URL imgURL2 = Rule.class.getResource("Resource/Talker.gif");
		JLabel tImage = new JLabel(new ImageIcon(imgURL2)); // 创建一个带图片的 JLabel
		tImage.setBounds(0, 200, 400, 400); // 设置 图片的横坐标、纵坐标、宽、高
		rulePanel.add(tImage);
		talk();
	}

	public void talk() {
		JLabel talk = new JLabel();
		JLabel talk2 = new JLabel();
		talk.setFont(new java.awt.Font("Dialog", 1, 20));// 1是粗體
		talk.setForeground(Color.CYAN);
		talk.setBounds(80, 10, 500, 300);
		rulePanel.add(talk);
		talk2.setFont(new java.awt.Font("Dialog", 1, 15));// 1是粗體
		talk2.setForeground(Color.CYAN);
		talk2.setBounds(0, 0, 500, 300);
		rulePanel.add(talk2);
		String s[] = { "你好，", "尊貴的客人", "歡迎來到外星煩躁體會中心", "跟您介紹一下規則", "規則1:在限時60秒內取得最高分", "規則2:特別的怪獸有特別積分", "以上", "" };

		new Thread() {
			public void run() {
				for (int i = 0; i <= 7; i++) {
					talk.setText(s[i]);

					talk.validate();
					// talk.setBounds(0, 0, 10,20 );

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}.start();
	}
}
