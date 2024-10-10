package coe528.project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Bank extends Application {

    private Button loginButton;
    private Scene loginPage, managerPage, customerPage;
    private static ArrayList<Customer> customers = new ArrayList<>();
    private static Manager admin = new Manager();
    private Customer tempCust;
    private int index = -1;

    private static final String USER_DIR = System.getProperty("user.dir");
    private static final String LOGIN_TITLE = "Bank Login";
    private static final String INVALID_LOGIN_MSG = "Invalid login credentials!";
    private static final String USER_NOT_EXIST_MSG = "User does not exist!";
    private static final String ERROR_MSG = "Error";
    // Global dimensions for windows
    private static final double WINDOW_WIDTH = 600;
    private static final double WINDOW_HEIGHT = 600;

    public static void main(String[] args) {
          String currentDirectory = System.getProperty("user.dir"); 
         //Adding already existing text files to arrayList of customers
         File dir = new File(currentDirectory);
         for(File file : dir.listFiles()){
             if(file.getName().endsWith((".txt"))){
                    try(Scanner readFile = new Scanner(file)){
                        String un = readFile.next();
                        String pw = readFile.next();
                        int balance = Integer.parseInt(readFile.next());
                    //    System.out.println(un);
                    //    System.out.println(pw);
                     //   System.out.println(balance);
                        customers.add(new Customer(un,pw));
                        for(Customer c : customers){
                            if(c.getUsername().equals(un))
                                c.depositMoney(balance - 100); 
                                c.setAccountLevel();
                        }
                    }
                    catch(Exception e3){}  
             }
         }
        launch(args);
    }

    

    @Override
    public void start(Stage window) throws Exception {
        window.setTitle(LOGIN_TITLE);
        setupLoginPage(window);
        setupManagerPage(window);
        setupCustomerPage(window);
        window.setScene(loginPage);
        window.setWidth(WINDOW_WIDTH);
        window.setHeight(WINDOW_HEIGHT);
        window.show();
        
    }

    private void setupLoginPage(Stage window) {
        Label header = new Label("Login to your Bank Account");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label label1 = new Label("Username");
        Label label2 = new Label("Password");

        TextField username = new TextField();
        PasswordField password = new PasswordField();
        loginButton = new Button("Login");

        loginButton.setOnAction(e -> handleLogin(window, username.getText(), password.getText(), username, password));

        VBox layout1 = new VBox(10);
        layout1.setPadding(new Insets(20));
        layout1.getChildren().addAll(header, label1, username, label2, password, loginButton);
        layout1.setAlignment(Pos.CENTER);

        loginPage = new Scene(layout1, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    private void handleLogin(Stage window, String username, String password, TextField usernameField, PasswordField passwordField) {
        File file = new File(username + ".txt");
        if (file.exists()) {
            try (Scanner readFile = new Scanner(file)) {
                String usernameOnFile = readFile.next();
                String passwordOnFile = readFile.next();

                if (username.equals(usernameOnFile) && password.equals(passwordOnFile)) {
                    if (username.equals("admin")) {
                        window.setScene(managerPage);
                    } else {
                        tempCust = customers.stream().filter(c -> c.getUsername().equals(username)).findFirst().orElse(null);
                        if (tempCust != null) {
                            window.setScene(customerPage);
                        }
                    }
                    usernameField.clear();
                    passwordField.clear();
                } else {
                    AlertBox.display(ERROR_MSG, INVALID_LOGIN_MSG);
                }
            } catch (Exception e) {
                System.out.println("Error reading file: " + e.getMessage());
            }
        } else {
            AlertBox.display(ERROR_MSG, USER_NOT_EXIST_MSG);
        }
    }

   private void setupManagerPage(Stage window) {
    Label header = new Label("Manager Console");
    header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
    
    Label label4 = new Label("New Customer's username:");
    Label label5 = new Label("New Customer's password:");
    Label label7 = new Label("Enter the username of the customer you wish to delete:");

    TextField createUsername = new TextField();
    TextField createPassword = new TextField();
    TextField userToDelete = new TextField();

    Button createCustomer = new Button("Create Customer");
    Button deleteCustomer = new Button("Delete Customer");
    Button managerLogout = new Button("Logout");

    createCustomer.setOnAction(e -> {
        String username = createUsername.getText();
        String password = createPassword.getText();
        Customer newCustomer = new Customer(username, password);
        customers.add(newCustomer);
        saveCustomerToFile(newCustomer);
        createUsername.clear();
        createPassword.clear();
        AlertBox.display("New Customer", "Customer has been created!");
    });

    deleteCustomer.setOnAction(e -> {
    String deleteThisUsername = userToDelete.getText();
    boolean customerExists = customers.stream().anyMatch(c -> c.getUsername().equals(deleteThisUsername));

    if (customerExists) {
        // Remove the customer from the ArrayList
        customers.removeIf(c -> c.getUsername().equals(deleteThisUsername));

        // Delete the corresponding file
        File file = new File(deleteThisUsername + ".txt");
        if (file.exists()) {
            if (file.delete()) {
                AlertBox.display("Delete Customer", "Customer profile deleted successfully!");
            } else {
                AlertBox.display("Error", "Failed to delete customer profile!");
            }
        }
    } else {
        AlertBox.display("Error", "Customer does not exist!");
    }

    userToDelete.clear();
});

    managerLogout.setOnAction(e -> {
        window.setScene(loginPage);
        createUsername.clear();
        createPassword.clear();
        userToDelete.clear();
    });

    VBox layout2 = new VBox(10);
    layout2.setPadding(new Insets(20));
    layout2.getChildren().addAll(header, label4, createUsername, label5, createPassword, createCustomer, label7, userToDelete, deleteCustomer, managerLogout);
    layout2.setAlignment(Pos.CENTER);

    managerPage = new Scene(layout2, WINDOW_WIDTH, WINDOW_HEIGHT);
}

    private void saveCustomerToFile(Customer customer) {
        try (FileWriter writeToFile = new FileWriter(customer.getUsername() + ".txt")) {
            writeToFile.write(customer.getUsername() + "\n");
            writeToFile.write(customer.getPassword() + "\n");
            writeToFile.write("" + customer.getBalance());
        } catch (Exception e) {
            System.out.println("Error saving customer data: " + e.getMessage());
        }
    }

    private void setupCustomerPage(Stage window) {
        Label header = new Label("Your Bank account");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label dLabel = new Label("Enter amount you wish to deposit:");
        Label wLabel = new Label("Enter amount you wish to withdraw:");
        Label opLabel = new Label("Enter cost of product you wish to purchase online:");

        TextField depositMoney = new TextField();
        TextField withdrawMoney = new TextField();
        TextField op = new TextField();

        Button getBalanceButton = new Button("Get Balance");
        Button getCurrentLevel = new Button("Get Current Level");
        Button doDepositAction = new Button("Deposit");
        Button doWithdrawAction = new Button("Withdraw");
        Button doOnlinePurchase = new Button("Complete Transaction");
        Button userLogout = new Button("Logout");

        getBalanceButton.setOnAction(e -> AlertBox.display("Balance", "Balance is $" + tempCust.getBalance()));

        getCurrentLevel.setOnAction(e -> AlertBox.display("Account Level", tempCust.getAccountLevel()));

        doDepositAction.setOnAction(e -> handleDeposit(depositMoney));

        doWithdrawAction.setOnAction(e -> handleWithdraw(withdrawMoney));

        doOnlinePurchase.setOnAction(e -> handleOnlinePurchase(op));

        userLogout.setOnAction(e -> handleLogout(window, depositMoney, withdrawMoney, op));

        VBox layout3 = new VBox(10);
        layout3.setPadding(new Insets(20));
        layout3.getChildren().addAll(header, getBalanceButton, getCurrentLevel, dLabel, depositMoney, doDepositAction, wLabel, withdrawMoney, doWithdrawAction, opLabel, op, doOnlinePurchase, userLogout);
        layout3.setAlignment(Pos.CENTER);

        customerPage = new Scene(layout3, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    private void handleDeposit(TextField depositMoney) {
        try {
            int amount = Integer.parseInt(depositMoney.getText());
            tempCust.depositMoney(amount);
            depositMoney.clear();
        } catch (NumberFormatException e) {
            AlertBox.display(ERROR_MSG, "An integer must be entered!");
            depositMoney.clear();
        }
    }

    private void handleWithdraw(TextField withdrawMoney) {
        try {
            int amount = Integer.parseInt(withdrawMoney.getText());
            tempCust.withdrawMoney(amount);
            withdrawMoney.clear();
        } catch (NumberFormatException e) {
            AlertBox.display(ERROR_MSG, "An integer must be entered!");
            withdrawMoney.clear();
        }
    }

    private void handleOnlinePurchase(TextField op) {
        try {
            int amount = Integer.parseInt(op.getText());
            tempCust.onlinePurchase(amount);
            op.clear();
        } catch (NumberFormatException e) {
            AlertBox.display(ERROR_MSG, "An integer must be entered!");
            op.clear();
       
}
}
    private void handleLogout(Stage window, TextField depositMoney, TextField withdrawMoney, TextField op) {
    saveCustomerToFile(tempCust);
    window.setScene(loginPage);
    depositMoney.clear();
    withdrawMoney.clear();
    op.clear();
}
}
