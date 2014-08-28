package emmaguy.l.choreog;

import android.app.Activity;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;

import emmaguy.l.R;
import emmaguy.l.shared.TransListener;

public class ConsistentChroeography2Activity extends Activity {

    public static final String CIRCLE_VIEW_NAME = "circ_view";

    private static final int NUM_VIEWS = 4;
    private static final int MOVE_AND_FADE_ITEM_ANIMATION_DELAY = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_consistent_chroeography2);

        View header = findViewById(R.id.header);
        header.setViewName(CIRCLE_VIEW_NAME);

        getWindow().getEnterTransition().addListener(new TransListener() {
            @Override
            public void onTransitionEnd(Transition transition) {
                getWindow().getEnterTransition().removeListener(this);

                for (int i = 0; i < NUM_VIEWS; i++) {
                    int resID = getResources().getIdentifier("view_" + (i + 1), "id", getPackageName());
                    findViewById(resID).animate().setStartDelay(i * MOVE_AND_FADE_ITEM_ANIMATION_DELAY).alpha(1);
                }
            }
        });
    }
}
