package com.ait.kim.pantryrescue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.ait.kim.pantryrescue.data.Post;

import java.util.List;

/**
 * Created by alicetan on 12/11/17.
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {


    private List<Post> postList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
