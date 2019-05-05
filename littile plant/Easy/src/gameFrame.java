import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.io.InputStream;
import java.util.EventListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.glass.events.WindowEvent;

public class GameFrame implements MouseListener, MouseMotionListener {
	private static int x = 1000, y = 600;
	private static int x2[] = { 0, 0, 0 };
	private static int y2[] = { 0, 0, 0 };
	private static int speedx[] = new int[3];
	private static int speedy[] = new int[3];
	private static int mx, my;
	private static int score = 0, count = 0;
	private boolean hit;
	private Timer t = new Timer();

	InputStream music = GameFrame.class.getClassLoader().getResourceAsStream("Resource/gamems.mp3");
	InputStream music2 = GameFrame.class.getClassLoader().getResourceAsStream("Resource/bo.mp3");
	InputStream music3 = GameFrame.class.getClassLoader().getResourceAsStream("Resource/monster.mp3");
	InputStream music4 = GameFrame.class.getClassLoader().getResourceAsStream("Resource/monster2.mp3");
	
	String filename2, filename3, filename4, filename5;
	static MP3 press_mp3, hit_mp3, hit2_mp3, games_mp3;
	
	
	
	JLabel e[] = new JLabel[10];
	JLabel wordLabel = null, bgLabel = null;
	JPanel contentPanel = null, imagePanel = null;
	ImageIcon background = null;
	JFrame game = new JFrame("game");

