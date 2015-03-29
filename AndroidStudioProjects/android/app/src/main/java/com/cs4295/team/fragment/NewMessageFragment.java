package com.cs4295.team.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cs4295.team.MainActivity;
import com.cs4295.team.R;
import com.cs4295.team.util.APICallBuilder;
import com.cs4295.team.util.Sharedinfo;

import org.json.JSONObject;

public class NewMessageFragment extends Fragment {

    private EditText msgTopic;
    private EditText msgContent;
    private int teamId;
    static Handler newMsgHandler;

    {
        newMsgHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                if(msg.what==1) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    TeamMessageFragment fragment = new TeamMessageFragment();
                    fragment.setTeamID(teamId);
                    //fragment.refresh();
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();
                }
                super.handleMessage(msg);
            }
        };
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.new_message, container, false);
        msgTopic = (EditText) rootView.findViewById(R.id.newmsg_topic);
        msgContent = (EditText) rootView.findViewById(R.id.newmsg_content);
        rootView.findViewById(R.id.button1).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
               NewMsg();

            }
        });

        return rootView;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    private Boolean NewMsg() {
        Log.d("DEBUG", "submiting");
        try {
            Thread thread=new Thread(
                    new Runnable(){
                        @Override
                        public void run(){
                            try{
                                Sharedinfo share = Sharedinfo.getInstance();
                                int uid = share.getUser().getUid();
                                String New_msgTopic = msgTopic.getText().toString();
                                String New_msgContent = msgContent.getText().toString();
                                String serverURL= PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()).getString("Server", "ds216.net");
                                APICallBuilder Apicall= new APICallBuilder("http://"+serverURL+"/team/");
                                Apicall.setGETpara("handler=message&action=add");
                                JSONObject obj = new JSONObject();
                                obj.put("replyid",0);
                                obj.put("teamid",teamId);
                                obj.put("uid", uid);
                                obj.put("msg",New_msgContent);
                                obj.put("title",New_msgTopic);
                                Log.d("NewTeam", obj.toString());
                                Apicall.setPOSTpara(obj.toString());
                                String HTML = Apicall.getResponse();
                                Message message = new Message();
                                message.what = 1;
                                message.obj = HTML;
                                newMsgHandler.sendMessage(message);
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
