package emmaguy.l;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import emmaguy.l.choreog.ConsistentChoreographyActivity;
import emmaguy.l.recycler.RecyclerActivity;
import emmaguy.l.timing.HierarchicalTimingActivity;


public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.button_hie_timing).setOnClickListener(this);
        findViewById(R.id.button_recycler).setOnClickListener(this);
        findViewById(R.id.button_cons_choreo).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_hie_timing) {
            startActivity(new Intent(this, HierarchicalTimingActivity.class));
        } else if (view.getId() == R.id.button_recycler) {
            startActivity(new Intent(this, RecyclerActivity.class));
        } else if (view.getId() == R.id.button_cons_choreo) {
            startActivity(new Intent(this, ConsistentChoreographyActivity.class));
        }
    }
}
