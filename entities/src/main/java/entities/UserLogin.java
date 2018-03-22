package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "login")
@NamedQueries(
        value = {
                @NamedQuery(name = "UserLogin.getAll", query = "SELECT ul FROM login ul")
        }
)
public class UserLogin implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(length = 128, nullable = false)
    private String password;
    @Column(length = 20)
    private String role;
    @Column(nullable = false)
    private int salt;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getSalt() {
        return salt;
    }

    public void setSalt(int salt) {
        this.salt = salt;
    }
}
