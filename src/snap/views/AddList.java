package snap.views;

import java.util.ArrayList;
import java.util.List;

import snap.controllers.Const;
import snap.controllers.Utils;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class AddList extends CustomActivity{
	
	public static ParseUser user;
	private ArrayList<ParseUser> uList;
	private EditText etSearch;
	final Handler mHandler = new Handler();
	private UserAdapter adp = new UserAdapter();
	private ArrayList<String> friends = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_list);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		etSearch = (EditText)findViewById(R.id.etSearchFriends);
		friends = getIntent().getStringArrayListExtra("friends");
		Log.v("friends",friends.toString());
	}
	@Override
	protected void onResume(){
		super.onResume();
		loadUserList();
	}
	
	private void loadUserList(){

		
		final ProgressDialog dia = ProgressDialog.show(this, null,
				getString(R.string.alert_loading));
		
		if(user != null){
		
		ParseUser.getQuery().whereNotEqualTo("objectId", user.getObjectId())
				.whereNotContainedIn("username", friends)
				.findInBackground(new FindCallback<ParseUser>() {

					@Override
					public void done(List<ParseUser> li, ParseException e){
						dia.dismiss();
						if (li != null){
							if (li.size() == 0)
								Toast.makeText(AddList.this,
										R.string.msg_no_user_found,
										Toast.LENGTH_SHORT).show();

							uList = new ArrayList<ParseUser>(li);
							ListView list = (ListView) findViewById(R.id.list);
							list.setAdapter(adp);
							etSearch.addTextChangedListener(new TextWatcher(){

								@Override
								public void beforeTextChanged(CharSequence s, int start, int count,
										int after) {
									// TODO Auto-generated method stub
									
								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before,
										int count) {
									if(s.toString().equals("")){
										loadUserList();
									}else{
										AddList.this.adp.getFilter().filter(s);
										
									}
									
								}
								@Override
								public void afterTextChanged(Editable s) {
									// TODO Auto-generated method stub
									
								}
							});
							list.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int pos, long arg3){
									//Toast.makeText(AddList.this,uList.get(pos).getObjectId(),Toast.LENGTH_SHORT).show();
									ParseObject po = new ParseObject("Friend");		
									po.put("id", UserList.user.getObjectId());
									po.put("fid", uList.get(pos).getObjectId());
									po.saveInBackground();
									
									ParseObject poReverse = new ParseObject("Friend");
									poReverse.put("fid", UserList.user.getObjectId());
									poReverse.put("id", uList.get(pos).getObjectId());
									poReverse.saveInBackground();
									uList.remove(pos);
					                new Thread() {
					        	        public void run() {
					        	        	AddList.this.runOnUiThread(new Runnable() {
					        	                @Override
					        	                public void run() {
					        	                	
					        	                	//uList = (ArrayList<ParseUser>) results.values;
					        	                	adp.notifyDataSetChanged();
					        	                }
					        	            });
					        	        }
					        	    }.start();
								}
							});
						}
						else{
							Utils.showDialog(
									AddList.this,
									getString(R.string.err_users) + " "
											+ e.getMessage());
							e.printStackTrace();
						}
					}
				});
		}else{
			Utils.showDialog(this,"Error getting user");
		}
	}
	private class UserAdapter extends BaseAdapter implements Filterable{

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
				v = getLayoutInflater().inflate(R.layout.add_item, null);

			ParseUser c = getItem(pos);
			TextView lbl = (TextView) v;
			lbl.setText(c.getUsername());
			return v;
		}
		/*
		@Override
		public boolean isEnabled(int position) {
			return true;
		};
		*/
		@Override
		public Filter getFilter() {
			Log.v("abc","is in");
			Filter myFilter = new Filter() {
		        @Override
		        protected FilterResults performFiltering(CharSequence constraint) {
		         FilterResults filterResults = new FilterResults();   
		         ArrayList<ParseUser> tempList=new ArrayList<ParseUser>();
			         for(ParseUser pu: uList){
			        	 if(pu.getUsername().toLowerCase().startsWith(constraint.toString().toLowerCase())){
			        		 Log.v("abc","match Found");
			        		 tempList.add(pu);
			        	 }
			        	 filterResults.values = tempList;
			        	 filterResults.count = tempList.size();
			         }
					return filterResults;
		        }
		        @SuppressWarnings("unchecked")
				@Override
				protected void publishResults(CharSequence constraint,
						final FilterResults results) {
		        	if (results.count == 0) {
		                notifyDataSetInvalidated();
		            } else {
		                new Thread() {
		        	        public void run() {
		        	        	AddList.this.runOnUiThread(new Runnable() {
		        	                @Override
		        	                public void run() {
		        	                	uList = (ArrayList<ParseUser>) results.values;
		        	                	adp.notifyDataSetChanged();
		        	                }
		        	            });
		        	        }
		        	    }.start();
		            }
				}
			};
			return myFilter;
		}      
	}
}
