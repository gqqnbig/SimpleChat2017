package gqqnbig.simplechat;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;


import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Tan on 2017/8/11.
 */

public class Test extends AppCompatActivity {
    String userID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        LoginFragment login = new LoginFragment();

        FragmentManager manager = getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();

        trans.replace(R.id.framelayout, login);
        trans.commit();
    }

}
