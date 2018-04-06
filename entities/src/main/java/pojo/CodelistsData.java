package pojo;

import javax.persistence.Access;
import javax.persistence.AccessType;

@Access(AccessType.FIELD)
public class CodelistsData {

    public CodelistsData(String name, String displayName, String endpoint, int entriesCount, String[] columnNames, String[] columnTypes) {
        this.name = name;
        this.displayName = displayName;
        this.endpoint = endpoint;
        this.entriesCount = entriesCount;
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
    }

    public String name;

    public String displayName;

    public String endpoint;

    public int entriesCount;

    public String[] columnNames;

    public String[] columnTypes;
}
