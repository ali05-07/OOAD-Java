public class InvestmentAccount extends Account implements InterestBearing {
    private double interestRate = 0.05; // 5% as per your diagram
    private double minimumBalance = 500.0;
    
    public InvestmentAccount(String accountNumber, double balance, String branch, Customer customer) {
        super(accountNumber, balance, branch, customer);
        if (balance < minimumBalance) {
            throw new IllegalArgumentException("Minimum balance for Investment account is " + minimumBalance);
        }
    }
    
    @Override
    public boolean withdraw(double amount) {
        if (validateTransaction(amount)) {
            balance -= amount;
            transactions.add(new Transaction("WITHDRAWAL", amount, balance));
            return true;
        }
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
