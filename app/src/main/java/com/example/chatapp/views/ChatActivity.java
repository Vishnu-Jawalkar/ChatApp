package com.example.chatapp.views;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityChatBinding;
import com.example.chatapp.model.ChatMessage;
import com.example.chatapp.viewmodel.MyViewModel;
import com.example.chatapp.views.adapters.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private MyViewModel myViewModel;
    private RecyclerView recyclerView;
    private ChatAdapter myAdapter;

    private List<ChatMessage> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);


        binding = DataBindingUtil.setContentView(this,R.layout.activity_chat);

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        //RecyclerView with DataBinding
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //Getting the Group Name from the Clicked Item in the GroupActivity

        String groupName = getIntent().getStringExtra("GROUP_NAME");

        myViewModel.getMessagesLiveData(groupName).observe(this, new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(List<ChatMessage> chatMessages) {
                messageList = new ArrayList<>();
                messageList.addAll(chatMessages);

                myAdapter = new ChatAdapter(messageList,getApplicationContext());

                recyclerView.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();

                //Scroll to the latest msg added
                int latestPosition =  myAdapter.getItemCount() -1;
                if(latestPosition > 0){
                    recyclerView.smoothScrollToPosition(latestPosition);
                }


            }
        });

        binding.setViewModel(myViewModel);

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = binding.edittextChatMsg.getText().toString();

                myViewModel.sendMessage(msg,groupName);

                binding.edittextChatMsg.getText().clear();
            }
        });

    }
}