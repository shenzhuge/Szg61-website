package work.szg61.website.versionLog.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.szg61.website.versionLog.entity.VersionLogEntity;
import work.szg61.website.versionLog.entity.VersionLogType;
import work.szg61.website.versionLog.repository.VersionLogRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VersionLogService {
    VersionLogRepository versionLogRepository;

    @Autowired
    public VersionLogService(VersionLogRepository versionLogRepository) {
        this.versionLogRepository = versionLogRepository;
    }

    public JSONObject getVersionList() {
        List<VersionLogEntity> entities = versionLogRepository.findAllByType(VersionLogType.VERSION);

        String[] versions = new String[entities.size()];
        IdInfos latest = IdInfos.getInfo(entities.get(0).getVersion()), loop_i;
        versions[0] = latest.versionToString();

        for (int i = 1; i < entities.size(); i++) {
            loop_i = IdInfos.getInfo(entities.get(i).getVersion());
            versions[i] = loop_i.versionToString();
            if (loop_i.versionNewerThan(latest)) latest = loop_i;
        }

        JSONObject re = new JSONObject();
        re.put("versions", versions);
        re.put("latest", latest.versionToString());
        return re;
    }

    public JSONObject getVersionLog(String version) {
        List<VersionLogEntity> entities = versionLogRepository.findAllByVersion(IdInfos.versionConvert(version));
        JSONObject re = new JSONObject(), o;
        List<JSONObject> features = new ArrayList<>();
        List<JSONObject> debugs = new ArrayList<>();
        List<JSONObject> refactors = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (VersionLogEntity e : entities) {
            switch (e.getType()) {
                case VERSION:
                    re.put("description", e.getContent());
                    break;
                case FEATURE:
                    o = new JSONObject();
                    o.put("content", e.getContent());
                    o.put("time", formatter.format(e.getTimeStamp()));
                    features.add(o);
                    break;
                case DEBUG:
                    o = new JSONObject();
                    o.put("content", e.getContent());
                    o.put("time", formatter.format(e.getTimeStamp()));
                    debugs.add(o);
                    break;
                case REFACTOR:
                    o = new JSONObject();
                    o.put("content", e.getContent());
                    o.put("time", formatter.format(e.getTimeStamp()));
                    refactors.add(o);
                    break;
            }
        }
        re.put("features", features);
        re.put("debugs", debugs);
        re.put("refactors", refactors);
        return re;
    }

    public boolean updateVersionDescription(String version, String description) {
        return versionLogRepository.updateVersionDescription(IdInfos.versionConvert(version), description);
    }

    public boolean deleteVersionLog(String version, String content) {
        return versionLogRepository.deleteVersionLog(IdInfos.versionConvert(version), content);
    }

    public boolean addVersionLog(String version, String content, VersionLogType type) {
        VersionLogEntity e = new VersionLogEntity();
        e.setVersion(IdInfos.versionConvert(version));
        e.setContent(content);
        e.setTimeStamp(new Date().getTime());
        e.setType(type);
        return versionLogRepository.save(e);
    }

    public boolean versionClose(String version, String nextVersion) {
        if (!IdInfos.isNextVersion(IdInfos.getInfo(version), IdInfos.getInfo(nextVersion))) return false;
        if (versionLogRepository.updateVersionTimeStamp(IdInfos.versionConvert(version), new Date().getTime())) {
            VersionLogEntity e = new VersionLogEntity();
            e.setVersion(IdInfos.versionConvert(nextVersion));
            e.setContent("版本描述");
            e.setTimeStamp(-1);
            e.setType(VersionLogType.VERSION);
            return versionLogRepository.save(e);
        }
        return false;
    }
}

class IdInfos {
    private int[] version;
    private int version_num;

    static public IdInfos getInfo(int version_num) {
        IdInfos re = new IdInfos();
        re.version = new int[3];
        re.version[0] = version_num / 1_00_00;
        re.version[1] = (version_num % 1_00_00) / 1_00;
        re.version[2] = version_num % 1_00;
        re.version_num = version_num;
        return re;
    }

    static public IdInfos getInfo(String version_str) {
        return getInfo(versionConvert(version_str));
    }

    static public boolean isNextVersion(IdInfos version, IdInfos nextVersion) {
        if (version.version[0] == nextVersion.version[0]) {
            // 大版本号相同
            if (version.version[1] == nextVersion.version[1]) {
                // 中版本号相同
                // 判断小版本号相邻
                return version.version[2] == nextVersion.version[2] - 1;
            } else {
                // 中版本号不同
                // 判断中版本号相邻，小版本号为0
                return version.version[1] == nextVersion.version[1] - 1 && nextVersion.version[2] == 0;
            }
        } else {
            // 大版本号不同
            // 判断大版本号相邻，中小版本号为0
            return version.version[0] == nextVersion.version[0] - 1 && nextVersion.version[1] == 0 && nextVersion.version[2] == 0;
        }
    }

    public int getVersion() {
        return version_num;
    }

    static public int versionConvert(String version) {
        String[] vs = version.split("\\.");
        if (vs.length != 3) return 1;
        return Integer.parseInt(vs[0]) * 1_00_00 + Integer.parseInt(vs[1]) * 1_00 + Integer.parseInt(vs[2]);
    }

    public String versionToString() {
        return "" + version[0] + '.' + version[1] + '.' + version[2];
    }

    public boolean versionNewerThan(IdInfos other) {
        return version_num > other.version_num;
    }
}
