package work.szg61.website.versionLog.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.szg61.website.versionLog.entity.VersionLogEntity;
import work.szg61.website.versionLog.entity.VersionLogType;
import work.szg61.website.versionLog.repository.VersionLogRepository;

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
        for (int i = 0; i < entities.size(); i++) {
            versions[i] = IdInfos.getInfo(entities.get(i).getId()).versionToString();
        }
        JSONObject re = new JSONObject();
        re.put("versions", versions);
        return re;
    }
}

class IdInfos {
    public int[] version;
    public int id;

    static public IdInfos getInfo(int id) {
        IdInfos re = new IdInfos();
        re.version = new int[3];
        re.version[0] = id / 1_00_00_00;
        re.version[1] = (id % 1_00_00_00) / 1_00_00;
        re.version[2] = (id % 1_00_00) / 1_00;
        re.id = id % 1_00;
        return re;
    }

    public int getId() {
        return version[0] * 1_00_00_00 + version[1] * 1_00_00 + version[2] * 1_00 + id;
    }

    public String versionToString() {
        return "" + version[0] + '.' + version[1] + '.' + version[2];
    }
}
