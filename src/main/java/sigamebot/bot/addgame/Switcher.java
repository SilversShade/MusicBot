package sigamebot.bot.addgame;

import com.google.gson.Gson;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import sigamebot.bot.addgame.containers.Game;
import sigamebot.bot.core.ITelegramBot;

import java.io.*;
import java.util.Objects;

public class Switcher implements IState {
    private IState previousState;

    public Switcher(IState state){
        previousState = state;
    }

    @Override
    public void action(ITelegramBot bot, Message msg, Logic logic, Game game) {

        if(Objects.equals(msg.getText(), "да")) {
            changeStateNext(logic, msg, bot);
        }
        else if(Objects.equals(msg.getText(), "нет")) {
            if(previousState instanceof AddRound) {
                bot.sendMessage("Игра сохранена", msg.getChatId());
                logic.serialize(game, bot, msg);
                bot.sendDocument(new InputFile(new File("savegame.json")), msg.getChatId());
            }
            else{
                changePreviousState(bot, msg);
            }
        }
        else
            bot.sendMessage("Некорректный ввод. Ответьте да/нет", msg.getChatId());


    }

    public void changePreviousState(ITelegramBot bot,Message msg){
        if(previousState instanceof AddQuestion){
            bot.sendMessage("Хотите добавить еще категорию? Ответьте да/нет", msg.getChatId());
            previousState= new AddCategory(((AddQuestion) previousState).currentRound);
        }
        else if(previousState instanceof AddCategory){
            bot.sendMessage("Хотите добавить еще раунд? Ответьте да/нет", msg.getChatId());
            previousState=new AddRound();
        }

    }
    public void changeStateNext(Logic logic, Message msg, ITelegramBot bot){
        if(previousState instanceof AddQuestion){
            bot.sendMessage("Введите название вопроса", msg.getChatId());
            logic.currentState = new AddQuestion(((AddQuestion) previousState).currentCategory, ((AddQuestion) previousState).currentRound);
        }
        else if(previousState instanceof AddCategory){
            bot.sendMessage("Введите название категории", msg.getChatId());
            logic.currentState = new AddCategory(((AddCategory) previousState).currentRound);
        }
        else if(previousState instanceof AddRound){
            bot.sendMessage("Введите название раунда", msg.getChatId());
            logic.currentState = new AddRound();
        }
    }
}
