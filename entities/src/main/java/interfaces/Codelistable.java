package interfaces;

import javax.xml.bind.annotation.XmlTransient;

public interface Codelistable {

    String TYPE_STRING = "s", TYPE_NUMBER = "n";

    @XmlTransient
    String[] getColumnNames();

    @XmlTransient
    String[] getColumnTypes();

}
