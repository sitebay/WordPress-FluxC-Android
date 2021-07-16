package org.sitebay.android.fluxc.model;

import androidx.annotation.NonNull;

import org.sitebay.android.fluxc.Payload;
import org.sitebay.android.fluxc.network.BaseRequest.BaseNetworkError;

import java.util.ArrayList;
import java.util.List;

public class PostsModel extends Payload<BaseNetworkError> {
    private List<PostModel> mPosts;

    public PostsModel() {
        mPosts = new ArrayList<>();
    }

    public PostsModel(@NonNull List<PostModel> posts) {
        mPosts = posts;
    }

    public List<PostModel> getPosts() {
        return mPosts;
    }

    public void setSites(List<PostModel> posts) {
        this.mPosts = posts;
    }
}
