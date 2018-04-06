package entities;

import interfaces.Codelistable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import java.io.Serializable;

@Entity(name = "country")
@NamedQueries(
        value = {
                @NamedQuery(name = "Country.getAll", query = "SELECT c FROM country c"),
        }
)
public class Country implements Serializable, Codelistable {

    @Id
    @XmlID
    @XmlElement
    private int id;

    @Column(length = 2, nullable = false)
    private String code2;

    @Column(length = 3, nullable = false)
    private String code3;

    private String name;

    @Column(name = "name_iso")
    private String isoName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //@XmlTransient
    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public String getCode3() {
        return code3;
    }

    public void setCode3(String code3) {
        this.code3 = code3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //@XmlTransient
    public String getIsoName() {
        return isoName;
    }

    public void setIsoName(String isoName) {
        this.isoName = isoName;
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{"id", "code2", "code3", "name", "isoName"};
    }

    @Override
    public String[] getColumnTypes() {
        return new String[]{TYPE_NUMBER, TYPE_STRING, TYPE_STRING, TYPE_STRING, TYPE_STRING};
    }

}
