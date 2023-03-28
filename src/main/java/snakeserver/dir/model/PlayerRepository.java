package snakeserver.dir.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByName(String name);

    @Query("select t from Player t where t.email = ?1")
    boolean thereIsSuchAEmail(String email);

    @Query("select t from Player t where t.name = ?1")
    boolean thereIsSuchAName(String name);
}
