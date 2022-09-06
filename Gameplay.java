import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import javax.swing.Timer;

import java.util.Random;

import javax.swing.JOptionPane;

// There are 2 major functions in the following class.
// Function paint() is for displaying the game contents. The "Whats".
// Function actionPerformed() contains the actual working of the game. The "Hows".

public class Gameplay extends JPanel implements KeyListener, ActionListener
{
	private boolean play = false;
	private int score;
	
	private int totalBricks;

	private Timer timer;
	private int delay = 8;

	Random rand = new Random();
	private int ballposX;
	private int ballposY;
	private int ballXdir;
	private int ballYdir;

	private int ptBall;
	private int fireBall;
	
	private int playerX; 
	private int playerY; 

	private int BarW = 100;
	private int BarH = 8;
	private int BarWdefault = 100;

	private int dirBar = 0;
	
	private int gunBarEnabled[] = new int[6];

	private int gX;
	private int gX2;
	private int gY[] = new int[3];

	private int menuEnable = 1;
	private int wait = 0;

	private MapGenerator map;
	private Database db;
	String username;
	private int lose;

	int level;

	CirRectOverlapCheck g;
	
	public Gameplay() // Initially:
	{		
		lose = 1; // We are at the losing position.
		totalBricks = 1; // Only for the start.
		db = new Database();
		g = new CirRectOverlapCheck();
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
		timer.start();
	}	
	
	public void paint(Graphics g)
	{    
		// Background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);

		if(wait == 1) // When the waiting screen displays we print the following and not the rest.
		{
			g.setColor(Color.yellow);
			g.fillRect(0, 0, 3, 592);
			g.fillRect(0, 0, 692, 3);
			g.fillRect(691, 0, 3, 592);
			g.fillRect(0, 565, 691, 3);

			g.setColor(Color.white);
			g.setFont(new Font("Peace Sans",Font.BOLD, 50));
			g.drawString("...Loading...", 200,280);
		}
		
		if(wait == 0) // When the waiting screen doesn't display we print the following and not the rest.
		{
			if(menuEnable == 1) // When menu displays and not the loading screen (from outer if) then we print the following and not the rest.
			{
				play = false;
				ballXdir = 0;
				ballYdir = 0;

				g.setColor(Color.BLUE);
				g.fillRect(0, 0, 3, 592);
				g.fillRect(0, 0, 692, 3);
				g.fillRect(691, 0, 3, 592);
				g.fillRect(0, 565, 691, 3);

				g.setColor(Color.white);
				g.setFont(new Font("Peace Sans",Font.BOLD, 50));
				g.drawString("BRICK-BREAKER GAME", 50,100);
				
				g.setColor(Color.BLUE);
				g.setFont(new Font("serif",Font.BOLD, 40));           
				g.drawString("2D Featureful GUI-Based", 170,150);

				g.setColor(Color.red);
				g.setFont(new Font("serif",Font.BOLD, 20));           
				g.drawString("Press  'Enter'  to  Start.", 250,480);
			}

			if(menuEnable == 0) // When menu as well as the loading screen (from outer if) doesn't display and then we print the following (the game) and not the rest.
			{
				// Bricks layout according to the current level.
				map.draw(level, (Graphics2D) g);
				
				// Left, right and the upper Borders.
				g.setColor(Color.blue);
				g.fillRect(0, 0, 3, 592);
				g.fillRect(0, 0, 692, 3);
				g.fillRect(691, 0, 3, 592);

				// The lower border.
				g.setColor(Color.RED);
				g.fillRect(0, 565, 691, 3);
				
				// The current score. 		
				g.setColor(Color.yellow);
				g.setFont(new Font("serif",Font.BOLD, 25));
				g.drawString(""+score, 635,30);

				// The current level.
				g.setColor(Color.yellow);
				g.setFont(new Font("serif",Font.BOLD, 15));
				g.drawString("Level: "+level, 8,20);

				// The current user's username.
				g.setColor(Color.yellow);
				g.setFont(new Font("Peace Sans",Font.BOLD, 12));
				g.drawString(username, 8, 558);
				
				// The bar when the feature "Gun Bar" is enabled is Red in color and when this feature is disabled then it is in the default colors. O: Disable, 1: Enable.
				if(gunBarEnabled[0] == 0 && gunBarEnabled[1] == 0 && gunBarEnabled[2] == 0 && gunBarEnabled[3] == 0 && gunBarEnabled[4] == 0 && gunBarEnabled[5] == 0)
				{
					g.setColor(Color.blue);
					g.fillRect(playerX, playerY, BarW/4, BarH);
					g.setColor(Color.green);
					g.fillRect(playerX + BarW/4, playerY, BarW/4, BarH);
					g.setColor(Color.blue);
					g.fillRect(playerX + 2 * BarW/4, playerY, BarW/4, BarH);
					g.setColor(Color.green);
					g.fillRect(playerX + 3 * BarW/4, playerY, BarW/4, BarH);
				}
				if(gunBarEnabled[0] == 1 || gunBarEnabled[1] == 1 || gunBarEnabled[2] == 1 || gunBarEnabled[3] == 1 || gunBarEnabled[4] == 1 || gunBarEnabled[5] == 1)
				{
					g.setColor(Color.RED);
					g.fillRect(playerX, playerY, BarW, BarH);
				}
				
				if(fireBall == 1)
				{
					g.setColor(Color.RED);
					g.fillOval(ballposX, ballposY, 18, 18);
				}

				if(ptBall == 1)
				{
					g.setColor(Color.GRAY);
					g.fillOval(ballposX, ballposY, 18, 18);
				}

				if(fireBall == 0 && ptBall == 0)
				{
					g.setColor(Color.white);
					g.fillOval(ballposX, ballposY, 18, 18);
				}
				
				// The following 6 if statements control the color of each of the bullets of the "Gun Bar". When a bullet is live (O: Disable, 1: Enable/live), its color is red, otherwise it disappears. 
				if(gunBarEnabled[0] == 1)
				{		
					g.setColor(Color.red);
					g.fillRect(gX, gY[0], 5, 5);
				}
				if(gunBarEnabled[1] == 1)
				{		
					g.setColor(Color.red);
					g.fillRect(gX2, gY[1], 5, 5);
				}
				if(gunBarEnabled[2] == 1)
				{		
					g.setColor(Color.red);
					g.fillRect(gX, gY[1], 5, 5);
				}
				if(gunBarEnabled[3] == 1)
				{		
					g.setColor(Color.red);
					g.fillRect(gX2, gY[0], 5, 5);
				}
				if(gunBarEnabled[4] == 1)
				{		
					g.setColor(Color.red);
					g.fillRect(gX, gY[2], 5, 5);
				}
				if(gunBarEnabled[5] == 1)
				{		
					g.setColor(Color.red);
					g.fillRect(gX2, gY[2], 5, 5);
				}

				// Winning condition for each level.
				if(totalBricks <= 0)
				{
					play = false; // After winning play becomes false to stop the game.
					ballXdir = 0;
					ballYdir = 0;

					g.setColor(Color.black);
					g.fillRect(1, 1, 692, 592);

					g.setColor(Color.yellow);
					g.fillRect(0, 0, 3, 592);
					g.fillRect(0, 0, 692, 3);
					g.fillRect(691, 0, 3, 592);
					g.fillRect(0, 565, 691, 3);

					g.setColor(Color.RED);
					g.setFont(new Font("serif",Font.BOLD, 30));
					g.drawString("You Won! Your Current Score: "+score, 135,270);
					
					g.setColor(Color.BLUE);
					g.setFont(new Font("serif",Font.BOLD, 40));           
					g.drawString("High Score: " + db.showHS(), 110,170); 

					g.setColor(Color.green);
					g.setFont(new Font("serif",Font.BOLD, 20));           
					g.drawString("Press  'Enter'  to  Continue!", 250,400); 

				}
				
				// Losing condition for each level.
				if(ballposY > 570)
				{
					play = false; // After losing play becomes false to stop the game.
					ballXdir = 0;
					ballYdir = 0;

					g.setColor(Color.black);
					g.fillRect(1, 1, 692, 592);

					g.setColor(Color.yellow);
					g.fillRect(0, 0, 3, 592);
					g.fillRect(0, 0, 692, 3);
					g.fillRect(691, 0, 3, 592);
					g.fillRect(0, 565, 691, 3);

					g.setColor(Color.RED);
					g.setFont(new Font("serif",Font.BOLD, 30));
					g.drawString("Game Over! Your Score: "+score, 175,270);
					
					g.setColor(Color.BLUE);
					g.setFont(new Font("serif",Font.BOLD, 40));           
					g.drawString("High Score: " + db.showHS(), 110,170); 

					g.setColor(Color.green);
					g.setFont(new Font("serif",Font.BOLD, 20));           
					g.drawString("Press  'R'  to  Restart.", 265,400);   
					
					// -- 1
					lose = 1;
					dbcon();

				}
			}
		}
		g.dispose();
	}	

