package snakeserver.dir.server.message;

import snakeserver.dir.model.ResultClass;

import java.util.List;

public class Top10 extends Message{

    List<ResultClass> results;

    public Top10(List<ResultClass> results) {
        this.results = results;
    }

    public List<ResultClass> getResults() {
        return results;
    }

    public void setResults(List<ResultClass> results) {
        this.results = results;
    }
}
