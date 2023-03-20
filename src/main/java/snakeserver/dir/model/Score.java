package snakeserver.dir.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "snake_score")
public class Score {
    @EmbeddedId
    private ScoreId score_id;

    @ManyToOne
    @MapsId("snake_player_id")
    private Player player_id;

    @ManyToOne
    @MapsId("snake_game_id")
    private Game game_id;

    private Long score;

}
