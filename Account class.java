package model;

public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected boolean isActive;
    
    public Account(String accountNumber, double balance, String branch) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
        this.isActive = true;
    }
    
    // Abstract methods
    public abstract boolean withdraw(double amount);
    public abstract String getAccountType();
    
    // Concrete methods
    public boolean deposit(double amount) {
        if (amount > 0 && isActive) {
            balance += amount;
            return true;
        }
        return false;
    }
    
    protected boolean validateTransaction(double amount) {
        return amount > 0 && isActive;
    }
    
    // Getters and setters
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getBranch() { return branch; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
