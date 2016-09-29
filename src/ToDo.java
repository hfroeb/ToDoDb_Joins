import org.h2.tools.Server;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by halleyfroeb on 9/28/16.
 */


public class ToDo {

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, userName VARCHAR, password VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS todos (id IDENTITY, text VARCHAR, is_done BOOLEAN, author VARCHAR)");
    }
    public static void insertUser(Connection conn, String userName, String password) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?)");
        stmt.setString(1, userName);
        stmt.setString(2, password);
        stmt.execute();
    }

    public static void insertItem(Connection conn, String text, String author) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO todos VALUES (NULL, ?, false, ?)");
        stmt.setString(1, text);
        stmt.setString(2, author);
        stmt.execute();
    }
    public static void updateItem(Connection conn, int id, String text, String author)throws SQLException{
        PreparedStatement stmt = conn.prepareStatement
                ("UPDATE todos SET text = ?, author = ? WHERE id = ?");
        stmt.setString(1, text);
        stmt.setString(2, author);
        stmt.setInt(3, id);
        stmt.execute();
    }
    public static void deleteItem(Connection conn, int id)throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("DELETE todos WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }

    public static ToDoItem selectItem(Connection conn, int id)throws SQLException {
        PreparedStatement stmt = conn.prepareStatement
                ("SELECT * FROM todos INNER JOIN users ON todos.id = users.id WHERE todos.id = ?");
        stmt.setInt(1, id);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            int toDoId = results.getInt("id");
            String text = results.getString("text");
            boolean isDone = results.getBoolean("is_done");
            String author = results.getString("author");
            return new ToDoItem(toDoId, text, isDone, author);
        }
        return null;
    }

    public static ArrayList<ToDoItem> selectItems(Connection conn) throws SQLException {
        ArrayList<ToDoItem> items = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM todos");
        while (results.next()) {
            int id = results.getInt("id");
            String text = results.getString("text");
            boolean isDone = results.getBoolean("is_done");
            String author = results.getString("author");
            items.add(new ToDoItem(id, text, isDone, author));
        }
        return items;
    }
    public static User selectUser(Connection conn, String userName) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE userName = ?");
        stmt.setString(1, userName);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            int id = results.getInt("id");
            String password = results.getString("password");
            return new User(id, userName, password);
        }
        return null;
    }


    public static void toggleToDo(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE todos SET is_done = NOT is_done WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }


    public static void main(String[] args) throws SQLException {

        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Create to-do item");
            System.out.println("2. Toggle to-do item");
            System.out.println("3. List to-do items");

            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.println("Enter your to-do item:");
                    String text = scanner.nextLine();
                    String author = scanner.nextLine();
                    //  ToDoItem item = new ToDoItem(text, false);
                    insertItem(conn, text, author);
                    break;
                case "2":
                    System.out.println("Enter the number of the item you want to toggle:");
                    int itemNum = Integer.valueOf(scanner.nextLine());
                    // ToDoItem item2 = items.get(itemNum - 1);
                    //  item2.isDone = !item2.isDone;
                    toggleToDo(conn, itemNum);
                    break;
                case "3":
                    ArrayList<ToDoItem> todos = selectItems(conn);
                    for (ToDoItem todo : todos) {
                        String checkbox = "[ ] ";
                        if (todo.isDone) {
                            checkbox = "[x] ";
                        }
                        System.out.println(checkbox + todo.id + ". " + todo.text);
                        break;
                    }
            }
        }
    }
}