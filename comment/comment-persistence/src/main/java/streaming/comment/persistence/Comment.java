package streaming.comment.persistence;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "comment")
@NamedQueries(value =
        {
                @NamedQuery(name = "comment.getAll", query = "SELECT s FROM comment s")
        })
@UuidGenerator(name =  "idGenerator")

public class Comment {
    @Id
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "timestamp")
    private Date timestamp;

    @Column(name = "text")
    private String text;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
