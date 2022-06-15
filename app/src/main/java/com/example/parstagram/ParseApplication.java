package com.example.parstagram;

import android.app.Application;

import com.example.parstagram.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("2hFg7JyTuldqlcqkzfk5onXg7cA8Ugw80pvvmqcu")
                .clientKey("ey0crZeJzFVzS6J1v8DTVTJ5uVhnF35ydQ4mMljZ")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
