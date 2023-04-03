package snakeserver.dir.model;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("select id from Game order by id desc limit 1")
    Long findMaxId();

    @NotNull
    @Override
    Game getReferenceById(@NotNull Long aLong);
}
