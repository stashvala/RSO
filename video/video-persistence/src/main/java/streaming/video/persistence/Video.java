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

    @Column(name = "name")
    private String name;


    // getter and setter methods

    public String getId(){
        return id;
    }

    public void setId(String new_id){
        id = new_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}