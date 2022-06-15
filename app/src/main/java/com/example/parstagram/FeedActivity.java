package com.example.parstagram;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class FeedActivity extends AppCompatActivity {

    public static final String TAG = "FeedActivity";

    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    private RecyclerView rvPosts;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        rvPosts = findViewById(R.id.rvPosts);

        // pullToRefresh();

        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(this, allPosts);

        // set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        // query posts from Parstagram
        queryPosts();

    }

    private void pullToRefresh() {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                //fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

//    public void fetchTimelineAsync(int page) {
//        // Send the network request to fetch the updated data
//        // `client` here is an instance of Android Async HTTP
//        // getHomeTimeline is an example endpoint.
//        client.getHomeTimeline(new JsonHttpResponseHandler() {
//        public void onSuccess(JSONArray json) {
//                // Remember to CLEAR OUT old items before appending in the new ones
//                adapter.clear();
//                // ...the data has come back, add new items to your adapter...
//                adapter.addAll(...);
//                // Now we call setRefreshing(false) to signal refresh has finished
//                swipeContainer.setRefreshing(false);
//            }
//
//            public void onFailure(Throwable e) {
//                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
//            }
//        });
//    }

    private void queryPosts() {
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                // save received posts to list and notify adapter of new data
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }

}