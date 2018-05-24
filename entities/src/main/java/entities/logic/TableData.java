package entities.logic;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class TableData {
    @XmlElement
    private List<String> head;
    @XmlElement
    private List<String> coloumnNames;
    @XmlElement
    private List<List<String>> rows;
    @XmlElement
    private List<Boolean> inLegend;

    public List<String> getHead() {
        return head;
    }

    public void setHead(List<String> head) {
        this.head = head;
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

    public List<Boolean> getInLegend() {
        return inLegend;
    }

    public void setInLegend(List<Boolean> inLegend) {
        this.inLegend = inLegend;
    }

    @Override
    public String toString() {
        return "TableData{" +
                "head=" + head +
                ", coloumnNames=" + coloumnNames +
                ", rows=" + rows +
                ", inLegend=" + inLegend +
                '}';
    }
}
