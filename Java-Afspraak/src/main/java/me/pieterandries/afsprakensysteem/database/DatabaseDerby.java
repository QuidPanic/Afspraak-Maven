package me.pieterandries.afsprakensysteem.database;
import java.sql.*;


public  final class DatabaseDerby {
    //creating handler als datatype DatabaseDerby
    private static DatabaseDerby handler;

    //Deze string DB_URL zorgt er voor dat de me.pieterandries.afsprakensysteem.database wordt opgeslagen als folder genaamd me.pieterandries.afsprakensysteem.database
    private static final String DB_URL = "jdbc:derby:me.pieterandries.afsprakensysteem.database;create=true";

    private static Connection dbConnection = null; //creating Connection dbConnection
    private static Statement statement = null; //creating statement statement



    //constructor om connectie/tabel aan te maken
    private DatabaseDerby(){
        createConnection();
        setupAfspraakTabel();
        setupKlantTabel();
    }

    //we kunnen deze method gebruiken om een instance van dit object in andere classes te gebruiken
    public static DatabaseDerby getInstance() {
        if (handler==null)  {
            handler = new DatabaseDerby();
        }
        return handler;
    }

    private void setupKlantTabel() {
        String TABLE_NAME = "KLANT";
        try {
            statement = dbConnection.createStatement();
            DatabaseMetaData dbm = dbConnection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            //Als TABLE_NAME bestaat wordt deze niet aangemaakt als deze niet bestaat wordt hij bij else aangemaakt hiervoor wordt de if else gebruikt
            if (tables.next()) {
                System.out.println("Tabel " + TABLE_NAME + " bestaat al. We kunnen starten");
            } else {
                // hier wordt er daadwerkelijk een tabel aangemaakt met de velden naam,telefoonnummer,email,behandeling, afspraakdatum, afspraakid
                statement.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "     naam varchar(200) ,\n"       //maximaal 200 characters
                        + "     id varchar(200) primary key ,\n" //Maximaal 200 characters
                        + "     tel varchar(200) ,\n "        //MAximaal 200 characters
                        + "     email varchar(100) ,\n"        //MAximaal 200 characters
                        + "     woonplaats varchar(200) " //MAximaal 200 characters
                        + ")");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " --- setupDatabase");
        }
    }

    //connectie maken met java applicatie en de jdbc er wordt gebruik gemaakt van exception (try catch)
    void createConnection(){
        try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            dbConnection = DriverManager.getConnection(DB_URL); // hier wordt dbConnection een waarde gegeven om een connectie met de me.pieterandries.afsprakensysteem.database op te bouwen wanneer we de programma runnen.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //tabel maken in de me.pieterandries.afsprakensysteem.database voor afspraken de naam wordt zoals aangeven door TABLE_NAME AFSPRAAK
   void setupAfspraakTabel() {
        String TABLE_NAME = "AFSPRAAK";
       try {
           statement = dbConnection.createStatement();
           DatabaseMetaData dbm = dbConnection.getMetaData();
           ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
           //Als TABLE_NAME bestaat wordt deze niet aangemaakt als deze niet bestaat wordt hij bij else aangemaakt hiervoor wordt de if else gebruikt
           if (tables.next()) {
               System.out.println("Tabel " + TABLE_NAME + " bestaat al. We kunnen starten");
           } else {
               // hier wordt er daadwerkelijk een tabel aangemaakt met de velden naam,telefoonnummer,email,behandeling,afspraakdatum, afspraakid
               statement.execute("CREATE TABLE " + TABLE_NAME + "("
                       + "     behandeling varchar(100),\n"       //maximaal 100 characters
                       + "     afspraakDatum varchar(100),\n" //Maximaal 100 characters
                       + "     id varchar(100)primary key, \n "        //MAximaal 100 characters
                       + "     klantid varchar(200) REFERENCES KLANT(id) ON DELETE CASCADE" //MAximaal 100 characters
                       + ")");
           }
       } catch (SQLException e) {
           System.err.println(e.getMessage() + " --- setupDatabase");
       }
   }
    public ResultSet execQuery (String query){
        ResultSet result;
        try {
            statement = dbConnection.createStatement();
            result = statement.executeQuery(query);
        } catch (SQLException ex){
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        }
        return result;
    }

    public boolean execAction(PreparedStatement pushStatement) {
        try {
            pushStatement.executeUpdate();
            return true;
        } catch (SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    public static Boolean doesUserExists(String id) throws SQLException {
        PreparedStatement checkKlantID = getDbConnection().prepareStatement("SELECT * FROM KLANT WHERE id = ?");
        checkKlantID.setString(1, id);
        Boolean result = false;
        try (ResultSet rs = checkKlantID.executeQuery()){
            result = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Connection getDbConnection() {
        return dbConnection;
    }
    public static Statement getStatement() { return statement; }

}