	public GameFrame() {
		super();
		score = 0;
		gameAudio2.start();
		gameAudio3.start();
		gameAudio4.start();
		gameAudio5.start();
		
		//filename2 = "src/Resource/gamems.mp3";
		game.setTitle("Little Planet");
		game.setLayout(new java.awt.BorderLayout());
		// game.add(enemy, BorderLayout.CENTER);
		game.setMinimumSize(new java.awt.Dimension(1000, 600));
		game.setResizable(false);
		game.setLocationRelativeTo(null);
		game.addMouseListener(this);
		game.addMouseMotionListener(this);

		// 3.於JFrame中設置背景圖片 - 圖片無法縮放大小
		java.net.URL imgURL = GameFrame.class.getResource("Resource/center.jpg");
		background = new ImageIcon(imgURL); // 背景圖片
		bgLabel = new JLabel(background); // 把背景圖顯示在Label中
		bgLabel.setBounds(0, 0, background.getIconWidth(), background.getIconHeight()); // 把含有背景圖之Label位置設置為圖片剛好填充整個版面

		// 把内容視窗轉為JPanel，否則不能使用setOpaque()來使視窗變成透明
		imagePanel = (JPanel) game.getContentPane();
		imagePanel.setOpaque(false);
		game.getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE)); // 把背景圖添加到分層窗格的最底層以作為背景

		game.setResizable(false);
		game.setLocationRelativeTo(null);
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setVisible(true);
		// 加入分數
		games_mp3.play();
		printScore();
		java.net.URL imgURL2 = GameFrame.class.getResource("Resource/enemy.png");
		ImageIcon img = new ImageIcon(imgURL2);
		e[0] = new JLabel(img);

		game.getLayeredPane().add(e[0], new Integer(Integer.MIN_VALUE) + 1);
		//img = new ImageIcon("src/Resource/enemy.png");

		e[0].addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				// 处理鼠标点击
			}

			public void mouseEntered(MouseEvent e) {
				// 处理鼠标移入
			}

			public void mouseExited(MouseEvent e) {
				// 处理鼠标离开
			}

			public void mousePressed(MouseEvent ef) {
				score += 20;
				press_mp3.play();
				hit_mp3.play();
				e[0].setBounds(1000, 600, 100, 125);
			}

			public void mouseReleased(MouseEvent e) {
				// 处理鼠标释放
			}
		});

		e[1] = new JLabel(img);
		game.getLayeredPane().add(e[1], new Integer(Integer.MIN_VALUE) + 1);
		e[1].addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				// 处理鼠标点击
			}

			public void mouseEntered(MouseEvent e) {
				// 处理鼠标移入
			}

			public void mouseExited(MouseEvent e) {
				// 处理鼠标离开
			}

			public void mousePressed(MouseEvent ef) {
				score += 20;
				press_mp3.play();
				hit_mp3.play();
				e[1].setBounds(1000, 600, 100, 125);
			}

			public void mouseReleased(MouseEvent e) {
				// 处理鼠标释放
			}
		});
		java.net.URL imgURL3 = GameFrame.class.getResource("Resource/enemy2.png");
		img = new ImageIcon(imgURL3);
		e[2] = new JLabel(img);
		game.getLayeredPane().add(e[2], new Integer(Integer.MIN_VALUE) + 1);
		e[2].addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				// 处理鼠标点击
			}

			public void mouseEntered(MouseEvent e) {
				// 处理鼠标移入
			}

			public void mouseExited(MouseEvent e) {
				// 处理鼠标离开
			}

			public void mousePressed(MouseEvent ef) {
				score += 50;
				press_mp3.play();
				hit2_mp3.play();
				e[2].setBounds(1000, 600, 100, 125);
			}

			public void mouseReleased(MouseEvent e) {
				// 处理鼠标释放
			}
		});

		enemys();
		enemys1();
		enemys2();

		game.pack();
		game.setVisible(true);
		t.schedule(tt, 0, 1000);

		game.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	}

	public void enemys() {

		enemyrun(0);

	}

	public void enemys1() {

		enemyrun(1);

	}

	public void enemys2() {

		enemyrun(2);

	}

	public void printScore() {
		JLabel scorePrint = new JLabel();

		game.getLayeredPane().add(scorePrint, new Integer(Integer.MIN_VALUE) + 1);
		new Thread() {
			public void run() {
				while (true) {

					scorePrint.setText(score + "");
					scorePrint.setFont(new java.awt.Font("Dialog", 1, 20));// 1是粗體
					scorePrint.setForeground(Color.CYAN);
					scorePrint.setBounds(0, 0, 200, 20);
					scorePrint.validate();
					// System.out.println("Score");
				}
			}
		}.start();
	}

	public void enemyrun(int n) {
		new Thread() {
			public void run() {

				while (x >= 0) {
					int scores = 0;
					if (n == 0) {
						speedx[n] += (int) (Math.random() * 10);
						speedy[n] += (int) (Math.random() * 10);

					} else if (n == 1) {
						speedx[n] += (int) (Math.random() * 11);
						speedy[n] += (int) (Math.random() * 11);

					} else if (n == 2) {
						speedx[n] += (int) (Math.random() * 15);
						speedy[n] += (int) (Math.random() * 15);
					}
					x2[n] = x - speedx[n];
					y2[n] = y - speedy[n];
					e[n].setBounds(x2[n], y2[n], 100, 125);

					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// game.setLayer(e[n], new Integer(Integer.MIN_VALUE)+1));

					if (x - speedx[n] <= 0) {
						x = 1000;
						speedx[n] = 0;
					}
					if (y - speedy[n] <= 0) {
						y = 600;
						speedy[n] = 0;
					}

				}

			}
		}.start();
	}

	public int getX() {
		return x;
	}

	public static void setX(int x) {
		GameFrame.x = x;
	}

	public int getY() {
		return y;
	}

	Thread gameAudio2 = new Thread() {
		public void run() {
			games_mp3 = new MP3(music);
			games_mp3.setLoop(false);

		}
	};

	Thread gameAudio3 = new Thread() {
		public void run() {
			
			//filename3 = "src/Resource/bo.mp3";
			press_mp3 = new MP3(music2);
			press_mp3.setLoop(false);
			//press_mp3.stop();
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

		}
	};
	Thread gameAudio4 = new Thread() {
		public void run() {
			
			//filename4 = "src/Resource/monster.mp3";
			hit_mp3 = new MP3(music3);
			hit_mp3.setLoop(false);

		}
	};
	Thread gameAudio5 = new Thread() {
		public void run() {
			
			//filename5 = "src/Resource/monster2.mp3";
			hit2_mp3 = new MP3(music4);
			hit2_mp3.setLoop(false);

		}
	};

	TimerTask tt = new TimerTask() {
		JLabel jl = new JLabel();

		public void run() {
			game.getLayeredPane().add(jl, new Integer(Integer.MIN_VALUE) + 1);
			count = 30;
			while (count >= 0) {
				jl.setText(String.valueOf(count) + "s");
				jl.setFont(new Font("", Font.BOLD, 50));
				jl.setBounds(400, 0, 200, 50);
				jl.setForeground(Color.magenta);
				count--;

				if (count == 0) {

					try {
						new Gameover();
						game.dispose();
						Thread.sleep(500);

						t.cancel();
					} catch (Exception e1) {
						e1.printStackTrace();
					}

				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	};

	public static void setX2(int n, int x2) {
		GameFrame.x2[n] = x2;
	}

	public static void setY2(int n, int y2) {
		GameFrame.y2[n] = y2;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO 自動產生的方法 Stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {

		// TODO 自動產生的方法 Stub

		// System.out.printf("getx=%d,gety=%d%n",e.getX(),e.getY());
		//
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO 自動產生的方法 Stub
		// mx=e.getX();
		// my=e.getY();
		// hit(mx,my);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		hit = true;
		score -= 5;
		press_mp3.play();
		games_mp3.stop();

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自動產生的方法 Stub
		hit = false;

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自動產生的方法 Stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自動產生的方法 Stub

	}

	public static int getScore() {
		return score;
	}

	public static void setScore(int score) {
		GameFrame.score = score;
	}

}
