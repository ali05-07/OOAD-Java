import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected Customer customer;
    protected List<Transaction> transactions;
    
    public Account(String accountNumber, double balance, String branch, Customer customer) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
        this.customer = customer;
        this.transactions = new ArrayList<>();
    }
    
    // Public methods from your diagram
    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            // Record transaction
            transactions.add(new Transaction("DEPOSIT", amount, balance));
            return true;
        }
        return false;
    }
    
    public abstract boolean withdraw(double amount);
    
    public double getBalance() {
        return balance;
    }
    
    // Protected validation method from your diagram
    protected boolean validateTransaction(double amount) {
        return amount > 0 && amount <= balance;
    }
    
    // Static methods from your diagram (these would be in BankingService)
    public static Account openAccount() {
        // This would be implemented in BankingService
        throw new UnsupportedOperationException("Use BankingService to open accounts");
    }
    
    public static boolean closeAccount(String accountNumber) {
        // This would be implemented in BankingService
        throw new UnsupportedOperationException("Use BankingService to close accounts");
    }
    
    // Getters
    public String getAccountNumber() { return accountNumber; }
    public String getBranch() { return branch; }
    public Customer getCustomer() { return customer; }
    public List<Transaction> getTransactions() { return new ArrayList<>(transactions); }
}
