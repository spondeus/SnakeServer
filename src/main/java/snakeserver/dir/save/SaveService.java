package snakeserver.dir.save;


import org.springframework.stereotype.Service;
import snakeserver.dir.model.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaveService {

    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;
    private final ScoreRepository scoreRepository;

    public SaveService(PlayerRepository playerRepository, GameRepository gameRepository, ScoreRepository scoreRepository) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.scoreRepository = scoreRepository;
    }

    public void savePlayerScore(Long id, Long score) {
        Player player = playerRepository.findPlayerById(id);
        Game game = gameRepository.getReferenceById(gameRepository.findMaxId());
        Score newScore = new Score(player,game,score);
        scoreRepository.save(newScore);
    }

    public void saveAllPlayerScore(List<Long> ids, List<Long> scores) {
        for (int i = 0; i < ids.size(); i++) {
            Long id = ids.get(i);
            Long score = scores.get(i);
            savePlayerScore(id, score);
        }

    }

    public void saveGame(){
        Game game = new Game(LocalDateTime.now(),LocalDateTime.now(),true);
        gameRepository.save(game);
    }

}
