import java.util.ArrayList;
import java.util.List;

public abstract class Customer {
    protected String customerID;
    protected String firstName;
    protected String surname;
    protected String address;
    protected String phoneNumber;
    protected List<Account> accounts;
    
    public Customer(String customerID, String firstName, String surname, 
                   String address, String phoneNumber) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.accounts = new ArrayList<>();
    }
    
    // Methods from your diagram
    public List<Account> getAccounts() {
        return new ArrayList<>(accounts); // Return copy for encapsulation
    }
    
    public void addAccount(Account acc) {
        accounts.add(acc);
    }
    
    // Getters and setters
    public String getCustomerID() { return customerID; }
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
}
