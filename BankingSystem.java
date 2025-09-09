
interface AccountOperations {
    void deposit(double amount);
    void withdraw(double amount);
    void calculateInterest();
}

// Abstract class Account
abstract class Account implements AccountOperations {
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected Customer customer;

    public Account(String accountNumber, String branch, Customer customer) {
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.customer = customer;
        this.balance = 0.0;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
        System.out.println(amount + " deposited. New balance: " + balance);
    }

    @Override
    public abstract void withdraw(double amount); // Different rules for each account
}

// Customer class for demonstration purposes
class Customer {
    private String name;
    
    public Customer(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}

// Savings Account
class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 0.0005; // 0.05% monthly

    public SavingsAccount(String accountNumber, String branch, Customer customer) {
        super(accountNumber, branch, customer);
    }

    @Override
    public void withdraw(double amount) {
        System.out.println("Withdrawals not allowed from Savings Account.");
    }

    @Override
    public void calculateInterest() {
        double interest = balance * INTEREST_RATE;
        balance += interest;
        System.out.println("Interest added: " + interest + " New balance: " + balance);
    }
}

// Investment Account
class InvestmentAccount extends Account {
    private static final double INTEREST_RATE = 0.05; // 5% monthly

    public InvestmentAccount(String accountNumber, String branch, Customer customer) {
        super(accountNumber, branch, customer);
        if (balance < 500) {
            balance = 500; // Initial deposit required
        }
    }

    @Override
    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println(amount + " withdrawn. New balance: " + balance);
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    @Override
    public void calculateInterest() {
        double interest = balance * INTEREST_RATE;
        balance += interest;
        System.out.println("Interest added: " + interest + " New balance: " + balance);
    }
}

// Cheque Account
class ChequeAccount extends Account {
    private String employer;

    public ChequeAccount(String accountNumber, String branch, Customer customer, String employer) {
        super(accountNumber, branch, customer);
        this.employer = employer;
    }

    @Override
    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println(amount + " withdrawn. New balance: " + balance);
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    @Override
    public void calculateInterest() {
        System.out.println("Cheque Account does not earn interest.");
    }
}

// Main class to run the program
public class BankApp {
    public static void main(String[] args) {
        Customer customer1 = new Customer("Alice");
        SavingsAccount savingsAccount = new SavingsAccount("SA123", "Main Branch", customer1);
        savingsAccount.deposit(1000);
        savingsAccount.calculateInterest();
        savingsAccount.withdraw(100); // Should not allow withdrawal

        Customer customer2 = new Customer("Bob");
        InvestmentAccount investmentAccount = new InvestmentAccount("IA123", "Main Branch", customer2);
        investmentAccount.deposit(600);
        investmentAccount.calculateInterest();
        investmentAccount.withdraw(200);

        Customer customer3 = new Customer("Charlie");
        ChequeAccount chequeAccount = new ChequeAccount("CA123", "Main Branch", customer3, "ACME Corp");
        chequeAccount.deposit(500);
        chequeAccount.withdraw(100);
        chequeAccount.calculateInterest(); // Should not earn interest
    }
}
