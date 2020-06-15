package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.net.ConnectivityManager;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.openclassrooms.realestatemanager.tools.Utils;
import com.openclassrooms.realestatemanager.ui.realestate.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class ConnectivityTest{

    @Rule
    public final ActivityTestRule<MainActivity> mMainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void networkConnectivity() {
        boolean networkConnection = false;
        ConnectivityManager mConnectivityManager = (ConnectivityManager) mMainActivityActivityTestRule.getActivity().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (mConnectivityManager != null) {
            networkConnection = mConnectivityManager.getActiveNetworkInfo() != null
                    && mConnectivityManager.getActiveNetworkInfo().isAvailable()
                    && mConnectivityManager.getActiveNetworkInfo().isConnected();
        }

        if (networkConnection) {
            assertTrue(Utils.isInternetAvailable(mMainActivityActivityTestRule.getActivity().getApplicationContext()));
        } else {
            assertFalse(Utils.isInternetAvailable(mMainActivityActivityTestRule.getActivity().getApplicationContext()));
        }
    }
}
