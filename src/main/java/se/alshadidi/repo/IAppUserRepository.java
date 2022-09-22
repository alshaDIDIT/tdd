package se.alshadidi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.alshadidi.AppUser;

import java.util.List;

@Repository
public interface IAppUserRepository extends JpaRepository<AppUser, Long> {
    List<AppUser> findAll();
    AppUser findRole(String username);
}
