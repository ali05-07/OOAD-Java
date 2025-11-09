import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

public class BankingController {

    // Login Tab
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Label loginErrorLabel;
    @FXML private Tab loginTab, registerTab, accountsTab, transactionsTab, historyTab;
    @FXML private TabPane mainTabPane;

    // Register Tab
    @FXML private RadioButton individualRadio, companyRadio;
    @FXML private VBox individualFields, companyFields;
    @FXML private TextField regUsernameField, regPasswordField;
    @FXML private TextField firstNameField, lastNameField, nationalIdField, addressField, phoneField;
    @FXML private TextField companyNameField, regNumberField, contactPersonField, companyAddressField;
    @FXML private Label registerMessageLabel;

    // Accounts Tab
    @FXML private ListView<String> accountListView;
    @FXML private Label totalBalanceLabel, selectedAccountLabel, accountBalanceLabel, minBalanceLabel;
    @FXML private ComboBox<String> accountTypeCombo;
    @FXML private TextField initialDepositField;
    @FXML private Button closeAccountButton;

    // Transactions Tab
    @FXML private ComboBox<String> depositAccountCombo, withdrawAccountCombo;
    @FXML private TextField depositAmountField, withdrawAccountField;
    @FXML private Label withdrawBalanceLabel, depositBalanceLabel;
    @FXML private Label depositMessageLabel, withdrawMessageLabel;

    // History Tab
    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, String> dateColumn, typeColumn, descriptionColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn, balanceColumn;
    @FXML private DatePicker startDatePicker, endDatePicker;
    @FXML private Label totalInterestLabel, totalTransactionsLabel;
    @FXML private Button deleteTransactionButton, refreshHistoryButton;

    // Data Storage
    private ObservableList<String> accountDisplayList = FXCollections.observableArrayList();
    private ObservableList<Transaction> transactions = FXCollections.observableArrayList();
    
    // ACTUAL DATA STORAGE
    private List<Customer> customers = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private Customer currentCustomer = null;
    private Account selectedAccount = null;

    // File paths for data persistence
    private static final String CUSTOMERS_FILE = "customers.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";
    private static final String ACCOUNTS_FILE = "accounts.txt";

    @FXML
    public void initialize() {
        setupCustomerRegistration();
        setupAccountsTab();
        setupTransactionsTab();
        setupHistoryTab();
        setupButtonStyles();
        
        // Load existing data from files
        loadAllData();
        
        // Add sample data only if no existing data
        if (customers.isEmpty() && transactions.isEmpty()) {
            addSampleData();
        }
        
        debugTransactionLoading();
    }

    private void setupCustomerRegistration() {
        // Hide company fields initially
        companyFields.setVisible(false);
        companyFields.setManaged(false);
        
        // Set individual as default
        individualRadio.setSelected(true);
    }

