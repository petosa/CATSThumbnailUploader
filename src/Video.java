/**
 * Created by Nick on 5/13/2016.
 */
public class Video {

    private String title;
    private String id;

    public Video(String myTitle, String myId) {
        title = myTitle;
        id = myId;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String toString() {
        return "{title=" + title + ", id=" + id + "}";
    }

}
