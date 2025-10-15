package model;

import java.util.Date;

public class Transaction {
    private String transactionId;
    private Date date;
    private String type;
    private double amount;
    private double postBalance;
    
    public Transaction(String transactionId, Date date, String type, double amount, double postBalance) {
        this.transactionId = transactionId;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.postBalance = postBalance;
    }
    
    @Override
    public String toString() {
        return String.format("Transaction[ID: %s, Date: %s, Type: %s, Amount: %.2f, Balance: %.2f]", 
                           transactionId, date, type, amount, postBalance);
    }
    
    // Getters
    public String getTransactionId() { return transactionId; }
    public Date getDate() { return date; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getPostBalance() { return postBalance; }
}

