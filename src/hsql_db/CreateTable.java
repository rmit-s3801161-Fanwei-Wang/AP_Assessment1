package hsql_db;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
	public static void main(String[] args) throws SQLException {
		
		final String DB_NAME = "UniLinkDB";
		final String TABLE_NAME = "uniqueID";
		
		//use try-with-resources Statement
		try (Connection con = ConnectionTest.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
//			int result = stmt.executeUpdate("CREATE TABLE student ("
//					+ "id INT NOT NULL,"
//					+ "student_number VARCHAR(8) NOT NULL,"
//					+ "first_name VARCHAR(20) NOT NULL,"
//					+ "last_name VARCHAR(20) NOT NULL,"
//					+ "PRIMARY KEY (id))");
			//String postId, double value, String responderId
//			int result = stmt.executeUpdate("CREATE TABLE reply("
//					+ "post_ID VARCHAR(20) NOT NULL,"
//					+ "value DOUBLE NOT NULL,"
//					+ "responder_ID VARCHAR(20) NOT NULL,"
//					+ "PRIMARY KEY (post_ID,responder_ID))");
//			int result = stmt.executeUpdate("CREATE TABLE uniqueID ("
//					+ "id INT IDENTITY NOT NULL,"
//					+ "event_unique_id INT NOT NULL,"
//					+ "sale_unique_id INT NOT NULL,"
//					+ "job_unique_id INT NOT NULL,"
//					+ "PRIMARY KEY (id))");
			int result = stmt.executeUpdate("CREATE TABLE job("
					+ "job_id VARCHAR(100)  NOT NULL,"
					+ "title VARCHAR(100) NOT NULL,"
					+ "description VARCHAR(100) NOT NULL,"
					+ "creator_id VARCHAR(100) NOT NULL,"
					+ "status VARCHAR(20) NOT NULL,"
					+ "photo_path VARCHAR(200) NOT NULL,"
					+ "proposed_price DOUBLE NOT NULL,"
					+ "PRIMARY KEY (job_id))");
//			int result = stmt.executeUpdate("CREATE TABLE sale("
//										+ "sale_id VARCHAR(100)  NOT NULL,"
//										+ "title VARCHAR(100) NOT NULL,"
//										+ "description VARCHAR(100) NOT NULL,"
//										+ "creator_id VARCHAR(100) NOT NULL,"
//										+ "status VARCHAR(20) NOT NULL,"
//										+ "photo_path VARCHAR(200) NOT NULL,"
//										+ "asking_price DOUBLE NOT NULL,"
//										+ "minimal_raise DOUBLE NOT NULL,"
//										+ "PRIMARY KEY (sale_id))");
//			int result = stmt.executeUpdate("CREATE TABLE EVENT("
//					+ "event_id VARCHAR(100)  NOT NULL,"
//					+ "title VARCHAR(100) NOT NULL,"
//					+ "description VARCHAR(100) NOT NULL,"
//					+ "creator_id VARCHAR(100) NOT NULL,"
//					+ "status VARCHAR(20) NOT NULL,"
//					+ "photo_path VARCHAR(100) NOT NULL,"
//					+ "venue VARCHAR(100) NOT NULL,"
//					+ "event_date VARCHAR(20) NOT NULL,"
//					+ "capacity INT NOT NULL,"
//					+ "PRIMARY KEY (event_id))");
			if(result == 0) {
				System.out.println("Table " + TABLE_NAME + " has been created successfully");
			} else {
				System.out.println("Table " + TABLE_NAME + " is not created");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
