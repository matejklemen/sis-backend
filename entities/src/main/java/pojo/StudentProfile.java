package pojo;

import entities.UserLogin;
import entities.Student;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *  Class used for retaining full profile of a student,
 *  primarily used at student Import as return values.
 */
@XmlRootElement
public class StudentProfile {

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String surname;
    @XmlElement(required = true)
    private String email;
    @XmlElement(required = true)
    private String course;
    @XmlElement(required = true)
    private String studentId;
    @XmlElement(required = true)
    private String username;
    @XmlElement(required = true)
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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


    public static StudentProfile setStudentProfile(Student sd, UserLogin ul, String password){
        StudentProfile sp = new StudentProfile();
        sp.setName(sd.getName());
        sp.setSurname(sd.getSurname());
        sp.setEmail(sd.getEmail());
        sp.setCourse(sd.getStudyProgram().getName());
        sp.setStudentId(sd.getRegisterNumber());
        sp.setPassword(password);
        sp.setUsername(ul.getUsername());

        return sp;
    }

}
