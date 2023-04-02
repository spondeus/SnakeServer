package snakeserver.dir.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter@Setter
@NoArgsConstructor
@Entity
@Table(name = "snake_score")
public class Score {
    @EmbeddedId
    private ScoreId score_id;

    @ManyToOne
    @MapsId("snake_player_id")
    @JoinColumn(referencedColumnName = "id")
    private Player player;

    @ManyToOne
    @MapsId("snake_game_id")
    @JoinColumn(referencedColumnName = "id")
    private Game game;

    private Long score;

}
