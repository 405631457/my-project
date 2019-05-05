import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Scores extends JFrame {
	StartPanel scorePanel;
	Image scorebg = null;
	JLabel up = new JLabel();
	JLabel rank = new JLabel();
	JLabel content1 = new JLabel();
	JLabel content2 = new JLabel();

	public Scores() {
		Container c = getContentPane();
		c.setLayout(null);

		up.setLocation(90, 60);
		up.setText("Rank    Scores    Player");
		up.setSize(400, 30);
		up.setFont(new Font("Gotham", Font.BOLD, 20));
		up.setForeground(Color.YELLOW);
		c.add(up);

		rank.setLocation(110, 88);
		rank.setText(
				"<html><br>1<br><br>2<br><br>3<br><br>4<br><br>5<br><br>6<br><br>7<br><br>8<br><br>9<br><br>10<br>");
		rank.setSize(50, 500);
		rank.setFont(new Font("Gotham", Font.BOLD, 20));
		rank.setForeground(Color.white);
		c.add(rank);

		// content1.setPreferredSize(new Dimension(100,500));
		content1.setLocation(180, 63);
		content2.setLocation(260, 63);
		try {
			ScoresSql sql = new ScoresSql();
			content1.setText(sql.getAll() + "</html>");
			content2.setText(sql.getAll2() + "</html>");
		} catch (Exception e) {
			e.printStackTrace();
		}

		content1.setSize(100, 500);
		content2.setSize(100, 500);
		content1.setFont(new Font("Gotham", Font.BOLD, 20));
		content2.setFont(new Font("Gotham", Font.BOLD, 20));
		content1.setForeground(Color.white);
		content2.setForeground(Color.white);
		c.add(content1);
		c.add(content2);

		scorePanel = new StartPanel();
		java.net.URL imgURL = Scores.class.getResource("Resource/Score.jpeg");
		scorebg = Toolkit.getDefaultToolkit().getImage(imgURL);
		scorePanel.setImage(scorebg);
		// scorePanel.setLayout(null);
		scorePanel.setLayout(new java.awt.BorderLayout());
		// rulePanel.add(contentPanel, BorderLayout.CENTER);

		this.setTitle("排行榜");
		this.setLayout(new java.awt.BorderLayout());
		this.add(scorePanel, BorderLayout.CENTER);
		this.setMinimumSize(new java.awt.Dimension(400, 600));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
}
