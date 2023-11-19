package underover.processing;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Player {

    private String id;
    private long balance = 0;
    private int profit = 0;
    private BigDecimal winRate;
    private int nrOfBets = 0;
    private int nrOrWins = 0;
    private boolean illegalAction = false;
    private ArrayList<String[]> illegalOperations = new ArrayList<>();

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    public int getNrOfBets() {
        return nrOfBets;
    }

    public void setNrOfBets(int nrOfBets) {
        this.nrOfBets = nrOfBets;
    }

    public int getNrOrWins() {
        return nrOrWins;
    }

    public void setNrOrWins(int nrOrWins) {
        this.nrOrWins = nrOrWins;
    }


    public ArrayList<String[]> getIllegalOperations() {
        return illegalOperations;
    }

    public void setIllegalOperations(ArrayList<String[]> illegalOperations) {
        this.illegalOperations = illegalOperations;
    }

    public boolean isIllegalAction() {
        return illegalAction;
    }

    public void setIllegalAction(boolean illegalAction) {
        this.illegalAction = illegalAction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public BigDecimal getWinRate() {
        return winRate;
    }

    public void setWinRate(BigDecimal winRate) {
        this.winRate = winRate;
    }
}
