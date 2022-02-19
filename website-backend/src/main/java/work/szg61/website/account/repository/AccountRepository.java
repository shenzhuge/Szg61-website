package work.szg61.website.account.repository;

import work.szg61.website.account.entity.AccountEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByUsername(String username);
}
