package work.szg61.website.versionLog.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import work.szg61.website.versionLog.entity.VersionLogEntity;
import work.szg61.website.versionLog.entity.VersionLogType;

import java.util.List;

@Repository

public interface VersionLogRepository extends CrudRepository<VersionLogEntity, Integer> {
    List<VersionLogEntity> findAllByType(VersionLogType type);
}
