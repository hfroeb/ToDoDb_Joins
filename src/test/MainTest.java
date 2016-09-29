

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by halleyfroeb on 9/28/16.
 */
public class MainTest {
    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");// will only go in temp memory
        ToDo.createTables(conn);
        return conn;
    }

    @Test //test insertUser and selectUser
    public void testUser() throws SQLException {
        Connection conn = startConnection();
        ToDo.insertUser(conn, "Charlie", "");     //test data put into database
        User user = ToDo.selectUser(conn, "Charlie"); // pull out of database
        conn.close(); // only do in tests
        assertTrue(user != null);
    }

    @Test //test insertItem, select Items and selectItem
    public void testItems() throws SQLException {
        Connection conn = startConnection();
        ToDo.insertItem(conn, "wash car", "Charlie");
        ToDo.insertItem(conn, "clean room", "Bob");
        ToDo.insertUser(conn, "Charlie", "");
        ArrayList<ToDoItem> items = ToDo.selectItems(conn);
        Assert.assertTrue(items.size() == 2); // select items
        ToDoItem item = ToDo.selectItem(conn, 1);
        conn.close();
        Assert.assertTrue(item.id == 1);
    }
    @Test // updateItem
    public void testUpdateItem() throws SQLException{
        Connection conn = startConnection();
        ToDo.insertItem(conn, "wash car", "Charlie");
        ToDo.insertItem(conn, "clean room", "Bob");
        ToDo.insertUser(conn, "Charlie", "");
        ArrayList<ToDoItem> items = ToDo.selectItems(conn);
        ToDoItem item = ToDo.selectItem(conn, 1);
        Assert.assertTrue(item.text.equalsIgnoreCase("wash car"));
        ToDo.updateItem(conn, 1, "wash face", "Charlie");
        ArrayList<ToDoItem> items2 = ToDo.selectItems(conn);
        ToDoItem item2 = ToDo.selectItem(conn, 1);
        assertTrue(item2.text.equalsIgnoreCase("wash face"));
        conn.close();

    }
    @Test // deleteItem
    public void testDeleteItem() throws SQLException{
        Connection conn = startConnection();
        ToDo.insertItem(conn, "wash car", "Charlie");
        ToDo.insertItem(conn, "clean room", "Bob");
        ToDo.deleteItem(conn, 1);
        ArrayList<ToDoItem> items = ToDo.selectItems(conn);
        conn.close();
        assertTrue(items.size() == 1);
    }
}

