package hi314.com.itsmap.grp14.hi3_201206094;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SecondActivity extends Activity {

    private final String TAG = ((Object)this).getClass().getSimpleName();

    @InjectView(R.id.second_activity_textview) TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        ButterKnife.inject(this);

        String text = getIntent().getExtras().getString(MainActivity.serviceIntentTextExtra);
        textView.setText(text);

        if(getActionBar() != null) {
            getActionBar().setTitle(getString(R.string.actionbar_title));
            getActionBar().setSubtitle(getString(R.string.actionbar_subtitle_second));
        }
    }
}