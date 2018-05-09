package entities;

import entities.curriculum.ExamSignUp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity(name = "exam_sign_up_history")
@NamedQueries(
        value = {
                @NamedQuery(name = "ExamSignUpHistory.getByExamSignUpId", query = "SELECT esuh FROM exam_sign_up_history esuh WHERE esuh.examSignUp.id = :exam_sign_up_id"),
        }
)
public class ExamSignUpHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_exam_sign_up")
    private ExamSignUp examSignUp;

    private Timestamp datetime;

    @ManyToOne
    @JoinColumn(name = "user_role")
    private UserLogin userLogin;

    private String action; // can be "prijava" or "odjava"

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ExamSignUp getExamSignUp() {
        return examSignUp;
    }

    public void setExamSignUp(ExamSignUp examSignUp) {
        this.examSignUp = examSignUp;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public UserLogin getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
