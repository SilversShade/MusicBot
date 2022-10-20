package SiGameBot.Logic;

public class Player {
    public String name;
    public long id;
    public int score;
    public Game game;
    public final GameDisplay gameDisplay;
    public Player(String name, long chatId, Game game){
        this.name = name;
        id = chatId;
        this.game = game;
        score = 0;
        gameDisplay = new GameDisplay();
    }
    public Player(String name, long chatId){
        this.name = name;
        id = chatId;
        score = 0;
        gameDisplay = new GameDisplay();
    }
}
