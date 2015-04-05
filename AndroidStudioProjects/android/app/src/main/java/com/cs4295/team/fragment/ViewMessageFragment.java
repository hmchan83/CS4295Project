package com.cs4295.team.fragment;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cs4295.team.R;
import com.cs4295.team.util.APICallBuilder;
import com.cs4295.team.util.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class ViewMessageFragment extends Fragment {
    private TextView Msg_topic;
    private TextView Msg_content;
    private int msgid;
    private int teamid;
    private Message thismsg;
    private ReplyAdapter adapter;
    private ListView replyList;
    private ArrayList<Message> replys = new ArrayList<Message>();

    static Handler msgdetailHandler;

    {
        msgdetailHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                if(msg.what==1) {
                    Log.d("Msg1", (String) msg.obj);
                    JSONObject json;
                    try {
                        json = new JSONObject((String) msg.obj);
                        //handle msg
                        JSONArray msgs = json.getJSONArray("msg");
                        JSONObject msgs1 = (JSONObject)msgs.get(0);
                        thismsg = new Message(msgs1.getString("timestamp"),msgs1.getInt("msgid"),msgs1.getInt("replyid"),msgs1.getInt("teamid"),
                                msgs1.getInt("uid"),msgs1.getString("msg"),msgs1.getString("title"));
                        Msg_topic.setText(thismsg.getTitle());
                        Msg_content.setText(thismsg.getMsg());
                        //handle reply
                        replys.clear();
                        JSONArray arr = json.getJSONArray("reply");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject item = arr.getJSONObject(i);
                            Message temp = new Message(item.getString("timestamp"), item.getInt("msgid"), item.getInt("replyid"), item.getInt("teamid"), item.getInt("uid"), item.getString("msg"), item.getString("title"));
                            replys.add(temp);
                        }
                        for(Message msgt : replys)
                            Log.i("replys", msgt.getMsg());
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_viewmessage, container, false);
        Msg_topic = (TextView)rootview.findViewById(R.id.val_topic);
        Msg_content = (TextView)rootview.findViewById(R.id.Msg_content);
        msgid = getArguments().getInt("Msgid");
        teamid = getArguments().getInt("Teamid");
        //replys.add(new Message(new Date().toString(), 0, 0, 0, 0, "test", "reply"));
        getMessage();
        replyList = (ListView)rootview.findViewById(R.id.replyList);
        adapter = new ReplyAdapter(getActivity(),replys);
        replyList.setAdapter(adapter);
        setHasOptionsMenu(true);
        return rootview;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.reply, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.new_reply) {
            FragmentManager fragmentManager2 = getFragmentManager();
            FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
            NewReplyFragment fragment2 = new NewReplyFragment();
            // int team_id = Sharedinfo.getInstance().getTeams().get(relativeTeamID).getTeamid();
            Bundle bundle = new Bundle();
            bundle.putInt("Msgid", msgid);
            Log.d("Msgid", msgid + "");
            bundle.putInt("Teamid", teamid);
            Log.d("Teamid", teamid + "");
            fragmentTransaction2.addToBackStack(null);
            fragment2.setArguments(bundle);
            fragmentTransaction2.replace(R.id.container, fragment2);
            fragmentTransaction2.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Boolean getMessage(){
        Log.d("DEBUG", "submiting");
        try {
            Thread thread=new Thread(
                    new Runnable(){
                        @Override
                        public void run(){
                            try{
                                String serverURL= PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()).getString("Server", "ds216.net");
                                APICallBuilder Apicall= new APICallBuilder("http://"+serverURL+"/team/");
                                Apicall.setGETpara("handler=message&action=getDetails");
                                JSONObject obj = new JSONObject();
                                //  int team_id = Sharedinfo.getInstance().getTeams().get(relativeTeamID).getTeamid();//get the real id
                                obj.put("teamid", teamid);
                                obj.put("msgid",msgid);
                                Log.d("GetMsg", obj.toString());
                                Apicall.setPOSTpara(obj.toString());
                                String HTML = Apicall.getResponse();
                                android.os.Message message = new android.os.Message();
                                message.what = 1;
                                message.obj = HTML;
                                msgdetailHandler.sendMessage(message);
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

    public ArrayList<Message> getReplys() {
        return replys;
    }

    public void setReplys(ArrayList<Message> replys) {
        this.replys = replys;
    }

    public Message getThismsg() {
        return thismsg;
    }

    public void setThismsg(Message thismsg) {
        this.thismsg = thismsg;
    }

    public class ReplyAdapter extends BaseAdapter {
        private ArrayList<Message> searchArrayList;

        private LayoutInflater mInflater;

        public ReplyAdapter(Context context, ArrayList<Message> results) {
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
                convertView = mInflater.inflate(R.layout.replylist_item, null);
                holder = new ViewHolder();
                //holder.topic = (TextView) convertView.findViewById(R.id.msg_topic);
                holder.brief = (TextView) convertView.findViewById(R.id.msg_brief);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

           // holder.topic.setText(searchArrayList.get(position).getTitle());
            holder.brief.setText(searchArrayList.get(position).getMsg());
            //Log.d("Title",searchArrayList.get(position).getTitle());
            Log.d("Msg",searchArrayList.get(position).getMsg());
            return convertView;
        }

        class ViewHolder {
            //TextView topic;
            TextView brief;
        }
    }
}
