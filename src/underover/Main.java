package underover;

import underover.game.GameService;
import underover.processing.DataProcessor;
import underover.validation.ValidationService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean isOn = true;
        while (isOn) {
            System.out.print("""
                
                Under/Over betting machine.
                
                
                1 - Start the game
                2 - Process game data
                3 - Close the machine
                ->""");
            Scanner scanner = new Scanner(System.in);
            try {
                int userInput = ValidationService.getValidInteger(scanner.nextLine());
                switch (userInput) {
                    case 1:
                        GameService.startGame();
                        break;
                    case 2:
                        DataProcessor.processBettingData();
                        System.out.println("result.txt has been created in the underOver folder.");
                        break;
                    case 3:
                        isOn = false;
                        break;
                    default:
                        throw new Exception("Enter a number in range of 1 - 3");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}