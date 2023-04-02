package snakeserver.dir.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter@Getter
@NoArgsConstructor
@Entity
@Table(name = "snake_game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private LocalDateTime begin_time;

    private LocalDateTime end_time;

    private  boolean type;

    @OneToMany(mappedBy = "game")
    private List<Score> scores;

    public Game(LocalDateTime begin_time, LocalDateTime end_time, boolean type) {
        this.begin_time = begin_time;
        this.end_time = end_time;
        this.type = type;
    }
}
