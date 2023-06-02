package Database;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.sql.*;
import java.util.Scanner;
public class Database {
    private Connection con;
    private Statement stmt;
    private boolean isValidConnection;
    public Database() {isValidConnection=false;con=null;stmt=null;userAuthentication();}
    public void userAuthentication(){
        Scanner input=new Scanner(System.in);
        System.out.println("Enter username");
        String username=input.next();
        System.out.println("Enter password ");
        String password= input.next();
        connectToDatabase(username,password);
    }

    public void connectToDatabase(String username,String password){
    System.out.println("Establishing connection to database...");

    try {
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
    } catch (Exception e) {
        System.out.printf("   Unable to load JDBC driver... '%s'\n", e.getMessage());
        return;
    }

    if (true) {
        try {

            String connectionString = "jdbc:jtds:sqlserver://postsql.mandela.ac.za/WRAP301Music;instance=WRR";

            con = DriverManager.getConnection(connectionString, username, password);

            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            isValidConnection=true;
            System.out.println("Connection to database established ");
        } catch (Exception e) {
            System.out.printf("   Unable to connect to DB... '%s'\n", e.getMessage());
        }
    } else {
    try {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser(username);
        ds.setPassword(password);
        ds.setServerName("postsql.mandela.ac.za");
        ds.setInstanceName("WRR");
        ds.setDatabaseName("WRAP301Music");
        con = ds.getConnection();
        stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
}

    public boolean isValidConnection() {
        return isValidConnection;
    }

    public void createNewAlbum(int aID,String title,int year) {

        try
        {
            PreparedStatement insertStatement=con.prepareStatement("INSERT INTO Album VALUES(?,?,?)");
            insertStatement.setInt(1,aID);
            insertStatement.setString(2,title);
            insertStatement.setInt(3,year);
            insertStatement.executeUpdate();
            System.out.println(" Album uploaded");
        }
        catch (Exception e)
        {
            System.out.println("unable to upload album ..."+e.getMessage());
        }
    }

    public void createNewSong(int aID,int songNo,String name,String artist,float length) {
        try
        {
            PreparedStatement insertStatement=con.prepareStatement("INSERT INTO Track(AID,SongNo,Name,Artist,Length) VALUES(?,?,?,?,?)");

            insertStatement.setInt(1,aID);
            insertStatement.setInt(2,songNo);
            insertStatement.setString(3,name);
            insertStatement.setString(4,artist);
            insertStatement.setFloat(5,length);
            if(isValidAlbum(aID))
            {
                insertStatement.executeUpdate();
                System.out.println(" Song was uploaded");
            }
            else
                System.out.println("Song was not uploaded");
        }
        catch (Exception e)
        {
            System.out.println("unable to upload song ..."+e.getMessage());
        }
    }

    private boolean isValidAlbum(int aID) throws SQLException {
        boolean exists=false;
        String sql="SELECT AID FROM Album WHERE AID="+aID;
        ResultSet results=stmt.executeQuery(sql);
        if (results.next()){
            exists=true;
        }
        return exists;
    }

    public void queryForArtist(String artist) throws SQLException {
        System.out.println();
        String sql = "SELECT Track.Name,Album.Title FROM Track INNER JOIN Album ON Track.AID=Album.AID WHERE Track.Artist='" + artist + "'";
        ResultSet results = stmt.executeQuery(sql);
        System.out.println("Song name and its album by artist: "+artist);
        System.out.println("Song Name" + "\tAlbum" );

        try {
            while (results.next()) {

                String songName = results.getString("Name");
                String albumTitle = results.getString("Title");
                System.out.println(songName + "\t" + albumTitle);
            }

        } catch (Exception e) {
            System.out.println("unable to query for artist");
        }
    }

    public void specificWordSongs(String word) {
        System.out.println();
        try
        {
            String sql="SELECT Track.Name FROM Track WHERE Name LIKE '%"+word+"%'";
            ResultSet results=stmt.executeQuery(sql);
            System.out.println("Songs with "+word+" in their titles:");
            while (results.next())
            {
                String name=results.getString("Name");
                System.out.println(name);

            }

        }
        catch (Exception e)
        {
            System.out.println("unable to query for Songs");
        }
    }

    public void specificWordAlbums(String word) {
        System.out.println();
        try
        {
            String sqlStatement="SELECT Album.Title FROM Album WHERE Title LIKE '%"+word+"%'";
            ResultSet results=stmt.executeQuery(sqlStatement);
            System.out.println("albums with "+word+" in their titles:");
            while (results.next())
            {
                String title=results.getString("Title");
                System.out.println(title);
            }

        }
        catch (Exception e)
        {
            System.out.println("unable to query for albums");
        }
    }

    public void disconnectDB() {
        System.out.println("Disconnecting from database...");

        try {
            con.close();
            System.out.println("Disconnected from database...");
        } catch (Exception ex) {
            System.out.println("   Unable to disconnect from database");
        }
    }

}
