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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

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
public class ListmemberFragment extends Fragment {
    private View rootView;
    private ListView listview;
    private BaseAdapter adapter;
    private int teamid;
    private List<Userinfo> members = new ArrayList<Userinfo>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_addmember, container, false);
        teamid = getArguments().getInt("Teamid");
        Log.d("teamid",teamid+"");
        ListView listview = (ListView)rootView.findViewById(R.id.listMember);
        rootView.findViewById(R.id.addMemberButton).setVisibility(View.GONE);
        SearchView sv = (SearchView) rootView.findViewById(R.id.searchMember);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener( ) {
            @Override
            public boolean  onQueryTextChange( String newText ) {
                List<Userinfo> searchResults = new ArrayList<Userinfo>();
                for(Userinfo u :members ){
                    if(u.getUsername().contains(newText) || u.getName().contains(newText) || u.getTel().contains(newText))
                        searchResults.add(u);
                }
                ListView list = (ListView) rootView.findViewById(R.id.listMember);
                adapter = new UserAdapter(getActivity(),searchResults);
                adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                return true;
            }
        });
        getUser();
        return rootView;
    }

    public  void getUser(){
        new AsyncTask<Void,String,String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    Sharedinfo share = Sharedinfo.getInstance();
                    int uid = share.getUser().getUid();
                    String serverURL = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()).getString("Server", "ds216.net");
                    APICallBuilder Apicall = new APICallBuilder("http://" + serverURL + "/team/");
                    Apicall.setGETpara("handler=member&action=get");
                    JSONObject obj = new JSONObject();
                    obj.put("teamid", teamid);
                    Log.d("getMember", obj.toString());
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
                    members = searchResults;
                    list.setAdapter(adapter);
                    list.invalidateViews();
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }.execute(null, null, null);
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
                CheckBox cb = (CheckBox)convertView.findViewById(R.id.selected);
                cb.setVisibility(View.GONE);
                holder = new ViewHolder();
                holder.member_username = (TextView) convertView.findViewById(R.id.member_username);
                holder.member_name = (TextView) convertView.findViewById(R.id.member_name);
                holder.selected = (CheckBox) convertView.findViewById(R.id.selected);

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
