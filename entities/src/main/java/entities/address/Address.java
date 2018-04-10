package entities.address;


import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "address")
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "line1", nullable = false)
    private String line1;

    @Column(name = "line2")
    private String line2;

    @ManyToOne
    @JoinColumn(name = "id_post_address", nullable = false)
    private PostAddress post;

    @ManyToOne
    @JoinColumn(name = "id_country")
    private Country country;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public PostAddress getPost() {
        return post;
    }

    public void setPost(PostAddress post) {
        this.post = post;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
