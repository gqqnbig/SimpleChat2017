package gqqnbig.simplechat;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Tan on 2017/8/11.
 */

public class LoginFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_main,null);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final EditText et = (EditText) getActivity().findViewById(R.id.IPTextBox);
        Button btn = (Button) getActivity().findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et.getText().toString().equals("")){
                Test test = (Test)getActivity();
                test.userID = et.getText().toString();
                FragmentManager manager = getActivity().getFragmentManager();
                manager.beginTransaction().replace(R.id.framelayout, new ChatFragment()).commit();
                }else{
                    Toast toast = Toast.makeText(getActivity(),"请输入ID",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
