public class ChequeAccount extends Account {
    private String employerName;
    private String employerAddress;
    
    public ChequeAccount(String accountNumber, double balance, String branch, 
                        Customer customer, String employerName, String employerAddress) {
        super(accountNumber, balance, branch, customer);
        this.employerName = employerName;
        this.employerAddress = employerAddress;
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
    
    // Getters
    public String getEmployerName() { return employerName; }
    public String getEmployerAddress() { return employerAddress; }
}
