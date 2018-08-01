package tt.reddit.application.com.myapplicationreddt;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by lazaro on 7/26/18.
 */

@Root(name = "entry", strict = false)
public class Entry implements Serializable{


    @Element(name = "content")
    private String content;

    @Element(required = false, name = "author")
    private Author author;

    @Element(name = "id")
    private String id;

    @Element(name = "title")
    private String title;

    @Element(name = "updated")
    private String updated;

    public Entry(){

    }


    public Entry(String content, Author author, String id, String title, String updated) {
        this.content = content;
        this.author = author;
        this.id = id;
        this.title = title;
        this.updated = updated;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "content='" + content + '\'' + "\n" +
                ", author='" + author + '\'' + "\n" +
                ", id='" + id + '\'' + "\n" +
                ", title='" + title + '\'' + "\n" +
                ", updated='" + updated + '\'' + "\n" +
                '}' + "-----------------------------\n";
    }
}
