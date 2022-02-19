package work.szg61.website.versionLog.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import work.szg61.website.versionLog.entity.VersionLogType;
import work.szg61.website.versionLog.service.VersionLogService;

@RestController
public class VersionLogController {
    VersionLogService versionLogService;

    @Autowired
    public VersionLogController(VersionLogService versionLogService) {
        this.versionLogService = versionLogService;
    }

    @PostMapping("/versionLog/versions")
    public JSONObject getVersions() {
        return versionLogService.getVersionList();
    }

    @PostMapping("/versionLog/{version}/log")
    public JSONObject getVersionLogs(@PathVariable String version) {
        return versionLogService.getVersionLog(version);
    }

    @PostMapping("/versionLog/{version}/update/description")
    public boolean updateVersionDescription(@RequestBody JSONObject info, @PathVariable String version) {
        return versionLogService.updateVersionDescription(version, info.getString("description"));
    }

    @PostMapping("/versionLog/{version}/delete/log")
    public boolean deleteVersionLog(@RequestBody JSONObject info, @PathVariable String version) {
        return versionLogService.deleteVersionLog(version, info.getString("content"));
    }

    @PostMapping("/versionLog/{version}/add/log")
    public boolean addVersionLog(@RequestBody JSONObject info, @PathVariable String version) {
        VersionLogType type;
        switch (info.getString("type")) {
            case "Features":
                type = VersionLogType.FEATURE;
                break;
            case "Bug fix":
                type = VersionLogType.DEBUG;
                break;
            case "Refactors":
                type = VersionLogType.REFACTOR;
                break;
            default:
                return false;
        }
        return versionLogService.addVersionLog(version, info.getString("content"), type);
    }

    @PostMapping("/versionLog/{version}/close")
    public boolean versionClose(@RequestBody JSONObject info, @PathVariable String version) {
        return versionLogService.versionClose(version, info.getString("nextVersion"));
    }
}
