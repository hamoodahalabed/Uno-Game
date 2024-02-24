public class MyGame extends Game {
    public MyGame(String[] pids) throws Exception {
        super(pids);
        // Additional initialization specific to MyGame
    }

    @Override
    public boolean validCardPlay(UnoCard card) {
        // Override if necessary for additional rules in MyGame
        return super.validCardPlay(card);
    }

    @Override
    public void submitPlayerCard(String pid, UnoCard card, UnoCard.Color declaredColor) throws Exception {
        // Implement specific behavior for card submission in MyGame
        super.submitPlayerCard(pid, card, declaredColor);

        // Add any additional logic if needed
    }

    @Override
    public void submitDraws (String playerId) throws Exception {

       super.submitDraws(getCurrnetPlayer());
       super.submitDraws(getCurrnetPlayer());
       System.out.println("you draw to card instead of one. ");

    }
    // Add more overrides or additional methods as necessary for MyGame
}
