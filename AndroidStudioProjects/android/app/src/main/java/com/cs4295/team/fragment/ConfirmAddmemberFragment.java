package com.cs4295.team.fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs4295.team.R;
import com.cs4295.team.util.APICallBuilder;
import com.cs4295.team.util.Sharedinfo;
import com.cs4295.team.util.Userinfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmAddmemberFragment extends Fragment {
    private ListView listview;
    private BaseAdapter adapter;
    private int teamid;



    private List<Userinfo> selected = new ArrayList<Userinfo>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_addmemberconfirm, container, false);
        Log.d("teamid", teamid + "");
        ListView listview = (ListView)rootView.findViewById(R.id.listMember);
        Button btn = (Button)rootView.findViewById(R.id.addMemberButton);
        btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                List<Integer> selectedUid = new ArrayList<Integer>();
                for(Userinfo u:selected){
                    selectedUid.add(u.getUid());
                }
                addUser(selectedUid);
            }
        });
        ListView list = (ListView) rootView.findViewById(R.id.listMember);
        adapter = new UserAdapter(getActivity(),selected);
        list.setAdapter(adapter);
        return rootView;
    }

    public  void addUser(List<Integer> uids){
        new AsyncTask<List<Integer>,String,String>() {
            @Override
            protected String doInBackground(List<Integer>... params) {
                try {
                    List<Integer> uids = params[0];
                    JSONArray arr = new JSONArray();
                    for(Integer i:uids){
                        arr.put(i);
                    }
                    Sharedinfo share = Sharedinfo.getInstance();
                    String serverURL = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()).getString("Server", "ds216.net");
                    APICallBuilder Apicall = new APICallBuilder("http://" + serverURL + "/team/");
                    Apicall.setGETpara("handler=member&action=add");
                    JSONObject obj = new JSONObject();
                    obj.put("teamid", teamid);
                    obj.put("uid", arr);
                    Log.d("addUser", obj.toString());
                    Apicall.setPOSTpara(obj.toString());
                    String HTML = Apicall.getResponse();
                    Log.d("addUser",HTML);
                    return HTML;
                }catch(Exception ex){
                    ex.printStackTrace();
                    return "";
                }
            }
            @Override
            protected void onPostExecute(String msg) {
                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                TeamMessageFragment fragment2 = new TeamMessageFragment();
                // int team_id = Sharedinfo.getInstance().getTeams().get(relativeTeamID).getTeamid();
                fragment2.setTeamID(teamid);
                FragmentManager fm = getActivity().getFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                fragmentTransaction2.replace(R.id.container, fragment2);
                Toast.makeText(getActivity(),"The selected Users are added",Toast.LENGTH_LONG).show();
                fragmentTransaction2.commit();

            }
        }.execute(uids, null, null);
    }

    public List<Userinfo> getSelected() {
        return selected;
    }

    public void setSelected(List<Userinfo> selected) {
        this.selected = selected;
    }
    public int getTeamid() {
        return teamid;
    }

    public void setTeamid(int teamid) {
        this.teamid = teamid;
    }

    public class UserAdapter extends BaseAdapter {
        private List<Userinfo> searchArrayList;

        private LayoutInflater mInflater;

        public UserAdapter(Context context, List<Userinfo> results) {
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
            final int  pos = position;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.memberlist_item, null);
                holder = new ViewHolder();
                holder.member_username = (TextView) convertView.findViewById(R.id.member_username);
                holder.member_name = (TextView) convertView.findViewById(R.id.member_name);
                holder.selected = (CheckBox) convertView.findViewById(R.id.selected);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.member_username.setText(searchArrayList.get(position).getUsername());
            holder.member_name.setText(searchArrayList.get(position).getUsername());
            return convertView;
        }

        class ViewHolder {
            TextView member_name;
            TextView member_username;
            CheckBox selected;
        }
    }
}
