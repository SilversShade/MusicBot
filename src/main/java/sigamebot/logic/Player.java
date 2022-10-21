package sigamebot.logic;

public class Player {
    public String name;
    public long id;
    public int score;
    public Game game;
    public Player(String name, long chatId, Game game){
        this.name = name;
        id = chatId;
        this.game = game;
        score = 0;
    }
    public Player(String name, long chatId){
        this.name = name;
        id = chatId;
        score = 0;
    }
}
