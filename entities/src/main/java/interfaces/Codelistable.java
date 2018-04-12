package interfaces;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

public interface Codelistable {

    @Transient
    String TYPE_STRING = "s", TYPE_NUMBER = "n";

    @XmlTransient
    String[] getColumnNames();

    @XmlTransient
    String[] getColumnTypes();

    @XmlTransient
    boolean getDeleted();

    @XmlTransient
    void setDeleted(boolean deleted);

}
