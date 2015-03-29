package com.cs4295.team.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cs4295.team.MainActivity;
import com.cs4295.team.R;
import com.cs4295.team.util.APICallBuilder;
import com.cs4295.team.util.Message;
import com.cs4295.team.util.Sharedinfo;
import com.cs4295.team.util.Teaminfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Marcus on 3/27/2015.
 */
public class TeamMessageFragment extends Fragment {

    private int teamID = -1; // the id in the arraylist of team, not same as the real id
    Sharedinfo shared = Sharedinfo.getInstance();
    private ArrayList<Message> msgs = new ArrayList<Message>();
    private final int rows = 20;
    int page = 0;
    static Handler msgHandler;
    MessageAdapter adapter;

    {
        msgHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                if(msg.what==1) {
                    Log.d("Msg1", (String) msg.obj);
                    JSONObject json;
                    try {
                        json = new JSONObject((String) msg.obj);
                        JSONArray arr = json.getJSONArray("msg");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject item = arr.getJSONObject(i);
                            Message temp = new Message(item.getString("timestamp"), item.getInt("msgid"), item.getInt("replyid"), item.getInt("teamid"), item.getInt("uid"), item.getString("msg"), item.getString("title"));
                            msgs.add(temp);

                        }
                        for(Message msgt : msgs)
                        Log.i("Msg",msgt.getTitle());
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                super.handleMessage(msg);
            }
        };
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.message, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.new_message) {
            FragmentManager fragmentManager2 = getFragmentManager();
            FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
            NewMessageFragment fragment2 = new NewMessageFragment();
           // int team_id = Sharedinfo.getInstance().getTeams().get(relativeTeamID).getTeamid();
            fragment2.setTeamId(teamID);
            fragmentTransaction2.replace(R.id.container, fragment2);
            fragmentTransaction2.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);
        /*
        if(relativeTeamID>=0) {
            page = 0;
            rootView = inflater.inflate(R.layout.viewteam, container, false);
            TextView name = (TextView) rootView.findViewById(R.id.view_teamname);
            name.setText("You are viewing Team " + shared.getTeams().get(relativeTeamID).getTeamname());
            GetMsg();
            Log.i("MsgList",msgs.toString());
        }*/

        if(teamID>=0){
            page = 0;
            rootView = inflater.inflate(R.layout.fragment_messgelist, container, false);
            //TextView name = (TextView) rootView.findViewById(R.id.MsglistView);
            //name.setText("You are viewing Team " + shared.getTeams().get(relativeTeamID).getTeamname());
            GetMsg();
            Log.i("MsgList",msgs.toString());
            super.onActivityCreated(savedInstanceState);
           // Context mContext = getView().getContext();

            adapter = new MessageAdapter(getActivity(), msgs);
            ListView list = (ListView) rootView.findViewById(R.id.MsglistView);
            list.setAdapter(adapter);
        }
        return rootView;
    }

    /*
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Context mContext = getView().getContext();
        MessageAdapter adapter;
        adapter = new MessageAdapter(getActivity(), msgs);
        ListView list = (ListView) getActivity().findViewById(R.id.MsglistView);
        list.setAdapter(adapter);
    }*/

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public void refresh(){
        GetMsg();
    }
    private Boolean GetMsg() {
        Log.d("DEBUG", "submiting");
        try {
            Thread thread=new Thread(
                    new Runnable(){
                        @Override
                        public void run(){
                            try{
                                String serverURL= PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()).getString("Server", "ds216.net");
                                APICallBuilder Apicall= new APICallBuilder("http://"+serverURL+"/team/");
                                Apicall.setGETpara("handler=message&action=get");
                                JSONObject obj = new JSONObject();
                              //  int team_id = Sharedinfo.getInstance().getTeams().get(relativeTeamID).getTeamid();//get the real id
                                obj.put("teamid", teamID);
                                obj.put("start",page);
                                obj.put("rows",rows);
                                Log.d("GetMsg", obj.toString());
                                Apicall.setPOSTpara(obj.toString());
                                String HTML = Apicall.getResponse();
                                android.os.Message message = new android.os.Message();
                                message.what = 1;
                                message.obj = HTML;
                                msgHandler.sendMessage(message);
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

    public class MessageAdapter extends BaseAdapter {
        private  ArrayList<Message> searchArrayList;

        private LayoutInflater mInflater;

        public MessageAdapter(Context context, ArrayList<Message> results) {
            searchArrayList = results;
            mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return searchArrayList.size();
        }

        public Object getItem(int position) {
            return searchArrayList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.messagelist_item, null);
                holder = new ViewHolder();
                holder.topic = (TextView) convertView.findViewById(R.id.msg_topic);
                holder.brief = (TextView) convertView.findViewById(R.id.msg_brief);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.topic.setText(searchArrayList.get(position).getTitle());
            holder.brief.setText(searchArrayList.get(position).getMsg());
            Log.d("Title",searchArrayList.get(position).getTitle());
            Log.d("Msg",searchArrayList.get(position).getMsg());
            return convertView;
        }

        class ViewHolder {
            TextView topic;
            TextView brief;
        }
    }
}
