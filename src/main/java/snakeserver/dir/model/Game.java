package snakeserver.dir.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter@Getter
@NoArgsConstructor@RequiredArgsConstructor
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

}
