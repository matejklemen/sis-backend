package entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import java.io.Serializable;

@Entity(name = "post_address")
@Access(AccessType.FIELD)
@NamedQueries(
        value = {
                @NamedQuery(name = "PostAddress.getAll", query = "SELECT pa FROM post_address pa"),
        }
)
public class PostAddress implements Serializable {

    @Id
    @XmlID
    @XmlElement
    private int id;

    @Column(nullable = false)
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
