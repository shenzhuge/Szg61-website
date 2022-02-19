package work.szg61.website.account.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import work.szg61.website.account.entity.AccountEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository {
    @Select("select * from account where username=#{username} limit 1")
    Optional<AccountEntity> findByUsername(String username);

    @Insert("insert into account values (#{id},#{username},#{password},#{email},#{avatar},#{registerTime},#{lastLogin})")
    void save(AccountEntity entity);

    @Select("select * from account where id=#{id}")
    Optional<AccountEntity> findById(long id);

    @Update("update account set last_login=#{last_login} where id=#{id}")
    void updateLastLogin(long id, long last_login);

    @Select("select exists(select * from account where id=#{id})")
    boolean existsById(long id);

    @Delete("delete from account where id=#{id}")
    void deleteById(long id);
}
