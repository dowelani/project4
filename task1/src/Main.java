import Database.Database;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Database database=new Database();
        if(database.isValidConnection()){
            Scanner input=new Scanner(System.in);
            display();
            int userInput= input.nextInt();
            while (userInput!=6){
                if(userInput==1){
                    System.out.println("Enter album ID");
                    int aID = input.nextInt();
                    System.out.println("Enter album Title");
                    String title = input.next();
                    System.out.println("Enter released Year");
                    int year = input.nextInt();
                    database.createNewAlbum(aID, title, year); System.out.println();
                }
                else if(userInput==2){
                    System.out.println("Enter album id");
                    int aID = input.nextInt();
                    System.out.println("Enter Song number");
                    int TNum = input.nextInt();
                    System.out.println("Enter song title");
                    String name = input.next();
                    System.out.println("Enter artist name");
                    String artist = input.next();
                    System.out.println("Enter song length");
                    float length=input.nextFloat();
                    database.createNewSong(aID, TNum,name, artist,length); System.out.println();
                }
                else if(userInput==3){
                    System.out.println("Enter the specific word");
                    String word = input.next();
                    database.specificWordAlbums(word); System.out.println();
                }
                else if(userInput==4){
                    System.out.println("Enter the specific word");
                    String word = input.next();
                    database.specificWordSongs(word);
                    System.out.println();
                }
                else if(userInput==5){
                    System.out.println("Enter artist name");
                    String artist = input.next();
                    database.queryForArtist(artist);
                    System.out.println();
                }else{ System.out.println("invalid entry, enter number from 1-6");System.out.println();}
                display();
                userInput= input.nextInt();
            }
            database.disconnectDB();

        }
    }
    static  void  display(){
        System.out.println("Select an option 1-6");
        System.out.println("1.Upload album");
        System.out.println("2.Upload song.");
        System.out.println("3.Get albums with a specific word");
        System.out.println("4.Get songs with a specific word");
        System.out.println("5.Display songs and their album for a particular artist");
        System.out.println("6.Close the app");
    }
}
