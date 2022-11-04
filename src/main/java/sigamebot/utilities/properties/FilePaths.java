package sigamebot.utilities.properties;

import java.util.Properties;
import java.util.ResourceBundle;

public class FilePaths {

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("paths");

    public static final String MENU_COMMAND_MESSAGE = resourceBundle.getString("menucommandmessage");
    public static final String SOLO_MENU_COMMAND_MESSAGE = resourceBundle.getString("solomenucommandmessage");
    public static final String START_COMMAND_MESSAGE = resourceBundle.getString("startcommandmessage");
    public static final String RESOURCES_DIRECTORY = resourceBundle.getString("resources");
    public static final String USERPACKS_DIRECTORY = resourceBundle.getString("userpacks");
}
