package sigamebot.utilities.properties;

import java.util.ResourceBundle;

public class CallbackPrefix {
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("callback_prefix");

    public static final String START = resourceBundle.getString("start");
    public static final String MENU = resourceBundle.getString("menu");
    public static final String SOLO_GAME = resourceBundle.getString("solo_game");
    public static final String SOLO_MENU = resourceBundle.getString("solo_menu");
    public static final String SETTINGS = resourceBundle.getString("settings");
    public static final String SOLO_BUILDER = resourceBundle.getString("solo_builder");
}
