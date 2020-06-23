package hsql_db;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SelectQuery {
	public static void main(String[] args) {
		final String DB_NAME = "UniLinkDB";
		final String TABLE_NAME = "EVENT";
		
		//use try-with-resources Statement
		try (Connection con = ConnectionTest.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = "SELECT * FROM " + TABLE_NAME;
			
			try (ResultSet resultSet = stmt.executeQuery(query)) {
				while(resultSet.next()) {
//					System.out.printf("Id: %d | Student Number: %s | First Name: %s | Last Name: %s\n",
//							resultSet.getInt("id"), resultSet.getString("student_number"),
//							resultSet.getString("first_name"), resultSet.getString("last_name"));
					System.out.printf("event_id: %s | title: %s | description: %s | creator_id: %s | venue: %s | photo_path: %s | event_date %s | capacity: %s | attendee_count %s\n",
							resultSet.getString("event_id"),
							resultSet.getString("title"),
							resultSet.getString("description"),
							resultSet.getString("creator_id"),
							resultSet.getString("venue"),
							resultSet.getString("photo_path"),
							resultSet.getString("event_date"),
							resultSet.getString("capacity"),
							resultSet.getString("attendee_count"));
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
