package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "user_role")
@NamedQueries(
        value = {
            @NamedQuery(name = "UserRole.getByName", query = "SELECT ur FROM user_role ur WHERE ur.name = :name")
        }
)
public class UserRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 20, nullable = false)
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
