package lou.magistrale.quicksms;

import android.app.Instrumentation;
import android.app.LauncherActivity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityInstrumentationTestCase2<LauncherActivity> {

    Intent mLaunchIntent;
    public ApplicationTest(Class<LauncherActivity> activityClass) {
        super(activityClass);
    }



    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @MediumTest
    public void testSendMessageToReceiverActivity(){
       final Button sendToReceiverButton = (Button) getActivity().findViewById(R.id.insert_contact);

        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(AddContact.class.getName(),
                        null, false);

// Validate that ReceiverActivity is started
        TouchUtils.clickView(this, sendToReceiverButton);
        AddContact receiverActivity = (AddContact)
        receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called", 1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type", AddContact.class, receiverActivity.getClass());

// Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);

    }


}