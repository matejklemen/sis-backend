package pojo;

import javax.persistence.Access;
import javax.persistence.AccessType;

@Access(AccessType.FIELD)
public class CodelistsData {

    public CodelistsData(String name, String displayName, int entriesCount) {
        this.name = name;
        this.displayName = displayName;
        this.entriesCount = entriesCount;
    }

    public String name;

    public String displayName;

    public int entriesCount;
}
