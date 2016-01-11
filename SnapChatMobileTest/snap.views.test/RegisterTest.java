package snap.views.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import snap.views.Register;
import android.app.AlertDialog;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class RegisterTest extends ActivityInstrumentationTestCase2<Register>{

	private Register activity;
	private Button butReg;
	private EditText user, pwd, email;
	private AlertDialog dialog; 
	
	public RegisterTest() {
	    super(Register.class);
	}

	@Before
	public void setUp() throws Exception {
		activity = getActivity();
		butReg = (Button)activity.findViewById(snap.views.R.id.btnReg);
		user = (EditText)activity.findViewById(snap.views.R.id.user);
		pwd = (EditText)activity.findViewById(snap.views.R.id.pwd);
		email = (EditText)activity.findViewById(snap.views.R.id.pwd);
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/* Ta tests logo bug tou Instrumentation test runner  prepei na trexoun to kathena ksexwrista
	 * Vazw se sxolia auta pou den thelw na treksw
	 *  */
	@Test
	public void testElementsNotNull() {
		assertNotNull(activity);
		assertNotNull(butReg);
		assertNotNull(user);
		assertNotNull(pwd);
		assertNotNull(email);
	}

	@UiThreadTest
	public void testUserEditText(){
		user.setText("foo");
		assertEquals("foo",user.getText().toString());
	}
	@UiThreadTest
	public void testPwdEditText(){
		pwd.setText("foo");
		assertEquals("foo",pwd.getText().toString());
	}
	
	@UiThreadTest
	public void testEmailEditText(){
		email.setText("foo");
		assertEquals("foo",email.getText().toString());
	}
	
	@UiThreadTest
	public void testButtonClicked(){
		assertEquals(true, butReg.performClick());
		AlertDialog dialog = activity.getDialog();
		if(dialog!=null){
			Log.v("dialog",dialog.toString(), null);
		}
	}
	@UiThreadTest
	public void testDialogOpens(){
		butReg.performClick();
		AlertDialog dialog = activity.getDialog();
		assertNotNull(dialog);
	}

	
	/* ********************** */
	/*TESTING GIA TO CUSTOM ACTIVITY*/
	@UiThreadTest
	public void testSetTouchNClick(){
		assertNotNull(activity.setTouchNClick(snap.views.R.id.btnReg));
	}
	
	/* To button den anikei stin activity!*/
	@UiThreadTest
	public void testSetTouchNClick2(){
		assertNull(activity.setTouchNClick(snap.views.R.id.btnCamera));
	}
	
	@UiThreadTest
	public void testSetClick(){
		assertNotNull(activity.setClick(snap.views.R.id.btnReg));
	}
	
	/* To button den anikei stin activity!*/
	@UiThreadTest
	public void testSetClick2(){
		assertNull(activity.setClick(snap.views.R.id.btnCamera));
	}

}
