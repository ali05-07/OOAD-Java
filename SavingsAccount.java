public class SavingsAccount extends Account implements InterestBearing {
    private double interestRate = 0.0005; // 0.05% as per your diagram
    
    public SavingsAccount(String accountNumber, double balance, String branch, Customer customer) {
        super(accountNumber, balance, branch, customer);
    }
    
    @Override
    public boolean withdraw(double amount) {
        // Savings account does not allow withdrawals (as per requirements)
        return false;
    }
    
    @Override
    public double calculateInterest() {
        return balance * interestRate;
    }
    
    public void applyInterest() {
        double interest = calculateInterest();
        balance += interest;
        transactions.add(new Transaction("INTEREST", interest, balance));
    }
}
