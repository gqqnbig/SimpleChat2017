package gqqnbig.simplechat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_PHONE_STATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            showDeviceNumber();
        } else {
            Toast.makeText(this, "本程序需要获取手机识别码，请允许", Toast.LENGTH_LONG).show();

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
        }

    }

    private void showDeviceNumber() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        final String sid = telephonyManager.getDeviceId();

        getSupportActionBar().setTitle("设备ID是" + sid);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    showDeviceNumber();
                else
                    Toast.makeText(this, "没有获得权限", Toast.LENGTH_LONG).show();
            }
        }
    }

    //XML android:onClick属性自动连接事件处理方法，要求该方法访问性为public。
    public void OnSendButtonClicked(View v) {
        EditText messageTextBox= (EditText)findViewById(R.id.messageTextBox);
        Editable text= messageTextBox.getText();

        TextView historyTextView=  (TextView)findViewById(R.id.historyTextView);
        historyTextView.setText(historyTextView.getText() + "\n" + text.toString());

        text.clear();
    }

}
