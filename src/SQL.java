import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class SQL{
	//private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/spellingbee";
	private static String USER = "root";
	private static String PASS = "rootpassword";
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
	
	public void insertWords(int id, String[] words) throws SQLException{
		PreparedStatement p;
		StringBuilder query = new StringBuilder();
		String s;
		
		for(int i = 0;i<words.length;i++) {
			query.append("insert into words values (" + id + ", '" + words[i] + "');");
			s = query.toString();
			System.out.println(s);
			p = connection.prepareStatement(s);
			p.execute();
			query.setLength(0);
		}
	}
	
	public ResultSet wordsQuery(int id) throws SQLException {
		System.out.println("Executing a query");
		StringBuilder query = new StringBuilder();
		
		query.append("select * from words where userid = " + id + ";");
		String stringQuery = query.toString();
		return statement.executeQuery(stringQuery);
	}
	
	public int getMaxID() {
		int val = -1;
		
		try {
			ResultSet result = statement.executeQuery("select MAX(id) from scores;");
			result.next();
			val = Integer.parseInt(result.getString("MAX(id)"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return val;
	}
	
	public void insert(String username, int score, String table) throws SQLException {
		PreparedStatement p;
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		
		StringBuilder query = new StringBuilder();
		query.append("insert into " + table + " (username,score,date) values");
		query.append("('" +  username + "'," + "'" + score + "'" + "," + " '" + dtf.format(now) + "');");
		
		String stringQuery = query.toString();
		p = connection.prepareStatement(stringQuery);
		p.execute();
		
	}
	
	public ResultSet usernameQuery(String username) throws SQLException {
		System.out.println("Executing a query");
		StringBuilder query = new StringBuilder();
		
		query.append("select * from scores where username = '" + username + "'" + " order by score desc;");
		String stringQuery = query.toString();
		return statement.executeQuery(stringQuery);
	}
	
	public ResultSet selectAll() throws SQLException {
		System.out.println("Executing a query");
		
		return statement.executeQuery("select * from scores order by score desc");
	}
	
}
	