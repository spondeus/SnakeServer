package snakeserver.dir.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

//    Optional<Player> findByName(String name);


    @Transactional
    @Modifying
    @Query("select p from Player p where p.name = ?1")
    Optional<Player> findByName(String name);

    @Transactional
    @Modifying
    @Query("update Player p set p.enable = true where p.name like ?1")
    void updateEnableByName(String name);

    @Transactional
    @Modifying
    @Query("update Player p set p.password = ?2 where p.name like ?1")
    void updatePasswordByName(String name, String password);

}
