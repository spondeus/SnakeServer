package snakeserver.dir.server.message;

public class ScoreMessage extends Message{

    public ScoreMessage(int score) {
        this.score = score;
    }

    int score;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
