package com.example.csci310_group15;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private ArrayList<User> users;
    private Context context;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    public ChatListAdapter(Context context, ArrayList<User> users) {
        this.users = users;
        this.context = context;
        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
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

        myRef.child("blockedUsers").child(mAuth.getUid()).orderByValue().equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    // Enable on click listeners
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra("name", name);
                            intent.putExtra("uid", uid);
                            context.startActivity(intent);
                        }
                    });

                    holder.getBlockBtn().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myRef.child("blockedUsers").child(mAuth.getUid()).push().setValue(uid);
                        }
                    });
                } else {
                    holder.getBlockBtn().setText("UNDO");
                    holder.getBlockBtn().setBackgroundResource(R.drawable.block_btn);
                    holder.getBlockBtn().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myRef.child("blockedUsers").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot child: snapshot.getChildren()) {
                                        String val = child.getValue(String.class);
                                        if (val.equals(uid)) {
                                            child.getRef().removeValue();
                                            holder.getBlockBtn().setBackgroundResource(R.drawable.btn_bg);
                                            holder.getBlockBtn().setText("BLOCK");
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    System.out.println("Error unblocking");
                                }
                            });
                        }
                    });
                    holder.itemView.setOnClickListener(null);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Do nothing
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
        private final Button blockBtn;
        public ViewHolder(View view) {
            super(view);
            contactName = view.findViewById(R.id.contactName);
            contactRole = view.findViewById(R.id.contactRole);
            profilePic = view.findViewById(R.id.profilePic);
            blockBtn = view.findViewById(R.id.blockBtn);
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
        public Button getBlockBtn() {
            return blockBtn;
        }
    }
}
