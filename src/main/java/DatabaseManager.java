import java.sql.*;
import java.util.Scanner;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost/filmai?useSSL=false";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    public void runRegistrationApp() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nPlease select an option:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    registerUser();
                    break;
                case "2":
                    loginUser(scanner);
                    break;
                case "3":
                    System.out.println("Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    private void registerUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter your username:");
        String username = scanner.nextLine();

        System.out.println("Please enter your password:");
        String password = scanner.nextLine();

        System.out.println("Is the user an admin? (true/false):");
        boolean isAdmin = scanner.nextBoolean();
        scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password, isAdmin) VALUES (?, ?, ?)")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setBoolean(3, isAdmin);
            stmt.executeUpdate();

            System.out.println("Registration successful!");
        } catch (SQLException e) {
            System.out.println("Registration failed. Please try again.");
            e.printStackTrace();
        }
    }

    private void loginUser(Scanner scanner) {

        System.out.println("Enter your username:");
        String username = scanner.nextLine();

        System.out.println("Enter your password:");
        String password = scanner.nextLine();


        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            String query = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
            try (ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    boolean isAdmin = rs.getBoolean("isAdmin");
                    System.out.println("Login successful!");

                    if (isAdmin) {
                        AdminManager adminManager = new AdminManager();
                        adminManager.runAdminApp(username);
                    } else {
                        runUserApp(scanner);
                    }
                } else {
                    System.out.println("Invalid username or password.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Login failed. Please try again.");
            e.printStackTrace();
        }
    }



    private void runUserApp(Scanner scanner) {
        while (true) {
            System.out.println("\nPlease select an option:");
            System.out.println("1. Search movies by name");
            System.out.println("2. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    searchMoviesByName(scanner);
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    private void searchMoviesByName(Scanner scanner) {
        System.out.println("Enter the movie name to search:");
        String searchName = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            String query = "SELECT * FROM movies WHERE name LIKE '%" + searchName + "%'";
            try (ResultSet rs = stmt.executeQuery(query)) {
                boolean foundMovies = false;
                while (rs.next()) {
                    int movieId = rs.getInt("movie_id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    double rating = rs.getDouble("rating");
                    int categoryId = rs.getInt("category_id");

                    System.out.println("Movie ID: " + movieId);
                    System.out.println("Name: " + name);
                    System.out.println("Description: " + description);
                    System.out.println("Rating: " + rating);
                    System.out.println("Category ID: " + categoryId);
                    System.out.println("--------------------");
                    foundMovies = true;
                }
                if (!foundMovies) {
                    System.out.println("No movies found with the name '" + searchName + "'.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Failed to search movies. Please try again.");
            e.printStackTrace();
        }
    }
}
