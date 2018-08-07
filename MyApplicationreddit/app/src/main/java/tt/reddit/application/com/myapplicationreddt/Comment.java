package tt.reddit.application.com.myapplicationreddt;

/**
 * Created by lazaro on 8/6/18.
 */

public class Comment {

    private String author;
    private String comment;
    private String id;
    private String updated;

    public Comment(String author, String comment, String id, String updated) {
        this.author = author;
        this.comment = comment;
        this.id = id;
        this.updated = updated;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "author='" + author + '\'' +
                ", comment='" + comment + '\'' +
                ", id='" + id + '\'' +
                ", updated='" + updated + '\'' +
                '}';
    }
}
