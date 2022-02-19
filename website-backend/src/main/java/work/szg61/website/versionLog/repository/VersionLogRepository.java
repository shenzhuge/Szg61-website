package work.szg61.website.versionLog.repository;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import work.szg61.website.versionLog.entity.VersionLogEntity;
import work.szg61.website.versionLog.entity.VersionLogType;

import java.util.List;

@Repository
public interface VersionLogRepository {
    @Select("select * from version_log where type='VERSION'")
    List<VersionLogEntity> findAllByType(VersionLogType type);
}
