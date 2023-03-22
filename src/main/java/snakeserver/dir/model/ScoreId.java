package snakeserver.dir.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class ScoreId implements Serializable {

    private Long snake_player_id;
    private Long snake_game_id;
}
