package lethbridge.model.entities;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import java.io.Serializable;

@Stateful
@LocalBean
public class CurNews implements Serializable {

    private int tick;
    private String ticker;
    private String headLine;
    private String story;

    public CurNews() {
    }

    public CurNews(int tick, String ticker, String headLine, String story) {
        this.tick = tick;
        this.ticker = ticker;
        this.headLine = headLine;
        this.story = story;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getHeadLine() {
        return headLine;
    }

    public void setHeadLine(String headLine) {
        this.headLine = headLine;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

}