	public void dbcon()
	{
		db.insertValues(username, score);
	}

	public void keyPressed(KeyEvent e) 
	{
		if (e.getKeyCode() == KeyEvent.VK_RIGHT && dirBar == 0)
		{        
			if(playerX >= 600)
			{
				playerX = 600;
			}
			else
			{
				moveRight();
			}
        }
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT && dirBar == 0)
		{          
			if(playerX < 10)
			{
				playerX = 10;
			}
			else
			{
				moveLeft();
			}
        }		

		if (e.getKeyCode() == KeyEvent.VK_RIGHT && dirBar == 1)
		{        
			if(playerX < 10)
			{
				playerX = 10;
			}
			else
			{
				moveLeft();
			}
        }
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT && dirBar == 1)
		{       
			if(playerX >= 600)
			{
				playerX = 600;
			}
			else
			{
				moveRight();
			}   
			
        }

		if (e.getKeyCode() == KeyEvent.VK_R)
		{
			if(!play)
				menuEnable = 1;
			repaint();
		}

		if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{          
			if(!play)
			{
				if(lose == 1)
				{
					Object result;
					while((result = JOptionPane.showInputDialog(this, "Please enter a username:")) == null);
					username = (String) result;
					if(username.isEmpty())
						username = "nil";

					lose = 0;
					score = 0;
					level = 0;
				}

				menuEnable = 0;

				if(totalBricks <= 0) // if(totalBricks <= 0) // if(ballposY > 570) // -- 1
					level++;
				if(level == 4)
					level = 0;

				ballposX = rand.nextInt(650);;
				ballposY = 480;
				ballXdir = -1;
				ballYdir = -2;
				playerX = 310;
				playerY = 520;

				totalBricks = 143;

				if(level == 0)
					totalBricks = 24;
				else if(level == 1)
					totalBricks = 44;
				else if(level == 2)
					totalBricks = 143;
				else if(level == 3)
					totalBricks = 114;
				else if(level == 4)
					level = 0;

				ptBall = 0;
				fireBall = 0;
				gX = playerX + BarW;
				gX2 = playerX;
				gY[0] = playerY;
				gY[1] = playerY - 25;
				gY[2] = playerY - 50;
				dirBar = 0;

				BarWdefaultore();
				BarDirRestore();
				PassThruBallrestore();
				FireBallrestore();
				barLevRestore();

				wait = 1;

				for(int i = 0; i < 6; i++)
					gunBarEnabled[i] = 0;

				if(level == 0)
					map = new MapGenerator(4, 12, 0);
				else if(level == 1)
					map = new MapGenerator(4, 12, 1);
				else if(level == 2)
					map = new MapGenerator(12, 23, 2);
				else if(level == 3)
					map = new MapGenerator(12, 23, 3);

				Timer t = new Timer(1000, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						wait = 0;
						play = true;
					}
				  });
				  t.setRepeats(false);
				  t.start();
				
				repaint();
			}
        }
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
	public void moveRight()
	{
		play = true;
		playerX += 20;	
	}
	
	public void moveLeft()
	{
		play = true;
		playerX -= 20;	 	
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if(play)
		{	
			// The following 4 if statements contains the implementations of the collision of the ball with the bar at the four different bar-segments.
			if(g.checkOverlap(9, ballposX + 9, ballposY + 9, playerX, playerY, playerX + BarW/4, playerY + BarH)) // Alternative but will result in anomalies: if(new Rectangle(ballposX, ballposY, 18, 18).intersects(new Rectangle(playerX, playerY, BarW/4, BarH)))
			{
				ballYdir = -ballYdir;
				if(ballXdir > 0)
				{
					ballXdir = -ballXdir;
				}

				if(level == 2 || level == 3)
				{
					if(Math.abs(ballXdir) < 4)
						ballXdir = 2*ballXdir;
					else if(ballXdir == 4)
						ballXdir = 2;
					else if(ballXdir == -4)
						ballXdir = -2;
				}
				else if(level == 0 || level == 1)
				{
					if(Math.abs(ballXdir) <= 4)
						ballXdir = 2*ballXdir;
					else if(ballXdir > 4)
						ballXdir = 3;
					else if(ballXdir < -4)
						ballXdir = -3;
				}
			}
			else if(g.checkOverlap(9, ballposX + 9, ballposY + 9, playerX + BarW/4, playerY, playerX + 2* BarW/4, playerY + BarH)) // if(new Rectangle(ballposX, ballposY, 18, 18).intersects(new Rectangle(playerX + (BarW/4), playerY, BarW/4, BarH)))
			{ // If the above alternative is used then else-if is imperative.
				ballYdir = -ballYdir;
				if(ballXdir < 0)
				{
					ballXdir = -ballXdir;
				}

				if(level == 2 || level == 3)
				{
					if(Math.abs(ballXdir) < 4)
						ballXdir = 2*ballXdir;
					else if(ballXdir == 4)
						ballXdir = 2;
					else if(ballXdir == -4)
						ballXdir = -2;
				}
				else if(level == 0 || level == 1)
				{
					if(Math.abs(ballXdir) <= 4)
						ballXdir = 2*ballXdir;
					else if(ballXdir > 4)
						ballXdir = 3;
					else if(ballXdir < -4)
						ballXdir = -3;
				}
			}
			else if(g.checkOverlap(9, ballposX + 9, ballposY + 9, playerX + 2* BarW/4, playerY, playerX + 3* BarW/4, playerY + BarH)) // if(new Rectangle(ballposX, ballposY, 18, 18).intersects(new Rectangle(playerX + 2 * (BarW/4), playerY, BarW/4, BarH)))
			{
				ballYdir = -ballYdir;
				if(ballXdir > 0)
				{
					ballXdir = -ballXdir;
				}

				if(level == 2 || level == 3)
				{
					if(Math.abs(ballYdir) < 4)
						ballYdir = 2*ballYdir;
					else if(ballYdir == -4)
						ballYdir = -2;
				}
				else if(level == 0 || level == 1)
				{
					if(Math.abs(ballYdir) <= 4)
						ballYdir = 2*ballYdir;
					else if(ballYdir <= -4)
						ballYdir = -2;
				}
			}
			else if(g.checkOverlap(9, ballposX + 9, ballposY + 9, playerX + 3* BarW/4, playerY, playerX + 4* BarW/4, playerY + BarH)) // if(new Rectangle(ballposX, ballposY, 18, 18).intersects(new Rectangle(playerX + 3 * (BarW/4), playerY, BarW/4, BarH)))
			{
				ballYdir = -ballYdir;
				if(ballXdir < 0)
				{
					ballXdir = -ballXdir;
				}

				if(level == 2 || level == 3)
				{
					if(Math.abs(ballYdir) < 4)
						ballYdir = 2*ballYdir;
					else if(ballYdir == -4)
						ballYdir = -2;
				}
				else if(level == 0 || level == 1)
				{
					if(Math.abs(ballYdir) <= 4)
						ballYdir = 2*ballYdir;
					else if(ballYdir <= -4)
						ballYdir = -2;
				}
			}

			// Reduces/removes the ball-bar anomaly.
			if((ballposY >= playerY && ballposY <= playerY + BarH && ballposX >= playerX && ballposX <= playerX + BarW))
			{
				ballposY = ballposY - 15;

				if(ballXdir < 0)
					ballposX = ballposX - 15;
				if(ballXdir > 0)
					ballposX = ballposX + 15;
			}

			/*

			// Extra Conditions: 

				{
					ballYdir = -ballYdir;
					ballXdir = 2; //ballXdir + 1
				}
				
				{
					ballYdir = -ballYdir;
					ballXdir = -2;
				}

			*/

		// X: Column number, Y: Row number. Each start from 0.
		// On the following brick coordinate, a feature is assigned such that we enable that particular feature when the ball collides with that particular brick on which that particular feature has been assigned.
		// For each level different bricks are assigned.

		// Feature name: "Bar Size Decrement"
		int rXbarSizeDec = 0;
		int rYbarSizeDec = 0;

		// Feature name: "Bar Size Increment"
		int rXBarSizeInc = 0;
		int rYBarSizeInc = 0;

		// Feature name: "Bar Direction Interchange"
		int rXbarDir = 0;
		int rYbarDir = 0;

		// Feature name: "Fire Ball"
		int rXfireBall = 0;
		int rYfireBall = 0;

		// Feature name: "Pass Through Ball"
		int rXpassThruBall = 0;
		int rYpassThruBall = 0;

		// Feature name: "Gun Bar"
		int rXgunBar = 0;
		int rYgunBar = 0;

		// Feature name: "Vertical Bar Level(Height) 1"
		int rXVBarL1 = 0;
		int rYVBarL1 = 0;

		// Feature name: "Vertical Bar Level(Height) 2"
		int rXVBarL2 = 0;
		int rYVBarL2 = 0;	

		if(level == 1)
		{
			rXbarSizeDec = 0 * (map.brickWidth) + 80;
			rYbarSizeDec = 0 * (map.brickHeight) + 50;
 
			rXBarSizeInc = 7 * (map.brickWidth) + 80;
			rYBarSizeInc = 1 * (map.brickHeight) + 50;

			rXbarDir = 6 * (map.brickWidth) + 80;
			rYbarDir = 3 * (map.brickHeight) + 50;

			rXfireBall = 1 * (map.brickWidth) + 80;
			rYfireBall = 3 * (map.brickHeight) + 50;

			rXpassThruBall = 2 * (map.brickWidth) + 80;
			rYpassThruBall = 2 * (map.brickHeight) + 50;

			rXgunBar = 0 * (map.brickWidth) + 80;
			rYgunBar = 3 * (map.brickHeight) + 50;

			rXVBarL1 = 5 * (map.brickWidth) + 80;
			rYVBarL1 = 0 * (map.brickHeight) + 50;

			rXVBarL2 = 6 * (map.brickWidth) + 80;
			rYVBarL2 = 0 * (map.brickHeight) + 50;
		}
		else if(level == 2)
		{
			rXbarSizeDec = 11 * (map.brickWidth) + 0;
			rYbarSizeDec = 2 * (map.brickHeight) + 50;
 
			rXBarSizeInc = 15 * (map.brickWidth) + 0;
			rYBarSizeInc = 2 * (map.brickHeight) + 50;

			rXbarDir = 16 * (map.brickWidth) + 0;
			rYbarDir = 5 * (map.brickHeight) + 50;

			rXfireBall = 21 * (map.brickWidth) + 0;
			rYfireBall = 10 * (map.brickHeight) + 50;

			rXpassThruBall = 1 * (map.brickWidth) + 0;
			rYpassThruBall = 10 * (map.brickHeight) + 50;

			rXgunBar = 6 * (map.brickWidth) + 0;
			rYgunBar = 5 * (map.brickHeight) + 50;

			rXVBarL1 = 10 * (map.brickWidth) + 0;
			rYVBarL1 = 1 * (map.brickHeight) + 50;

			rXVBarL2 = 12 * (map.brickWidth) + 0;
			rYVBarL2 = 1 * (map.brickHeight) + 50;
		}
		else if(level == 3)
		{
			rXbarSizeDec = 0 * (map.brickWidth) + 0;
			rYbarSizeDec = 4 * (map.brickHeight) + 50;
 
			rXBarSizeInc = 7 * (map.brickWidth) + 0;
			rYBarSizeInc = 4 * (map.brickHeight) + 50;

			rXbarDir = 22 * (map.brickWidth) + 0;
			rYbarDir = 10 * (map.brickHeight) + 50;

			rXfireBall = 22 * (map.brickWidth) + 0;
			rYfireBall = 6 * (map.brickHeight) + 50;

			rXpassThruBall = 4 * (map.brickWidth) + 0;
			rYpassThruBall = 10 * (map.brickHeight) + 50;

			rXgunBar = 0 * (map.brickWidth) + 0;
			rYgunBar = 9 * (map.brickHeight) + 50;

			rXVBarL1 = 0 * (map.brickWidth) + 0;
			rYVBarL1 = 0 * (map.brickHeight) + 50;

			rXVBarL2 = 18 * (map.brickWidth) + 0;
			rYVBarL2 = 0 * (map.brickHeight) + 50;
		}

			// Checking Bricks' collision with the Ball:					
			A: for(int i = 0; i < map.map.length; i++)
			{
				for(int j = 0; j < map.map[0].length; j++)
				{			
					if(map.map[i][j] > 0)
					{
						int brickX = 0;
						if(level == 0 || level == 1)
							brickX = j * (map.brickWidth) + 80;
						if(level == 2 || level == 3)
							brickX = j * (map.brickWidth) + 0;

						int brickY = i * (map.brickHeight) + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);					
						// Alternative but will result in anomalies: Rectangle ballRect = new Rectangle(ballposX, ballposY, 18, 18);
						Rectangle brickRect = rect;

						int R = 9;
						int Xc = ballposX + 9;
						int Yc = ballposY + 9;
						int X1 = brickX;
						int Y1 = brickY;
						int X2 = brickX + brickWidth;
						int Y2 = brickY + brickHeight;

						


						if(g.checkOverlap(R, Xc, Yc, X1, Y1, X2, Y2)) // Alternative but will result in anomalies: if(ballRect.intersects(brickRect))
						{	
							if(j != 0 && level == 1)
								map.setBrickCollisionValue(i, j);
							if(level == 0 || level == 2 || level == 3)
								map.setBrickCollisionValue(i, j);
							if(map.mapHit[i][j] == 0)				
							{
								map.setBrickValue(0, i, j);
								score+=5;	
								totalBricks--;
							}

							if(ptBall == 0)
							{
								// if(ballposX + 18 <= brickRect.x || brickRect.x + brickRect.width - 7 <= ballposX && ballposX <= brickRect.x + brickRect.width || ballposX >= brickRect.x + brickRect.width || brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 7)	
								// 	ballXdir = -ballXdir;
								// else if(ballposY + 18 <= brickRect.y || brickRect.y + brickRect.height - 7 <= ballposY && ballposY <= brickRect.y + brickRect.height || ballposY >= brickRect.y + brickRect.height || brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 7)
								// 	ballYdir = -ballYdir;	

								if(ballposX + 18 <= brickRect.x || brickRect.x + brickRect.width - 7 <= ballposX && ballposX <= brickRect.x + brickRect.width || ballposX >= brickRect.x + brickRect.width || brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 7)	
								{
									ballXdir = -ballXdir;
									if(ballXdir < 0)
									{
										if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 1)
											ballposX -= 2;
										if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 2)
											ballposX -= 3;
										if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 3)
											ballposX -= 4;
										if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 4)
											ballposX -= 5;
										if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 5)
											ballposX -= 6;
										if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 6)
											ballposX -= 7;
										if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 7)
											ballposX -= 8;
									}
									if(ballXdir > 0)
									{
										if(brickRect.x + brickRect.width - 1 <= ballposX && ballposX <= brickRect.x + brickRect.width)
											ballposX += 2;
										if(brickRect.x + brickRect.width - 2 <= ballposX && ballposX <= brickRect.x + brickRect.width)
											ballposX += 3;
										if(brickRect.x + brickRect.width - 3 <= ballposX && ballposX <= brickRect.x + brickRect.width)
											ballposX += 4;
										if(brickRect.x + brickRect.width - 4 <= ballposX && ballposX <= brickRect.x + brickRect.width)
											ballposX += 5;
										if(brickRect.x + brickRect.width - 5 <= ballposX && ballposX <= brickRect.x + brickRect.width)
											ballposX += 6;
										if(brickRect.x + brickRect.width - 6 <= ballposX && ballposX <= brickRect.x + brickRect.width)
											ballposX += 7;
										if(brickRect.x + brickRect.width - 7 <= ballposX && ballposX <= brickRect.x + brickRect.width)
											ballposX += 8;
									}	
								}
								else if(ballposY + 18 <= brickRect.y || brickRect.y + brickRect.height - 7 <= ballposY && ballposY <= brickRect.y + brickRect.height || ballposY >= brickRect.y + brickRect.height || brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 7)
								{
									
									ballYdir = -ballYdir;	
									if(ballYdir < 0)
									{
										if(brickRect.y + brickRect.height - 1 <= ballposY && ballposY <= brickRect.y + brickRect.height)
											ballposY += 2;
										if(brickRect.y + brickRect.height - 2 <= ballposY && ballposY <= brickRect.y + brickRect.height)
											ballposY += 3;
										if(brickRect.y + brickRect.height - 3 <= ballposY && ballposY <= brickRect.y + brickRect.height)
											ballposY += 4;
										if(brickRect.y + brickRect.height - 4 <= ballposY && ballposY <= brickRect.y + brickRect.height)
											ballposY += 5;
										if(brickRect.y + brickRect.height - 5 <= ballposY && ballposY <= brickRect.y + brickRect.height)
											ballposY += 6;
										if(brickRect.y + brickRect.height - 6 <= ballposY && ballposY <= brickRect.y + brickRect.height)
											ballposY += 7;
										if(brickRect.y + brickRect.height - 7 <= ballposY && ballposY <= brickRect.y + brickRect.height)
											ballposY += 8;
									}
									if(ballYdir > 0)
									{
										if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 1)
											ballposY -= 2;
										if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 2)
											ballposY -= 3;
										if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 3)
											ballposY -= 4;
										if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 4)
											ballposY -= 5;
										if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 5)
											ballposY -= 6;
										if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 6)
											ballposY -= 7;
										if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 7)
											ballposY -= 8;
									}			
								}

								
								if(fireBall == 1)
								{									
									int ki;
									int kj;
									B: for(ki = i-1 ; ki <= i + 1 ; ki++ )
									{
										for(kj = j - 1; kj <= j + 1 ; kj ++)
										{
											if(ki >= map.map.length || ki < 0)
											{
												continue B;
											}
											if(kj >= map.map[i].length || kj < 0)
											{
												continue;
											}
											try
											{
												if(kj != 0)
													map.setBrickCollisionValue(ki , kj);
												if(map.mapHit[ki][kj] == 0)
												{
													map.setBrickValue(0, ki, kj);
													totalBricks--;
													score+=5;
												}		
											}
											catch(ArrayIndexOutOfBoundsException obj)
											{
												continue;
											}
										}
									}
								}
							}

							if(rXVBarL1 == brickX && rYVBarL1 == brickY)
							{
								if(level == 0 || level == 1 || level == 2)
									playerY = 440;
								
								Timer t = new Timer(10000, new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent arg0) {
										barLevRestore();
									}
								});
								t.setRepeats(false);
								t.start();
								if(play == false)
								{
									t.stop();
								}
							}	

							if(rXVBarL2 == brickX && rYVBarL2 == brickY)
							{
								if(level == 0 || level == 1 || level == 2)
									playerY = 380;
								
								Timer t = new Timer(10000, new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent arg0) {
										barLevRestore();
									}
								});
								t.setRepeats(false);
								t.start();
								if(play == false)
								{
									t.stop();
								}
							}	

							if(rXbarSizeDec == brickX && rYbarSizeDec == brickY && BarW >= 100)
							{
								BarW = BarW - 40;
								Timer t = new Timer(10000, new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent arg0) {
										BarWdefaultore();
									}
								});
								t.setRepeats(false);
								t.start();
								if(play == false)
								{
									t.stop();
								}
							}	
							
							if(rXBarSizeInc == brickX && rYBarSizeInc == brickY)
							{
								BarW = BarW + 50;
								Timer t = new Timer(10000, new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent arg0) {
										BarWdefaultore();;
									}
								});
								t.setRepeats(false);
								t.start();
								if(play == false)
								{
									t.stop();
								}
							}	

							if(rXbarDir == brickX && rYbarDir == brickY)
							{
								dirBar = 1;
								Timer t = new Timer(10000, new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent arg0) {
									BarDirRestore();
								}
							});
							t.setRepeats(false);
							t.start();
							if(play == false)
								{
									t.stop();
								}
							}	

							if(rXpassThruBall == brickX && rYpassThruBall == brickY)
							{
								ptBall = 1;
								Timer t = new Timer(10000, new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent arg0) {
										PassThruBallrestore();
									}
								});
								t.setRepeats(false);
								t.start();
								if(play == false)
								{
									t.stop();
								}
							}	

							if(rXfireBall == brickX && rYfireBall == brickY)
							{
								fireBall = 1;
								ptBall = 0;
								Timer t = new Timer(10000, new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent arg0) {
										FireBallrestore();
									}
								});
								t.setRepeats(false);
								t.start();
								if(play == false)
								{
									t.stop();
								}
							}

							if(rXgunBar == brickX && rYgunBar == brickY)
							{
								gX = playerX + BarW; 
								gX2 = playerX; 
								gY[0] = playerY;
								gY[1] = playerY - 25;
								gY[2] = playerY - 50;
								
								for(int ii = 0; ii < 6; ii++)
									gunBarEnabled[ii] = 1;

								GunBar(gX, gY, 0, gunBarEnabled, 0);
								GunBar(gX2, gY, 1, gunBarEnabled, 1);
								GunBar(gX, gY, 1, gunBarEnabled, 2);
								GunBar(gX2, gY, 0, gunBarEnabled, 3);
								GunBar(gX, gY, 2, gunBarEnabled, 4);
								GunBar(gX2, gY, 2, gunBarEnabled, 5);
								
							}

							break A;
						}
					}
					
				}
			}

			ballposX += ballXdir;
			ballposY += ballYdir;
			
			if(ballposX < 0)
			{
				ballXdir = -ballXdir;
			}
			if(ballposY < 0)
			{
				ballYdir = -ballYdir;
			}
			if(ballposX > 670)
			{
				ballXdir = -ballXdir;
			}		
			
			repaint();
		}
	}

	public void GunBar(int X, int Y[], int d1, int gunBarEnabled[], int d2)
	{
		final Timer ptimer = new Timer(250, null);
		ptimer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				for(int i = 0; i < map.map.length; i++)
				{
					for(int j = 0; j < map.map[0].length; j++)
					{				
						if(map.map[i][j] > 0)
						{
							int bX = 0;
							if(level == 0 || level == 1)
								bX = j * (map.brickWidth) + 80;
							if(level == 2 || level == 3)
								bX = j * (map.brickWidth) + 0;

							int bY = i * (map.brickHeight) + 50;
							int brickW = map.brickWidth;
							int brickH = map.brickHeight;
							
							Rectangle rect = new Rectangle(bX, bY, brickW, brickH);					
							Rectangle gunRect = new Rectangle(X, Y[d1], 5, 5);
							Rectangle brickRect = rect;
							
							if(gunRect.intersects(brickRect))
							{	
								if(j != 0 && level == 1)
									map.setBrickCollisionValue(i, j);
								if(level == 0 || level == 2 || level == 3)
									map.setBrickCollisionValue(i, j);
								if(map.mapHit[i][j] == 0)			
								{
									map.setBrickValue(0, i, j);
									totalBricks--;
									score+=5;	
								}
								
								gunBarEnabled[d2] = 0;
								ptimer.stop();
							}
							if(Y[d1] <= 0)
							{
								gunBarEnabled[d2] = 0;
								ptimer.stop();
							}
							if(play == false)
							{
								ptimer.stop();
							}
						}
					}
				}

				Y[d1] = Y[d1] - 12;
			}
			});
		ptimer.setRepeats(true);
		ptimer.start();
	}

	public void barLevRestore()
	{
		playerY = 520;
	}

	public void BarWdefaultore()
	{
		BarW = BarWdefault;
	}

	public void BarDirRestore()
	{
		dirBar = 0;
	}

	public void PassThruBallrestore()
	{
		ptBall = 0;
	}

	public void FireBallrestore()
	{
		fireBall = 0;
	}
}

