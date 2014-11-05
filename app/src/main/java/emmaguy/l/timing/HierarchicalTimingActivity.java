package emmaguy.l.timing;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewPropertyAnimator;

import emmaguy.l.R;
import emmaguy.l.shared.AnimListener;


public class HierarchicalTimingActivity extends Activity {

    private static final int NUM_VIEWS = 8;
    private static final int SCALE_ITEM_ANIMATION_DELAY = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hierarchical_timing);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < NUM_VIEWS; i++) {
                    int resID = getResources().getIdentifier("view_" + (i + 1), "id", getPackageName());
                    findViewById(resID).animate().setStartDelay(i * SCALE_ITEM_ANIMATION_DELAY).scaleX(1).scaleY(1);
                }
            }
        }, 500);
    }

    @Override
    public void onBackPressed() {
        for (int i = 0; i < NUM_VIEWS; i++) {
            int resID = getResources().getIdentifier("view_" + (i + 1), "id", getPackageName());

            ViewPropertyAnimator viewPropertyAnimator = findViewById(resID).animate().setStartDelay(i * SCALE_ITEM_ANIMATION_DELAY).scaleX(0).scaleY(0);
            if (i + 1 >= NUM_VIEWS) {
                viewPropertyAnimator.setListener(new AnimListener() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        finishAfterTransition();
                    }
                });
            }
        }
    }
}
