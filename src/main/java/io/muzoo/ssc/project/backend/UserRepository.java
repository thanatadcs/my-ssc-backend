package io.muzoo.ssc.project.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByUsername(String username);

    int deleteByUsername(String username);

    @Modifying
    @Query("UPDATE User SET timestamp = :timestamp where username = :username")
    int updateTimestampByUsername(@Param(value = "username") String username, @Param(value = "timestamp") float timestamp);
}
