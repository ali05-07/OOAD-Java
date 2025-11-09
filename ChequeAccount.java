public class ChequeAccount extends Account {
    public ChequeAccount(String accountNumber, double balance, String branch) {
        super(accountNumber, balance, branch);
    }

    @Override
    public String getAccountType() { return "Cheque Account"; }

    @Override
    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            return true;
        }
        return false;
    }

    @Override
    public boolean withdraw(double amount) {
        // Cheque account might allow overdraft
        if (amount > 0) {
            balance -= amount;
            return true;
        }
        return false;
    }
}