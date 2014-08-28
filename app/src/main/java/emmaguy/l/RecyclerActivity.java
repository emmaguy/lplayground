package emmaguy.l;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class RecyclerActivity extends Activity {
    private final String[] mSubreddits = new String[]{"pics"};
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recycler);

        final RecyclerAdapter adapter = new RecyclerAdapter();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        //mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        getApp().retrieveLatestPostsFromReddit(getSubreddit(), getSortType(), getNumberToRequest(), new Action1<List<Post>>() {
            @Override
            public void call(List<Post> posts) {
                adapter.addPosts(posts);
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
        private final List<Post> mItems = new ArrayList<Post>();

        public void addPosts(List<Post> posts) {
            mItems.addAll(posts);

            notifyDataSetChanged();
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

            holder.mImageView.setViewName(post.getPermalink()); // unique
            holder.mTextView.setText(post.getTitle());
            Picasso.with(RecyclerActivity.this).load(post.getUrl()).fit().centerCrop().into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView mTextView;
            public ImageView mImageView;

            public ViewHolder(View v) {
                super(v);

                v.findViewById(R.id.ripple_view).setOnClickListener(this);

                mTextView = (TextView) v.findViewById(R.id.text_view);
                mImageView = (ImageView) v.findViewById(R.id.image_view);
            }

            @Override
            public void onClick(View view) {
                Post p = mItems.get(getPosition());

                Intent i = new Intent(RecyclerActivity.this, DetailActivity.class);
                i.putExtra(DetailActivity.EXTRA_URL, p.getUrl());
                i.putExtra(DetailActivity.EXTRA_SUBREDDIT, p.getSubreddit());
                i.putExtra(DetailActivity.EXTRA_TITLE, p.getTitle());
                i.putExtra(DetailActivity.EXTRA_TEXT, p.getDescription());

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RecyclerActivity.this, mImageView, DetailActivity.VIEW_NAME_HEADER_IMAGE);

                startActivity(i, options.toBundle());
            }
        }
    }
}
