package game.intern.test;

import android.content.Context;
import android.content.SharedPreferences;

public class StageUnlock {
    private static final String PREF_NAME = "StageUnlockPreferences";

    public static void setStageUnlocked(Context context, int stageNumber, boolean unlocked) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean("stage" + stageNumber + "Unlocked", unlocked);
        editor.apply();
    }

    public static boolean isStageUnlocked(Context context, int stageNumber) {
        return getSharedPreferences(context).getBoolean("stage" + stageNumber + "Unlocked", false);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
