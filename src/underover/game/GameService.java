package underover.game;

import underover.processing.Player;
import underover.processing.FileHandler;
import underover.validation.ValidationService;
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.UUID;

public class GameService {

    static final Scanner SCANNER = new Scanner(System.in);
    static final Images IMAGES = new Images();
    static final SecureRandom RANDOM = new SecureRandom();

    public static void startGame() {
        boolean isOn = true;
        while (isOn) {
            System.out.println(IMAGES.getLogo());
            System.out.println("\nWelcome to the Under and Over betting game!\n");
            System.out.print("""
                        
                        1 - Show instructions
                        2 - Enter the game
                        Enter nr ->""");
            try {
                int playerInput = ValidationService.getValidInteger(SCANNER.nextLine());
                switch (playerInput) {
                    case 1:
                        displayInstructions();
                        break;
                    case 2:
                        enterGame();
                        break;
                    case 66:
                        isOn = false;
                        break;
                    default:
                        throw new Exception("Enter a number in range of 1 - 2");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void displayInstructions() {
        System.out.println("""
                
                Objective:
                Predict whether the dice sum will be higher or lower than the randomly selected number for a chance to win.
                
                Gameplay:
                A random number between 4 and 9 is chosen at the start.
                Players bet on whether the total sum of two dice will be higher or lower than the generated number.
                Winning occurs if the guessed outcome matches the dice sum compared to the chosen number.
                It's a draw when you roll the same dice sum as the generated number.
                
                Betting:
                To place a bet write: higher/lower and the bet amount, ex. higher 100, lower 10
                """);
        System.out.print("Hit Enter to continue: ");
        SCANNER.nextLine();
    }

    private static void enterGame() {
        Player player = createNewPlayer();
        boolean isOn = true;
        while (isOn) {
            System.out.println("\n Your current balance: " + player.getBalance());
            System.out.print("""
                       
                        1 - Deposit Credits
                        2 - Play Game
                        3 - Withdraw Credits
                        4 - Exit game
                        ->""");
            try {
                int userInput = ValidationService.getValidInteger(SCANNER.nextLine());
                switch (userInput) {
                    case 1:
                        depositCredits(player);
                        break;
                    case 2:
                        playGame(player);
                        break;
                    case 3:
                        withdrawCredits(player);
                        break;
                    case 4:
                        isOn = false;
                        break;
                    default:
                        throw new Exception("Enter a number in range of 1 - 4");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static Player createNewPlayer() {
        Player player = new Player();
        String newRandomUuid = UUID.randomUUID().toString();
        player.setId(newRandomUuid);
        return player;
    }

    private static void depositCredits(Player player) {
        System.out.println("You currently have: " + player.getBalance() + " coins in your balance\n");
        System.out.print("Enter the amount of credits you wish to deposit: ");
        try {
            int depositAmount = ValidationService.getValidInteger(SCANNER.nextLine());
            player.setBalance(player.getBalance() + depositAmount);
            System.out.println("\nYou deposited: " + depositAmount);
            FileHandler.addActionToPlayerData(player, depositAmount, "DEPOSIT");
            System.out.print("\nHit Enter to continue: ");
            SCANNER.nextLine();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void withdrawCredits(Player player) {
        System.out.println("You currently have: " + player.getBalance() + " coins in your balance.\n");
        System.out.print("Enter the amount of credits you wish to withdraw: ");
        try {
            int withdrawAmount = ValidationService.getValidInteger(SCANNER.nextLine());
            FileHandler.addActionToPlayerData(player, withdrawAmount, "WITHDRAW");
            if (withdrawAmount > player.getBalance()) {
                throw new Exception("Cannot withdraw: " + withdrawAmount + ". Insufficient funds on your balance");
            } else {
                player.setBalance(player.getBalance() - withdrawAmount);
                System.out.println("Here you go: " + withdrawAmount);
                System.out.print("\nHit Enter to continue: ");
                SCANNER.nextLine();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void playGame(Player player) {
        String newMatchId = UUID.randomUUID().toString();
        int underOverNr = getUnderOverNr();
        BetRatio betRatio = getCurrentBetRatio(underOverNr);

        System.out.println("\nWill the dice throw be higher or lower than: " + underOverNr);
        System.out.println("\nLower ratio: " + betRatio.getLowerRatio() + ", Higher ratio: " + betRatio.getHigherRatio());
        System.out.println("Your current balance is: " + player.getBalance());
        System.out.println("\nPlace you bet 'higher/lower amount': ");

        try {
            Bet bet = ValidationService.getValidBet(SCANNER.nextLine(), player, newMatchId);
            player.setBalance(player.getBalance() - bet.getAmount());
            throwDiceAndHandleOutcome(player, underOverNr, bet, betRatio, newMatchId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void throwDiceAndHandleOutcome(Player player, int underOverNr, Bet bet, BetRatio betRatio, String newMatchId) {
        int diceThrowSum = throwDice();
        System.out.println("UnderOverNr :"+ underOverNr +", Thrown dice sum: " + diceThrowSum);

        String outcome;
        if (diceThrowSum > underOverNr) {
            outcome = "higher";
        } else {
            outcome = "lower";
        }
        if (diceThrowSum == underOverNr) {
            outcome = handleDraw(player, bet);
        } else if (bet.getName().equals(outcome)) {
            handleWin(player, bet, betRatio);
        } else {
            System.out.println("You Lose");
        }
        FileHandler.addDataToMatchData(newMatchId, betRatio, outcome);
    }

    private static String handleDraw(Player player, Bet bet) {
        System.out.println("It's a draw");
        player.setBalance(player.getBalance() + bet.getAmount());
        return "DRAW";
    }

    private static void handleWin(Player player, Bet bet, BetRatio betRatio) {
        int winProfit;
        if (bet.getName().equals("higher")) {
            winProfit = (int) (betRatio.getHigherRatio() * bet.getAmount());
        } else {
            winProfit = (int) (betRatio.getLowerRatio() * bet.getAmount());
        }
        System.out.println("\nYou win: " + winProfit);
        player.setBalance(player.getBalance() + winProfit + bet.getAmount());
    }

    private static int throwDice() {
        int randomNr1 = RANDOM.nextInt(IMAGES.getDices().length);
        int randomNr2 = RANDOM.nextInt(IMAGES.getDices().length);

        System.out.println("Dice throw: \n");
        System.out.println(IMAGES.getDices()[randomNr1]);
        System.out.println(IMAGES.getDices()[randomNr2]);
        return randomNr1 + randomNr2 + 2;
    }

    private static int getUnderOverNr() {
        int rangeMin = 4;
        int rangeMax = 9;
        return RANDOM.nextInt(rangeMax - rangeMin + 1) + rangeMin;
    }

    private static BetRatio getCurrentBetRatio(int underOverNr) {
        BetRatio betRatio = BetRatio.SEVEN;

        for (BetRatio ratio : BetRatio.values()) {
            if (ratio.getId() == underOverNr) {
                betRatio = ratio;
            }
        }
        return betRatio;
    }
}