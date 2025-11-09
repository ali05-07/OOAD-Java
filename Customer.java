import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String nationalId;
    private String username;
    private String password;
    private List<Account> accounts = new ArrayList<>();

    public Customer(String firstName, String lastName, String address, String phone, String nationalId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.nationalId = nationalId;
    }

    // Getters and setters
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getNationalId() { return nationalId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    
    public List<Account> getAccounts() { return accounts; }
    public void addAccount(Account account) { accounts.add(account); }
    public void removeAccount(Account account) { accounts.remove(account); }
}