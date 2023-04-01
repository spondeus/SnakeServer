package snakeserver.dir.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("select max(id) from Game")
    Game findMaxGameId();
}
