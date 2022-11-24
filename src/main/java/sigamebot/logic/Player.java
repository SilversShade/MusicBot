package sigamebot.logic;

public class Player {
    public final String name;
    public int score;
    public Game game;

    public Player(String name, Game game){
        this.name = name;
        this.game = game;
        score = 0;
    }
    public Player(String name){
        this.name = name;
        score = 0;
    }
}
