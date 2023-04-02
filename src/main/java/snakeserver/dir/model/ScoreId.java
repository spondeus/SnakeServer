package snakeserver.dir.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Setter
@Getter
public class ScoreId implements Serializable {

    private Long snake_player_id;
    private Long snake_game_id;
}
