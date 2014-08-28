package emmaguy.l.recycler.data;

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

public class RedditDownloader {

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
                        if (!isDirectUrlToImage(post.getUrl())) {
                            return;
                        }

                        Log.d("L", "Adding post: " + post.getTitle());

                        newPosts.add(post);

                        if (post.getCreatedUtc() > mLatestCreatedUtc) {
                            mLatestCreatedUtc = post.getCreatedUtc();
                            Log.d("L", "updating mLatestCreatedUtc to: " + mLatestCreatedUtc);
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
                            newPostsCallback.call(newPosts);
                        }
                    }
                });
    }

    private boolean isDirectUrlToImage(String url) {
        return url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".png");
    }
}
