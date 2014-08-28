package emmaguy.l;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.button_hie_timing).setOnClickListener(this);
        findViewById(R.id.button_recycler).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_hie_timing) {
            startActivity(new Intent(this, HierarchicalTimingActivity.class));
        } else if(view.getId() == R.id.button_recycler) {
            startActivity(new Intent(this, RecyclerActivity.class));
        }
    }
}
