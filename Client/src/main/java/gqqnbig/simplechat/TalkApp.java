package gqqnbig.simplechat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

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
    private ManagedChannel channel;
    private LinearLayout layout;
    private String message="";
    private String check = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk_app1);
        btn = (Button) findViewById(R.id.sendButton);


        et = (EditText) findViewById(R.id.messageTextBox);
        layout = (LinearLayout) findViewById(R.id.layout);
        final Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                if (message!=check){
                TextView tv = new TextView(TalkApp.this);
                tv.setText(message);
                layout.addView(tv);
                check = message;
                }
                handler.postDelayed(this, 200);
            }
        };
        handler.postDelayed(runnable, 2000);
                channel = ManagedChannelBuilder.forAddress("172.31.100.52",50051)
                        .usePlaintext(true)
                        .build();
                ChatServerGrpc.ChatServerStub stub = ChatServerGrpc.newStub(channel);



                server = stub.chat(new StreamObserver<ChatFromServer>() {
                    @Override
                    public void onNext(ChatFromServer value) {
                //TextView tv = new TextView(TalkApp.this);
                //tv.setText(value.getMessage());
                //layout.addView(tv);
                //view.setText(value.getMessage());
                message = value.getMessage();

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onCompleted() {

                    }
                });
        server.onNext(ChatMessage.newBuilder().setMessage("").setId("b").setTo("b").build());





    }

    public void OnSendButtonClicked(View v) {

        server.onNext(ChatMessage.newBuilder().setMessage(et.getText().toString()).setId("b").setTo("").build());
        et.getText().clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            channel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread.currentThread().interrupt();
     }
}
