import java.util.Date;

public class Transaction {
    private String transactionId;
    private Date date;
    private String type;
    private double amount;
    private double postBalance;
    
    public Transaction(String type, double amount, double postBalance) {
        this.transactionId = "TXN" + System.currentTimeMillis();
        this.date = new Date();
        this.type = type;
        this.amount = amount;
        this.postBalance = postBalance;
    }
    
    @Override
    public String toString() {
        return String.format("Transaction[%s]: %s - Amount: %.2f, Balance: %.2f", 
                           transactionId, type, amount, postBalance);
    }
    
    // Getters
    public String getTransactionId() { return transactionId; }
    public Date getDate() { return date; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getPostBalance() { return postBalance; }
}
