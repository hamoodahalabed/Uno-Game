import java.util.Scanner;

public class MyGame extends Game {
    public MyGame(String[] pids) throws Exception {
        super(pids);
        // Additional initialization specific to MyGame
    }

    @Override
    public void play(Game game) {
        Scanner scanner = new Scanner(System.in);
        while (!game.isGameOver()) {
            System.out.println("\n***************************************************************************");
            System.out.println("Current Player: " + game.getCurrnetPlayer());
            System.out.println("Top Card: " + game.getTopCard());
            game.printPlayerHand(game.getCurrnetPlayer());
            System.out.println("Enter the index of the card to play (0 to " + (game.getPlayerHandSize(game.getCurrnetPlayer()) - 1) + "), or '+' to draw a card:");

            String input = scanner.next();
            if (input.equals("+")) {
                try {
                    game.submitDraws(game.getCurrnetPlayer());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                continue;
            }

            int choice = Integer.parseInt(input);
            UnoCard chosenCard = game.getPlayerCard(game.getCurrnetPlayer(), choice);

            if (game.validCardPlay(chosenCard)) {
                UnoCard.Color declaredColor = null;
                if (chosenCard.getColor() == UnoCard.Color.WILD) {
                    boolean validInput = false;
                    while (!validInput) {
                        System.out.println("Enter the declared color for the Wild card (RED, BLUE, YELLOW, GREEN): ");
                        String colorInput = scanner.next();
                        try {
                            declaredColor = UnoCard.Color.valueOf(colorInput.toUpperCase());
                            validInput = true;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid color. Please enter a valid color.");
                        }
                    }
                }

                try {
                    game.submitPlayerCard(game.getCurrnetPlayer(), chosenCard, declaredColor);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("Invalid card play. Try again.");
            }

            try {
                game.checkPlayerTurn(game.getCurrnetPlayer());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Game over! Winner: " + game.getCurrnetPlayer());
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
        // Add any additional logic if needed

    }
    // Add more overrides or additional methods as necessary for MyGame
}
