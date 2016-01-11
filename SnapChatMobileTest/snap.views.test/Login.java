package snap.views.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import snap.views.Login;
import android.test.ActivityInstrumentationTestCase2;
import android.text.Editable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class LoginTest extends ActivityInstrumentationTestCase2<Login>{
	private Login activity;
	private Button btnLog, btnReg;
	private EditText user, pwd;
	
	public LoginTest() {
	    super(Login.class);
	}

	@Before
	public void setUp() throws Exception {
		setActivityInitialTouchMode(false);	
		activity = getActivity();
		btnLog = (Button) activity.findViewById(snap.views.R.id.btnLogin);
		btnReg = (Button) activity.findViewById(snap.views.R.id.btnReg);
		user = (EditText) activity.findViewById(snap.views.R.id.user);
		pwd = (EditText) activity.findViewById(snap.views.R.id.pwd);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	/* Ta tests logo bug tou Instrumentation test runner  prepei na trexoun to kathena ksexwrista
	 * Vazw se sxolia auta pou den thelw na treksw
	 *  */
	
	@Test
	public void testActivityExists() {
		assertNotNull(activity);
		assertNotNull(btnLog);
		assertNotNull(btnReg);
		assertNotNull(user);
		assertNotNull(pwd);
	}
	
	@Test
	public void testEditBoxTextEmpty(){
		Editable text1 = user.getText();
		Editable text2 = pwd.getText();
		assertEquals(text1.toString(), "");
		assertEquals(text2.toString(), "");
	}
	
	@Test
	public void testButtonClick(){
		
		activity.runOnUiThread(new Runnable() {
			  @Override
			  public void run() {
				  btnLog.performClick();
			  }
			});
		getInstrumentation().waitForIdleSync();
		//assert that the dialog has opened (ÂãÜæåé ìÞíõìá ãéá Üäåéá ðåäßá!)
	}
	
}
