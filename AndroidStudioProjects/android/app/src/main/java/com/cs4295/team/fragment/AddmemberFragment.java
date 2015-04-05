package com.cs4295.team.fragment;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.cs4295.team.R;
import com.cs4295.team.util.APICallBuilder;
import com.cs4295.team.util.Message;
import com.cs4295.team.util.Sharedinfo;
import com.cs4295.team.util.Userinfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddmemberFragment extends Fragment {
    private View rootView;
    private ListView listview;
    private BaseAdapter adapter;
    private int teamid;
    private List<Userinfo> selected = new ArrayList<Userinfo>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_addmember, container, false);
        teamid = getArguments().getInt("Teamid");
        Log.d("teamid",teamid+"");
        ListView listview = (ListView)rootView.findViewById(R.id.listMember);
        Button btn = (Button)rootView.findViewById(R.id.addMemberButton);
        btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                ConfirmAddmemberFragment fragment2 = new ConfirmAddmemberFragment();
                fragmentTransaction2.addToBackStack(null);
                fragment2.setSelected(selected);
                fragment2.setTeamid(teamid);
                fragmentTransaction2.replace(R.id.container, fragment2);
                fragmentTransaction2.commit();
            }
        });
        SearchView sv = (SearchView) rootView.findViewById(R.id.searchMember);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener( ) {
            @Override
            public boolean  onQueryTextChange( String newText ) {
                searchUser(newText);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                return true;
            }
        });
        return rootView;
    }

    public  void searchUser(String hint){
        new AsyncTask<String,String,String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    Sharedinfo share = Sharedinfo.getInstance();
                    int uid = share.getUser().getUid();
                    String serverURL = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()).getString("Server", "ds216.net");
                    APICallBuilder Apicall = new APICallBuilder("http://" + serverURL + "/team/");
                    Apicall.setGETpara("handler=login&action=searchUser");
                    JSONObject obj = new JSONObject();
                    obj.put("teamid", teamid);
                    obj.put("str", params[0]);
                    Log.d("searchUser", obj.toString());
                    Apicall.setPOSTpara(obj.toString());
                    String HTML = Apicall.getResponse();
                    return HTML;
                }catch(Exception ex){
                    ex.printStackTrace();
                    return "";
                }
            }
            @Override
            protected void onPostExecute(String msg) {
                Log.d("result",msg);
                List<Userinfo> searchResults = new ArrayList<Userinfo>();
                try {
                    JSONObject json = new JSONObject(msg);
                    JSONArray arr = json.getJSONArray("users");
                    for(int i=0;i<arr.length();i++){
                        JSONObject userinfo = (JSONObject)arr.get(i);
                        searchResults.add(new Userinfo(userinfo.getInt("uid"),userinfo.getString("username"),userinfo.getString("name"),userinfo.getString("tel")));
                    }
                    ListView list = (ListView) rootView.findViewById(R.id.listMember);
                    adapter = new UserAdapter(getActivity(),searchResults);
                    list.setAdapter(adapter);
                    list.invalidateViews();
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }.execute(hint, null, null);
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
                holder.selected.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked==true){
                            selected.add(new Userinfo(searchArrayList.get(pos).getUid(),searchArrayList.get(pos).getUsername(),searchArrayList.get(pos).getName(),searchArrayList.get(pos).getTel()));
                        }else{
                            for(Userinfo u : selected){
                                if(u.getUid() == searchArrayList.get(pos).getUid()){
                                    selected.remove(u);
                                }
                            }
                        }
                        for(Userinfo u : selected)
                        Log.d("selected",u.getUsername());
                    }
                });

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.member_username.setText(searchArrayList.get(position).getUsername());
            holder.member_name.setText(searchArrayList.get(position).getName());
            return convertView;
        }

        class ViewHolder {
            TextView member_name;
            TextView member_username;
            CheckBox selected;
        }
    }
}
