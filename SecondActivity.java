package kr.ac.mjc.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity implements MessageAdapter.OnMessageClickListener {

    RecyclerView messageListRv;
    EditText messageEt;
    Button submitBtn;

    FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    List<Message> mMessageList=new ArrayList<>();
    MessageAdapter mMessageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        messageListRv=findViewById(R.id.message_list_rv);
        messageEt=findViewById(R.id.message_et);
        submitBtn=findViewById(R.id.submit_btn);

        String email=auth.getCurrentUser().getEmail();

        mMessageAdapter=new MessageAdapter(this, mMessageList,email);
        messageListRv.setAdapter(mMessageAdapter);

        mMessageAdapter.setOnMessageClickListener(this);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        messageListRv.setLayoutManager(layoutManager);

        firestore.collection("message")
                .orderBy("date", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value!=null){
                            List<DocumentChange>documentChangeList=value.getDocumentChanges();
                            for(DocumentChange dc:documentChangeList){
                                if(dc.getType()==DocumentChange.Type.ADDED){
                                    Message message=dc.getDocument().toObject(Message.class);
                                    String id=dc.getDocument().getId();
                                    message.setId(id);
                                    mMessageList.add(message);
                                }
                                if(dc.getType()==DocumentChange.Type.REMOVED){
                                    String id=dc.getDocument().getId();
                                    for(Message message:mMessageList){
                                        if(id.equals(message.getId())){
                                            mMessageList.remove(message);
                                            break;
                                        }
                                    }
                                }
                            }
                            mMessageAdapter.notifyDataSetChanged();
                            messageListRv.scrollToPosition(mMessageList.size()-1);
                        }

                    }
                });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=messageEt.getText().toString();
                if(text.equals("")){
                    return;
                }
                messageEt.setText("");

                Message message=new Message();
                message.setText(text);

                String email=auth.getCurrentUser().getEmail();
                message.setWriterId(email);

                firestore.collection("message")
                        .document().set(message);
            }
        });

    }

    @Override
    public void onMessageLongClick(Message message) {
        Toast.makeText(SecondActivity.this, message.getId(), Toast.LENGTH_SHORT).show();
        firestore.collection("message").document(message.getId()).delete();
    }
}


