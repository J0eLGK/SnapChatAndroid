package snap.views.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import snap.views.Chat;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.EditText;

public class ChatTest extends ActivityInstrumentationTestCase2<Chat>{
	
	private Chat activity;
	Button cam, send;
	EditText txt;
	
	public ChatTest() {
	    super(Chat.class);
	}
	
	@Before
	public void setUp() throws Exception {
		activity = getActivity();
		cam = (Button) activity.findViewById(snap.views.R.id.btnCamera);
		send = (Button) activity.findViewById(snap.views.R.id.btnSend);
		txt = (EditText) activity.findViewById(snap.views.R.id.txt);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testElementsNull() {
		assertNotNull(activity);
		assertNotNull(cam);
		assertNotNull(send);
		assertNotNull(txt);
	}
	
	@UiThreadTest
	public void testEditTextIsEmpty(){
		txt.setText("");
		assertEquals(txt.getText().toString(), "");
	}
	
	@UiThreadTest
	public void testEditTextIsValid(){
		txt.setText("foo Text");
		assertEquals(txt.getText().toString(), "foo Text");
	}
	
	@UiThreadTest
	public void testCheckCameraButtonClicked(){
		assertEquals(cam.performClick(), true);
	}
	
	@UiThreadTest
	public void testCheckSendButtonClicked(){
		assertEquals(send.performClick(), true);
	}
}
