package src.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.JPanel;

import src.entity.Player;

public class GamePanel extends JPanel implements Runnable{
    //size of og tiles
    final int originalTileSize = 16; 

    //scaling to fit monitors
    final int scale = 3; 

    //scale variable - 48/48 pixel
    public final int tileSize = originalTileSize * scale; 

    //size of screen
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;

    //768 pixels
    final int screenWidth = tileSize * maxScreenCol; 

    //576 pixels
    final int screenHeight = tileSize * maxScreenRow; 

    //fps
    int fps = 60;
    
    //decs
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this,keyH);

    //player def pos
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/fps; //0.016666 sec
        double delta = 0;
        double lastTime = System.nanoTime();
        long nowTime;

        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {

            nowTime = System.nanoTime();

            delta += (nowTime - lastTime) / drawInterval;
            timer += (nowTime - lastTime);
            lastTime = nowTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;

            }

            if(timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
            
        }
    }

    public void update() {
        player.update();
    }

    public void paintComponent(Graphics g) {
        Toolkit.getDefaultToolkit().sync();
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        player.draw(g2);

        g2.dispose();
    }
}