    private void setupAccountsTab() {
        // Setup account types
        accountTypeCombo.getItems().addAll("Savings Account", "Investment Account", "Cheque Account");
        
        // Setup account list selection listener
        accountListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> onAccountSelected(newValue)
        );
    }

    private void setupTransactionsTab() {
        // Add listeners to update balance when account selection changes
        depositAccountCombo.setOnAction(e -> onDepositAccountSelected());
        withdrawAccountCombo.setOnAction(e -> onWithdrawAccountSelected());
        
        // Initialize balance labels
        depositBalanceLabel.setText("Current Balance: P0.00");
        withdrawBalanceLabel.setText("Current Balance: P0.00");
    }

    private void setupHistoryTab() {
        // Clear any existing cell value factories
        dateColumn.setCellValueFactory(null);
        typeColumn.setCellValueFactory(null);
        descriptionColumn.setCellValueFactory(null);
        amountColumn.setCellValueFactory(null);
        balanceColumn.setCellValueFactory(null);
        
        // Setup table columns with proper property bindings
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        
        // Format numeric columns
        amountColumn.setCellFactory(column -> new TableCell<Transaction, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(String.format("P%.2f", amount));
                }
            }
        });
        
        balanceColumn.setCellFactory(column -> new TableCell<Transaction, Double>() {
            @Override
            protected void updateItem(Double balance, boolean empty) {
                super.updateItem(balance, empty);
                if (empty || balance == null) {
                    setText(null);
                } else {
                    setText(String.format("P%.2f", balance));
                }
            }
        });
        
        // Set the items
        transactionsTable.setItems(transactions);
        
        // Enable multiple selection for transactions table
        transactionsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        System.out.println("Transaction table setup complete. Items: " + transactions.size());
    }

    private void setupButtonStyles() {
        // This method will be called after FXML loads to style buttons
    }

    private void debugTransactionLoading() {
        System.out.println("Total transactions loaded: " + transactions.size());
        for (Transaction t : transactions) {
            System.out.println("Transaction: " + t.getDate() + " | " + t.getType() + " | " + t.getAmount());
        }
        System.out.println("Table items count: " + transactionsTable.getItems().size());
    }

    // ========== DATA PERSISTENCE METHODS ==========
    
    private void loadAllData() {
        loadCustomers();
        loadAccounts();
        loadTransactions();
        updateTransactionSummary();
    }
    
    private void loadCustomers() {
        try {
            if (Files.exists(Paths.get(CUSTOMERS_FILE))) {
                List<String> lines = Files.readAllLines(Paths.get(CUSTOMERS_FILE));
                for (String line : lines) {
                    if (!line.trim().isEmpty()) {
                        Customer customer = parseCustomer(line);
                        if (customer != null) {
                            customers.add(customer);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }
    }
    
    private Customer parseCustomer(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length >= 7) {
                if (parts[0].equals("INDIVIDUAL")) {
                    Customer customer = new Customer(parts[1], parts[2], parts[3], parts[4], parts[5]);
                    customer.setUsername(parts[6]);
                    customer.setPassword(parts[7]);
                    return customer;
                } else if (parts[0].equals("COMPANY")) {
                    CompanyCustomer company = new CompanyCustomer(parts[1], parts[2], parts[3], parts[4]);
                    company.setRegistrationNumber(parts[5]);
                    company.setUsername(parts[6]);
                    company.setPassword(parts[7]);
                    return company;
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing customer: " + e.getMessage());
        }
        return null;
    }
    
    private void loadAccounts() {
        try {
            if (Files.exists(Paths.get(ACCOUNTS_FILE))) {
                List<String> lines = Files.readAllLines(Paths.get(ACCOUNTS_FILE));
                for (String line : lines) {
                    if (!line.trim().isEmpty()) {
                        Account account = parseAccount(line);
                        if (account != null) {
                            accounts.add(account);
                            // Link account to customer
                            String customerId = line.split("\\|")[4];
                            for (Customer customer : customers) {
                                if (customer.getUsername() != null && 
                                    customer.getUsername().equals(customerId)) {
                                    customer.addAccount(account);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading accounts: " + e.getMessage());
        }
    }
    
    private Account parseAccount(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length >= 5) {
                String accountNumber = parts[0];
                String type = parts[1];
                double balance = Double.parseDouble(parts[2]);
                String branch = parts[3];
                
                switch (type) {
                    case "Savings Account":
                        return new SavingsAccount(accountNumber, balance, branch);
                    case "Investment Account":
                        return new InvestmentAccount(accountNumber, balance, branch);
                    case "Cheque Account":
                        return new ChequeAccount(accountNumber, balance, branch);
                    default:
                        return null;
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing account: " + e.getMessage());
        }
        return null;
    }
    
    private void loadTransactions() {
        try {
            // Clear current transactions before loading
            List<Transaction> loadedTransactions = new ArrayList<>();
            
            if (Files.exists(Paths.get(TRANSACTIONS_FILE))) {
                List<String> lines = Files.readAllLines(Paths.get(TRANSACTIONS_FILE));
                for (String line : lines) {
                    if (!line.trim().isEmpty()) {
                        Transaction transaction = parseTransaction(line);
                        if (transaction != null) {
                            loadedTransactions.add(transaction);
                        }
                    }
                }
            }
            
            // Replace all transactions with loaded ones
            transactions.setAll(loadedTransactions);
            
        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
        }
    }
    
    private Transaction parseTransaction(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length >= 5) {
                String date = parts[0];
                String type = parts[1];
                String description = parts[2];
                double amount = Double.parseDouble(parts[3]);
                double balance = Double.parseDouble(parts[4]);
                return new Transaction(date, type, description, amount, balance);
            }
        } catch (Exception e) {
            System.err.println("Error parsing transaction: " + e.getMessage());
        }
        return null;
    }
    
    private void saveCustomers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer customer : customers) {
                if (customer instanceof CompanyCustomer) {
                    CompanyCustomer company = (CompanyCustomer) customer;
                    writer.println("COMPANY|" + company.getCompanyName() + "|" + 
                                 company.getAddress() + "|" + company.getContactPerson() + "|" + 
                                 company.getPhone() + "|" + company.getRegistrationNumber() + "|" +
                                 company.getUsername() + "|" + company.getPassword());
                } else {
                    writer.println("INDIVIDUAL|" + customer.getFirstName() + "|" + 
                                 customer.getLastName() + "|" + customer.getAddress() + "|" + 
                                 customer.getPhone() + "|" + customer.getNationalId() + "|" +
                                 customer.getUsername() + "|" + customer.getPassword());
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving customers: " + e.getMessage());
        }
    }
    
    private void saveAccounts() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ACCOUNTS_FILE))) {
            for (Account account : accounts) {
                String customerId = "";
                // Find which customer owns this account
                for (Customer customer : customers) {
                    if (customer.getAccounts().contains(account)) {
                        customerId = customer.getUsername();
                        break;
                    }
                }
                writer.println(account.getAccountNumber() + "|" + 
                             account.getAccountType() + "|" + account.getBalance() + "|" + 
                             account.getBranch() + "|" + customerId);
            }
        } catch (IOException e) {
            System.err.println("Error saving accounts: " + e.getMessage());
        }
    }
    
    private void saveTransactions() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTIONS_FILE))) {
            for (Transaction transaction : transactions) {
                writer.println(transaction.getDate() + "|" + 
                             transaction.getType() + "|" + 
                             transaction.getDescription() + "|" + 
                             transaction.getAmount() + "|" + 
                             transaction.getBalance());
            }
            System.out.println("Saved " + transactions.size() + " transactions to file.");
        } catch (IOException e) {
            System.err.println("Error saving transactions: " + e.getMessage());
        }
    }
    
    private void saveAllData() {
        saveCustomers();
        saveAccounts();
        saveTransactions();
    }

    private void addSampleData() {
        // Just initialize empty lists - NO HARDCODED CUSTOMER
        customers = new ArrayList<>();
        accounts = new ArrayList<>();
        transactions = FXCollections.observableArrayList();
        
        // Add sample transactions for demo
        transactions.add(new Transaction(LocalDate.now().toString(), "Deposit", "Initial deposit", 1000.0, 1000.0));
        transactions.add(new Transaction(LocalDate.now().toString(), "Interest", "Monthly interest", 5.0, 1005.0));
        
        updateTransactionSummary();
        saveAllData();
    }

    // ========== LOGIN METHODS ==========
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            loginErrorLabel.setText("Please enter username and password!");
            loginErrorLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        
        // Check if we have any registered customers
        if (customers.isEmpty()) {
            loginErrorLabel.setText("No customers registered yet! Please register first.");
            loginErrorLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        
        // Find customer by username
        Customer foundCustomer = findCustomerByUsername(username);
        
        if (foundCustomer != null) {
            // Check password
            if (foundCustomer.getPassword() != null && foundCustomer.getPassword().equals(password)) {
                currentCustomer = foundCustomer;
                
                // Show welcome message with ACTUAL customer name
                loginErrorLabel.setText("Login successful! Welcome " + currentCustomer.getFirstName() + " " + currentCustomer.getLastName());
                loginErrorLabel.setStyle("-fx-text-fill: green;");
                
                // NAVIGATE TO ACCOUNTS TAB AFTER LOGIN
                loginTab.getTabPane().getSelectionModel().select(accountsTab);
                
                updateAccountDisplay();
                
                // Clear login fields
                usernameField.clear();
                passwordField.clear();
            } else {
                loginErrorLabel.setText("Invalid password!");
                loginErrorLabel.setStyle("-fx-text-fill: red;");
            }
        } else {
            loginErrorLabel.setText("Username not found! Please check your username.");
            loginErrorLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private Customer findCustomerByUsername(String username) {
        for (Customer customer : customers) {
            if (customer.getUsername() != null && customer.getUsername().equals(username)) {
                return customer;
            }
        }
        return null;
    }

    // ========== REGISTRATION METHODS ==========
    @FXML
    private void onCustomerTypeChange() {
        if (individualRadio.isSelected()) {
            individualFields.setVisible(true);
            individualFields.setManaged(true);
            companyFields.setVisible(false);
            companyFields.setManaged(false);
        } else {
            individualFields.setVisible(false);
            individualFields.setManaged(false);
            companyFields.setVisible(true);
            companyFields.setManaged(true);
        }
    }

    @FXML
    private void handleRegister() {
        // Validate common fields (username and password)
        if (regUsernameField.getText().isEmpty() || regPasswordField.getText().isEmpty()) {
            registerMessageLabel.setText("Please enter username and password!");
            registerMessageLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        
        // Check if username already exists
        if (findCustomerByUsername(regUsernameField.getText()) != null) {
            registerMessageLabel.setText("Username already exists! Please choose a different username.");
            registerMessageLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        
        if (individualRadio.isSelected()) {
            // Register individual customer
            if (validateIndividualRegistration()) {
                Customer newCustomer = createIndividualCustomer();
                customers.add(newCustomer);
                currentCustomer = newCustomer;
                registerMessageLabel.setText("Individual customer registered successfully!");
                registerMessageLabel.setStyle("-fx-text-fill: green;");
                
                // Save to file
                saveCustomers();
                
                // AUTO-LOGIN AFTER REGISTRATION AND GO TO ACCOUNTS TAB
                loginTab.getTabPane().getSelectionModel().select(accountsTab);
                updateAccountDisplay();
                
                clearRegistrationForm();
            }
        } else {
            // Register company customer
            if (validateCompanyRegistration()) {
                Customer newCustomer = createCompanyCustomer();
                customers.add(newCustomer);
                currentCustomer = newCustomer;
                registerMessageLabel.setText("Company customer registered successfully!");
                registerMessageLabel.setStyle("-fx-text-fill: green;");
                
                // Save to file
                saveCustomers();
                
                // AUTO-LOGIN AFTER REGISTRATION AND GO TO ACCOUNTS TAB
                loginTab.getTabPane().getSelectionModel().select(accountsTab);
                updateAccountDisplay();
                
                clearRegistrationForm();
            }
        }
    }

    private Customer createIndividualCustomer() {
        Customer customer = new Customer(
            firstNameField.getText(),
            lastNameField.getText(),
            addressField.getText(), 
            phoneField.getText(),
            nationalIdField.getText()
        );
        customer.setUsername(regUsernameField.getText());
        customer.setPassword(regPasswordField.getText());
        return customer;
    }

    private Customer createCompanyCustomer() {
        CompanyCustomer company = new CompanyCustomer(
            companyNameField.getText(),
            companyAddressField.getText(),
            contactPersonField.getText(), 
            phoneField.getText()
        );
        company.setRegistrationNumber(regNumberField.getText());
        company.setUsername(regUsernameField.getText());
        company.setPassword(regPasswordField.getText());
        return company;
    }

    private boolean validateIndividualRegistration() {
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || 
            nationalIdField.getText().isEmpty() || phoneField.getText().isEmpty()) {
            registerMessageLabel.setText("Please fill all required fields!");
            registerMessageLabel.setStyle("-fx-text-fill: red;");
            return false;
        }
        return true;
    }

    private boolean validateCompanyRegistration() {
        if (companyNameField.getText().isEmpty() || regNumberField.getText().isEmpty() ||
            contactPersonField.getText().isEmpty()) {
            registerMessageLabel.setText("Please fill all required fields!");
            registerMessageLabel.setStyle("-fx-text-fill: red;");
            return false;
        }
        return true;
    }

    private void clearRegistrationForm() {
        regUsernameField.clear();
        regPasswordField.clear();
        firstNameField.clear();
        lastNameField.clear();
        nationalIdField.clear();
        addressField.clear();
        phoneField.clear();
        companyNameField.clear();
        regNumberField.clear();
        contactPersonField.clear();
        companyAddressField.clear();
    }

    // ========== ACCOUNT METHODS ==========
    @FXML
    private void onAccountTypeSelected() {
        String selected = accountTypeCombo.getValue();
        if ("Investment Account".equals(selected)) {
            minBalanceLabel.setText("Minimum Deposit: P500.00");
        } else {
            minBalanceLabel.setText("Minimum Deposit: P0.00");
        }
    }

    @FXML
    private void handleOpenAccount() {
        if (currentCustomer == null) {
            showAlert("Error", "Please register or login first!");
            return;
        }
        
        String type = accountTypeCombo.getValue();
        String depositText = initialDepositField.getText();
        
        if (type == null || type.isEmpty()) {
            showAlert("Error", "Please select account type!");
            return;
        }
        
        try {
            double deposit = Double.parseDouble(depositText);
            
            // Check minimum balance for investment account
            if ("Investment Account".equals(type) && deposit < 500.0) {
                showAlert("Error", "Investment account requires minimum P500.00 deposit!");
                return;
            }
            
            // Create new account based on type
            Account newAccount = createAccount(type, deposit);
            if (newAccount != null) {
                accounts.add(newAccount);
                currentCustomer.addAccount(newAccount);
                
                // Add transaction
                Transaction newTransaction = new Transaction(LocalDate.now().toString(), 
                    "Open Account", "New " + type, deposit, deposit);
                transactions.add(newTransaction);
                
                updateAccountDisplay();
                updateTransactionSummary();
                refreshTransactionTable();
                
                // Save to files
                saveAccounts();
                saveTransactions();
                
                showAlert("Success", "Account opened successfully!");
                initialDepositField.clear();
            }
            
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid deposit amount!");
        }
    }

    private Account createAccount(String type, double initialDeposit) {
        String accountNumber = generateAccountNumber();
        String branch = "Main Branch";
        
        switch (type) {
            case "Savings Account":
                return new SavingsAccount(accountNumber, initialDeposit, branch);
            case "Investment Account":
                return new InvestmentAccount(accountNumber, initialDeposit, branch);
            case "Cheque Account":
                return new ChequeAccount(accountNumber, initialDeposit, branch);
            default:
                return null;
        }
    }

    private String generateAccountNumber() {
        return "ACC" + (accounts.size() + 1);
    }

    @FXML
    private void handleCloseAccount() {
        if (selectedAccount == null) {
            showAlert("Error", "Please select an account to close!");
            return;
        }
        
        if (selectedAccount.getBalance() > 0) {
            showAlert("Error", "Cannot close account with balance. Withdraw funds first!");
            return;
        }
        
        // Remove account from customer and accounts list
        currentCustomer.removeAccount(selectedAccount);
        accounts.remove(selectedAccount);
        
        // Update display
        updateAccountDisplay();
        selectedAccount = null;
        selectedAccountLabel.setText("Select an account from list");
        accountBalanceLabel.setText("Balance: P0.00");
        
        // Save to file
        saveAccounts();
        
        showAlert("Success", "Account closed successfully!");
    }

    private void onAccountSelected(String accountDisplay) {
        if (accountDisplay != null && currentCustomer != null) {
            // Extract account number from display string
            String accountNumber = accountDisplay.split(" - ")[0];
            
            // Find the actual account object
            selectedAccount = findAccountByNumber(accountNumber);
            
            if (selectedAccount != null) {
                selectedAccountLabel.setText("Selected: " + selectedAccount.getAccountNumber());
                accountBalanceLabel.setText("Balance: P" + String.format("%.2f", selectedAccount.getBalance()));
            }
        }
    }

    private Account findAccountByNumber(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    private void updateAccountDisplay() {
        accountDisplayList.clear();
        double totalBalance = 0.0;
        
        if (currentCustomer != null) {
            for (Account account : currentCustomer.getAccounts()) {
                String display = account.getAccountNumber() + " - " + account.getAccountType() + " - P" + 
                    String.format("%.2f", account.getBalance());
                accountDisplayList.add(display);
                totalBalance += account.getBalance();
            }
        }
        
        accountListView.setItems(accountDisplayList);
        totalBalanceLabel.setText("Total Balance: P" + String.format("%.2f", totalBalance));
        
        // Update transaction combos
        updateTransactionCombos();
    }

    private void updateTransactionCombos() {
        ObservableList<String> accountNumbers = FXCollections.observableArrayList();
        if (currentCustomer != null) {
            for (Account account : currentCustomer.getAccounts()) {
                accountNumbers.add(account.getAccountNumber() + " - " + account.getAccountType());
            }
        }
        depositAccountCombo.setItems(accountNumbers);
        withdrawAccountCombo.setItems(accountNumbers);
    }

    // ========== TRANSACTION METHODS ==========
    @FXML
    private void onDepositAccountSelected() {
        String selectedAccountDisplay = depositAccountCombo.getValue();
        if (selectedAccountDisplay != null) {
            String accountNumber = selectedAccountDisplay.split(" - ")[0];
            Account account = findAccountByNumber(accountNumber);
            if (account != null) {
                depositBalanceLabel.setText("Current Balance: P" + String.format("%.2f", account.getBalance()));
            } else {
                depositBalanceLabel.setText("Current Balance: P0.00");
            }
        } else {
            depositBalanceLabel.setText("Current Balance: P0.00");
        }
    }

    @FXML
    private void onWithdrawAccountSelected() {
        String selectedAccountDisplay = withdrawAccountCombo.getValue();
        if (selectedAccountDisplay != null) {
            String accountNumber = selectedAccountDisplay.split(" - ")[0];
            Account account = findAccountByNumber(accountNumber);
            if (account != null) {
                withdrawBalanceLabel.setText("Current Balance: P" + String.format("%.2f", account.getBalance()));
            } else {
                withdrawBalanceLabel.setText("Current Balance: P0.00");
            }
        } else {
            withdrawBalanceLabel.setText("Current Balance: P0.00");
        }
    }

    @FXML
    private void handleDeposit() {
        if (currentCustomer == null) {
            showAlert("Error", "Please register or login first!");
            return;
        }
        
        String selectedAccountDisplay = depositAccountCombo.getValue();
        String amountText = depositAmountField.getText();
        
        if (selectedAccountDisplay == null || amountText.isEmpty()) {
            depositMessageLabel.setText("Please select account and enter amount!");
            depositMessageLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        
        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                depositMessageLabel.setText("Amount must be positive!");
                depositMessageLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            // Find the account
            String accountNumber = selectedAccountDisplay.split(" - ")[0];
            Account account = findAccountByNumber(accountNumber);
            
            if (account != null && account.deposit(amount)) {
                // Add transaction
                Transaction newTransaction = new Transaction(LocalDate.now().toString(), 
                    "Deposit", "Deposit to " + account.getAccountNumber(), amount, account.getBalance());
                transactions.add(newTransaction);
                
                depositMessageLabel.setText("Deposit successful! +P" + String.format("%.2f", amount));
                depositMessageLabel.setStyle("-fx-text-fill: green;");
                depositAmountField.clear();
                
                updateAccountDisplay();
                updateTransactionSummary();
                refreshTransactionTable();
                
                // Update the balance display after deposit
                onDepositAccountSelected();
                
                // Save to files
                saveAccounts();
                saveTransactions();
            } else {
                depositMessageLabel.setText("Deposit failed!");
                depositMessageLabel.setStyle("-fx-text-fill: red;");
            }
            
        } catch (NumberFormatException e) {
            depositMessageLabel.setText("Please enter valid amount!");
            depositMessageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleWithdraw() {
        if (currentCustomer == null) {
            showAlert("Error", "Please register or login first!");
            return;
        }
        
        String selectedAccountDisplay = withdrawAccountCombo.getValue();
        String amountText = withdrawAccountField.getText();
        
        if (selectedAccountDisplay == null || amountText.isEmpty()) {
            withdrawMessageLabel.setText("Please select account and enter amount!");
            withdrawMessageLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        
        try {
            double amount = Double.parseDouble(amountText);
            
            // Find the account
            String accountNumber = selectedAccountDisplay.split(" - ")[0];
            Account account = findAccountByNumber(accountNumber);
            
            if (account == null) {
                withdrawMessageLabel.setText("Account not found!");
                withdrawMessageLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            if (amount <= 0) {
                withdrawMessageLabel.setText("Amount must be positive!");
                withdrawMessageLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            if (account.withdraw(amount)) {
                // Add transaction
                Transaction newTransaction = new Transaction(LocalDate.now().toString(), 
                    "Withdrawal", "Withdrawal from " + account.getAccountNumber(), -amount, account.getBalance());
                transactions.add(newTransaction);
                
                withdrawMessageLabel.setText("Withdrawal successful! -P" + String.format("%.2f", amount));
                withdrawMessageLabel.setStyle("-fx-text-fill: green;");
                withdrawAccountField.clear();
                
                updateAccountDisplay();
                updateTransactionSummary();
                refreshTransactionTable();
                
                // Update the balance display after withdrawal
                onWithdrawAccountSelected();
                
                // Save to files
                saveAccounts();
                saveTransactions();
            } else {
                withdrawMessageLabel.setText("Withdrawal failed! Check balance or account type.");
                withdrawMessageLabel.setStyle("-fx-text-fill: red;");
            }
            
        } catch (NumberFormatException e) {
            withdrawMessageLabel.setText("Please enter valid amount!");
            withdrawMessageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    // ========== HISTORY & REPORT METHODS ==========
    @FXML
    private void handleGenerateReport() {
        updateTransactionSummary();
        showAlert("Report Generated", "Transaction report updated successfully!");
    }

    @FXML
    private void handleDeleteTransactions() {
        ObservableList<Transaction> selectedTransactions = transactionsTable.getSelectionModel().getSelectedItems();
        
        if (selectedTransactions.isEmpty()) {
            showAlert("No Selection", "Please select transactions to delete.");
            return;
        }
        
        // Confirm deletion
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete Transactions");
        confirmation.setContentText("Are you sure you want to delete " + selectedTransactions.size() + " selected transaction(s)?");
        
        if (confirmation.showAndWait().get() == ButtonType.OK) {
            // Remove selected transactions
            transactions.removeAll(selectedTransactions);
            
            // Save changes to file IMMEDIATELY after deletion
            saveTransactions();
            
            // Update summary
            updateTransactionSummary();
            
            // Refresh table
            refreshTransactionTable();
            
            showAlert("Success", selectedTransactions.size() + " transaction(s) deleted successfully!");
        }
    }

    @FXML
    private void handleRefreshHistory() {
        // Clear current transactions first to avoid duplicates
        transactions.clear();
        
        // Reload transactions from file
        loadTransactions();
        
        // Update table
        refreshTransactionTable();
        
        // Update summary
        updateTransactionSummary();
        
        showAlert("Refreshed", "Transaction history refreshed successfully! Loaded " + transactions.size() + " transactions.");
    }

    private void updateTransactionSummary() {
        double totalInterest = 0.0;
        for (Transaction transaction : transactions) {
            if ("Interest".equals(transaction.getType())) {
                totalInterest += transaction.getAmount();
            }
        }
        
        totalInterestLabel.setText("Total Interest Earned: P" + String.format("%.2f", totalInterest));
        totalTransactionsLabel.setText("Total Transactions: " + transactions.size());
    }

    private void refreshTransactionTable() {
        transactionsTable.refresh();
    }

    // ========== HELPER METHODS ==========
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ========== TRANSACTION CLASS ==========
    public static class Transaction {
        private final SimpleStringProperty date;
        private final SimpleStringProperty type;
        private final SimpleStringProperty description;
        private final SimpleDoubleProperty amount;
        private final SimpleDoubleProperty balance;

        public Transaction(String date, String type, String description, double amount, double balance) {
            this.date = new SimpleStringProperty(date);
            this.type = new SimpleStringProperty(type);
            this.description = new SimpleStringProperty(description);
            this.amount = new SimpleDoubleProperty(amount);
            this.balance = new SimpleDoubleProperty(balance);
        }

        // Standard getters for PropertyValueFactory
        public String getDate() { return date.get(); }
        public String getType() { return type.get(); }
        public String getDescription() { return description.get(); }
        public Double getAmount() { return amount.get(); }
        public Double getBalance() { return balance.get(); }

        // Property getters
        public SimpleStringProperty dateProperty() { return date; }
        public SimpleStringProperty typeProperty() { return type; }
        public SimpleStringProperty descriptionProperty() { return description; }
        public SimpleDoubleProperty amountProperty() { return amount; }
        public SimpleDoubleProperty balanceProperty() { return balance; }
    }
}