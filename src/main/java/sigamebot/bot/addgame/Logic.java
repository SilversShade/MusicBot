package sigamebot.bot.addgame;
import com.google.gson.Gson;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import sigamebot.bot.addgame.containers.Game;
import sigamebot.bot.core.ITelegramBot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logic {
    private ITelegramBot bot;
    public IState currentState;
    private Game game;
    public Logic(ITelegramBot bot){
        this.game = new Game();
        this.bot=bot;
    }
    public void init(Message msg) throws IOException {
        currentState = new AddGame();
        currentState.action(bot, msg, this, game);
    }
    public void processMessage(Message msg) throws IOException {
        currentState.action(bot, msg, this, game);
    }
    public void serialize(Game game, ITelegramBot bot, Message msg) {

        try (PrintWriter out = new PrintWriter(new FileWriter("savegame.json"))) {
            Gson gson = new Gson();
            String json = gson.toJson(game);
            out.write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
