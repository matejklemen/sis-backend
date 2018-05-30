package entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(name = "agreement")
public class Agreement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agreement")
    private int idAgreement;

    @Column(name = "issue_date", nullable = false, columnDefinition = "DATE")
    // ! note: String formatted as: YYYY-MM-DD
    private Date issueDate;

    @Column(name = "valid_until", nullable = true, columnDefinition = "DATE")
    // ! note: String formatted as: YYYY-MM-DD
    private Date validUntil;

    @Column(name = "content_slo", length = 512)
    private String contentSlovene;

    @Column(name = "content_eng", length = 512)
    private String contentEnglish;

    private String issuer;

    @ManyToOne
    @JoinColumn
    private Student student;

    private boolean deleted;

    public int getIdAgreement() {
        return idAgreement;
    }

    public void setIdAgreement(int idAgreement) {
        this.idAgreement = idAgreement;
    }

    @XmlTransient
    public Date getIssueDateObject() { /* I guess technically String is also an object... You get the point though. */
        return issueDate;
    }

    public String getIssueDateString() {
        // format for JSON output
        return new SimpleDateFormat("yyyy-MM-dd").format(issueDate);
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    @XmlTransient
    public Date getValidUntilObject() {
        return validUntil;
    }

    public String getValidUntilString() {
        if(validUntil != null)
            return new SimpleDateFormat("yyyy-MM-dd").format(validUntil);

        return null;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public String getContentSlovene() {
        return contentSlovene;
    }

    public void setContentSlovene(String contentSlovene) {
        this.contentSlovene = contentSlovene;
    }

    public String getContentEnglish() {
        return contentEnglish;
    }

    public void setContentEnglish(String contentEnglish) {
        this.contentEnglish = contentEnglish;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
