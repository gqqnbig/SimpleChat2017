package gqqnbig.simplechat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;
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

/**
 * Created by Tan on 2017/8/8.
 */

public class TalkApp extends AppCompatActivity {
    private Button btn;
    private TextView view;
    private EditText et;
    private StreamObserver<ChatMessage> server;
    ManagedChannel channel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk_app);
        btn = (Button) findViewById(R.id.sendButton);

        view = (TextView) findViewById(R.id.View);
        et = (EditText) findViewById(R.id.messageTextBox);



        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                channel = ManagedChannelBuilder.forAddress("172.31.100.52",50051)
                        .usePlaintext(true)
                        .build();
                ChatServerGrpc.ChatServerStub stub = ChatServerGrpc.newStub(channel);



                server = stub.chat(new StreamObserver<ChatFromServer>() {
                    @Override
                    public void onNext(ChatFromServer value) {
                        view.setText(value.getMessage().toString());

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onCompleted() {

                    }
                });
            }
        });
        thread.start();


    }

    public void OnSendButtonClicked(View v) {

        server.onNext(ChatMessage.newBuilder().setMessage(et.getText().toString()).build());
        et.getText().clear();
    }


}
