//Code By :- Yash Brid
package todolist;
import java.sql.Connection;
import java.sql.DriverManager;


public class DB {
	Connection con=null;
	java.sql.PreparedStatement pst;
	public static Connection dbconnect()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/todolist","root","");
			return conn;
		}
		catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
}
