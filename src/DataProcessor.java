import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class DataProcessor {

    static final String PLAYER_FILE_PATH = "./resources/player_data.txt";
    static final String MATCH_FILE_PATH = "./resources/match_data.txt";

    public static void processBettingData() {
        ArrayList<String[]> playerData = FileHandler.getInputData(PLAYER_FILE_PATH);

        Casino casino = new Casino();

        ArrayList<Player> playerList = processAndGetPlayerList(playerData, casino);

        FileHandler.outputResultFile(playerList, casino);
    }

    private static ArrayList<Player> processAndGetPlayerList(ArrayList<String[]> playerData, Casino casino) {
        Player player = new Player();
        ArrayList<Player> playerList = new ArrayList<>();

        for (int i = 0; i < playerData.size(); i++) {
            if (i == 0) {
                player.setId(playerData.get(i)[0]);
                handlePlayerAction(playerData.get(i), player, casino);
            } else if (!playerData.get(i)[0].equals(playerData.get(i - 1)[0])) {
                playerList.add(player);
                player = new Player();
                player.setId(playerData.get(i)[0]);
                handlePlayerAction(playerData.get(i), player, casino);
            } else {
                handlePlayerAction(playerData.get(i), player, casino);
            }
        }
        playerList.add(player);
        return playerList;
    }

    private static void handlePlayerAction(String[] playerData, Player player, Casino casino) {
        switch (playerData[1]) {
            case "DEPOSIT":
                handleDeposit(playerData, player);
                break;
            case "WITHDRAW":
                handleWithdraw(playerData, player);
                break;
            case "BET":
                handleBet(playerData, player, casino);
                break;
        }
    }

    private static void handleDeposit(String[] playerData, Player player) {
        long depositAmount = Integer.parseInt(playerData[3]);
        player.setBalance(player.getBalance() + depositAmount);
    }

    private static void handleWithdraw(String[] playerData, Player player) {
        long balance = player.getBalance();
        long withdrawAmount = Integer.parseInt(playerData[3]);
        if (withdrawAmount > balance) {
            setPlayerIllegalActionAndOperation(playerData, player);
        } else {
            player.setBalance(balance - withdrawAmount);
        }
    }

    private static void setPlayerIllegalActionAndOperation(String[] playerData, Player player) {
        player.setIllegalAction(true);

        if (playerData[1].equals("WITHDRAW")) {
            String[] modifiedPlayerData = {playerData[0], playerData[1], null, playerData[3], null};
            player.setIllegalOperation(modifiedPlayerData);
        } else {
            player.setIllegalOperation(playerData);
        }
    }

    private static void handleBet(String[] playerData, Player player, Casino casino) {
        long balance = player.getBalance();
        int betAmount = Integer.parseInt(playerData[3]);

        if (betAmount > balance) {
            setPlayerIllegalActionAndOperation(playerData, player);
        } else {
            makeBet(playerData, player, casino);
        }
    }

    private static void makeBet(String[] playerData, Player player, Casino casino) {
        int betAmount = Integer.parseInt(playerData[3]);

        player.setBalance(player.getBalance() - betAmount);
        casino.setBalance(casino.getBalance() + betAmount);
        player.setNrOfBets(player.getNrOfBets() + 1);

        handleBettingOutcome(player, casino, playerData);
    }

    private static void handleBettingOutcome(Player player, Casino casino, String[] playerData) {
        String bettingSide = playerData[4];
        String matchId = playerData[2];
        int betAmount = Integer.parseInt(playerData[3]);

        ArrayList<String[]> matchData = FileHandler.getInputData(MATCH_FILE_PATH);

        for (String[] match : matchData) {
            if (match[0].equals(matchId)) {
                if (match[3].equals(bettingSide)) {
                    handleBettingWin(player, casino, match, bettingSide, betAmount);
                } else if (match[3].equals("DRAW")) {
                    casino.setBalance(casino.getBalance() - betAmount);
                    player.setBalance(player.getBalance() + betAmount);
                }
            }
        }
        player.setWinRate(BigDecimal.valueOf(player.getNrOrWins())
                .divide(BigDecimal.valueOf(player.getNrOfBets()), 2, RoundingMode.HALF_DOWN));
    }

    private static void handleBettingWin(Player player, Casino casino, String[] match, String bettingSide, int betAmount) {
        double winRate = getMatchWinRate(match, bettingSide);
        int winAmount = (int) (winRate * betAmount) + betAmount;

        casino.setBalance(casino.getBalance() - winAmount);
        player.setBalance(player.getBalance() + winAmount);
        player.setNrOrWins(player.getNrOrWins() + 1);
    }

    private static double getMatchWinRate(String[] match, String bettingSide) {
        double winRate;
        if (bettingSide.equals("A")) {
            winRate = Double.parseDouble(match[1]);
        } else {
            winRate = Double.parseDouble(match[2]);
        }
        return winRate;
    }
}