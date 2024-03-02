import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class User {
    private String userID;
    private String userPIN;
    private double accountBalance;

    public User(String userID, String userPIN, double accountBalance) {
        this.userID = userID;
        this.userPIN = userPIN;
        this.accountBalance = accountBalance;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserPIN() {
        return userPIN;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }
}

class ATM {
    private Map<String, User> users;
    private Scanner scanner;
    private final String DATA_FILE = "userdata.txt";

    public ATM() {
        this.users = new HashMap<>();
        this.scanner = new Scanner(System.in);
        loadUserData(); // Load user data from file on startup
    }

    public void addUser(String userID, String userPIN, double initialBalance) {
        users.put(userID, new User(userID, userPIN, initialBalance));
    }

    public void start() {
        System.out.println("Welcome to the ATM!");

        // Simulate user authentication
        User currentUser = authenticateUser();
        if (currentUser == null) {
            System.out.println("Authentication failed. Exiting...");
            return;
        }

        // Display ATM functionalities
        System.out.println("Welcome, " + currentUser.getUserID() + "!");
        System.out.println("1. Check Balance");
        System.out.println("2. Withdraw Money");
        System.out.println("3. Deposit Money");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                checkBalance(currentUser);
                break;
            case 2:
                withdrawMoney(currentUser);
                break;
            case 3:
                depositMoney(currentUser);
                break;
            default:
                System.out.println("Invalid choice. Exiting...");
        }

        saveUserData(); // Save user data to file after each operation
    }

    private User authenticateUser() {
        System.out.print("Enter User ID: ");
        String userID = scanner.next();
        System.out.print("Enter PIN: ");
        String userPIN = scanner.next();

        if (users.containsKey(userID)) {
            User user = users.get(userID);
            if (user.getUserPIN().equals(userPIN)) {
                return user;
            }
        }
        return null;
    }

    private void checkBalance(User user) {
        System.out.println("Your current balance: $" + user.getAccountBalance());
    }

    private void withdrawMoney(User user) {
        System.out.print("Enter amount to withdraw: $");
        double amount = scanner.nextDouble();
        if (amount > user.getAccountBalance()) {
            System.out.println("Insufficient funds!");
        } else {
            user.setAccountBalance(user.getAccountBalance() - amount);
            System.out.println("Withdrawal successful. Remaining balance: $" + user.getAccountBalance());
        }
    }

    private void depositMoney(User user) {
        System.out.print("Enter amount to deposit: $");
        double amount = scanner.nextDouble();
        user.setAccountBalance(user.getAccountBalance() + amount);
        System.out.println("Deposit successful. Updated balance: $" + user.getAccountBalance());
    }

    private void loadUserData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                String userID = userData[0];
                String userPIN = userData[1];
                double accountBalance = Double.parseDouble(userData[2]);
                users.put(userID, new User(userID, userPIN, accountBalance));
            }
        } catch (IOException e) {
            System.out.println("Error loading user data: " + e.getMessage());
        }
    }

    private void saveUserData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (User user : users.values()) {
                writer.write(user.getUserID() + "," + user.getUserPIN() + "," + user.getAccountBalance());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }


}

public class Main {
    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.addUser("ANIKET", "1234", 10000.0); // Sample user
        atm.start();
    }
}
