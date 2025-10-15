package model;

public class SavingsAccount extends Account implements InterestBearing {
    private double interestRate = 0.0005; // 0.05% as per assignment
    
    public SavingsAccount(String accountNumber, double balance, String branch) {
        super(accountNumber, balance, branch);
    }
    
    @Override
    public boolean withdraw(double amount) {
        // Savings account doesn't allow withdrawals as per requirements
        return false;
    }
    
    @Override
    public double calculateInterest() {
        return balance * interestRate;
    }
    
    @Override
    public void applyInterest() {
        double interest = calculateInterest();
        deposit(interest);
    }
    
    @Override
    public String getAccountType() {
        return "SAVINGS";
    }
    
    public double getInterestRate() { return interestRate; }
}
