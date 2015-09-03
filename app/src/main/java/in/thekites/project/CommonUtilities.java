package in.thekites.project;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public final class CommonUtilities {
	
	// give your server registration url here
    static final String SERVER_URL = "http://jsr.esy.es/kchat/register.php";

    // Google project id
    static final String SENDER_ID = "920078492764";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "KitesGCM";

    static final String DISPLAY_MESSAGE_ACTION = "in.thekites.in.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";
////
    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
//    	Log.i(TAG, message);
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
