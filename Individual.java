package model;

import java.util.Date;

public class Individual extends Customer {
    private String nationalId;
    private Date dateOfBirth;
    
    public Individual(String customerId, String firstName, String surname, 
                     String address, String phoneNumber, String nationalId, Date dateOfBirth) {
        super(customerId, firstName, surname, address, phoneNumber);
        this.nationalId = nationalId;
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getNationalId() { return nationalId; }
    public Date getDateOfBirth() { return dateOfBirth; }
    
    @Override
    public String getCustomerType() {
        return "INDIVIDUAL";
    }
}
