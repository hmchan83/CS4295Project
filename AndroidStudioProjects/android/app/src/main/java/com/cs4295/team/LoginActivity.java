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

import com.cs4295.team.util.APICallBuilder;
import com.cs4295.team.util.Base64;
import com.cs4295.team.util.MD5;
import com.cs4295.team.util.Sharedinfo;
import com.cs4295.team.util.Teaminfo;
import com.cs4295.team.util.Userinfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak") 
public class LoginActivity extends Activity {
	private Button LoginButton;
	private EditText LoginUser;
	private EditText LoginPass;
	private String serverURL;
	private String username;
	private String token;
	SharedPreferences prefs;
	private Sharedinfo share = Sharedinfo.getInstance();
	
	Handler myHandler = new Handler() {  
		 @Override
        public void handleMessage(Message msg) {
			 if(msg.what==1){
				 try{
					 String res = (String)msg.obj;
					 JSONObject json = new JSONObject(res);
					 Log.d("handleMessage","'"+res+"'");
					 Log.d("handleMessage",""+(json.getString("result").equals("True")));
					 if(json.getString("result").equals("True")){
						 share.setUser(new Userinfo(json.getInt("uid"),json.getString("username"),json.getString("name"),json.getString("tel")));
						 getTeam();
						 SharedPreferences.Editor editor = prefs.edit();
						 editor.putString("username", json.getString("username"));
						 editor.putString("token", json.getString("token"));
						 editor.commit();

					 }else{
						 Context context = getApplicationContext();
						 CharSequence text = getString(R.string.login_fail);
						 int duration = Toast.LENGTH_SHORT;
	
						 Toast toast = Toast.makeText(context, text, duration);
						 toast.show();
					 }
				 }catch(JSONException e){
                     Context context = getApplicationContext();
                     CharSequence text = getString(R.string.login_error);
                     int duration = Toast.LENGTH_SHORT;
                     Toast toast = Toast.makeText(context, text, duration);
                     toast.show();
				 }
			 }if(msg.what==2){
					Log.d("DEBUG",(String)msg.obj);
					JSONObject json;
					try {
						json = new JSONObject((String)msg.obj);
						JSONArray arr = json.getJSONArray("teams");
						ArrayList<Teaminfo> maps = new ArrayList<Teaminfo>();
						for(int i=0;i<arr.length();i++){
							JSONObject item = arr.getJSONObject(i);
							Teaminfo temp = new Teaminfo(item.getInt("teamid"),item.getString("teamname"),item.getString("desrc"),item.getString("create"));
							maps.add(temp);
						}			
						share.setTeams(maps);
						Intent intent = new Intent(getApplicationContext(), MainActivity.class);
						 startActivity(intent);
						 finish();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
			 }else if(msg.what==3){
				 try{
					 String res = (String)msg.obj;
					 JSONObject json = new JSONObject(res);
					 Log.d("handleMessage","'"+res+"'");
					 Log.d("handleMessage",""+(json.getString("result").equals("True")));
					 if(json.getString("result").equals("True")){
						 share.setUser(new Userinfo(json.getInt("uid"), json.getString("username"),json.getString("name"),json.getString("tel")));
						 getTeam();
						 SharedPreferences.Editor editor = prefs.edit();
						 editor.putString("username", json.getString("username"));
						 //editor.putString("token", json.getString("token"));
						 editor.commit();

					 }else{
						 Context context = getApplicationContext();
						 CharSequence text = getString(R.string.login_fail);
						 int duration = Toast.LENGTH_SHORT;

						 Toast toast = Toast.makeText(context, text, duration);
						 toast.show();
					 }
				 }catch(JSONException e){
					 Context context = getApplicationContext();
					 CharSequence text = getString(R.string.login_error);
					 int duration = Toast.LENGTH_SHORT;
					 Toast toast = Toast.makeText(context, text, duration);
					 toast.show();
				 }
			 }
             super.handleMessage(msg);   
        }   
   };


    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("DEBUG", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		LoginButton = (Button) findViewById(R.id.btn_login);
		LoginUser = (EditText) findViewById(R.id.input_username);
		LoginPass = (EditText) findViewById(R.id.input_passowrd);
		prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		username = prefs.getString("username", "");
		token = prefs.getString("token","");
		if(!username.isEmpty() && !token.isEmpty()) {
			Log.d("token", "Checking Token");
			checkToken(username, token);
		}
		TextView test = (TextView) findViewById(R.id.test);
		test.setText(username+" ,"+token);
		LoginButton.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Form_submit();
			}			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, LoginSettings.class);
    	    startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void checkToken(final String username,final String token){
		try{
			Thread thread = new Thread(
				new Runnable() {
					@Override
					public void run() {
						try{
							serverURL=prefs.getString("Server", "ds216.net");
							MD5 md5encode = new MD5();
							APICallBuilder Apicall= new APICallBuilder("http://"+serverURL+"/team/");
							Apicall.setGETpara("handler=login&action=checkToken");
							JSONObject obj = new JSONObject();
							obj.put("username", username);
							obj.put("token", md5encode.getMD5EncryptedString(token));
							Log.d("DEBUG", obj.toString());
							Apicall.setPOSTpara(obj.toString());
							String HTML = Apicall.getResponse();
							Log.d("HTML",HTML);
							Message message = new Message();
							message.what = 3;
							message.obj = HTML;
							myHandler.sendMessage(message);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			);
			thread.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean Form_submit(){
		Log.d("DEBUG", "submiting");
		try {			
			Thread thread=new Thread(
					new Runnable(){  
			            @Override  
			            public void run(){  
			            	try{
			            		MD5 md5encode = new MD5();
			            		serverURL=prefs.getString("Server", "ds216.net");
				                APICallBuilder Apicall= new APICallBuilder("http://"+serverURL+"/team/");
				                Apicall.setGETpara("handler=login&action=login");
				                JSONObject obj = new JSONObject();
				                obj.put("username", LoginUser.getText().toString());
				                obj.put("password", md5encode.getMD5EncryptedString(LoginPass.getText().toString()));
				                Log.d("DEBUG", obj.toString());
				                Apicall.setPOSTpara(obj.toString());
				                String HTML = Apicall.getResponse();
				                Log.d("HTML",HTML);
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
	   public boolean getTeam(){
			try {			
				Thread thread=new Thread(
						new Runnable(){  
				            @Override  
				            public void run(){  
				            	try{
				            		serverURL=prefs.getString("Server", "ds216.net");
				            		String request = "handler=team&action=get&uid="+share.getUser().getUid();
				            		Log.d("DEBUG", request);
				            		Base64.Encoder encoder = Base64.getEncoder();			            		
					                String surl = "http://"+serverURL+"/team/?request="+new String(encoder.encode(request.getBytes()));
					                Log.d("DEBUG", surl);
					                /*HttpClient httpClient = new DefaultHttpClient();
					                HttpContext localContext = new BasicHttpContext();
					                HttpGet httpGet = new HttpGet(surl); // URL!	         
						   			HttpResponse response = httpClient.execute(httpGet, localContext);;
						   			String result = "";
						   			String HTML = "";
						   			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
						   			String line = null;
						   			while ((line = reader.readLine()) != null) {
						   				result += line;
						   				HTML = result;
						   			}*/
					                APICallBuilder Apicall= new APICallBuilder("http://"+serverURL+"/team/");
					                Apicall.setGETpara("handler=team&action=get");
					                JSONObject obj = new JSONObject();
					                obj.put("uid", share.getUser().getUid());
					                Log.d("DEBUG", obj.toString());
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

}
