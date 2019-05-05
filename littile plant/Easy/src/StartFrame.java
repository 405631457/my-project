
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

/*
 * 選單視窗
 */
public class StartFrame extends JFrame implements ActionListener {
	Frame frame;
	StartPanel bgPanel = null;
	JPanel contentPanel = null;
	JLabel label = null;
	Image image = null;
	private JButton Start;
	private JButton Scores;
	private JButton Rule;
	private JButton Exit;
	boolean close = false;

	String filename1;
	MP3 start_mp3;

	public StartFrame() {
		super();
		// 1.生成元件
		bgPanel = new StartPanel();
		// 請參考java swing
		// 獲取默認工具包
		java.net.URL imgURL = StartFrame.class.getResource("Resource/Start.jpg");
		image = Toolkit.getDefaultToolkit().getImage(imgURL);
		contentPanel = new JPanel();
		java.net.URL imgURL1 = StartFrame.class.getResource("Resource/button.png");
		ImageIcon icon = new ImageIcon(imgURL1);
		//ImageIcon icon = new ImageIcon("src/Resource/button.png");
		icon.setImage(icon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
		
		java.net.URL imgURL2 = StartFrame.class.getResource("Resource/button2.png");
		ImageIcon icon2 = new ImageIcon(imgURL2);
		//ImageIcon icon2 = new ImageIcon("src/Resource/button2.png");
		icon2.setImage(icon2.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
		
		java.net.URL imgURL3 = StartFrame.class.getResource("Resource/button3.png");
		ImageIcon icon3 = new ImageIcon(imgURL3);
		//ImageIcon icon3 = new ImageIcon("src/Resource/button3.png");
		icon3.setImage(icon3.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
		
		java.net.URL imgURL4 = StartFrame.class.getResource("Resource/button4.png");
		ImageIcon icon4 = new ImageIcon(imgURL4);
		//ImageIcon icon4 = new ImageIcon("src/Resource/button4.png");
		icon4.setImage(icon4.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));

		java.net.URL imgURL5 = StartFrame.class.getResource("Resource/title.gif");
		JLabel tImage = new JLabel(new ImageIcon(imgURL5)); // 创建一个带图片的 JLabel
		tImage.setBounds(100, 0, 600, 500); // 设置 图片的横坐标、纵坐标、宽、高
		contentPanel.add(tImage);

		// 2.設置要顯示之資訊與元件
		Start = new JButton("Start", icon);
		Start.setBounds(775, 60, 100, 100);// start botton 位置大小
		Start.setOpaque(false);
		Start.setVerticalTextPosition(JButton.CENTER);
		Start.setHorizontalTextPosition(JButton.CENTER);
		Start.setFont(new Font("Gotham", Font.BOLD, 18));
		Start.setForeground(Color.black);
		Start.setContentAreaFilled(false);
		// Start.setSize(Start.getPreferredSize());
		contentPanel.add(Start);
		Start.addActionListener(this);
		Start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StartFrame.this.dispose();
			}
		});

		Scores = new JButton("Scores", icon2);
		Scores.setBounds(775, 180, 100, 100);
		Scores.setOpaque(false);
		Scores.setVerticalTextPosition(JButton.CENTER);
		Scores.setHorizontalTextPosition(JButton.CENTER);
		Scores.setFont(new Font("Gotham", Font.BOLD, 18));
		Scores.setForeground(Color.white);
		Scores.setContentAreaFilled(false);
		// Start.setSize(Start.getPreferredSize());
		contentPanel.add(Scores);
		Scores.addActionListener(this);

		Rule = new JButton("Rule", icon3);
		Rule.setBounds(775, 300, 100, 100);
		Rule.setOpaque(false);
		Rule.setVerticalTextPosition(JButton.CENTER);
		Rule.setHorizontalTextPosition(JButton.CENTER);
		Rule.setFont(new Font("Gotham", Font.BOLD, 18));
		Rule.setForeground(Color.white);
		Rule.setContentAreaFilled(false);
		// Start.setSize(Start.getPreferredSize());
		contentPanel.add(Rule);
		Rule.addActionListener(this);

		Exit = new JButton("Exit", icon4);
		Exit.setBounds(775, 420, 100, 100);
		Exit.setOpaque(false);
		Exit.setVerticalTextPosition(JButton.CENTER);
		Exit.setHorizontalTextPosition(JButton.CENTER);
		Exit.setFont(new Font("Gotham", Font.BOLD, 18));
		Exit.setForeground(Color.BLACK);
		Exit.setContentAreaFilled(false);
		// Start.setSize(Start.getPreferredSize());
		contentPanel.add(Exit);
		Exit.addActionListener(this);
		// 不要borderlayout
		contentPanel.setLayout(null);
		// 透明化
		contentPanel.setOpaque(false);
		// 3.設置背景圖panel之設定與加入contentPanel
		bgPanel.setImage(image);
		bgPanel.setLayout(new java.awt.BorderLayout());
		bgPanel.add(contentPanel, BorderLayout.CENTER);
		// 4.設置frame之基本設定
		this.setTitle("Little Planet");
		this.setLayout(new java.awt.BorderLayout());
		this.add(bgPanel, BorderLayout.CENTER);
		this.setMinimumSize(new java.awt.Dimension(1000, 600));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

		Thread gameAudio = new Thread() {
			public void run() {
				InputStream in = StartFrame.class.getClassLoader().getResourceAsStream("Resource/startms.mp3");
				//filename1 = "src/Resource/startms.mp3";
				//start_mp3 = new MP3(filename1);
				start_mp3 = new MP3(in);
				start_mp3.setLoop(true);
				start_mp3.play();
			}
		};
		gameAudio.start();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == Start) {
			start_mp3.stop();
			try {
				new GameFrame();
				Thread.sleep(500);
				// game_mp3.play();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
		if (e.getSource() == Scores) {
			new Scores();
		}
		if (e.getSource() == Rule) {
			new Rule();
		}
		if (e.getSource() == Exit) {
			start_mp3.stop();
			dispose();
		}
	}
}