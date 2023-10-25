package com.example.csci310_group15;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private ArrayList<User> users;
    private Context context;

    public ChatListAdapter(Context context, ArrayList<User> users) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.user_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        String name = user.getName();
        String uid = user.getUid();
        String role = user.getStanding().toUpperCase();
        String uri = user.getUri();
        holder.getContactName().setText(name);
        holder.getContactRole().setText(role);
        Picasso.get().load(uri).into(holder.getProfilePic());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("uid", uid);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView contactName;
        private final TextView contactRole;
        private final ImageView profilePic;
        public ViewHolder(View view) {
            super(view);
            contactName = view.findViewById(R.id.contactName);
            contactRole = view.findViewById(R.id.contactRole);
            profilePic = view.findViewById(R.id.profilePic);
        }
        public TextView getContactName() {
            return contactName;
        }
        public TextView getContactRole() {
            return contactRole;
        }
        public ImageView getProfilePic() {
            return profilePic;
        }
    }
}
