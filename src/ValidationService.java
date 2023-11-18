public class ValidationService {

    public static int getValidInteger(String input) throws Exception {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new Exception("\nEnter a valid number\n");
        }
    }

    public static Bet getValidBet(String input, Player player) throws Exception {
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
            if (amount > player.getBalance()) {
                throw new Exception("Entered amount is higher than your current balance, deposit more coins.");
            } else {
                bet.setAmount(amount);
            }

        } catch (NumberFormatException e) {
            throw new Exception("Enter a valid bet amount in numbers");
        }
        return bet;
    }
}
