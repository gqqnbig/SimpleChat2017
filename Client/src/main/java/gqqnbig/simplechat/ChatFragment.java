package gqqnbig.simplechat;

import android.app.Fragment;

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.examples.helloworld.ChatFromServer;
import io.grpc.examples.helloworld.ChatMessage;
import io.grpc.examples.helloworld.ChatServerGrpc;
import io.grpc.stub.StreamObserver;


/**
 * Created by Tan on 2017/8/11.
 */

public class ChatFragment extends Fragment {
    private Button send,back;
    private EditText et, ipet;
    private StreamObserver<ChatMessage> server;
    private ManagedChannel channel;
    private String message, check, user;
    private ListView listView;
    private List<ChatBubble> ChatBubbles;
    private ArrayAdapter<ChatBubble> adapter;


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
        send = (Button) getActivity().findViewById(R.id.sendButton);
        back = (Button) getActivity().findViewById(R.id.back_btn);


        et = (EditText) getActivity().findViewById(R.id.messageTextBox);
        ipet = (EditText) getActivity().findViewById(R.id.IPTextBox);
        ChatBubbles = new ArrayList<>();

        final SimpleChat test = (SimpleChat) getActivity();
        user = test.userID;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                test.userID = "";
                FragmentManager manager = getActivity().getFragmentManager();
                manager.beginTransaction().replace(R.id.framelayout, new LoginFragment()).commit();

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Please input some text...", Toast.LENGTH_SHORT).show();
                } else {
                    server.onNext(ChatMessage.newBuilder().setMessage(et.getText().toString()).setId(user).setTo(ipet.getText().toString()).build());

                    ChatBubbles.add(new ChatBubble(et.getText().toString(), false));
                    adapter.notifyDataSetChanged();
                    et.getText().clear();


                }
            }
        });


        listView = (ListView) getActivity().findViewById(R.id.list_msg);

        adapter = new MessageAdapter(getActivity(), R.layout.right_chat_bubble, ChatBubbles);
        listView.setAdapter(adapter);
        final Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                if (message!=check){
                    ChatBubbles.add(new ChatBubble(message,true));
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
