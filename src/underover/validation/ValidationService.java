package underover.validation;

import underover.game.Bet;
import underover.processing.FileHandler;
import underover.processing.Player;

public class ValidationService {

    public static int getValidInteger(String input) throws Exception {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new Exception("\nEnter a valid number\n");
        }
    }

    public static Bet getValidBet(String input, Player player, String newMatchId) throws Exception {
        String[] inputSplit = input.split(" ");
        Bet bet = new Bet();
        try {
            if ("higher".equals(inputSplit[0])) {
                bet.setName("higher");
            } else if ("lower".equals(inputSplit[0])) {
                bet.setName("lower");
            } else {
                throw new Exception("Enter a valid bet name higher or lower");
            }

            int amount = Integer.parseInt(inputSplit[1]);
            bet.setAmount(amount);
            FileHandler.addBetToPlayerData(player, newMatchId, bet);
            if (amount > player.getBalance()) {
                throw new Exception("Entered amount is higher than your current balance, deposit more coins.");
            }
        } catch (NumberFormatException e) {
            throw new Exception("Enter a valid bet amount in numbers");
        }
        return bet;
    }
}
