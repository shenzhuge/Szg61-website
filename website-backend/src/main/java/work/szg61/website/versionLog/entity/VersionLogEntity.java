package work.szg61.website.versionLog.entity;

public class VersionLogEntity {
    private int id;
    private String content;
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
