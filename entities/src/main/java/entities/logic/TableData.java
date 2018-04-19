package entities.logic;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class TableData {
    @XmlElement
    private String tableName;

    @XmlElement
    private List<String> coloumnNames;
    @XmlElement
    private List<List<String>> rows;

    public String getTable_name() {
        return tableName;
    }

    public void setTable_name(String table_name) {
        this.tableName = table_name;
    }

    public List<String> getColoumnNames() {
        return coloumnNames;
    }

    public void setColoumnNames(List<String> coloumnNames) {
        this.coloumnNames = coloumnNames;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public void setRows(List<List<String>> rows) {
        this.rows = rows;
    }
}
