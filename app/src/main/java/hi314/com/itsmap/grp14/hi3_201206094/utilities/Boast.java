package hi314.com.itsmap.grp14.hi3_201206094.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Boast {

    //region Variables
    //Debug TAG
    private static transient final String TAG = Boast.class.getSimpleName();

    private static int customLayoutResourceId = 0;
    private static int customTextViewId = 0;

    private static Level defaultBoastLevel = Level.Info;
    private static int defaultBoastLength = Toast.LENGTH_SHORT;

    private static boolean autoCancel = true;

    private volatile static Boast globalBoast = null;

    private Toast internalToast;
    //endregion

    //region Public setup functions - Are not required to be used
    /**
     * Set the default used Boast layout resource ID and textView ID, to avoid having to send it as parameter each time makeText() is used.
     * <br/>
     * If this function is used, the custom resources will always be used when creating Boasts with makeText().
     * <br/>
     * If this function is used with both parameters being 0, it will reset the values and use the standard Boast layout.
     * @param layoutResourceId Resource layout (ex. 'R.layout.custom_toast_layout')
     * @param textViewId textView ID (ex. 'R.id.custom_toast_text')
     */
    public static void setCustomResource(int layoutResourceId, int textViewId) {
        customLayoutResourceId = layoutResourceId;
        customTextViewId = textViewId;
    }

    /**
     * Set the default used Boast level when using makeText() without a Boast level parameter
     * @param level The wanted default Boast level (Default is 'Boast.Level.Info')
     */
    public static void setDefaultBoastLevel(Level level) {
        defaultBoastLevel = level;
    }

    /**
     * Set the default used Boast duration when using makeText() without a Boast duration parameter
     * @param duration The wanted default Boast duration (Default is 'Toast.LENGTH_SHORT')
     */
    public static void setDefaultBoastLength(int duration) {
        defaultBoastLength = duration;
    }

    /**
     * Enable or disable the automatic cancel of old Boast when a new one is shown
     * @param enable 'true' to enable automatic cancel, 'false' to disable it
     */
    public static void setAutoCancel(boolean enable) {
        autoCancel = enable;
    }
    //endregion

    //region Private functions
    private Boast(Toast toast) {
        if (toast == null) {
            throw new NullPointerException("Boast.Boast(Toast) requires a non-null parameter");
        }

        internalToast = toast;
    }

    private void cancel() {
        internalToast.cancel();
    }

    private void show() {
        if (globalBoast != null && autoCancel) {
            globalBoast.cancel();
        }

        globalBoast = this;

        internalToast.show();
    }
    //endregion

    //region Public functions used to create Boasts
    /**
     * Create and automatically show a Boast
     * @param context Used to create internal Toast & inflate view
     * @param text Text to show in the Boast
     */
    public static void makeText(Context context, CharSequence text) {
        makeText(context, text, defaultBoastLevel);
    }

    /**
     * Create and automatically show a Boast
     * @param context Used to create internal Toast & inflate view
     * @param text Text to show in the Boast
     * @param level Boast level (ex. 'Boast.Level.Info')
     */
    public static void makeText(Context context, CharSequence text, Level level) {
        makeText(context, text, level, defaultBoastLength);
    }

    /**
     * Create and automatically show a Boast
     * @param context Used to create internal Toast & inflate view
     * @param text Text to show in the Boast
     * @param duration Boast duration (ex. 'Toast.LENGTH_SHORT')
     */
    public static void makeText(Context context, CharSequence text, int duration) {
        makeText(context, text, defaultBoastLevel, duration);
    }

    /**
     * Create and automatically show a Boast
     * @param context Used to create internal Toast & inflate view
     * @param text Text to show in the Boast
     * @param level Boast level (ex. 'Boast.Level.Info')
     * @param duration Boast duration (ex. 'Toast.LENGTH_SHORT')
     */
    public static void makeText(Context context, CharSequence text, Level level, int duration) {
        //If custom view resources have been set, it'll use and inflate those, otherwise it'll use the standard setup
        if(customLayoutResourceId == 0 && customTextViewId == 0) {
            inflateProgrammatically(context, text, level, duration);
        } else {
            inflateFromResource(context, customLayoutResourceId, customTextViewId, text, level, duration);
        }
    }

    /**
     * Create and automatically show a Boast
     * @param context Used to create internal Toast & inflate view
     * @param layoutResource Resource layout (ex. 'R.layout.custom_toast_layout')
     * @param textViewId TextView ID (ex. 'R.id.custom_toast_text')
     * @param text Text to show in the Boast
     */
    public static void makeText(Context context, int layoutResource, int textViewId, CharSequence text) {
        makeText(context, layoutResource, textViewId, text, defaultBoastLevel);
    }

    /**
     * Create and automatically show a Boast
     * @param context Used to create internal Toast & inflate view
     * @param layoutResource Resource layout (ex. 'R.layout.custom_toast_layout')
     * @param textViewId TextView ID (ex. 'R.id.custom_toast_text')
     * @param text Text to show in the Boast
     * @param level Boast level (ex. 'Boast.Level.Info')
     */
    public static void makeText(Context context, int layoutResource, int textViewId, CharSequence text, Level level) throws Resources.NotFoundException {
        makeText(context, layoutResource, textViewId, text, level, defaultBoastLength);
    }

    /**
     * Create and automatically show a Boast
     * @param context Used to create internal Toast & inflate view
     * @param layoutResource Resource layout (ex. 'R.layout.custom_toast_layout')
     * @param textViewId TextView ID (ex. 'R.id.custom_toast_text')
     * @param text Text to show in the Boast
     * @param duration Boast duration (ex. 'Toast.LENGTH_SHORT')
     */
    public static void makeText(Context context, int layoutResource, int textViewId, CharSequence text, int duration) throws Resources.NotFoundException {
        makeText(context, layoutResource, textViewId, text, defaultBoastLevel, duration);
    }

    /**
     * Create and automatically show a Boast
     * @param context Used to create internal Toast & inflate view
     * @param layoutResource Resource layout (ex. 'R.layout.custom_toast_layout')
     * @param textViewId TextView ID (ex. 'R.id.custom_toast_text')
     * @param text Text to show in the Boast
     * @param level Boast level (ex. 'Boast.Level.Info')
     * @param duration Boast duration (ex. 'Toast.LENGTH_SHORT')
     */
    public static void makeText(Context context, int layoutResource, int textViewId, CharSequence text, Level level, int duration) throws Resources.NotFoundException {
        inflateFromResource(context, layoutResource, textViewId, text, level, duration);
    }
    //endregion

    //region Internal helper functions
    private static int convertPxToDp(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int)(px * density);
    }

    private static void inflateProgrammatically(Context context, CharSequence text, Level level, int duration) {
        //Create parent layout
        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(linearParams);
        int paddingDp = convertPxToDp(context, 10);
        linearLayout.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        //Create textView
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(textViewParams);
        textView.setText(text);
        textView.setTextColor(LResources.materialWhite);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);

        //Add textView to layout
        linearLayout.addView(textView);

        finalizeToastSetup(context, linearLayout, level, duration);
    }

    private static void inflateFromResource(Context context, int resourceLayoutId, int textViewId, CharSequence text, Level level, int duration) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(resourceLayoutId, null);

        TextView textView = (TextView) layout.findViewById(textViewId);
        textView.setText(text);

        finalizeToastSetup(context, layout, level, duration);
    }

    private static void finalizeToastSetup(Context context, View view, Level level, int duration) {
        view.setBackgroundColor(level.getColor());

        //Setup the toast
        Toast toast = new Toast(context);
        toast.setDuration(duration);
        toast.setView(view);

        //Show the new boast
        Boast boast = new Boast(toast);
        boast.show();
    }
    //endregion

    //region Color resources
    private static abstract class LResources {
        static int materialWhite = 0xFFFFFFFF; //White

        static int materialGreen = 0xFF388E3C; //Info
        static int materialBlue = 0xFF2196F3; //Message
        static int materialYellow = 0xFFFFEB3B; //Caution
        static int materialRed = 0xFFF44336; //Warning
    }
    //endregion

    //region Level enum - for colors
    /**
     * Used to determine the importance of the Boast, and the background color depending on that importance
     * <br/>
     * Each level have different background colors
     * <br/>
     * Info --> Green
     * <br/>
     * Message --> Blue
     * <br/>
     * Caution --> Yellow
     * <br/>
     * Warning --> Red
     */
    public enum Level {
        Info(LResources.materialGreen),
        Message(LResources.materialBlue),
        Caution(LResources.materialYellow),
        Warning(LResources.materialRed);

        private int color;

        Level(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }
    }
    //endregion
}