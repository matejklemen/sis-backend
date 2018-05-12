package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "staff")
@NamedQueries(
        value = {
                @NamedQuery(name = "Staff.getByLoginid", query = "SELECT s FROM staff s WHERE s.loginData.id = :loginId ")
        }
)
public class Staff implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name1", nullable = false)
    private String lastName1;

    @Column(name = "last_name2")
    private String lastName2;

    @OneToOne
    @JoinColumn(name = "id_login")
    private UserLogin loginData;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName1() {
        return lastName1;
    }

    public void setLastName1(String lastName1) {
        this.lastName1 = lastName1;
    }

    public String getLastName2() {
        return lastName2;
    }

    public void setLastName2(String lastName2) {
        this.lastName2 = lastName2;
    }

    public UserLogin getLoginData() {
        return loginData;
    }

    public void setLoginData(UserLogin loginData) {
        this.loginData = loginData;
    }

}
