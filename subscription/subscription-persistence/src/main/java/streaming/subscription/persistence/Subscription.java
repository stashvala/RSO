package streaming.subscription.persistence;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "subscription")
@NamedQueries(value =
        {
                @NamedQuery(name = "subscription.getAll", query = "SELECT s FROM subscription s")
        })
@UuidGenerator(name =  "idGenerator")

public class Subscription {
    @Id
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "type")
    private String type;

    @Column(name = "valid_from")
    private Date validFrom;

    @Column(name = "valid_until")
    private Date validUntil;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }
    // getter and setter methods


}
