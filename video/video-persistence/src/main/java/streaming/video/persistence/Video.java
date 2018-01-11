package streaming.video.persistence;
import org.eclipse.persistence.annotations.UuidGenerator;

        import javax.persistence.*;
        import java.io.Serializable;
        import java.util.Date;

@Entity(name = "video")
@NamedQueries(value =
        {
                @NamedQuery(name = "Video.getAll", query = "SELECT v FROM video v")
        })
@UuidGenerator(name =  "idGenerator")
public class Video{

    @Id
    @GeneratedValue(generator = "idGenerator")
    private String id;

    //User that saved video "for later" - should be a list
    @Column(name = "user_id")
    private String userId;

    @Column(name ="uploader_id")
    private String uploaderId;

    @Column(name = "title")
    private String title;

    @Column(name = "duration")
    private double duration;

    @Column(name = "views")
    private int views;

    @Column(name = "genre")
    private String genre;
    // getter and setter methods

    public String getId(){
        return id;
    }

    public void setId(String new_id){
        id = new_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}