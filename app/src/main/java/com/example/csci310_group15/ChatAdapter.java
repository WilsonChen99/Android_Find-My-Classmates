package com.example.csci310_group15;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Message> messages;

    public ChatAdapter(ArrayList<Message>  messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.send, parent, false);
            return new ViewHolderSend(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.receive, parent, false);
            return new ViewHolderReceive(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getClass() == ViewHolderSend.class) {
            ViewHolderSend send = (ViewHolderSend) holder;
            send.getTextMessage().setText(messages.get(position).message);

        } else {
            ViewHolderReceive receive = (ViewHolderReceive) holder;
            receive.getTextMessage().setText(messages.get(position).message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (FirebaseAuth.getInstance().getUid().equals(message.uid)) {
            return 0;
        } else {
            return 1;
        }
    }

    public static class ViewHolderSend extends RecyclerView.ViewHolder {
        private final TextView textMessage;
        public ViewHolderSend(View view) {
            super(view);
            textMessage = view.findViewById(R.id.textMessage);
        }
        public TextView getTextMessage() {
            return textMessage;
        }

    }

    public static class ViewHolderReceive extends RecyclerView.ViewHolder {
        private final TextView textMessage;
        public ViewHolderReceive(View view) {
            super(view);
            textMessage = view.findViewById(R.id.textMessage);
        }
        public TextView getTextMessage() {
            return textMessage;
        }

    }
}
