package sigamebot.logic;

import sigamebot.bot.core.SigameBot;

import java.util.ArrayList;

public class Game {
    private final SigameBot bot;
    private final Player leading;
    private final ArrayList<Player> players = new ArrayList<>();
    private final String gameName;
    private final String password;
    public Game(SigameBot bot, long leadingId, String gameName){
        this.gameName = gameName;
        this.bot = bot;
        this.password = "";
        this.leading = new Player("Ведущий", this);
    }
    public Game(SigameBot bot, long leadingId, String gameName, String password){
        this.gameName = gameName;
        this.bot = bot;
        this.password = password;
        this.leading = new Player("Ведущий", this);
    }
    public String getGameName(){ return gameName; }
    public String getPassword(){ return password; }
    public void addPlayer(String name){
        Player player = new Player(name,this);
        players.add(player);
    }
    public boolean addPlayer(String name, String pass, long chatId){
        if(pass.equals(password)){
            addPlayer(name);
            return true;
        }
        return false;
    }
}
