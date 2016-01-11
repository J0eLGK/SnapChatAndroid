package snap.views.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import snap.views.UserList;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.ListView;

public class UserListTest extends ActivityInstrumentationTestCase2<UserList>{
	
	private UserList activity;
	private Button btnLogout;
	private ListView lv;
	
	public UserListTest() {
	    super(UserList.class);
	}

	@Before
	public void setUp() throws Exception {
		activity = getActivity();
		lv = (ListView) activity.findViewById(snap.views.R.id.list);
	}

	@After
	public void tearDown() throws Exception {
		
	}
/*
	@Test
	public void testElementsNotNull() {
		assertNotNull(activity);
		assertNotNull(lv);
	}

	
	// Poy simainei oti epeidi exoume null user emfanizetai ena alert dialog 
	@UiThreadTest
	public void testAlertDialog(){
		assertNotNull(activity.returnDialog());
	}
	
	// Afou i lista einai adeia den patiete
	@UiThreadTest
	public void testListClick(){
		assertEquals(lv.performClick(), false);
	}
	*/
	
	// H lista mas einai adeia
	@UiThreadTest
	public void testListIsEmpty(){
		assertEquals(lv.getCount(), 0);
	}

}