/*

Alternative logic for Ball-Brick collision:

								// 1
								if(ballXdir < 0 && ballYdir < 0)
								{
									if(ballposX-(brickX+brickWidth/2) <= (brickWidth/2)+1 &&  ballposX-(brickX+brickWidth/2) > (brickWidth/2) - 8 && ballposY+9 <= brickY + brickHeight && ballposY+9 >= brickY)
									{
										ballXdir = -ballXdir;
										if(brickRect.x + brickRect.width - 1 <= ballposX && ballposX <= brickRect.x + brickRect.width)
											ballposX += 2;
										if(brickRect.x + brickRect.width - 2 <= ballposX && ballposX <= brickRect.x + brickRect.width)
											ballposX += 3;
										if(brickRect.x + brickRect.width - 3 <= ballposX && ballposX <= brickRect.x + brickRect.width)
											ballposX += 4;
										if(brickRect.x + brickRect.width - 4 <= ballposX && ballposX <= brickRect.x + brickRect.width)
											ballposX += 5;
										if(brickRect.x + brickRect.width - 5 <= ballposX && ballposX <= brickRect.x + brickRect.width)
											ballposX += 6;
										if(brickRect.x + brickRect.width - 6 <= ballposX && ballposX <= brickRect.x + brickRect.width)
											ballposX += 7;
										if(brickRect.x + brickRect.width - 7 <= ballposX && ballposX <= brickRect.x + brickRect.width)
											ballposX += 8;
									}

									else if(ballposY-(brickY + brickHeight/2) <= (brickHeight/2)+1 && ballposY-(brickY + brickHeight/2) > (brickHeight/2)-8 && ballposX+9 <= brickX + brickWidth && ballposX+9 >= brickX)
									{
										ballYdir = -ballYdir;
										if(brickRect.y + brickRect.height - 1 <= ballposY && ballposY <= brickRect.y + brickRect.height)
											ballposY += 2;
										if(brickRect.y + brickRect.height - 2 <= ballposY && ballposY <= brickRect.y + brickRect.height)
											ballposY += 3;
										if(brickRect.y + brickRect.height - 3 <= ballposY && ballposY <= brickRect.y + brickRect.height)
											ballposY += 4;
										if(brickRect.y + brickRect.height - 4 <= ballposY && ballposY <= brickRect.y + brickRect.height)
											ballposY += 5;
										if(brickRect.y + brickRect.height - 5 <= ballposY && ballposY <= brickRect.y + brickRect.height)
											ballposY += 6;
										if(brickRect.y + brickRect.height - 6 <= ballposY && ballposY <= brickRect.y + brickRect.height)
											ballposY += 7;
										if(brickRect.y + brickRect.height - 7 <= ballposY && ballposY <= brickRect.y + brickRect.height)
											ballposY += 8;
									}
									else
										ballXdir = -ballXdir;
								}

								// 2
								else if(ballXdir > 0 && ballYdir < 0)
								{
									if((brickX+brickWidth/2)-(ballposX + 18) <= (brickWidth/2)+1 &&  (brickX+brickWidth/2)-(ballposX + 18) > (brickWidth/2) - 8 && ballposY+9 <= brickY + brickHeight && ballposY+9 >= brickY)
									{
										ballXdir = -ballXdir;
										if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 1)
											ballposX -= 2;
										if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 2)
											ballposX -= 3;
										if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 3)
											ballposX -= 4;
										if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 4)
											ballposX -= 5;
										if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 5)
											ballposX -= 6;
										if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 6)
											ballposX -= 7;
										if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 7)
											ballposX -= 8;
										
									}

									else if(ballposY-(brickY + brickHeight/2) <= (brickHeight/2)+1 && ballposY-(brickY + brickHeight/2) > (brickHeight/2)-8 && ballposX+9 <= brickX + brickWidth && ballposX+9 >= brickX)
									{
										ballYdir = -ballYdir;
										if(brickRect.y + brickRect.height - 1 <= ballposY && ballposY <= brickRect.y + brickRect.height)
											ballposY += 2;
										if(brickRect.y + brickRect.height - 2 <= ballposY && ballposY <= brickRect.y + brickRect.height)
											ballposY += 3;
										if(brickRect.y + brickRect.height - 3 <= ballposY && ballposY <= brickRect.y + brickRect.height)
											ballposY += 4;
										if(brickRect.y + brickRect.height - 4 <= ballposY && ballposY <= brickRect.y + brickRect.height)
											ballposY += 5;
										if(brickRect.y + brickRect.height - 5 <= ballposY && ballposY <= brickRect.y + brickRect.height)
											ballposY += 6;
										if(brickRect.y + brickRect.height - 6 <= ballposY && ballposY <= brickRect.y + brickRect.height)
											ballposY += 7;
										if(brickRect.y + brickRect.height - 7 <= ballposY && ballposY <= brickRect.y + brickRect.height)
											ballposY += 8;
									}
									else
										ballXdir = -ballXdir;
								}

								// 3
								else if(ballXdir > 0 && ballYdir > 0)
								{
									if((brickX+brickWidth/2)-(ballposX + 18) <= (brickWidth/2)+1 &&  (brickX+brickWidth/2)-(ballposX + 18) > (brickWidth/2) - 8 && ballposY+9 <= brickY + brickHeight && ballposY+9 >= brickY)
									{
										ballXdir = -ballXdir;
										if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 1)
											ballposX -= 2;
										if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 2)
											ballposX -= 3;
										if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 3)
											ballposX -= 4;
										if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 4)
											ballposX -= 5;
										if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 5)
											ballposX -= 6;
										if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 6)
											ballposX -= 7;
										if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 7)
											ballposX -= 8;
										
									}

									else if((brickY + brickHeight/2)-(ballposY + 18) <= (brickHeight/2)+1 && (brickY + brickHeight/2)-(ballposY + 18) > (brickHeight/2)-8 && ballposX+9 <= brickX + brickWidth && ballposX+9 >= brickX)
									{
										ballYdir = -ballYdir;
										if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 1)
											ballposY -= 2;
										if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 2)
											ballposY -= 3;
										if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 3)
											ballposY -= 4;
										if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 4)
											ballposY -= 5;
										if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 5)
											ballposY -= 6;
										if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 6)
											ballposY -= 7;
										if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 7)
											ballposY -= 8;
									}
									else
										ballXdir = -ballXdir;
								}
								// 4
								else if(ballXdir < 0 && ballYdir > 0)
								{
									if(ballposX-(brickX+brickWidth/2) <= (brickWidth/2)+1 &&  ballposX-(brickX+brickWidth/2) > (brickWidth/2) - 8 && ballposY+9 <= brickY + brickHeight && ballposY+9 >= brickY)
									{
										ballXdir = -ballXdir;
										if(brickRect.x + brickRect.width - 1 <= ballposX && ballposX <= brickRect.x + brickRect.width)
											ballposX += 2;
										if(brickRect.x + brickRect.width - 2 <= ballposX && ballposX <= brickRect.x + brickRect.width)
											ballposX += 3;
										if(brickRect.x + brickRect.width - 3 <= ballposX && ballposX <= brickRect.x + brickRect.width)
											ballposX += 4;
										if(brickRect.x + brickRect.width - 4 <= ballposX && ballposX <= brickRect.x + brickRect.width)
											ballposX += 5;
										if(brickRect.x + brickRect.width - 5 <= ballposX && ballposX <= brickRect.x + brickRect.width)
											ballposX += 6;
										if(brickRect.x + brickRect.width - 6 <= ballposX && ballposX <= brickRect.x + brickRect.width)
											ballposX += 7;
										if(brickRect.x + brickRect.width - 7 <= ballposX && ballposX <= brickRect.x + brickRect.width)
											ballposX += 8;
										
									}

									else if((brickY + brickHeight/2)-(ballposY + 18) <= (brickHeight/2)+1 && (brickY + brickHeight/2)-(ballposY + 18) > (brickHeight/2)-8 && ballposX+9 <= brickX + brickWidth && ballposX+9 >= brickX)
									{
										ballYdir = -ballYdir;
										if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 1)
											ballposY -= 2;
										if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 2)
											ballposY -= 3;
										if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 3)
											ballposY -= 4;
										if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 4)
											ballposY -= 5;
										if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 5)
											ballposY -= 6;
										if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 6)
											ballposY -= 7;
										if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 7)
											ballposY -= 8;
									}
									else
										ballXdir = -ballXdir;
								}

 */

