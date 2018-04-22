package entities;

import entities.address.Address;
import entities.address.Country;
import entities.address.Municipality;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import java.io.Serializable;
import java.sql.Date;

@Entity(name = "student")
@NamedQueries(
        value = {
                @NamedQuery(name = "Student.getByLoginId", query = "SELECT st FROM student st WHERE st.loginData.id = :loginId"),
                @NamedQuery(name = "Student.getAllReverse", query = "SELECT st FROM student st ORDER BY st.id DESC"),
                @NamedQuery(name = "Student.getStudent", query = "SELECT st FROM student st WHERE st.name = :name AND st.surname = :surname AND st.studyProgram = :studyProgram AND st.email = :email")
        }
)
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlID
    @XmlElement
    private int id;

    @Column(name = "register_number", length = 8, nullable = false, unique = true)
    private String registerNumber;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 60, nullable = false)
    private String surname;

    @Column(name = "date_of_birth", columnDefinition = "DATE")
    // ! note: String formatted as: YYYY-MM-DD
    private Date dateOfBirth;

    @Column(name = "place_of_birth")
    private String placeOfBirth;

    @ManyToOne
    @JoinColumn(name = "id_municipality_of_birth")
    private Municipality municipalityOfBirth;

    @ManyToOne
    @JoinColumn(name = "id_country_of_birth")
    private Country countryOfBirth;

    private char gender = '-';

    @Column(length = 13)
    private String emso;

    @ManyToOne
    @JoinColumn(name = "id_citizenship")
    private Country citizenship;

    @Column(name = "tax_number", length = 8)
    private String taxNumber;

    @ManyToOne
    @JoinColumn(name = "id_address1")
    private Address address1;

    @ManyToOne
    @JoinColumn(name = "id_address2")
    private Address address2;

    @Column(name = "sending_address")
    private int sendingAddress = 1;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(length = 80, nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "id_study_program")
    private StudyProgram studyProgram;

    //@XmlIDREF // if enabled, JSON has only ID, not whole userlogin object
    @OneToOne
    @JoinColumn(name = "id_login")
    private UserLogin loginData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public Municipality getMunicipalityOfBirth() {
        return municipalityOfBirth;
    }

    public void setMunicipalityOfBirth(Municipality municipalityOfBirth) {
        this.municipalityOfBirth = municipalityOfBirth;
    }

    public Country getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(Country countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getEmso() {
        return emso;
    }

    public void setEmso(String emso) {
        this.emso = emso;
    }

    public Country getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(Country citizenship) {
        this.citizenship = citizenship;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public Address getAddress1() {
        return address1;
    }

    public void setAddress1(Address address1) {
        this.address1 = address1;
    }

    public Address getAddress2() {
        return address2;
    }

    public void setAddress2(Address address2) {
        this.address2 = address2;
    }

    public int getSendingAddress() {
        return sendingAddress;
    }

    public void setSendingAddress(int sendingAddress) {
        this.sendingAddress = sendingAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserLogin getLoginData() {
        return loginData;
    }

    public void setLoginData(UserLogin loginData) {
        this.loginData = loginData;
    }

    public StudyProgram getStudyProgram() {
        return studyProgram;
    }

    public void setStudyProgram(StudyProgram studyProgram) {
        this.studyProgram = studyProgram;
    }
}
