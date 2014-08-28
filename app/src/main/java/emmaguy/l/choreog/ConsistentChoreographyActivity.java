package emmaguy.l.choreog;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;

import emmaguy.l.R;


public class ConsistentChoreographyActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_consistent_choreography);

        findViewById(R.id.circle_1).setOnClickListener(this);
        findViewById(R.id.circle_2).setOnClickListener(this);
        findViewById(R.id.circle_3).setOnClickListener(this);
        findViewById(R.id.circle_4).setOnClickListener(this);

        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.circular_reveal);

        getWindow().setSharedElementEnterTransition(transition);
        getWindow().setSharedElementExitTransition(transition);
    }

    @Override
    public void onClick(View view) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, view, ConsistentChroeography2Activity.CIRCLE_VIEW_NAME);

        startActivity(new Intent(this, ConsistentChroeography2Activity.class), options.toBundle());
    }
}
