package work.szg61.website.versionLog.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
