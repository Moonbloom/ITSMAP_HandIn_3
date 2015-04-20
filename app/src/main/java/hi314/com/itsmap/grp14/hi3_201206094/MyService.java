package hi314.com.itsmap.grp14.hi3_201206094;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class MyService extends Service {

    private final String TAG = ((Object)this).getClass().getSimpleName();

    private final String intentTag = "itsmap_handin_3_intent";

    private BroadcastReceiver broadcastReceiver;
    private CountDownTimer countDownTimer;

    @Override
    public void onCreate() {
        //Create new broadcastReceiver
        broadcastReceiver = new MyReceiver();

        //Register the receiver with the filter
        registerReceiver(broadcastReceiver, new IntentFilter(intentTag));
    }

    @Override
    public void onDestroy() {
        //Unregister receiver and cancel countdown when destroying the service
        unregisterReceiver(broadcastReceiver);
        countDownTimer.cancel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Get the extras
        final String text = intent.getStringExtra(MainActivity.serviceIntentTextExtra);
        final int waitTime = intent.getIntExtra(MainActivity.serviceIntentNumberExtra, 0);

        //Create new intent with the correct setup
        Intent mIntent = new Intent(intentTag);
        mIntent.putExtra(MainActivity.serviceIntentTextExtra, text);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Start the alarm manager with the intent and correct wait time
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + waitTime, pendingIntent);

        //Start the timer that is used to send feedback back to the MainActivity
        final int offset = 100;
        countDownTimer = new CountDownTimer(waitTime + offset, 250) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick");

                //Calculate the percentage that has been waited
                double done = (waitTime + offset) - (int)millisUntilFinished;
                double percentDoneDouble = (done / waitTime) * 100;
                int percentDone = (int) percentDoneDouble;

                sendLocalBroadcast(percentDone);
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "onFinish");
                onTick(offset);
            }

            private void sendLocalBroadcast(int percentDone) {
                //Broadcast the update intent with the percentage as an extra
                Intent intent = new Intent(MainActivity.localBroadcastUpdateMsg);
                intent.putExtra(MainActivity.localBroadcastUpdatePercentExtra, percentDone);
                LocalBroadcastManager.getInstance(MyService.this).sendBroadcast(intent);
            }
        }.start();

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //Not using a bound service, so this is never called
        return null;
    }
}