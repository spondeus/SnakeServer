package snakeserver.dir.model;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("select max(id) from snake_game")
    Long findMaxId();

    @NotNull
    @Override
    Game getReferenceById(@NotNull Long aLong);
}
