import javax.swing.JFrame;

public class Main 
{
	public static void main(String[] args)
	{
		JFrame obj=new JFrame();
		Gameplay gamePlay = new Gameplay();
		
		obj.setBounds(390, 100, 708, 605);
		obj.setTitle("2D Featureful GUI-Based Brick-Breaker Game");		
		obj.setResizable(false);
		obj.setVisible(true);
		obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		obj.add(gamePlay); // 'gamePlay' is a JPanel which is added as a component in the JFrame.
	}
}

// Reference: https://sourcecodehero.com/brick-breaker-game-in-java-with-source-code/