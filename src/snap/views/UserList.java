package snap.views;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import snap.controllers.Const;
import snap.controllers.Utils;
import snap.models.Users;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class UserList extends CustomActivity{

	private static ArrayList<ParseUser> uList = new ArrayList<ParseUser>();
	private ArrayList<Users> list = new ArrayList<Users>();
	public static ParseUser user;
	private static Handler handler;
	public AlertDialog dialog;
	private ArrayList<String> fNames = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_list);

		getActionBar().setDisplayHomeAsUpEnabled(false);
		
		setTouchNClick(R.id.btnLogout);
		setTouchNClick(R.id.btnAddFriends);

		updateUserStatus(true);
		handler = new Handler();
	}
	
	@Override
	public void onClick(View v){
		super.onClick(v);
		if (v.getId() == R.id.btnLogout){
			logout();
			
		}
		if(v.getId() == R.id.btnAddFriends){
			AddList.user = user;
			
			startActivity(new Intent(UserList.this,AddList.class).putStringArrayListExtra("friends", fNames));
			fNames.clear();
		}
	}
	
	private void logout(){
		ParseUser.logOut();
		Intent intent = new Intent(this, Login.class);
		startActivity(intent);
		this.finish();
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		updateUserStatus(false);
	}

	@Override
	protected void onResume(){
		super.onResume();
		loadUserList();
	}
	
	private void updateUserStatus(boolean online){
		if(user != null){
			user.put("online", online);
			user.saveEventually();
		}
	}

	private void loadUserList(){
		uList.clear();
		final ProgressDialog dia = ProgressDialog.show(this, null,
				getString(R.string.alert_loading));
			ParseQuery<ParseObject> q = ParseQuery.getQuery("Friend");
			q.whereEqualTo("id", user.getObjectId());
			dia.dismiss();
			q.orderByDescending("createdAt");
			q.findInBackground(new FindCallback<ParseObject>() {
				@Override
				public void done(List<ParseObject> li, ParseException e){
					
					if (li != null && li.size() > 0){
						for (int i=0;i<li.size();i++){
							ParseObject po = li.get(i);
							ParseQuery<ParseUser> query = ParseUser.getQuery();
							query.whereEqualTo("objectId", po.getString("fid"));
							query.findInBackground(new FindCallback<ParseUser>() {
								
								  public void done(List<ParseUser> objects, ParseException e) {
								    if (e == null) {
								    	Set<String> hs = new HashSet<String>();
								    	ParseUser pu = objects.get(0);
								    	uList.add(pu);
								    	fNames.add(pu.getUsername());
								    	Log.v("fnames",fNames.toString());
								    	Log.v("well",String.valueOf(uList));
								    	ListView list = (ListView) findViewById(R.id.list);
										list.setAdapter(new UserAdapter());
										list.setOnItemClickListener(new OnItemClickListener() {

											@Override
											public void onItemClick(AdapterView<?> arg0,
													View arg1, int pos, long arg3){
												startActivity(new Intent(UserList.this,
														Chat.class).putExtra(
														Const.EXTRA_DATA, uList.get(pos)
																.getUsername()));
											}
										});
								    } else {
								        // Something went wrong.
								    	Utils.showDialog(
												UserList.this,
												getString(R.string.err_users) + " "
														+ e.getMessage());
										e.printStackTrace();
								    }
								  }
								});
						}
						Log.v("well",String.valueOf(uList));
					}
				}
			});
		}
	
	public AlertDialog returnDialog(){
		return dialog;
	}
	
	private class UserAdapter extends BaseAdapter{

		@Override
		public int getCount(){
			return uList.size();
		}
		
		@Override
		public ParseUser getItem(int arg0){
			return uList.get(arg0);
		}
		
		@Override
		public long getItemId(int arg0){
			return arg0;
		}

		@Override
		public View getView(int pos, View v, ViewGroup arg2){
			if (v == null)
				v = getLayoutInflater().inflate(R.layout.chat_item, null);

			ParseUser c = getItem(pos);
			TextView lbl = (TextView) v;
			lbl.setText(c.getUsername());
			lbl.setCompoundDrawablesWithIntrinsicBounds(
					c.getBoolean("online") ? R.drawable.ic_online
							: R.drawable.ic_offline, 0, R.drawable.arrow, 0);
			return v;
		}
	}
}
