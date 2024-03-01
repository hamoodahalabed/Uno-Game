import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        String[] stringArray = {"Mohammad", "Sarah", "Jad" , "osama"};

        Game myGame = new MyGame(stringArray);

        myGame.play(myGame);
    }
}