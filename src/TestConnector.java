import java.sql.*;

public class TestConnector{
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("success");
		}catch(Exception e) {
			System.out.println("Failure");
		}
	}
}