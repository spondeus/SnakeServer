package snakeserver.dir.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "snake_player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @NonNull
    private Long id;

    @Size(min = 6, max = 20)
    private String name;

    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]){2}.{6,30}$")
    private String password;

    @Email
    private String email;

    private boolean enable = false;

    @OneToMany(mappedBy = "player")
    private List<Score> scores;

}
