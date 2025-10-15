package model;

public class ChequeAccount extends Account {
    private String employerName;
    private String employerAddress;
    
    public ChequeAccount(String accountNumber, double balance, String branch, 
                        String employerName, String employerAddress) {
        super(accountNumber, balance, branch);
        this.employerName = employerName;
        this.employerAddress = employerAddress;
    }
    
    @Override
    public boolean withdraw(double amount) {
        if (validateTransaction(amount) && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }
    
    @Override
    public String getAccountType() {
        return "CHEQUE";
    }
    
    // Getters
    public String getEmployerName() { return employerName; }
    public String getEmployerAddress() { return employerAddress; }
}

