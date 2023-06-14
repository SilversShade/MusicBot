package sigamebot.user;

import sigamebot.logic.TestResult;

import java.util.ArrayList;
import java.util.HashMap;

public class Admin {
    public static ArrayList<TestResult> results = new ArrayList<>();

    public static HashMap<Integer, String> packsIdToName = new HashMap<>();

    public static HashMap<Integer, String> userpacksIdToName = new HashMap<>();

    public static String lastNickname;

    public static String lastGameName;
}