/* 

Another alternative logic for Ball-Brick collision:
 
								if(ballposX + 18 <= brickRect.x || brickRect.x + brickRect.width - 7 <= ballposX && ballposX <= brickRect.x + brickRect.width || ballposX >= brickRect.x + brickRect.width || brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 7)	
								{
									ballXdir = -ballXdir;
									// if(ballXdir < 0)
									// {
									// 	if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 1)
									// 		ballposX -= 2;
									// 	if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 2)
									// 		ballposX -= 3;
									// 	if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 3)
									// 		ballposX -= 4;
									// 	if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 4)
									// 		ballposX -= 5;
									// 	if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 5)
									// 		ballposX -= 6;
									// 	if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 6)
									// 		ballposX -= 7;
									// 	if(brickRect.x <= ballposX + 18 && ballposX + 18 <= brickRect.x + 7)
									// 		ballposX -= 8;
									// }
									// if(ballXdir > 0)
									// {
									// 	if(brickRect.x + brickRect.width - 1 <= ballposX && ballposX <= brickRect.x + brickRect.width)
									// 		ballposX += 2;
									// 	if(brickRect.x + brickRect.width - 2 <= ballposX && ballposX <= brickRect.x + brickRect.width)
									// 		ballposX += 3;
									// 	if(brickRect.x + brickRect.width - 3 <= ballposX && ballposX <= brickRect.x + brickRect.width)
									// 		ballposX += 4;
									// 	if(brickRect.x + brickRect.width - 4 <= ballposX && ballposX <= brickRect.x + brickRect.width)
									// 		ballposX += 5;
									// 	if(brickRect.x + brickRect.width - 5 <= ballposX && ballposX <= brickRect.x + brickRect.width)
									// 		ballposX += 6;
									// 	if(brickRect.x + brickRect.width - 6 <= ballposX && ballposX <= brickRect.x + brickRect.width)
									// 		ballposX += 7;
									// 	if(brickRect.x + brickRect.width - 7 <= ballposX && ballposX <= brickRect.x + brickRect.width)
									// 		ballposX += 8;
									// }	
								}
								if(ballposY + 18 <= brickRect.y || brickRect.y + brickRect.height - 7 <= ballposY && ballposY <= brickRect.y + brickRect.height || ballposY >= brickRect.y + brickRect.height || brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 7)
								{
									
									ballYdir = -ballYdir;	
									// if(ballYdir < 0)
									// {
									// 	if(brickRect.y + brickRect.height - 1 <= ballposY && ballposY <= brickRect.y + brickRect.height)
									// 		ballposY += 2;
									// 	if(brickRect.y + brickRect.height - 2 <= ballposY && ballposY <= brickRect.y + brickRect.height)
									// 		ballposY += 3;
									// 	if(brickRect.y + brickRect.height - 3 <= ballposY && ballposY <= brickRect.y + brickRect.height)
									// 		ballposY += 4;
									// 	if(brickRect.y + brickRect.height - 4 <= ballposY && ballposY <= brickRect.y + brickRect.height)
									// 		ballposY += 5;
									// 	if(brickRect.y + brickRect.height - 5 <= ballposY && ballposY <= brickRect.y + brickRect.height)
									// 		ballposY += 6;
									// 	if(brickRect.y + brickRect.height - 6 <= ballposY && ballposY <= brickRect.y + brickRect.height)
									// 		ballposY += 7;
									// 	if(brickRect.y + brickRect.height - 7 <= ballposY && ballposY <= brickRect.y + brickRect.height)
									// 		ballposY += 8;
									// }
									// if(ballYdir > 0)
									// {
									// 	if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 1)
									// 		ballposY -= 2;
									// 	if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 2)
									// 		ballposY -= 3;
									// 	if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 3)
									// 		ballposY -= 4;
									// 	if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 4)
									// 		ballposY -= 5;
									// 	if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 5)
									// 		ballposY -= 6;
									// 	if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 6)
									// 		ballposY -= 7;
									// 	if(brickRect.y <= ballposY + 18 && ballposY + 18 <= brickRect.y + 7)
									// 		ballposY -= 8;
									// }			
								}

 */