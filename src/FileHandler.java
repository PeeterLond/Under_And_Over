import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler {

    static final String RESULT_FILE_PATH = "./src/result.txt";

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

        for (Player playerRecord : playerList) {
            if (playerRecord.isIllegalAction()) {
                illegalPlayers.add(playerRecord);
            } else {
                legitPlayers.add(playerRecord);
            }
        }

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(RESULT_FILE_PATH));
            for (Player legitPlayer : legitPlayers) {
                writer.printf("%s %d %.2f%n%n", legitPlayer.getId(), legitPlayer.getBalance(), legitPlayer.getWinRate());
            }
            for (Player illegalPlayer : illegalPlayers) {
                String[] operation = illegalPlayer.getIllegalOperation();
                writer.printf("%s %s %s %s %s%n%n", operation[0], operation[1], operation[2], operation[3], operation[4]);
            }
            writer.print(casino.getBalance());
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}