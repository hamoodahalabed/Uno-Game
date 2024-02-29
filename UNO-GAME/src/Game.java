import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
public abstract class Game {
    private int currentPlayer;
    private String[] playerIds;
    private UnoDeck deck;
    private ArrayList<ArrayList<UnoCard>> playersHand;
    private ArrayList<UnoCard> stockPile;
    private UnoCard.Color validColor;
    private UnoCard.Value validValue;
    private boolean gameDirection;

    public Game (String[] pids) throws Exception {
        deck = new UnoDeck();
        deck.shuffle();
        stockPile = new ArrayList<UnoCard>();
        playerIds = pids;
        currentPlayer = 0;
        gameDirection = false;
        playersHand = new ArrayList<ArrayList<UnoCard>>();

        for (int i =0; i < pids.length; i++) {
            ArrayList<UnoCard> hand = new ArrayList<UnoCard>(Arrays.asList(deck.drawCard(7)));
            playersHand.add(hand);
        }

        printPlayer();
    }

    public void printPlayer () {
        System.out.print("Players: ");
        for (int i = 0; i < playerIds.length; i++)
            System.out.print(playerIds[i] + ", ");
    }
    public void play (Game game) {
        UnoCard card = null;
        try {
            card = deck.drawCard();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        validColor = card.getColor();
        validValue = card.getValue();

        if (card.getValue() == UnoCard.Value.SKIP) {
            System.out.println(playerIds[currentPlayer] + " was skipped !!!");

            if (gameDirection == false) {
                currentPlayer = (currentPlayer + 1) % playerIds.length;
            }else if (gameDirection == true) {
                currentPlayer = (currentPlayer - 1) % playerIds.length;
                if(currentPlayer == -1) {
                    currentPlayer = playerIds.length - 1;
                }
            }
        }

        if (card.getValue() == UnoCard.Value.REVERSE){
            System.out.println(playerIds[currentPlayer] + " the game direction changed !!!");
            gameDirection ^= true;
            currentPlayer = playerIds.length - 1;
        }
        stockPile.add(card);

        Scanner scanner = new Scanner(System.in);
        while (!game.isGameOver()) {

            System.out.println("\n***************************************************************************");
            System.out.println("Current Player: " + game.getCurrnetPlayer());
            System.out.println("Top Card: " + game.getTopCard());
            game.printPlayerHand(game.getCurrnetPlayer());
            System.out.println("Enter the index of the card to play (0 to " + (game.getPlayerHandSize(game.getCurrnetPlayer()) - 1) + "): ");

            int choice = scanner.nextInt();
            UnoCard chosenCard = game.getPlayerCard(game.getCurrnetPlayer(), choice);

            if (game.validCardPlay(chosenCard)) {

                UnoCard.Color declaredColor = null;
                if (chosenCard.getColor() == UnoCard.Color.WILD) {
                    System.out.println("Enter the declared color for the Wild card (RED, BLUE, YELLOW, GREEN): ");
                    String colorInput = scanner.next();
                    declaredColor = UnoCard.Color.valueOf(colorInput.toUpperCase());
                }

                try {
                    game.submitPlayerCard(game.getCurrnetPlayer(), chosenCard, declaredColor);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }else {
                System.out.println("Invalid card play. Try again.");
            }
            try {
                game.checkPlayerTurn(game.getCurrnetPlayer());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                System.out.println("press + to draw a card :) or 0 to finish your turn or any other button to replay");
                String draw = scanner.next();
                if (draw.equals("+")){
                    game.submitDraws(game.getCurrnetPlayer());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Game over! Winner: " + game.getCurrnetPlayer());
    }
    public UnoCard getTopCard () {
        return new UnoCard(validColor, validValue);
    }
    public boolean isGameOver () {
        for(String player: this.playerIds) {
            if(hasEmptyHand(player)) {
                return true;
            }
        }
        return false;
    }
    private boolean hasEmptyHand(String playerId) {
        return getPlayerHand(playerId).isEmpty();
    }
    public String getCurrnetPlayer() {
        return this.playerIds[this.currentPlayer];
    }
    public String getPreviousPlayer(int i) {
        int index = this.currentPlayer - i;
        if (index == -1) {
            index = playerIds.length - 1;
        }
        return this.playerIds[index];
    }
    public String[] getPLayers () {
        return playerIds;
    }
    public void printPlayerHand(String playerId) {
        int index = Arrays.asList(playerIds).indexOf(playerId);
        ArrayList<UnoCard> hand = playersHand.get(index);

        System.out.print("Your Hand:");
        for (int i = 0; i < hand.size(); i++) {
            UnoCard card = hand.get(i);
            System.out.print("[" + i + "]" + card + ", ");
        }
        System.out.println();
    }

    public ArrayList<UnoCard> getPlayerHand(String playerId) {
        int index = Arrays.asList(playerIds).indexOf((playerId));
        return playersHand.get(index);
    }
    public int getPlayerHandSize(String playerId) {
        return getPlayerHand(playerId).size();
    }
    public UnoCard getPlayerCard (String playerId, int choice) {
        ArrayList<UnoCard> hand = getPlayerHand(playerId);
        return hand.get(choice);
    }
    public boolean validCardPlay (UnoCard card) {
        return card.getColor() == validColor || card.getValue() == validValue || card.getColor() == UnoCard.Color.WILD;
    }
    public void checkPlayerTurn(String playerId) throws Exception {
        if (this.playerIds[this.currentPlayer] != playerId) {
            throw new Exception("it is not " + playerId + " turn");
        }
    }
    public void submitDraws (String playerId) throws Exception {
        checkPlayerTurn(playerId);

        if(deck.isEmpty()) {
            deck.replaceDeckWith(stockPile);
            deck.shuffle();
        }

        getPlayerHand(playerId).add(deck.drawCard());

        if(gameDirection == false) {
            currentPlayer = (currentPlayer + 1) % playerIds.length;
        }

        else if (gameDirection == true) {
            currentPlayer = (currentPlayer - 1) % playerIds.length;
            if (currentPlayer == -1) {
                currentPlayer = playerIds.length - 1;
            }
        }

    }
    public void setCardColor(UnoCard.Color color) {
        validColor = color;
    }
    public void submitPlayerCard (String pid, UnoCard card, UnoCard.Color declaredColor) throws Exception {
        checkPlayerTurn(pid);

        ArrayList<UnoCard> pHand = getPlayerHand(pid);

        if (!validCardPlay(card)) {
            if(card.getColor() == UnoCard.Color.WILD) {
                validColor = card.getColor();
                validValue = card.getValue();
            }

            if(card.getColor() != validColor) {
                System.out.println("not valid player move, expect color: " + validColor);
            }

            else if (card.getValue() != validValue) {
                System.out.println("not valid player move, expect value: " + validValue);
            }
        }

        pHand.remove(card);

        if(hasEmptyHand(this.playerIds[currentPlayer])) {
            System.out.println("won the game !!! thanks for playing");
        }

        validColor = card.getColor();
        validValue = card.getValue();
        stockPile.add(card);

        if (gameDirection == false) {
            currentPlayer = (currentPlayer + 1) % playerIds.length;
        }else {
            currentPlayer = (currentPlayer - 1) % playerIds.length;
            if (currentPlayer == -1) {
                currentPlayer = playerIds.length - 1;
            }
        }

        if (card.getColor() == UnoCard.Color.WILD) {
            validColor = declaredColor;
        }

        if (card.getValue() == UnoCard.Value.DRAW_TOW) {
            pid =playerIds[currentPlayer];
            getPlayerHand(pid).add(deck.drawCard());
            getPlayerHand(pid).add(deck.drawCard());
            System.out.println("drew 2 cards !!!");
        }
        if (card.getValue() == UnoCard.Value.WILD_FOUR) {
            pid =playerIds[currentPlayer];
            getPlayerHand(pid).add(deck.drawCard());
            getPlayerHand(pid).add(deck.drawCard());
            getPlayerHand(pid).add(deck.drawCard());
            getPlayerHand(pid).add(deck.drawCard());
            System.out.println("drew 4 cards !!!");
        }

        if (card.getValue() == UnoCard.Value.SKIP) {
            System.out.println(playerIds[currentPlayer] + " was skipped !!!");
            if (gameDirection == false) {
                currentPlayer = (currentPlayer + 1) % playerIds.length;
            }
            else if (gameDirection == true) {
                currentPlayer = (currentPlayer - 1) % playerIds.length;
                if (currentPlayer == -1) {
                    currentPlayer = playerIds.length -1;
                }
            }
        }

        if (card.getValue() == UnoCard.Value.REVERSE) {
            System.out.println(pid + " Change the game direction !!!");

            gameDirection ^=true;
            if (gameDirection == true) {
                currentPlayer = (currentPlayer - 2) % playerIds.length;
                if(currentPlayer == -1) {
                    currentPlayer = playerIds.length - 1;
                }
                if(currentPlayer == -2) {
                    currentPlayer = playerIds.length - 2;
                }
            }
            else if (gameDirection == false) {
                currentPlayer = (currentPlayer + 2) % playerIds.length;
            }
        }

    }

    @Override
    public String toString() {
        return "Game{" +
                "currentPlayer=" + currentPlayer +
                ", playerIds=" + Arrays.toString(playerIds) +
                ", deck=" + deck +
                ", playerHand=" + playersHand +
                ", stockPile=" + stockPile +
                ", validColor=" + validColor +
                ", validValue=" + validValue +
                ", gameDirection=" + gameDirection +
                '}';
    }
}
