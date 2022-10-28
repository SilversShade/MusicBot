package sigamebot.logic;

import java.util.ArrayList;
import java.util.List;

public class Player {
    public String name;
    public long chatId;
    public int score;
    public Game game;
    public String gameType;
    public int questionMessageId = -1;
    public int[] categoryMessageIds;
    public int roundMessageId = -1;
    public final List<Integer> someMessageId = new ArrayList<>();
    public Player(String name, long chatId, Game game){
        this.name = name;
        this.chatId = chatId;
        this.game = game;
        this.gameType = "online";
        score = 0;
    }
    public Player(String name, long chatId){
        this.name = name;
        this.chatId = chatId;
        this.gameType = "solo";
        score = 0;
    }

}
