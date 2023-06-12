import java.sql.*;
import java.util.Scanner;

public class AdminManager {
    private static final String DB_URL = "jdbc:mysql://localhost/filmai?useSSL=false";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    public void runAdminApp(String username) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nPlease select an option:");
            System.out.println("1. Add Film Category");
            System.out.println("2. Edit Film Category");
            System.out.println("3. Delete Film Category");
            System.out.println("4. Add Movie");
            System.out.println("5. Edit Movie");
            System.out.println("6. Delete Movie");
            System.out.println("7. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addCategory();
                    break;
                case 2:
                    editCategory();
                    break;
                case 3:
                    deleteCategory(scanner);
                    break;
                case 4:
                    addMovie(scanner);
                    break;
                case 5:
                    editMovie();
                    break;
                case 6:
                    deleteMovie(scanner);
                    break;
                case 7:
                    System.out.println("Returning to the main menu.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    private void addCategory() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the name of the category:");
        String name = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO categories (name) VALUES (?)")) {

            stmt.setString(1, name);
            stmt.executeUpdate();

            System.out.println("Category added successfully!");
        } catch (SQLException e) {
            System.out.println("Failed to add category. Please try again.");
            e.printStackTrace();
        }
    }

    private void editCategory() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the ID of the category to edit:");
        int categoryId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter the new name for the category:");
        String newName = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("UPDATE categories SET name = ? WHERE category_id = ?")) {

            stmt.setString(1, newName);
            stmt.setInt(2, categoryId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Category updated successfully!");
            } else {
                System.out.println("No category found with the specified ID.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to edit category. Please try again.");
            e.printStackTrace();
        }
    }

    private void deleteCategory(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {


            System.out.println("List of Categories:");
            try (ResultSet rs = stmt.executeQuery("SELECT category_id, name FROM categories")) {
                while (rs.next()) {
                    int categoryId = rs.getInt("category_id");
                    String categoryName = rs.getString("name");
                    System.out.println(categoryId + ". " + categoryName);
                }
            }


            System.out.println("Enter the category ID to delete:");
            int categoryId = scanner.nextInt();


            String checkQuery = "SELECT COUNT(*) AS count FROM movies WHERE category_id = " + categoryId;
            try (ResultSet rs = stmt.executeQuery(checkQuery)) {
                rs.next();
                int movieCount = rs.getInt("count");
                if (movieCount > 0) {
                    System.out.println("Can't delete category because it is being used by a movie.");
                    return;
                }
            }


            String deleteQuery = "DELETE FROM categories WHERE category_id = " + categoryId;
            int rowsAffected = stmt.executeUpdate(deleteQuery);
            if (rowsAffected > 0) {
                System.out.println("Category with ID " + categoryId + " has been deleted.");
            } else {
                System.out.println("Category with ID " + categoryId + " does not exist.");
            }

        } catch (SQLException e) {
            System.out.println("Failed to delete category. Please try again.");
            e.printStackTrace();
        }
    }



    private void addMovie(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {


            System.out.println("List of Categories:");
            try (ResultSet rs = stmt.executeQuery("SELECT category_id, name FROM categories")) {
                boolean categoriesFound = false;
                while (rs.next()) {
                    int categoryId = rs.getInt("category_id");
                    String categoryName = rs.getString("name");
                    System.out.println(categoryId + ". " + categoryName);
                    categoriesFound = true;
                }
                if (!categoriesFound) {
                    System.out.println("No categories found, please make one first.");
                    return;
                }
            }


            System.out.println("Enter the name of the movie:");
            String name = scanner.nextLine();

            System.out.println("Enter the description of the movie:");
            String description = scanner.nextLine();

            System.out.println("Enter the rating of the movie:");
            double rating = scanner.nextDouble();

            System.out.println("Enter the category ID of the movie:");
            int categoryId = scanner.nextInt();


            String insertQuery = "INSERT INTO movies (name, description, rating, category_id) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertQuery);
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setDouble(3, rating);
            pstmt.setInt(4, categoryId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Movie created successfully.");
            } else {
                System.out.println("Failed to create movie. Please try again.");
            }

        } catch (SQLException e) {
            System.out.println("Failed to create movie. Please try again.");
            e.printStackTrace();
        }
    }



    private void editMovie() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the ID of the movie to edit:");
        int movieId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter the new name for the movie:");
        String newName = scanner.nextLine();

        System.out.println("Enter the new description for the movie:");
        String newDescription = scanner.nextLine();

        System.out.println("Enter the new rating for the movie:");
        double newRating = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Enter the new category ID for the movie:");
        int newCategoryId = scanner.nextInt();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("UPDATE movies SET name = ?, description = ?, rating = ?, category_id = ? WHERE movie_id = ?")) {

            stmt.setString(1, newName);
            stmt.setString(2, newDescription);
            stmt.setDouble(3, newRating);
            stmt.setInt(4, newCategoryId);
            stmt.setInt(5, movieId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Movie updated successfully!");
            } else {
                System.out.println("No movie found with the specified ID.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to edit movie. Please try again.");
            e.printStackTrace();
        }
    }

    private void deleteMovie(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {


            System.out.println("List of Movies:");
            try (ResultSet rs = stmt.executeQuery("SELECT movie_id, name FROM movies")) {
                while (rs.next()) {
                    int movieId = rs.getInt("movie_id");
                    String movieName = rs.getString("name");
                    System.out.println(movieId + ". " + movieName);
                }
            }


            System.out.println("Enter the movie ID to delete:");
            int movieId = scanner.nextInt();


            String deleteQuery = "DELETE FROM movies WHERE movie_id = " + movieId;
            int rowsAffected = stmt.executeUpdate(deleteQuery);
            if (rowsAffected > 0) {
                System.out.println("Movie with ID " + movieId + " has been deleted.");
            } else {
                System.out.println("Movie with ID " + movieId + " does not exist.");
            }

        } catch (SQLException e) {
            System.out.println("Failed to delete movie. Please try again.");
            e.printStackTrace();
        }
    }


}
