package emmaguy.l;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LApplication extends Application {

    public static final String PREFS_NUMBER_TO_RETRIEVE = "number_to_retrieve";
    public static final String PREFS_SORT_ORDER = "sort_order";
    public static final String PREFS_CREATED_UTC = "created_utc";

    private final RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://www.reddit.com/")
            .setConverter(new GsonConverter(new GsonBuilder().registerTypeAdapter(Listing.class, new Listing.ListingJsonDeserializer()).create()))
            .build();

    private final Reddit mRedditEndpoint = restAdapter.create(Reddit.class);

    private long mLatestCreatedUtc = 0;

    public void retrieveLatestPostsFromReddit(String subreddit, String sortType, int numberToRequest, final Action1<List<Post>> newPostsCallback) {
        final ArrayList<Post> newPosts = new ArrayList<Post>();

        mRedditEndpoint.latestPosts(subreddit, sortType, numberToRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Listing, Observable<Post>>() {
                    @Override
                    public Observable<Post> call(Listing listing) {
                        return Observable.from(listing.getPosts());
                    }
                })
                .subscribe(new Action1<Post>() {
                    @Override
                    public void call(Post post) {
                        Log.d("L", "url: " + post.getUrl());
                        if(!isDirectUrlToImage(post.getUrl())) {
                            return;
                        }

                        if (postIsNewerThanPreviouslyRetrievedPosts(post)) {
                            Log.d("L", "Adding post: " + post.getTitle());

                            newPosts.add(post);

                            if (post.getCreatedUtc() > mLatestCreatedUtc) {
                                mLatestCreatedUtc = post.getCreatedUtc();
                                Log.d("L", "updating mLatestCreatedUtc to: " + mLatestCreatedUtc);
                            }
                        } else {
                            Log.d("L", "Ignoring post: " + post.getTitle());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("L", "Failed to retrieve latest posts: " + throwable.getLocalizedMessage(), throwable);
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        Log.d("L", "Posts found: " + newPosts.size());
                        if (newPosts.size() > 0) {
                            if (mLatestCreatedUtc > 0) {
                                storeNewCreatedUtc(mLatestCreatedUtc);
                            }

                            newPostsCallback.call(newPosts);
                        }
                    }
                });
    }

    private boolean isDirectUrlToImage(String url) {
        return url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".png");
    }

    private boolean postIsNewerThanPreviouslyRetrievedPosts(Post post) {
        return post.getCreatedUtc() > getCreatedUtcOfPosts();
    }

    private void storeNewCreatedUtc(long createdAtUtc) {
        Log.d("L", "storeNewCreatedUtc: " + createdAtUtc);

        getSharedPreferences().edit().putLong(PREFS_CREATED_UTC, createdAtUtc).apply();
    }

    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    private long getCreatedUtcOfPosts() {
        return 0;//getSharedPreferences().getLong(PREFS_CREATED_UTC, 0);
    }
}
