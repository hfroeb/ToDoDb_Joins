/**
 * Created by halleyfroeb on 9/28/16.
 */
public class ToDoItem {
    int id;
    String text;
    boolean isDone;
    String author;

    public ToDoItem(int id, String text, boolean isDone, String author) {
        this.id = id;
        this.text = text;
        this.isDone = isDone;
        this.author = author;
    }

    public ToDoItem(String text, boolean isDone) {
        this.text = text;
        this.isDone = isDone;
    }
}

