package sigamebot.bot.commands;

import sigamebot.bot.core.SigameBot;
import sigamebot.user.Admin;
import sigamebot.user.ChatInfo;

public class AdminCommand extends SigameBotCommand{

    public static final String adminAccessKey = System.getenv("adminAccessKey");

    public AdminCommand(String command, String description, SigameBot bot) {
        super(command, description, bot);
    }


    @Override
    public void executeCommand(ChatInfo chatInfo) {
        if (Admin.results.isEmpty()) {
            this.bot.sendMessage("Нет данных о пройденных тестах.", chatInfo.getChatId());
            return;
        }

        var table = new StringBuilder();

        Admin.results.forEach(r ->
            table.append("Название теста: ")
                    .append(r.testName)
                    .append(", Никнейм: ")
                    .append(r.nickname)
                    .append(", кол-во баллов: ")
                    .append(r.score)
                    .append("\n\n")
        );

        this.bot.sendMessage(table.toString(), chatInfo.getChatId());
    }
}
