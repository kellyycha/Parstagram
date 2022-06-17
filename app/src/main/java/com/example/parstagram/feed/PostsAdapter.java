package com.example.parstagram.feed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.parstagram.Post;
import com.example.parstagram.R;
import com.parse.ParseFile;
import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    public static final String TAG = "PostsAdapter";

    private Context context;
    private List<Post> posts;
    // TODO: Fix Like to work through network
    private List<Post> likedPosts = new ArrayList<>();

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;
        private ImageView ivProfilePic;
        private TextView tvTime;
        private ImageButton btnHeart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvTime = itemView.findViewById(R.id.tvTime);
            btnHeart = itemView.findViewById(R.id.btnHeart);
            itemView.setOnClickListener(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @SuppressLint("ResourceAsColor")
        public void bind(Post post) {

            String username = post.getUser().getUsername();
            String description = post.getDescription();

            SpannableStringBuilder caption = new SpannableStringBuilder(username + " " + description);
            caption.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvDescription.setText(caption);

            tvUsername.setText(username);
            Date createdAt = post.getCreatedAt();
            tvTime.setText(Post.calculateTimeAgo(createdAt));
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }

            ParseFile pfp = post.getUser().getParseFile("profilePicture");
            if (pfp != null) {
                Glide.with(context).load(pfp.getUrl()).apply(RequestOptions.circleCropTransform()).into(ivProfilePic);
            }
            else{
                // default
                Glide.with(context).load(R.drawable.default_pfp).apply(RequestOptions.circleCropTransform()).into(ivProfilePic);
            }

            likeButton(post);
        }

        private void likeButton(Post post) {
            btnHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"clicked like button");
                    likePost(post);
                }
            });
        }

        private void likePost(Post post) {
            if (likedPosts.contains(post)){
                likedPosts.remove(post);
                btnHeart.setImageResource(R.drawable.ic_heart);
            }
            else{
                likedPosts.add(post);
                btnHeart.setImageResource(R.drawable.ic_heart_filled);
            }
            Log.d(TAG,"Liked Posts:" + likedPosts);
        }

        public void onClick(View v) {
            Log.i(TAG, "Click Post");
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                Post post = posts.get(position);
                // create intent for the new activity
                Intent i = new Intent(context, PostDetailActivity.class);
            // serialize the movie using parceler, use its short name as a key
            i.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
            // show the activity
                context.startActivity(i);
            }
        }
    }


    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }
}