package work.szg61.website.versionLog.entity;

public class VersionLogEntity {
    private int version;
    private String content;
    private long timeStamp;
    private VersionLogType type;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public VersionLogType getType() {
        return type;
    }

    public void setType(VersionLogType type) {
        this.type = type;
    }
}
