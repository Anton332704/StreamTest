package helppocket;

import android.app.Activity;
import android.os.Bundle;

import com.example.streamtest.R;

/**
 * Created by User on 14.03.2016.
 */
public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);


        findViewById(R.id.imageViewTest).setBackgroundResource(R.drawable.tux);
    }
}
