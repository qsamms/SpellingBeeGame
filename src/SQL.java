import java.sql.*;
import java.time.format.DateTimeFormatter;
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
	
	public int getMaxID() throws SQLException{
		StringBuilder query = new StringBuilder();
		query.append("select MAX(id) from scores;");
		String q = query.toString();
		
		ResultSet result = statement.executeQuery(q);
		
		result.next();
		return Integer.parseInt(result.getString("MAX(id)"));
	}
	
	public void insertWords(int id, String[] words) {
		
	}
	
	public void insert(int id, String username, int score, String table) throws SQLException {
		PreparedStatement p;
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		
		StringBuilder query = new StringBuilder();
		query.append("insert into " + table + " values");
		query.append("(" + id + ", '" +  username + "'," + "'" + score + "'" + "," + " '" + dtf.format(now) + "');");
		
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
	
	public static void main(String[] args) {
		SQL sql = new SQL();
		ResultSet result = null;
		
		try {
			result.next();
			System.out.println(result.getString("MAX(id)"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}