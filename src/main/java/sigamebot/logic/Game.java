package sigamebot.logic;

import sigamebot.bot.core.SigameBot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {
    private final SigameBot bot;
    private long leadingId;
    private final Map<Long, String> playersName = new HashMap<>();
    private final String gameName;
    private final String password;
    public Game(SigameBot bot, long leadingId, String gameName){
        this.gameName = gameName;
        this.bot = bot;
        this.password = "";
        this.leadingId = leadingId;
    }
    public Game(SigameBot bot, long leadingId, String gameName, String password){
        this.gameName = gameName;
        this.bot = bot;
        this.password = password;
        this.leadingId = leadingId;
    }
    public String getGameName(){ return gameName; }
    public String getPassword(){ return password; }
    public void addPlayer(String name){
        int i = 1;
    }
    public boolean addPlayer(String name, String pass, long chatId){
        if(pass.equals(password)){
            playersName.put(chatId, name);
            return true;
        }
        return false;
    }
    public void startGame(){

    }
}
