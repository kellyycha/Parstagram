package com.example.parstagram.feed;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.parstagram.Post;
import com.example.parstagram.R;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.Date;


public class PostDetailActivity extends AppCompatActivity {

    public static final String TAG = "PostDetailActivity";

    Post post;

    private ImageView ivPost;
    private TextView tvCaption;
    private TextView tvTimestamp;
    private TextView tvUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        ivPost = findViewById(R.id.ivPost);
        tvCaption = findViewById(R.id.tvCaption);
        tvTimestamp = findViewById(R.id.tvTimestamp);
        tvUser = findViewById(R.id.tvUser);

        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        Log.d(TAG, String.format("Showing details for '%s'", post.getDescription()));

        formatCaption();

        Date createdAt = post.getCreatedAt();
        tvTimestamp.setText(Post.calculateTimeAgo(createdAt));

        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivPost);
        }
    }

    //TODO: spannable string builder
    private void formatCaption() {
        String user = post.getUser().getUsername();
        String caption = post.getDescription();
        String placeholder = " ";
        for (int i = 0; i < user.length(); i++){
            if (user.charAt(i) !=  'i' && user.charAt(i) != 'l'){
                placeholder += " ";
            }
            placeholder += " ";
        }
        tvCaption.setText(placeholder + " " + caption);
        tvUser.setText(user);
    }

}
