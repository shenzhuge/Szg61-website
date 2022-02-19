package work.szg61.website.versionLog.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "version_log")
public class VersionLogEntity {
    @Id
    private int id;
    @Column
    private String content;
    @Column
    private VersionLogType type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public VersionLogType getType() {
        return type;
    }

    public void setType(VersionLogType type) {
        this.type = type;
    }
}
