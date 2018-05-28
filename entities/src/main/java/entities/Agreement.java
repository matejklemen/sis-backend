package entities;

import javax.persistence.*;
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

    @Column(name = "valid_until", columnDefinition = "DATE")
    // ! note: String formatted as: YYYY-MM-DD
    private Date validUntil;

    @Column(name = "content_slo")
    private String contentSlovene;

    @Column(name = "content_eng")
    private String contentEnglish;

    private String issuer;

    public int getIdAgreement() {
        return idAgreement;
    }

    public void setIdAgreement(int idAgreement) {
        this.idAgreement = idAgreement;
    }

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

    public Date getValidUntilObject() {
        return validUntil;
    }

    public String getValidUntilString() {
        // format for JSON output
        return new SimpleDateFormat("yyyy-MM-dd").format(validUntil);
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
}
