package underover.processing;

import underover.game.Bet;
import underover.game.BetRatio;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler {

    public static ArrayList<String[]> getInputData(String filepath) {
        ArrayList<String[]> data = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(filepath));
            while (scanner.hasNextLine()) {
                data.add(scanner.nextLine().split(","));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return data;
    }

    public static void outputResultFile(ArrayList<Player> playerList, Casino casino) {
        ArrayList<Player> legitPlayers = new ArrayList<>();
        ArrayList<Player> illegalPlayers = new ArrayList<>();

        for (Player player : playerList) {
            if (player.isIllegalAction()) {
                illegalPlayers.add(player);
//                handleIllegalActionSettlement(casino, player);
            } else {
                legitPlayers.add(player);
            }
        }
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(FilePath.RESULT_FILE_PATH.getPath()));
            for (Player legitPlayer : legitPlayers) {
                writer.printf("%s %d %.2f%n", legitPlayer.getId(), legitPlayer.getBalance(), legitPlayer.getWinRate());
            }
            writer.println();
            for (Player illegalPlayer : illegalPlayers) {
                String[] operation = illegalPlayer.getIllegalOperations().get(0);
                writer.printf("%s %s %s %s %s%n", operation[0], operation[1], operation[2], operation[3], operation[4]);
            }
            writer.print("\n" + casino.getBalance());
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleIllegalActionSettlement(Casino casino, Player player) {
        casino.setBalance(casino.getBalance() + player.getProfit());
        player.setBalance(player.getBalance() - player.getProfit());
        player.setProfit(0);
    }

    public static void addBetToPlayerData(Player player, String newMatchId, Bet bet) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(FilePath.PLAYER_FILE_PATH.getPath(), true));
            String betChar;
            if (bet.getName().equals("lower")) {
                betChar = "A";
            } else {
                betChar = "B";
            }
            writer.printf("%n%s,BET,%s,%d,%s", player.getId(), newMatchId, bet.getAmount(), betChar);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void addDataToMatchData(String newMatchId, BetRatio betRatio, String outcome) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(FilePath.MATCH_FILE_PATH.getPath(), true));
            if (outcome.equals("lower")) {
                outcome = "A";
            } else if (outcome.equals("higher")) {
                outcome = "B";
            }
            writer.printf("%n%s,%.2f,%.2f,%s", newMatchId, betRatio.getLowerRatio(), betRatio.getHigherRatio(), outcome);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void addActionToPlayerData(Player player, int amount, String action) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(FilePath.PLAYER_FILE_PATH.getPath(), true));
            writer.printf("%n%s,%s,,%d,", player.getId(), action, amount);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}