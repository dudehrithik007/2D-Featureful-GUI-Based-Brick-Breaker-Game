import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class MapGenerator 
{
	public int map[][];
	public int brickWidth;
	public int brickHeight;
	public int mapHit[][];
	
	public MapGenerator(int row, int col, int level)
	{
		if(level == 0)
		{
			map = new int[row][col];
			mapHit  = new int[row][col];	

			for(int i = 0; i < map.length; i++)
			{
				for(int j = 0; j < map[0].length; j = j + 2)
				{
					map[i][j] = 1;
					mapHit[i][j] = 1;
				}			
			}

			brickWidth = 45; // 56;
			brickHeight = 37; // 38;
		}
		
		else if(level == 1)
		{	
			map = new int[row][col];
			mapHit  = new int[row][col];	

			for(int i = 0; i < map.length; i++)
			{
				for(int j = 0; j < map[0].length; j++)
				{
					map[i][j] = 1;
					mapHit[i][j] = 2;
				}			
			}

			brickWidth = 45;
			brickHeight = 37;
		}

		else if(level == 2)
		{
			map = new int[row][col];
			mapHit = new int[row][col];
			
			for(int i = 0; i < map.length; i++)
			{
				for(int j = 0; j < map[0].length; j++)
				{
					if(j <= map.length - (i+1) || j >= map.length + (i-1))
					{
						map[i][j] = 1;

						if((i == 5 && j == 1) || (i == 5 && j == 2) || (i == 5 && j == 3) || (i == 5 && j == 4) || (i == 5 && j == 5) || (i == 5 && j == 6) || (i == 5 && j == 16) || (i == 5 && j == 17) || (i == 5 && j == 18) || (i == 5 && j == 19) || (i == 5 && j == 20) || (i == 5 && j == 21))
							mapHit[i][j] = 50;
						else if(i == 0 || j == 0 || j == 22 || i == 11)
							mapHit[i][j] = 5;
						else
							mapHit[i][j] = 1;
					}
				}	
			}		
		
			brickWidth = 30;
			brickHeight = 30;
		}

		else if(level == 3)
		{
			map = new int[row][col];
			mapHit=new int[row][col];	

			for(int i = 0; i<map.length; i=i+2)
			{
				if(i == 0)
				{
					for(int j = 0; j < map[0].length - 4; j++)
					{
						map[i][j] = 1;
						mapHit[i][j] = 5;
					}
				}
				if(i == 10)
				{
					for(int j = 4; j < map[0].length; j++)
					{
						map[i][j] = 1;
						mapHit[i][j] = 1;
					}
				}
				if(i == 4 || i == 8) 
				{
					for(int j = 0; j < map[0].length - 4; j++)
					{
						map[i][j] = 1;
						mapHit[i][j] = 2;
					}
				}
				else if(i == 2 || i == 6)
				{
					for(int j = 4; j < map[0].length; j++)
					{
						map[i][j] = 1;
						mapHit[i][j] = 3;

					}
				}
			}	

			brickWidth = 30;
			brickHeight = 30;
		}
	}	
	
	public void draw(int level, Graphics2D g)
	{
		if(level == 0)
		{
			for(int i = 0; i < map.length; i++)
			{
				for(int j = 0; j < map[0].length; j++)
				{
					if(map[i][j] > 0)
					{
						g.setColor(Color.magenta);
						g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
						
						g.setStroke(new BasicStroke(3));
						g.setColor(Color.black);
						g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);				
					}
				}
			}
		}
		
		else if(level == 1)
		{
			for(int i = 0; i < map.length; i++)
			{
				for(int j = 0; j < map[0].length; j++)
				{
					if(map[i][j] > 0)
					{
						if(mapHit[i][j] == 2)
						{
							g.setColor(Color.blue);
							g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

							g.setStroke(new BasicStroke(3));
							g.setColor(Color.black);
							g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);	
						}
						
						else if(mapHit[i][j] == 1)
						{
							g.setColor(Color.red);
							g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

							g.setStroke(new BasicStroke(3));
							g.setColor(Color.black);
							g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);	
						}
					}

					if(j == 0)
					{
						g.setColor(Color.green);
						g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

						g.setStroke(new BasicStroke(3));
						g.setColor(Color.black);
						g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);	
					}
				}
			}
		}

		else if(level == 2)
		{
			for(int i = 0; i < map.length; i++)
			{
				for(int j = 0; j < map[0].length; j++)
				{
					if(map[i][j] > 0)
					{
						if(mapHit[i][j] <= 50 && mapHit[i][j] >= 6)
						{
							g.setColor(Color.GRAY);
							g.fillRect(j * brickWidth + 0, i * brickHeight+ 50, brickWidth, brickHeight);
							
							g.setStroke(new BasicStroke(10));
							g.setColor(Color.black);
							g.drawRect(j * brickWidth + 0, i * brickHeight+ 50, brickWidth, brickHeight);				
						}
						if(mapHit[i][j] == 4)
						{
							g.setColor(Color.RED);
							g.fillRect(j * brickWidth + 0, i * brickHeight+ 50, brickWidth, brickHeight);
							
							g.setStroke(new BasicStroke(10));
							g.setColor(Color.black);
							g.drawRect(j * brickWidth + 0, i * brickHeight+ 50, brickWidth, brickHeight);				
						}
						if(mapHit[i][j] == 3)
						{
							g.setColor(Color.blue);
							g.fillRect(j * brickWidth + 0, i * brickHeight+ 50, brickWidth, brickHeight);
							
							g.setStroke(new BasicStroke(10));
							g.setColor(Color.black);
							g.drawRect(j * brickWidth + 0, i * brickHeight+ 50, brickWidth, brickHeight);				
						}
						
						if(mapHit[i][j] == 2)
						{
							g.setColor(Color.green);
							g.fillRect(j * brickWidth + 0, i * brickHeight+ 50, brickWidth, brickHeight);

							g.setStroke(new BasicStroke(10));
							g.setColor(Color.black);
							g.drawRect(j * brickWidth + 0, i * brickHeight+ 50, brickWidth, brickHeight);				
						}
						
						if(mapHit[i][j] == 1)
						{
							g.setColor(Color.white);
							g.fillRect(j * brickWidth + 0, i * brickHeight+ 50, brickWidth, brickHeight);
									
							g.setStroke(new BasicStroke(10));
							g.setColor(Color.black);
							g.drawRect(j * brickWidth + 0, i * brickHeight+ 50, brickWidth, brickHeight);				
						}
					}
				}
			}
		}
		else if(level == 3)
		{
			for(int i = 0; i<map.length; i++)
			{
				for(int j = 0; j<map[0].length; j++)
				{
					if(map[i][j] > 0)
					{
						if(mapHit[i][j] == 5)
						{
							g.setColor(Color.BLUE);
							g.fillRect(j * brickWidth + 0, i * brickHeight+ 50, brickWidth, brickHeight);
							
							g.setStroke(new BasicStroke(6));
							g.setColor(Color.black);
							g.drawRect(j * brickWidth + 0, i * brickHeight+ 50, brickWidth, brickHeight);				
						}
						if(mapHit[i][j] == 4)
						{
							g.setColor(Color.GRAY);
							g.fillRect(j * brickWidth + 0, i * brickHeight+ 50, brickWidth, brickHeight);
							
							g.setStroke(new BasicStroke(6));
							g.setColor(Color.black);
							g.drawRect(j * brickWidth + 0, i * brickHeight+ 50, brickWidth, brickHeight);				
						}
						if(mapHit[i][j] == 3)
						{
							g.setColor(Color.white);
							g.fillRect(j * brickWidth + 0, i * brickHeight+ 50, brickWidth, brickHeight);
							
							g.setStroke(new BasicStroke(6));
							g.setColor(Color.black);
							g.drawRect(j * brickWidth + 0, i * brickHeight+ 50, brickWidth, brickHeight);				
						}
						if(mapHit[i][j] == 2)
						{
							g.setColor(Color.green);
							g.fillRect(j * brickWidth + 0, i * brickHeight+ 50, brickWidth, brickHeight);
							
							g.setStroke(new BasicStroke(6));
							g.setColor(Color.black);
							g.drawRect(j * brickWidth + 0, i * brickHeight+ 50, brickWidth, brickHeight);				
						}
						
						if(mapHit[i][j] == 1)
						{
							g.setColor(Color.red);
							g.fillRect(j * brickWidth + 0, i * brickHeight+ 50, brickWidth, brickHeight);
							
							g.setStroke(new BasicStroke(6));
							g.setColor(Color.black);
							g.drawRect(j * brickWidth + 0, i * brickHeight+ 50, brickWidth, brickHeight);				
						}
					}
				}
			}
		}
	}
	
	public void setBrickValue(int value, int row, int col)
	{
		map[row][col] = value;
	}

	public void setBrickCollisionValue(int row, int col)
	{
		mapHit[row][col]--;
	}
}