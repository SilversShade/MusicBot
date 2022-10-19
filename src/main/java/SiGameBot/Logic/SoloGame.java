package SiGameBot.Logic;

import SiGameBot.Logic.ScenarioLogic.Scenario;
import SiGameBot.SigameBot;

import java.util.ArrayList;

public class SoloGame {
    private final SigameBot bot;
    Scenario scenario;
    Player player;
    public SoloGame(SigameBot bot, long chatId){
        this.bot = bot;
        player = new Player("player", chatId);
    }
}
