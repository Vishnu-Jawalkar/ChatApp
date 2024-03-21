package com.example.chatapp.views;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.example.chatapp.databinding.ActivityGroupsBinding;
import com.example.chatapp.model.ChatGroup;
import com.example.chatapp.viewmodel.MyViewModel;
import com.example.chatapp.views.adapters.GroupAdapter;

import java.util.ArrayList;
import java.util.List;

public class GroupsActivity extends AppCompatActivity {

    private ArrayList<ChatGroup> chatGroupArrayList;
    private RecyclerView recyclerView;
    private GroupAdapter groupAdapter;
    private ActivityGroupsBinding binding;
    private MyViewModel myViewModel;

    //Dialog
    private Dialog chatGroupDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_groups);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = DataBindingUtil.setContentView(this, R.layout.activity_groups);


        //Define the ViewModel

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        //RecycleView with Data Binding
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Setup an observer to listen for changes in a "Live Data" object
        myViewModel.getGroupList().observe(this, new Observer<List<ChatGroup>>() {
            @Override
            public void onChanged(List<ChatGroup> chatGroups) {
                //the updated data is received as "chatGroup" parameter in onChanged()

                chatGroupArrayList = new ArrayList<>();
                chatGroupArrayList.addAll(chatGroups);

                groupAdapter = new GroupAdapter(chatGroupArrayList);

                recyclerView.setAdapter(groupAdapter);
                groupAdapter.notifyDataSetChanged();
            }
        });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    public void showDialog() {
        chatGroupDialog = new Dialog(this);
        chatGroupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);


        chatGroupDialog.setContentView(view);
        chatGroupDialog.show();

        Button submit = view.findViewById(R.id.submit_btn);
        EditText editText = view.findViewById(R.id.chat_group_edt);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = editText.getText().toString();

                Toast.makeText(GroupsActivity.this,
                        "Your Chat Group:" + groupName, Toast.LENGTH_SHORT).show();

                myViewModel.createNewGroup(groupName);
                chatGroupDialog.dismiss();
            }
        });
    }


}