package work.szg61.website.versionLog.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import work.szg61.website.versionLog.entity.VersionLogEntity;
import work.szg61.website.versionLog.entity.VersionLogType;

import java.util.List;

@Repository
public interface VersionLogRepository {
    @Select("select * from version_log where type='VERSION'")
    List<VersionLogEntity> findAllByType(VersionLogType type);

    @Select("select * from version_log where version=#{version}")
    List<VersionLogEntity> findAllByVersion(int version);

    @Update("update version_log set content=#{description} where type='VERSION' and version=#{version}")
    boolean updateVersionDescription(int version, String description);

    @Delete("delete from version_log where version=#{version} and content=#{content}")
    boolean deleteVersionLog(int version, String content);

    @Insert("insert into version_log values(#{version},#{content},#{timeStamp},#{type})")
    boolean save(VersionLogEntity e);

    @Update("update version_log set time_stamp=#{timeStamp} where type='VERSION' and version=#{version}")
    boolean updateVersionTimeStamp(int version, long timeStamp);
}
