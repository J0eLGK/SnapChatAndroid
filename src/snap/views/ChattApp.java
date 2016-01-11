package snap.views;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

public class ChattApp extends Application{
	
	@Override
	public void onCreate(){
		super.onCreate();
		Parse.initialize(this, "XlUoocE4KS8JQkFdPWyhZGtP6ksuHOIzo1VAjij8", "syr0MzRvftQohn776NxmZh6S2mRab5EbrJV5nB32");

	}
}
