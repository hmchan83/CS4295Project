package com.cs4295.team.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.Button;
import android.widget.EditText;

import com.cs4295.team.MainActivity;
import com.cs4295.team.R;
import com.cs4295.team.util.APICallBuilder;
import com.cs4295.team.util.Sharedinfo;

import org.json.JSONObject;

/**
 * Created by Marcus on 3/27/2015.
 */
public class NewTeamFragment  extends Fragment {
    EditText inputname;
    EditText inputdesrc;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.newteam, container, false);
        inputname = (EditText)rootView.findViewById(R.id.newteam_inputname);
        inputdesrc = (EditText)rootView.findViewById(R.id.newteam_inputdesrc);
        rootView.findViewById(R.id.button1).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                NewTeam();

            }
        });

        return rootView;
    }

    private Boolean NewTeam() {
        Log.d("DEBUG", "submiting");
        try {
            Thread thread=new Thread(
                    new Runnable(){
                        @Override
                        public void run(){
                            try{
                                Sharedinfo share = Sharedinfo.getInstance();
                                int uid = share.getUser().getUid();
                                String New_teamname = inputname.getText().toString();
                                String New_teamdesrc = inputdesrc.getText().toString();
                                String serverURL= PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()).getString("Server", "ds216.net");
                                APICallBuilder Apicall= new APICallBuilder("http://"+serverURL+"/team/");
                                Apicall.setGETpara("handler=team&action=new");
                                JSONObject obj = new JSONObject();
                                obj.put("uid", uid);
                                obj.put("name",New_teamname);
                                obj.put("desrc",New_teamdesrc);
                                Log.d("NewTeam", obj.toString());
                                Apicall.setPOSTpara(obj.toString());
                                String HTML = Apicall.getResponse();
                                Message message = new Message();
                                message.what = 2;
                                message.obj = HTML;
                                MainActivity.getHandler().sendMessage(message);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
            );
            thread.start();
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }
}