package vo;

public class WordBookVo {
    private String word;
    private String mean;

    public WordBookVo(String word, String mean) {
        super();
        this.word = word;
        this.mean = mean;
    }

    @Override
    public String toString() {
        return "Vo [word=" + word + ", mean=" + mean + "]";
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

}
