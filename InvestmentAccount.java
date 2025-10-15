package model;

public class InvestmentAccount extends Account implements InterestBearing {
    private double interestRate = 0.05; // 5% as per assignment
    private double minimumBalance = 500.0;
    
    public InvestmentAccount(String accountNumber, double balance, String branch) {
        super(accountNumber, balance, branch);
    }
    
    @Override
    public boolean withdraw(double amount) {
        if (validateTransaction(amount) && (balance - amount) >= minimumBalance) {
            balance -= amount;
            return true;
        }
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
        return "INVESTMENT";
    }
    
    public double getInterestRate() { return interestRate; }
    public double getMinimumBalance() { return minimumBalance; }
}


