package sigamebot.logic;

import sigamebot.bot.core.SigameBot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnlineGame {
    private final SigameBot bot;
    private long leadingId;
    private final Map<Long, String> playersName = new HashMap<>();
    private final String gameName;
    private String password;
    public static final List<OnlineGame> ongoingGames = new ArrayList<>();
    public OnlineGame(SigameBot bot, long leadingId, String gameName) {
        this.gameName = gameName;
        this.bot = bot;
        this.leadingId = leadingId;
        ongoingGames.add(this);
    }

    public String getGameName() {
        return gameName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean addPlayer(String name, String pass, long chatId) {
        if (pass.equals(password)) {
            playersName.put(chatId, name);
            return true;
        }
        return false;
    }

    public void startGame() {

    }
}
