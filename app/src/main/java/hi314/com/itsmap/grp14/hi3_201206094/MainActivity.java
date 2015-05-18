package hi314.com.itsmap.grp14.hi3_201206094;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.moonbloom.boast.BStyle;
import com.moonbloom.boast.Boast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends Activity {

    private final String TAG = ((Object)this).getClass().getSimpleName();

    public static final String serviceIntentNumberExtra = "wait_time_extra";
    public static final String serviceIntentTextExtra = "text_extra";

    public static final String localBroadcastUpdateMsg = "local_broadcast_update_msg";
    public static final String localBroadcastUpdatePercentExtra = "percent_done_extra";

    private Intent serviceIntent;

    @InjectView(R.id.main_activity_button) Button button;
    @InjectView(R.id.main_activity_text_edittext) EditText textEditText;
    @InjectView(R.id.main_activity_number_edittext) EditText numberEditText;
    @InjectView(R.id.progressbar) ProgressBar progressBar;

    @OnClick(R.id.main_activity_button)
    public void serviceButtonClick() {
        //Check for valid text input
        if(TextUtils.isEmpty(textEditText.getText().toString())) {
            Boast.makeText(this, getString(R.string.no_current_text_input_error), BStyle.ALERT);
            return;
        }

        //Check for valid number input
        if(TextUtils.isEmpty(numberEditText.getText().toString()) || Integer.valueOf(numberEditText.getText().toString()) == 0) {
            Boast.makeText(this, getString(R.string.no_current_number_input_error), BStyle.ALERT);
            return;
        }

        //Make button inactive (to stop several services from going off at once)
        button.setClickable(false);

        //Hide keyboard
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(textEditText.getWindowToken(), 0);

        //Get values and format them correctly
        int waitTimeInput = Integer.valueOf(numberEditText.getText().toString());
        int waitTimeFormatted = waitTimeInput * 1000;
        String textToSend = textEditText.getText().toString();

        //Create the service, add the extras and start the service
        serviceIntent = new Intent(this, MyService.class);
        serviceIntent.putExtra(serviceIntentNumberExtra, waitTimeFormatted);
        serviceIntent.putExtra(serviceIntentTextExtra, textToSend);
        startService(serviceIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ButterKnife.inject(this);

        registerLocalReceiver();

        if(getActionBar() != null) {
            getActionBar().setTitle(getString(R.string.actionbar_title));
            getActionBar().setSubtitle(getString(R.string.actionbar_subtitle_main));
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        textEditText.requestFocus();
    }

    @Override
    protected void onStart() {
        super.onStart();

        createLog(getString(R.string.on_start));
    }

    @Override
    protected void onResume() {
        super.onResume();

        progressBar.setVisibility(View.GONE);
        progressBar.setProgress(0);

        button.setClickable(true);

        createLog(getString(R.string.on_resume));
    }

    @Override
    protected void onPause() {
        super.onPause();

        createLog(getString(R.string.on_pause));
    }

    @Override
    protected void onStop() {
        super.onStop();

        createLog(getString(R.string.on_stop));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(serviceIntent != null) {
            stopService(serviceIntent);
        }

        createLog(getString(R.string.on_destroy));
    }

    private void createLog(String text) {
        Log.d(TAG, text);
    }

    private void registerLocalReceiver() {
        BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                createLog(getString(R.string.on_receive));

                int percentDone = intent.getIntExtra(localBroadcastUpdatePercentExtra, 0);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(percentDone);

                if(percentDone == 100) {
                    button.setClickable(true);
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(localBroadcastUpdateMsg));
    }
}