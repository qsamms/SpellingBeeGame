import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class SQL{
	//private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/spellingbee";
	private static String USER = "root";
	private static String PASS = "District14champs";
	private static Connection connection;
	private static Statement statement;
	
	public SQL() {
		try{ 
			connection = DriverManager.getConnection(DB_URL,USER,PASS); 
			statement = connection.createStatement();
			// open a connection to the database
			System.out.println("Connecting to database");
		}catch(SQLException se){
			System.out.println("JDBC errors");
			se.printStackTrace();
		}catch(Exception err){
			System.out.println("Errors in Class.forName");
			err.printStackTrace();
		}
	}
	
	public void insert(String username, int score, String table) throws SQLException {
		PreparedStatement p;
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		
		StringBuilder query = new StringBuilder();
		query.append("insert into " + table + " values");
		query.append("('" + username + "'," + "'" + score + "'" + "," + " '" + dtf.format(now) + "');");
		
		String stringQuery = query.toString();
		p = connection.prepareStatement(stringQuery);
		p.execute();
		
	}
	
	public ResultSet usernameQuery(String username) throws SQLException {
		System.out.println("Executing a query");
		StringBuilder query = new StringBuilder();
		
		query.append("select * from scores where username = '" + username + "'");
		String stringQuery = query.toString();
		return statement.executeQuery(stringQuery);
	}
	
	public ResultSet selectAll() throws SQLException {
		System.out.println("Executing a query");
		
		return statement.executeQuery("select * from scores");
	}

	public static void main(String[] args) {
		SQL s = new SQL();
		String args2 = "*";
		String some = "username, score";
		
		String[] columnNames = {"username", "score", "date"};
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		ArrayList<String> temp = new ArrayList<String>();
		
		ResultSet result = null;
		try {
			result = s.selectAll();
			while(result.next()) {
				temp = new ArrayList<String>();
				temp.add(result.getString("username"));
				temp.add(result.getString("score"));
				temp.add(result.getString("date"));
				data.add(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println(data.toString());
	}
}