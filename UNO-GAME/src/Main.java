// mohammad al-abed

public class Main {
    public static void main(String[] args) throws Exception {

        // player name
        String[] stringArray = {"Mohammad", "Sarah", "Jad" , "osama"};

        Game myGame = new MyGame(stringArray);

        myGame.play(myGame);
    }
}