package emmaguy.l;

import android.app.Activity;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;


public class HierarchicalTimingActivity extends Activity implements Transition.TransitionListener {

    private static final int NUM_VIEWS = 8;
    private static final int SCALE_ITEM_ANIMATION_DELAY = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hierarchical_timing);

        getWindow().getEnterTransition().addListener(this);
    }

    @Override
    public void onTransitionStart(Transition transition) {

    }

    @Override
    public void onTransitionEnd(Transition transition) {
        getWindow().getEnterTransition().removeListener(this);

        for (int i = 0; i < NUM_VIEWS; i++) {
            int resID = getResources().getIdentifier("view_" + (i + 1), "id", getPackageName());
            findViewById(resID).animate().setStartDelay(i * SCALE_ITEM_ANIMATION_DELAY).scaleX(1).scaleY(1);
        }
    }

    @Override
    public void onTransitionCancel(Transition transition) {

    }

    @Override
    public void onTransitionPause(Transition transition) {

    }

    @Override
    public void onTransitionResume(Transition transition) {

    }
}
