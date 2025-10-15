package model;

public abstract class Customer {
    protected String customerId;
    protected String firstName;
    protected String surname;
    protected String address;
    protected String phoneNumber;
    
    public Customer(String customerId, String firstName, String surname, String address, String phoneNumber) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
    
    // Getters
    public String getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    
    public abstract String getCustomerType();
}

