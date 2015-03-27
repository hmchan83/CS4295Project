package com.cs4295.team;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;

import com.cs4295.team.util.APICallBuilder;
import com.cs4295.team.util.Base64;
import com.cs4295.team.util.Sharedinfo;
import com.cs4295.team.util.Teaminfo;
import org.json.JSONObject;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    
    private String serverURL;
	SharedPreferences prefs;
	private Sharedinfo share = Sharedinfo.getInstance();

    static Handler myHandler;

    {
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1) {
                    Log.d("Msg1", (String) msg.obj);
                    JSONObject json;
                    try {
                        json = new JSONObject((String) msg.obj);
                        JSONArray arr = json.getJSONArray("teams");
                        ArrayList<Teaminfo> maps = new ArrayList<Teaminfo>();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject item = arr.getJSONObject(i);
                            Teaminfo temp = new Teaminfo(item.getInt("teamid"), item.getString("teamname"), item.getString("desrc"), item.getString("create"));
                            maps.add(temp);
                        }
                        share.setTeams(maps);
                        MainActivity.this.getmNavigationDrawerFragment().refresh();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if (msg.what == 2) {
                    Log.d("Msg2", (String) msg.obj);
                    JSONObject json;
                    try {
                        json = new JSONObject((String) msg.obj);
                        String arr = json.getString("result");
                        if(arr.equals("true")){
                            MainActivity.this.getTeam();
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                super.handleMessage(msg);
            }
        };
    }

    public static Handler getHandler(){
        return myHandler;
    }

    public NavigationDrawerFragment getmNavigationDrawerFragment() {
		return mNavigationDrawerFragment;
	}

	public void setmNavigationDrawerFragment(
			NavigationDrawerFragment mNavigationDrawerFragment) {
		this.mNavigationDrawerFragment = mNavigationDrawerFragment;
	}

    public boolean getTeam(){
        try {
            Thread thread=new Thread(
                    new Runnable(){
                        @Override
                        public void run(){
                            try{
                                String serverURL=prefs.getString("Server", "ds216.net");
                                APICallBuilder Apicall= new APICallBuilder("http://"+serverURL+"/team/");
                                Apicall.setGETpara("handler=team&action=get");
                                JSONObject obj = new JSONObject();
                                Sharedinfo share = Sharedinfo.getInstance();
                                obj.put("uid", share.getUser().getUid());
                                Log.d("GETteam", obj.toString());
                                Apicall.setPOSTpara(obj.toString());
                                String HTML = Apicall.getResponse();
                                Message message = new Message();
                                message.what = 1;
                                message.obj = HTML;
                                myHandler.sendMessage(message);
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

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        //getTeam();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        EditText inputname;
        EditText inputdesrc;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
        	Log.d("PlaceholderFragment","Section "+sectionNumber);
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	int i = getArguments().getInt(ARG_SECTION_NUMBER);
        	View rootView;
        	final Sharedinfo shared = Sharedinfo.getInstance();
        	if(i==1){
        		rootView = inflater.inflate(R.layout.fragment_main, container, false);
        	}else if(i==2){
        		rootView = inflater.inflate(R.layout.newteam, container, false);
        		inputname = (EditText)rootView.findViewById(R.id.newteam_inputname);
        		inputdesrc = (EditText)rootView.findViewById(R.id.newteam_inputdesrc);
        		rootView.findViewById(R.id.button1).setOnClickListener(new Button.OnClickListener(){
        			@Override
        			public void onClick(View arg0) {
        				NewTeam();
        			}
        		});
        	}else{
        		rootView = inflater.inflate(R.layout.viewteam, container, false);
        		TextView name=(TextView)rootView.findViewById(R.id.view_teamname);
        		name.setText("You are viewing Team "+shared.getTeams().get(i-2-1).getTeamname());
        	}
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
				            		String serverURL=PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()).getString("Server", "ds216.net");
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
						   			myHandler.sendMessage(message);		
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

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
        
        
    }

}
