package com.example.parstagram.profile;

import android.util.Log;

import com.example.parstagram.Post;
import com.example.parstagram.feed.PostsFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment {

    private static final int QUERY_LIMIT = 20;

    @Override
    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER)
                .whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
                .setLimit(QUERY_LIMIT)
                .addDescendingOrder("createdAt")
                .findInBackground(new FindCallback<Post>() {
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
