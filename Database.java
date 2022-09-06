import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

// Used for storing the current player's score and retrieving the high score with the corresponding username.

public class Database
{
    Connection conn;
    Statement stmt;

    String uname; // Stores the current player's username passed as a parameter to insertValues().
    int uscore; // Stores the current player's score passed as a parameter to insertValues().
    String ret; // Stores and is used to return the high score with the corresponding username in showHS().

    Database()
    {
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Scores", "root", "6501");
        }
        catch(SQLException e) 
        {
            e.printStackTrace();
        } 
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        
    }

    public void insertValues(String username, int scores)
    {
        try 
        {
            String sql = "insert into scores.scores " + " (username, scores)" + " values (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);
            stmt.setInt(2, scores);

            stmt.executeUpdate();
        } 
        catch(SQLException e) 
        {
            e.printStackTrace();
        } 
    }

    public String showHS()
    {
        try 
        {
            String query = "Select* from scores.scores where scores = (select max(scores) from scores.scores);";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) 
            {
                uname = rs.getString("username");
                uscore = rs.getInt("scores");
                ret = uname + " = " + uscore;
            }
        }
        catch(SQLException e) 
        {
            e.printStackTrace();
        } 

        return ret;
    }
}