public class CompanyCustomer extends Customer {
    private String companyName;
    private String registrationNumber;
    private String contactPerson;

    public CompanyCustomer(String companyName, String address, String contactPerson, String phone) {
        super("", "", address, phone, "");
        this.companyName = companyName;
        this.contactPerson = contactPerson;
    }

    // Getters and setters
    public String getCompanyName() { return companyName; }
    public String getRegistrationNumber() { return registrationNumber; }
    public String getContactPerson() { return contactPerson; }
    
    public void setRegistrationNumber(String registrationNumber) { 
        this.registrationNumber = registrationNumber; 
    }
    
    // Override getFirstName and getLastName to return company info
    @Override
    public String getFirstName() { return companyName; }
    @Override
    public String getLastName() { return contactPerson; }
}