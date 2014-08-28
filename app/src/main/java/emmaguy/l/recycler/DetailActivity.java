package emmaguy.l.recycler;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.transition.Transition;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import emmaguy.l.R;


public class DetailActivity extends Activity {

    public static final String EXTRA_URL = "extra_url";
    public static final String EXTRA_SUBREDDIT = "extra_subreddit";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_TEXT = "extra_text";

    public static final String VIEW_NAME_HEADER_IMAGE = "detail:image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        String url = getIntent().getStringExtra(EXTRA_URL);
        String subreddit = getIntent().getStringExtra(EXTRA_SUBREDDIT);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        String text = getIntent().getStringExtra(EXTRA_TEXT);

        ImageView imageView = (ImageView) findViewById(R.id.detail_image_view);
        imageView.setViewName(VIEW_NAME_HEADER_IMAGE);

        Picasso.with(this).load(url).fit().centerCrop().into(imageView);

        TextView textView = (TextView) findViewById(R.id.detail_text_view);
        textView.setText(title + "\n\n" + text);

        getActionBar().setTitle(subreddit);
    }
}
