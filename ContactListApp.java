import java.sql.*;
import java.sql.DriverManager;
import java.util.Scanner;

public class ContactListApp {
    private static final String url = "jdbc:mysql://localhost:3306/contact_db";
    private static final String username = "Sujal";
    private static final String password = "4518@sujal";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load JDBC driver
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found: " + e.getMessage());
        }

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\nCONTACT LIST MANAGEMENT");
                System.out.println("1. Add New Contact");
                System.out.println("2. View All Contacts");
                System.out.println("3. Search Contact by ID");
                System.out.println("4. Update Contact");
                System.out.println("5. Delete Contact");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addNewContact(connection, scanner);
                        break;
                    case 2:
                        viewAllContacts(connection);
                        break;
                    case 3:
                        searchContactById(connection, scanner);
                        break;
                    case 4:
                        updateContact(connection, scanner);
                        break;
                    case 5:
                        deleteContact(connection, scanner);
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addNewContact(Connection connection, Scanner scanner) {
        String sql = "INSERT INTO contacts (name, phone_number) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            System.out.print("Enter contact name: ");
            String name = scanner.next();
            System.out.print("Enter phone number: ");
            String phoneNumber = scanner.next();

            statement.setString(1, name);
            statement.setString(2, phoneNumber);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Contact added successfully!");
            } else {
                System.out.println("Failed to add contact.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewAllContacts(Connection connection) {
        String sql = "SELECT * FROM contacts";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("+----+-----------------+-----------------+");
            System.out.println("| ID | Name            | Phone Number    |");
            System.out.println("+----+-----------------+-----------------+");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String phoneNumber = resultSet.getString("phone_number");

                System.out.printf("| %-2d | %-15s | %-15s |\n", id, name, phoneNumber);
            }

            System.out.println("+----+-----------------+-----------------+");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void searchContactById(Connection connection, Scanner scanner) {
        String sql = "SELECT * FROM contacts WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            System.out.print("Enter contact ID: ");
            int id = scanner.nextInt();

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Contact Details:");
                    System.out.println("ID: " + resultSet.getInt("id"));
                    System.out.println("Name: " + resultSet.getString("name"));
                    System.out.println("Phone Number: " + resultSet.getString("phone_number"));
                } else {
                    System.out.println("Contact not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateContact(Connection connection, Scanner scanner) {
        String sql = "UPDATE contacts SET name = ?, phone_number = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            System.out.print("Enter contact ID to update: ");
            int id = scanner.nextInt();
            System.out.print("Enter new name: ");
            String name = scanner.next();
            System.out.print("Enter new phone number: ");
            String phoneNumber = scanner.next();

            statement.setString(1, name);
            statement.setString(2, phoneNumber);
            statement.setInt(3, id);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Contact updated successfully!");
            } else {
                System.out.println("Contact update failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteContact(Connection connection, Scanner scanner) {
        String sql = "DELETE FROM contacts WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            System.out.print("Enter contact ID to delete: ");
            int id = scanner.nextInt();

            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Contact deleted successfully!");
            } else {
                System.out.println("Contact deletion failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

