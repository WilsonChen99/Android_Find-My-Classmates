package com.example.csci310_group15;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class MenuBar
{
    private FirebaseAuth mAuth;

    public enum Page {
        Home, Profile, Chat
    }

    public void loadMenuBar(Context context, LinearLayout grid, Page page)
    {
        mAuth = FirebaseAuth.getInstance();

        LayoutInflater li = LayoutInflater.from(context);
        LinearLayout menuBar = (LinearLayout) li.inflate(R.layout.activity_menu_bar, grid, true);
        Button bttnLeft = menuBar.findViewById(R.id.menu_bttn_left);
        Button bttnMid = menuBar.findViewById(R.id.menu_bttn_mid);
        Button bttnLogout = menuBar.findViewById(R.id.menu_bttn_right);

        // [ Left Bttn Setup ]
        // Set text
        if(page != Page.Chat)
        {
            bttnLeft.setText(R.string.menu_bar_chat);
        }
        // Chat page has left button of profile
        else
        {
            bttnLeft.setText(R.string.menu_bar_profile);

        }
        // Set onclick
        bttnLeft.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                // None chat page has left button of chat
                if(page != Page.Chat)
                {
                    bttnLeft.setText(R.string.menu_bar_chat);
                    Intent intent = new Intent(context, ChatListActivity.class);
                    context.startActivity(intent);
                }
                // Chat page has left button of profile
                else
                {
                    bttnLeft.setText(R.string.menu_bar_profile);
                    Intent intent = new Intent(context, ProfileActivity.class);
                    context.startActivity(intent);

                }
            }
        });

        // [ Mid Bttn Setup ]
        // Set Text
        if(page != Page.Home)
        {
            bttnMid.setText(R.string.menu_bar_home);
        }
        // Home page has mid bttn of profile
        else
        {
            bttnMid.setText(R.string.menu_bar_profile);
        }
        // Set Onclick
        bttnMid.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                // Non Home page has mid bttn of home
                if(page != Page.Home)
                {
                    bttnMid.setText(R.string.menu_bar_home);
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                }
                // Home page has mid bttn of profile
                else
                {
                    bttnMid.setText(R.string.menu_bar_profile);
                    Intent intent = new Intent(context, ProfileActivity.class);
                    context.startActivity(intent);
                }


            }
        });

        // [ Right Bttn Setup ] ( Always logout )
        bttnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                mAuth.signOut();
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        });


    }
}