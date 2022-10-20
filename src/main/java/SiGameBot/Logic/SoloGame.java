package SiGameBot.Logic;

import SiGameBot.Logic.ScenarioLogic.Category;
import SiGameBot.Logic.ScenarioLogic.Scenario;
import SiGameBot.SigameBot;

import java.util.ArrayList;

public class SoloGame {
    private final SigameBot bot;
    Category scenario;
    Player player;
    public SoloGame(SigameBot bot, long chatId){
        this.bot = bot;
        player = new Player("player", chatId);
    }
    public void Start(){
        bot.sendMessage("Игра начинается", player.id);
    }
}