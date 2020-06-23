package hsql_db;
import java.sql.Connection;
import java.sql.Statement;

public class InsertRow {
	public static void main(String[] args) {
		final String DB_NAME = "UniLinkDB";
		final String TABLE_NAME = "EVENT";
		
		//use try-with-resources Statement
		try (Connection con = ConnectionTest.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = "INSERT INTO " + TABLE_NAME
					+ " VALUES (1,'EVE001')"
			+ " VALUES (2,'EVE001')"
			+ " VALUES (3,'EVE001')"
			+ " VALUES (4,'EVE001')"+ " VALUES (5,'EVE001')"+ " VALUES (6,'EVE001')"
			+ " VALUES (7,'EVE001')"
			+ " VALUES (8,'EVE001')"
					+ " VALUES (9,'EVE001')";



					//" VALUES (2, 's3089940', 'Tom', 'Bruster')";

			int result = stmt.executeUpdate(query);
			
			con.commit();
			
			System.out.println("Insert into table " + TABLE_NAME + " executed successfully");
			System.out.println(result + " row(s) affected");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
