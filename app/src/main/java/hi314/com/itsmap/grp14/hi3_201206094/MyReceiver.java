package hi314.com.itsmap.grp14.hi3_201206094;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import hi314.com.itsmap.grp14.hi3_201206094.utilities.Boast;

public class MyReceiver extends BroadcastReceiver {

    private final String TAG = ((Object)this).getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Boast.makeText(context, "The alarm has been triggered - Starting new Activity");

        //When the broadcast receiver gets the signal, it will create the new intent, add the text extra and then start the new activity
        Intent activityIntent = new Intent(context, SecondActivity.class);
        String text = intent.getStringExtra(MainActivity.serviceIntentTextExtra);
        activityIntent.putExtra(MainActivity.serviceIntentTextExtra, text);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(activityIntent);
    }
}
