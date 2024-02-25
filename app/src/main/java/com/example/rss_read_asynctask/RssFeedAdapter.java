package com.example.rss_read_asynctask;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RssFeedAdapter extends RecyclerView.Adapter<RssFeedAdapter.ViewHolder> {
    private List<RssFeedItem> rssFeedItems;

    public RssFeedAdapter(List<RssFeedItem> rssFeedItems) {
        this.rssFeedItems = rssFeedItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rss_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RssFeedItem rssFeedItem = rssFeedItems.get(position);
        holder.textViewTitle.setText(rssFeedItem.getTitle());
        holder.textViewDescription.setText(rssFeedItem.getDescription());
        holder.btnOpenLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(rssFeedItem.getLink()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rssFeedItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        Button btnOpenLink;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            btnOpenLink = itemView.findViewById(R.id.btnOpenLink);
        }
    }
}

