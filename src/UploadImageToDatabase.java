import java.io.*;
import java.sql.*;

public class UploadImageToDatabase {
    public static void main(String[] args) {
        /*
        try {
            createTable();
            insertImage("C:\\Users\\filip\\Desktop\\datei.jpg");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

         */
        try {
            update("C:\\Users\\filip\\Desktop\\Leitprogramme, falls mal langweilig ist.png");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection connection(String database){
        Connection connection =null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + database + ".db");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return connection;
    }

    // TAbelle erstellen
    private static void createTable() throws SQLException {
        Connection connection = connection("C:\\Users\\filip\\Desktop\\Datenbank");
        String sql = "CREATE TABLE IF NOT EXISTS imagetest " + "(ID INT PRIMARY KEY NOT NULL, Photo BLOB)";

        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(sql);



        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            statement.close();
            connection.close();
        }
    }

    //Bild laden
    private static void insertImage(String filePath) throws IOException, SQLException {
        FileInputStream fileInputStream = null; //File lesen
        File image = new File(filePath);

        fileInputStream = new FileInputStream(image); //Exception möglich

        //Schreiben der Datei in der Datenbank
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        for (int readNum; (readNum = fileInputStream.read(buf)) != -1;){ //Spezielle For-Schleife, der inputstream liest das File, bis -1 erreicht wird
            byteArrayOutputStream.write(buf, 0, readNum); //Readnum folge von 0 und 1 usw...

        }
        fileInputStream.close();

        int num_rows = 0;//ANzahl der Druchgänge der Anfrage

        //Verbindung zur Datenbank
        Connection connection = connection("C:\\Users\\filip\\Desktop\\Datenbank");
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO imagetest (ID, Photo) VALUES (1, ?)"); //Prepared ist sicherer...Der Nutzer kann auf den SQL-Statment Zugriff haben und manipulieren, deswegen ?

        preparedStatement.setBytes(1, byteArrayOutputStream.toByteArray());
        num_rows = preparedStatement.executeUpdate();

        if (num_rows >0){
            System.out.println("Image hochgeladen");
        }

        preparedStatement.close();
        connection.close();
    }

    //Update des Image
    private static void update(String filePath) throws IOException, SQLException {
        File image = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(image);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[1024]; //typishce Grösse
        for (int readNum; (readNum = fileInputStream.read(buf)) !=-1;){
            byteArrayOutputStream.write(buf, 0, readNum);

        }

        fileInputStream.close();

        int num_rows = 0;//ANzahl der Druchgänge der Anfrage

        //Verbindung zur Datenbank
        Connection connection = connection("C:\\Users\\filip\\Desktop\\Datenbank");
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE imagetest SET Photo = ? WHERE id =1"); //Prepared ist sicherer...Der Nutzer kann auf den SQL-Statment Zugriff haben und manipulieren, deswegen ?

        preparedStatement.setBytes(1, byteArrayOutputStream.toByteArray()); //An der Stelle dort oben, diesen Wert hier einfügen...
        num_rows = preparedStatement.executeUpdate();

        if (num_rows >0){
            System.out.println("ImageUpdate erfolgreich");
        }

        preparedStatement.close();
        connection.close();
    }

}
