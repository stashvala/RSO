package streaming.rating.persistence;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "rating")
@NamedQueries(value =
        {
                @NamedQuery(name = "rating.getAll", query = "SELECT s FROM rating s")
        })
@UuidGenerator(name =  "idGenerator")

public class Rating {
    @Id
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "timestamp")
    private Date timestamp;

    @Column(name = "rating")
    private double rating;


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


    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
