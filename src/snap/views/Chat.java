package snap.views;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import snap.controllers.Const;
import snap.models.Conversation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

public class Chat extends CustomActivity{

	private ArrayList<Conversation> convList;

	private ChatAdapter adp;

	private EditText txt;

	private String buddy;

	private Date lastMsgDate;

	private boolean isRunning;
	private Bitmap photo;

	private static Handler handler;
	private static final int CAMERA_REQUEST = 1888;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);

		convList = new ArrayList<Conversation>();
		ListView list = (ListView) findViewById(R.id.list);
		adp = new ChatAdapter();
		list.setAdapter(adp);
		list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		list.setStackFromBottom(true);
		txt = (EditText) findViewById(R.id.txt);
		txt.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_FLAG_MULTI_LINE);

		setTouchNClick(R.id.btnSend);
		setTouchNClick(R.id.btnCamera);

		buddy = getIntent().getStringExtra(Const.EXTRA_DATA);
		getActionBar().setTitle(buddy);

		handler = new Handler();
	}

	@Override
	protected void onResume(){
		super.onResume();
		isRunning = true;
		loadConversationList();
	}

	@Override
	protected void onPause(){
		super.onPause();
		isRunning = false;
	}

	@Override
	public void onClick(View v){
		super.onClick(v);
		if (v.getId() == R.id.btnSend){
			sendMessage();
		}else if(v.getId() == R.id.btnCamera){
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	    if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
	        photo = (Bitmap) data.getExtras().get("data");
	    }  
	}
	
	private void sendMessage(){
		if (txt.length() == 0)
			return;

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(txt.getWindowToken(), 0);
		String s = txt.getText().toString();
		final Conversation c = new Conversation(s, new Date(),
				UserList.user.getUsername());
		c.setStatus(Conversation.STATUS_SENDING);
		convList.add(c);
		adp.notifyDataSetChanged();
		txt.setText(null);
		ParseFile file = null;
		if(photo != null){
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 70, stream);
			byte[] image = stream.toByteArray();
			file = new ParseFile("image.jpg", image);
			file.saveInBackground();
		}
		final ParseObject po = new ParseObject("Chat");		
		po.put("sender", UserList.user.getUsername());
		po.put("receiver", buddy);
		po.put("message", s);
		if(file!=null)
			po.put("photo", file);
		po.saveInBackground();			
		po.saveEventually(new SaveCallback() {

			@Override
			public void done(ParseException e){
				if (e == null)
					c.setStatus(Conversation.STATUS_SENT);
				else
					c.setStatus(Conversation.STATUS_FAILED);
				adp.notifyDataSetChanged();
			}
		});
		photo = null;
	}
	
	private void loadConversationList(){
		ParseQuery<ParseObject> q = ParseQuery.getQuery("Chat");
		if (convList.size() == 0 && UserList.user!=null){
			ArrayList<String> al = new ArrayList<String>();
			al.add(buddy);
			al.add(UserList.user.getUsername());
			q.whereContainedIn("sender", al);
			q.whereContainedIn("receiver", al);
		}
		else{
			if (lastMsgDate != null)
				q.whereGreaterThan("createdAt", lastMsgDate);
			q.whereEqualTo("sender", buddy);
			if(UserList.user != null)
				q.whereEqualTo("receiver", UserList.user.getUsername());
		}
		
		q.orderByDescending("createdAt");
		q.setLimit(30);
		q.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> li, ParseException e){
				Conversation c = null;
				//Log.v("size",String.valueOf(li.size()));
				if (li != null && li.size() > 0){
					for (int i = li.size() - 1; i >= 0; i--){
						ParseObject po = li.get(i);
						ParseFile file = po.getParseFile("photo");
						if(file != null){
							c = new Conversation(po
								.getString("message"), po.getCreatedAt(), po
								.getString("sender"),file.getUrl());
						}else{
							c = new Conversation(po
								.getString("message"), po.getCreatedAt(), po
								.getString("sender"));
						}
						convList.add(c);
						/*
						Date after = c.getDate();
						after.setTime(after.getTime() + 10000);
						if(new Date().after(after)){
							Log.v("insert remove",c.toString());
							convList.remove(c);
							po.deleteInBackground();
							adp.notifyDataSetChanged();
						}
						*/
						
						if (lastMsgDate == null || lastMsgDate.before(c.getDate()))
							lastMsgDate = c.getDate();
					}
					
				}
				adp.notifyDataSetChanged();
				handler.postDelayed(new Runnable() {
					@Override
					public void run(){
						if (isRunning){
							loadConversationList();
						}
					}
				}, 1000);
			}
		});

	}
	private class ChatAdapter extends BaseAdapter{
		
		@Override
		public int getCount(){
			return convList.size();
		}
		
		@Override
		public Conversation getItem(int arg0){
			return convList.get(arg0);
		}

		@Override
		public long getItemId(int arg0){
			return arg0;
		}

		@Override
		public View getView(int pos, View v, ViewGroup arg2){
			Conversation c = getItem(pos);
			if (c.isSent())
				v = getLayoutInflater().inflate(R.layout.chat_item_sent, null);
			else
				v = getLayoutInflater().inflate(R.layout.chat_item_rcv, null);

			TextView lbl = (TextView) v.findViewById(R.id.lbl1);
			lbl.setText(DateUtils.getRelativeDateTimeString(Chat.this, c
					.getDate().getTime(), DateUtils.SECOND_IN_MILLIS,
					DateUtils.DAY_IN_MILLIS, 0));

			lbl = (TextView) v.findViewById(R.id.lbl2);
			lbl.setText(c.getMsg());
			
			ImageView iv = (ImageView) v.findViewById(R.id.iv);
			Picasso.with(getApplication()).load(c.getUrl()).into(iv);

			lbl = (TextView) v.findViewById(R.id.lbl3);
			if (c.isSent()){
				if (c.getStatus() == Conversation.STATUS_SENT)
					lbl.setText("Delivered");
				else if (c.getStatus() == Conversation.STATUS_SENDING)
					lbl.setText("Sending...");
				else
					lbl.setText("Failed");
			}
			else
				lbl.setText("");
			return v;
		}

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if (item.getItemId() == android.R.id.home){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
