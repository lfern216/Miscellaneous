package tt.reddit.application.com.myapplicationreddt;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by lazaro on 7/26/18.
 */

@Root(name = "author", strict = false)
public class Author implements Serializable{

    @Element(name = "name")
    private String name;

    @Element(name = "url")
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}' + " ------------------------------------\n ";
    }
}
