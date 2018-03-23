package entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import java.io.Serializable;

@Entity(name = "user_login")
@NamedQueries(
        value = {
                @NamedQuery(name = "UserLogin.getAll", query = "SELECT ul FROM user_login ul"),
                @NamedQuery(name = "UserLogin.getSaltAndPasswordByUsername", query = "SELECT ul FROM user_login ul WHERE ul.username = :username")
        }
)
public class UserLogin implements Serializable {

    @XmlID
    @XmlElement
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 64, unique = true, nullable = false)
    private String username;

    @Column(length = 128, nullable = false)
    private String password;

    @Column(nullable = false)
    private int salt;

    @ManyToOne
    @JoinColumn(name = "id_user_role")
    private UserRole role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSalt() {
        return salt;
    }

    public void setSalt(int salt) {
        this.salt = salt;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

}
