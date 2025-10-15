package model;

public class Company extends Customer {
    private String companyName;
    private String registrationNumber;
    private String contactPerson;
    private String contactNumber;
    
    public Company(String customerId, String firstName, String surname, 
                   String address, String phoneNumber, String companyName, 
                   String registrationNumber, String contactPerson, String contactNumber) {
        super(customerId, firstName, surname, address, phoneNumber);
        this.companyName = companyName;
        this.registrationNumber = registrationNumber;
        this.contactPerson = contactPerson;
        this.contactNumber = contactNumber;
    }
    
    // Getters
    public String getCompanyName() { return companyName; }
    public String getRegistrationNumber() { return registrationNumber; }
    public String getContactPerson() { return contactPerson; }
    public String getContactNumber() { return contactNumber; }
    
    @Override
    public String getCustomerType() {
        return "COMPANY";
    }
}
