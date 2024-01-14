package camp.model;

public class Score {
//    private String scoreId;
    private int testScore;

    public Score(int testScore) {
//        this.scoreId = seq;
        this.testScore = testScore;
    }

    // Getter
//    public String getScoreId() {
//        return scoreId;
//    }

    public int getTestScore() {
        return testScore;
    }

    public void setTestScore(int testScore) {
        this.testScore = testScore;
    }
}
