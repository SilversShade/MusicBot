package sigamebot.logic;

public class Player {
    public String name;
    public long chatId;
    public int score;
    public Game game;

    public Player(String name, long chatId, Game game){
        this.name = name;
        this.game = game;
        this.chatId = chatId;
        score = 0;
    }
    public Player(String name, long chatId){
        this.name = name;
        score = 0;
    }
}
