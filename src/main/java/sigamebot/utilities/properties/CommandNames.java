package sigamebot.utilities.properties;

import java.util.ResourceBundle;

public class CommandNames {
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("command_names");

    public static final String START_COMMAND_NAME = resourceBundle.getString("start");
    public static final String SOLO_MENU_COMMAND_NAME = resourceBundle.getString("solo_menu");
    public static final String MENU_COMMAND_NAME = resourceBundle.getString("menu");
    public static final String CANCEL_COMMAND_NAME = resourceBundle.getString("cancel");

    public static final String ADMIN_COMMAND_NAME = resourceBundle.getString("admin");
}
