package pojo;

import javax.persistence.Access;
import javax.persistence.AccessType;

@Access(AccessType.FIELD)
public class CodelistsData {

    public CodelistsData(String name, String displayName, int entriesCount, String[] columnNames, String[] columnTypes) {
        this.name = name;
        this.displayName = displayName;
        this.entriesCount = entriesCount;
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
    }

    public String name;

    public String displayName;

    public int entriesCount;

    public String[] columnNames;

    public String[] columnTypes;
}
