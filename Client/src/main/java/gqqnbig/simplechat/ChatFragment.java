package gqqnbig.simplechat;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.examples.helloworld.ChatFromServer;
import io.grpc.examples.helloworld.ChatMessage;
import io.grpc.examples.helloworld.ChatServerGrpc;
import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import io.grpc.stub.StreamObserver;

import static gqqnbig.simplechat.R.layout.left_chat_bubble;
import static gqqnbig.simplechat.R.layout.right_chat_bubble;

/**
 * Created by Tan on 2017/8/11.
 */

public class ChatFragment extends Fragment {
    private Button btn;
    private TextView view,view1,view2;
    private EditText et,ipet;
    private StreamObserver<ChatMessage> server;
    private ManagedChannel channel;
    private LinearLayout layout,layout1;
    private String message,check,user;
    private ListView listView;
    private View btnSend;
    private List<ChatBubble> ChatBubbles;
    private ArrayAdapter<ChatBubble> adapter;
    private boolean myMessage = true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        message = check = "";
        View view = inflater.inflate(R.layout.talk_app,null);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn = (Button) getActivity().findViewById(R.id.sendButton);

        layout = (LinearLayout) getActivity().findViewById(R.id.layout);
        et = (EditText) getActivity().findViewById(R.id.messageTextBox);
        ipet = (EditText) getActivity().findViewById(R.id.IPTextBox);
        ChatBubbles = new ArrayList<>();

        Test test = (Test) getActivity();
        user = test.userID;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Please input some text...", Toast.LENGTH_SHORT).show();
                } else {
                    server.onNext(ChatMessage.newBuilder().setMessage(et.getText().toString()).setId(user).setTo(ipet.getText().toString()).build());
                    myMessage = false;
                    ChatBubbles.add(new ChatBubble(et.getText().toString(),right_chat_bubble));
                    adapter.notifyDataSetChanged();
                    et.getText().clear();


                }
            }
        });


        listView = (ListView) getActivity().findViewById(R.id.list_msg);
        btnSend = getActivity().findViewById(R.id.sendButton);

        //set ListView adapter first
        adapter = new MessageAdapter(getActivity(), R.layout.right_chat_bubble, ChatBubbles);
        listView.setAdapter(adapter);
        final Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                if (message!=check){
                    myMessage = true;
                    ChatBubbles.add(new ChatBubble(message,left_chat_bubble));
                    adapter.notifyDataSetChanged();
                    check = message;
                }
                handler.postDelayed(this, 200);
            }
        };
        handler.postDelayed(runnable, 200);

        channel = ManagedChannelBuilder.forAddress("172.31.100.52", 50051)
                .usePlaintext(true)
                .build();

        new GrpcTask().execute();
    }
    private class GrpcTask extends AsyncTask<String, Void, String> {

        private final int mPort = 50051;



        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(final String... messages) {
            ChatServerGrpc.ChatServerStub stub = ChatServerGrpc.newStub(channel);


            server = stub.chat(new StreamObserver<ChatFromServer>() {
                @Override
                public void onNext(ChatFromServer value) {
                    message = value.getFrom()+": "+value.getMessage();

                }
                @Override
                public void onError(Throwable t) {


                }
                @Override
                public void onCompleted() {

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            server.onNext(ChatMessage.newBuilder().setId(user).setTo(user).build());




        }
    }


}
