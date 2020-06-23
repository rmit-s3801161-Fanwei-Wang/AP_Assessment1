package model;

import hsql_db.ConnectionTest;
import javafx.scene.media.EqualizerBand;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class UniLinkDatabase {
    private static final String DB_NAME = "UniLinkDB";
    private static final String EVENT_TABLE_NAME = "EVENT";
    private static final String SALE_TABLE_NAME = "SALE";
    private static final String JOB_TABLE_NAME = "JOB";
    private static final String REPLY_TABLE_NAME = "REPLY";
    private static final String UNIQUE_ID_TABLE_NAME = "uniqueID";
    private static Connection con = null;

    private static HashMap<String, Post> posts = new HashMap<>();
    private static HashMap<String, Event> events = new HashMap<>();
    private static HashMap<String, Sale> sales = new HashMap<>();
    private static HashMap<String, Job> jobs = new HashMap<>();
    private static ArrayList<Reply> replies = new ArrayList<>();

    public static HashMap<String, Post> getPosts() {
        return posts;
    }

    public static HashMap<String, Event> getEvents() {
        return events;
    }

    public static HashMap<String, Sale> getSales() {
        return sales;
    }

    public static HashMap<String, Job> getJobs() {
        return jobs;
    }

    public static ArrayList<Reply> getReplies() {
        return replies;
    }


    public static void connectDB()  {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            con = DriverManager.getConnection
                    ("jdbc:hsqldb:file:database/" + DB_NAME, "SA", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //pass data to HashMap

        try {
            selectQuery(EVENT_TABLE_NAME);
            selectQuery(SALE_TABLE_NAME);
            selectQuery(JOB_TABLE_NAME);
            selectQuery(UNIQUE_ID_TABLE_NAME);
            selectQuery(REPLY_TABLE_NAME);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void insertRow(String tableName) throws SQLException {
        if (tableName == EVENT_TABLE_NAME) {
            insertEvent();
        } else if (tableName == SALE_TABLE_NAME) {
            insertSale();
        } else if (tableName == JOB_TABLE_NAME) {
            insertJob();
        } else if (tableName == UNIQUE_ID_TABLE_NAME) {
            insertUniqueID();
        } else if (tableName == REPLY_TABLE_NAME) {
            insertReply();
        }
    }

    private static void insertEvent() throws SQLException {
        for (Post post : posts.values()) {
            if (post instanceof Event)
                events.put(post.getId(), (Event) post);
        }

        for (Event event : events.values()) {
            String query = "INSERT INTO " + EVENT_TABLE_NAME + " VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, event.getId().trim());
            ps.setString(2, event.getTitle().trim());
            ps.setString(3, event.getDescription().trim());
            ps.setString(4, event.getCreatorId().trim());
            ps.setString(5, event.getStatus().trim());
            ps.setString(6, event.getPhotoPath());
            ps.setString(7, event.getVenue().trim());
            ps.setString(8, event.getDate().trim());
            ps.setInt(9, event.getCapacity());

            int result = ps.executeUpdate();

            con.commit();

            System.out.println("Insert into table " + EVENT_TABLE_NAME + " executed successfully");
            System.out.println(result + " row(s) affected");
        }
    }

    private static void insertReply() throws SQLException {
        for (Reply reply : replies) {

            String query = "INSERT INTO " + REPLY_TABLE_NAME + " VALUES (?,?,?)";


            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, reply.getPostId());
            ps.setDouble(2, reply.getValue());
            ps.setString(3, reply.getResponderId());

            int result = ps.executeUpdate();

            con.commit();

            System.out.println("Insert into table " + REPLY_TABLE_NAME + " executed successfully");
            System.out.println(result + " row(s) affected");
        }
    }

    private static void insertUniqueID() throws SQLException {
        int eventUniqueID = Event.getUniqueID();
        int saleUniqueID = Sale.getUniqueID();
        int jobUniqueID = Job.getUniqueID();

        String query = "INSERT INTO " + UNIQUE_ID_TABLE_NAME + " VALUES (?,?,?,?)";

        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, 0);
        ps.setInt(2, eventUniqueID);
        ps.setInt(3, saleUniqueID);
        ps.setInt(4, jobUniqueID);

        int result = ps.executeUpdate();

        con.commit();

        System.out.println("Insert into table " + UNIQUE_ID_TABLE_NAME + " executed successfully");
        System.out.println(result + " row(s) affected");
    }

    private static void insertJob() throws SQLException {
        for (Post post : posts.values()) {
            if (post instanceof Job)
                jobs.put(post.getId(), (Job) post);
        }

        for (Job job : jobs.values()) {
            String query = "INSERT INTO " + JOB_TABLE_NAME + " VALUES (?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, job.getId().trim());
            ps.setString(2, job.getTitle().trim());
            ps.setString(3, job.getDescription().trim());
            ps.setString(4, job.getCreatorId().trim());
            ps.setString(5, job.getStatus().trim());
            ps.setString(6, job.getPhotoPath());
            ps.setDouble(7, job.getProposedPrice());


            int result = ps.executeUpdate();

            con.commit();

            System.out.println("Insert into table " + JOB_TABLE_NAME + " executed successfully");
            System.out.println(result + " row(s) affected");
        }
    }

    private static void insertSale() throws SQLException {
        for (Post post : posts.values()) {
            if (post instanceof Sale)
                sales.put(post.getId(), (Sale) post);
        }

        for (Sale sale : sales.values()) {
            String query = "INSERT INTO " + SALE_TABLE_NAME + " VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, sale.getId().trim());
            ps.setString(2, sale.getTitle().trim());
            ps.setString(3, sale.getDescription().trim());
            ps.setString(4, sale.getCreatorId().trim());
            ps.setString(5, sale.getStatus().trim());
            ps.setString(6, sale.getPhotoPath());
            ps.setDouble(7, sale.getAskingPrice());
            ps.setDouble(8, sale.getMinRaise());

            int result = ps.executeUpdate();

            con.commit();

            System.out.println("Insert into table " + SALE_TABLE_NAME + " executed successfully");
            System.out.println(result + " row(s) affected");
        }
    }


    public static void selectQuery(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName;
        Statement stmt = con.createStatement();
        try (ResultSet resultSet = stmt.executeQuery(query)) {
            while (resultSet.next()) {
                if (tableName == EVENT_TABLE_NAME) {
                    Event event = new Event(resultSet.getString("event_id"), resultSet.getString("title"),
                            resultSet.getString("description"), resultSet.getString("creator_id"),
                            resultSet.getString("status"), resultSet.getString("photo_path"),
                            resultSet.getString("venue"), resultSet.getString("event_date"),
                            resultSet.getInt("capacity"));
                    posts.put(event.getId(), event);
                    events.put(event.getId(), event);
                } else if (tableName == SALE_TABLE_NAME) {
                    Sale sale = new Sale(resultSet.getString("sale_id"), resultSet.getString("title"),
                            resultSet.getString("description"), resultSet.getString("creator_id"),
                            resultSet.getString("status"), resultSet.getString("photo_path"),
                            resultSet.getDouble("asking_price"), resultSet.getDouble("minimal_raise"));
                    posts.put(sale.getId(), sale);
                    sales.put(sale.getId(), sale);
                } else if (tableName == JOB_TABLE_NAME) {
                    Job job = new Job(resultSet.getString("job_id"), resultSet.getString("title"), resultSet.getString("description"),
                            resultSet.getString("creator_id"), resultSet.getString("status"),
                            resultSet.getString("photo_path"), resultSet.getDouble("proposed_price"));
                    posts.put(job.getId(), job);
                    jobs.put(job.getId(), job);
                } else if (tableName == UNIQUE_ID_TABLE_NAME) {
                    Event.setUniqueID(resultSet.getInt("event_unique_id"));
                    Sale.setUniqueID(resultSet.getInt("sale_unique_id"));
                    Job.setUniqueID(resultSet.getInt("job_unique_id"));
                } else if (tableName == REPLY_TABLE_NAME) {
                    Reply reply = new Reply(resultSet.getString("post_ID"), resultSet.getDouble("value"), resultSet.getString("responder_ID"));
                    //maintain replies of each post
                    maintainPostReply(reply);
                    replies.add(reply);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void maintainPostReply(Reply reply) {
        //count the number of people who joined this event
        if (reply.getPostId().toUpperCase().contains("EVE")) {
            ((Event) UniLinkDatabase.getPosts().get(reply.getPostId())).increaseAttendeeCount();

        } else if (reply.getPostId().toUpperCase().contains("SAL")) {//Update the highest offer of this sale post
            if (((Sale) UniLinkDatabase.posts.get(reply.getPostId())).getHighestOffer() < reply.getValue())
                ((Sale) UniLinkDatabase.posts.get(reply.getPostId())).setHighestOffer(reply.getValue());

        } else if (reply.getPostId().toUpperCase().contains("JOB")) {//update the lowest offer of this job post
            //set first offer as the lowest offer
            if (((Job) UniLinkDatabase.posts.get(reply.getPostId())).getLowestOffer() == 0)
                ((Job) UniLinkDatabase.posts.get(reply.getPostId())).setLowestOffer(reply.getValue());

            if (((Job) UniLinkDatabase.posts.get(reply.getPostId())).getLowestOffer() > reply.getValue())
                ((Job) UniLinkDatabase.posts.get(reply.getPostId())).setLowestOffer(reply.getValue());
        }
    }

    private static void delete(String tableName) {
        final String DB_NAME = "UniLinkDB";
        final String TABLE_NAME = tableName;

        //use try-with-resources Statement
        try (Connection con = ConnectionTest.getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            int result = stmt.executeUpdate("DROP TABLE " + TABLE_NAME);

            if (result == 0) {
                System.out.println("Table " + TABLE_NAME + " has been deleted successfully");
            } else {
                System.out.println("Table " + TABLE_NAME + " was not deleted");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void update() {
        delete(EVENT_TABLE_NAME);
        delete(SALE_TABLE_NAME);
        delete(JOB_TABLE_NAME);
        delete(UNIQUE_ID_TABLE_NAME);
        delete(REPLY_TABLE_NAME);

        createEventTable();
        createSaleTable();
        createJobTable();
        createIDTable();
        createReplyTable();



        try {
            UniLinkDatabase.insertRow("EVENT");
            UniLinkDatabase.insertRow("SALE");
            UniLinkDatabase.insertRow("JOB");
            UniLinkDatabase.insertRow("uniqueID");
            UniLinkDatabase.insertRow("REPLY");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static void createEventTable() {

        final String DB_NAME = "UniLinkDB";
        final String TABLE_NAME = EVENT_TABLE_NAME;

        //use try-with-resources Statement
        try (Connection con = ConnectionTest.getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            int result = stmt.executeUpdate("CREATE TABLE EVENT("
                    + "event_id VARCHAR(100)  NOT NULL,"
                    + "title VARCHAR(100) NOT NULL,"
                    + "description VARCHAR(100) NOT NULL,"
                    + "creator_id VARCHAR(100) NOT NULL,"
                    + "status VARCHAR(20) NOT NULL,"
                    + "photo_path VARCHAR(100) NOT NULL,"
                    + "venue VARCHAR(100) NOT NULL,"
                    + "event_date VARCHAR(20) NOT NULL,"
                    + "capacity INT NOT NULL,"
                    + "PRIMARY KEY (event_id))");
            if (result == 0) {
                System.out.println("Table " + TABLE_NAME + " has been created successfully");
            } else {
                System.out.println("Table " + TABLE_NAME + " is not created");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createSaleTable() {

        final String DB_NAME = "UniLinkDB";
        final String TABLE_NAME = SALE_TABLE_NAME;

        //use try-with-resources Statement
        try (Connection con = ConnectionTest.getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            int result = stmt.executeUpdate("CREATE TABLE sale("
                    + "sale_id VARCHAR(100)  NOT NULL,"
                    + "title VARCHAR(100) NOT NULL,"
                    + "description VARCHAR(100) NOT NULL,"
                    + "creator_id VARCHAR(100) NOT NULL,"
                    + "status VARCHAR(20) NOT NULL,"
                    + "photo_path VARCHAR(200) NOT NULL,"
                    + "asking_price DOUBLE NOT NULL,"
                    + "minimal_raise DOUBLE NOT NULL,"
                    + "PRIMARY KEY (sale_id))");
            if (result == 0) {
                System.out.println("Table " + TABLE_NAME + " has been created successfully");
            } else {
                System.out.println("Table " + TABLE_NAME + " is not created");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createJobTable() {

        final String DB_NAME = "UniLinkDB";
        final String TABLE_NAME = JOB_TABLE_NAME;

        //use try-with-resources Statement
        try (Connection con = ConnectionTest.getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            int result = stmt.executeUpdate("CREATE TABLE job("
                    + "job_id VARCHAR(100)  NOT NULL,"
                    + "title VARCHAR(100) NOT NULL,"
                    + "description VARCHAR(100) NOT NULL,"
                    + "creator_id VARCHAR(100) NOT NULL,"
                    + "status VARCHAR(20) NOT NULL,"
                    + "photo_path VARCHAR(200) NOT NULL,"
                    + "proposed_price DOUBLE NOT NULL,"
                    + "PRIMARY KEY (job_id))");
            if (result == 0) {
                System.out.println("Table " + TABLE_NAME + " has been created successfully");
            } else {
                System.out.println("Table " + TABLE_NAME + " is not created");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createIDTable() {

        final String DB_NAME = "UniLinkDB";
        final String TABLE_NAME = UNIQUE_ID_TABLE_NAME;

        //use try-with-resources Statement
        try (Connection con = ConnectionTest.getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            int result = stmt.executeUpdate("CREATE TABLE uniqueID ("
                    + "id INT IDENTITY NOT NULL,"
                    + "event_unique_id INT NOT NULL,"
                    + "sale_unique_id INT NOT NULL,"
                    + "job_unique_id INT NOT NULL,"
                    + "PRIMARY KEY (id))");
            if (result == 0) {
                System.out.println("Table " + TABLE_NAME + " has been created successfully");
            } else {
                System.out.println("Table " + TABLE_NAME + " is not created");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createReplyTable() {

        final String DB_NAME = "UniLinkDB";
        final String TABLE_NAME = REPLY_TABLE_NAME;

        //use try-with-resources Statement
        try (Connection con = ConnectionTest.getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            int result = stmt.executeUpdate("CREATE TABLE reply("
                    + "post_ID VARCHAR(20) NOT NULL,"
                    + "value DOUBLE NOT NULL,"
                    + "responder_ID VARCHAR(20) NOT NULL,"
                    + "PRIMARY KEY (post_ID,responder_ID))");
            if (result == 0) {
                System.out.println("Table " + TABLE_NAME + " has been created successfully");
            } else {
                System.out.println("Table " + TABLE_NAME + " is not created");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public static void closeConnection() throws SQLException {
        con.close();
    }


}
