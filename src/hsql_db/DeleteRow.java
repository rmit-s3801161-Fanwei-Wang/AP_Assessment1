package hsql_db;
import java.sql.Connection;
import java.sql.Statement;

public class DeleteRow {
	public static void main(String[] args) {
		final String DB_NAME = "UniLinkDB";
		final String TABLE_NAME = "EVENT";
		
		//use try-with-resources Statement
		try (Connection con = ConnectionTest.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = "DELETE FROM " + TABLE_NAME + 
					" WHERE event_id LIKE 'EVE003'";
			
			int result = stmt.executeUpdate(query);
			
			System.out.println("Delete from table " + TABLE_NAME + " executed successfully");
			System.out.println(result + " row(s) affected");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
