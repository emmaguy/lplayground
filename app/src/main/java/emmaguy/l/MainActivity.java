package emmaguy.l;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import rx.functions.Action1;


public class MainActivity extends Activity {
    private final String[] mSubreddits = new String[]{"pics"};
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        getApp().retrieveLatestPostsFromReddit(getSubreddit(), getSortType(), getNumberToRequest(), new Action1<List<Post>>() {
            @Override
            public void call(List<Post> posts) {
                mRecyclerView.setAdapter(new RecyclerAdapter(posts));
            }
        });
    }

    private LApplication getApp() {
        return (LApplication) getApplicationContext();
    }

    private int getNumberToRequest() {
        return 15;//Integer.parseInt(getSharedPreferences().getString(LApplication.PREFS_NUMBER_TO_RETRIEVE, "5"));
    }

    private String getSortType() {
        return getSharedPreferences().getString(LApplication.PREFS_SORT_ORDER, "new");
    }

    private String getSubreddit() {
        return TextUtils.join("+", mSubreddits);
    }

    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }


    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        private final List<Post> mItems;

        public RecyclerAdapter(List<Post> posts) {
            mItems = posts;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);

            ViewHolder vh = new ViewHolder(v);

            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Post post = mItems.get(position);

            holder.mTextView.setText(post.getTitle());
            Picasso.with(MainActivity.this).load(post.getUrl()).fit().centerCrop().into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;
            public ImageView mImageView;

            public ViewHolder(View v) {
                super(v);

                mTextView = (TextView) v.findViewById(R.id.text_view);
                mImageView = (ImageView) v.findViewById(R.id.image_view);
            }
        }
    }
}
