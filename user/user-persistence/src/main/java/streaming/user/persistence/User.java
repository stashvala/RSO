package streaming.user.persistence;
import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "user")
@NamedQueries(value =
    {
            @NamedQuery(name = "User.getAll", query = "SELECT * FROM user")
    })
@UuidGenerator(name =  "idGenerator")
public class User{

    @Id
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String address;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    // getter and setter methods

    public String getId(){
        return id;
    }

    public void setId(String new_id){
        id = new_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dob) {
        this.dateOfBirth = dob;
    }
}