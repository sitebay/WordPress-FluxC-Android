package org.wordpress.android.fluxc.release;

import android.content.Context;

import org.greenrobot.eventbus.Subscribe;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.wordpress.android.fluxc.TestUtils;
import org.wordpress.android.fluxc.example.BuildConfig;
import org.wordpress.android.fluxc.generated.AccountActionBuilder;
import org.wordpress.android.fluxc.generated.AuthenticationActionBuilder;
import org.wordpress.android.fluxc.generated.SiteActionBuilder;
import org.wordpress.android.fluxc.model.SiteModel;
import org.wordpress.android.fluxc.store.AccountStore;
import org.wordpress.android.fluxc.store.AccountStore.AuthenticatePayload;
import org.wordpress.android.fluxc.store.AccountStore.OnAccountChanged;
import org.wordpress.android.fluxc.store.AccountStore.OnAuthenticationChanged;
import org.wordpress.android.fluxc.store.SiteStore;
import org.wordpress.android.fluxc.store.SiteStore.FetchSitesPayload;
import org.wordpress.android.fluxc.store.SiteStore.OnSiteChanged;
import org.wordpress.android.util.AppLog;
import org.wordpress.android.util.AppLog.T;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReleaseStack_WPComBase extends ReleaseStack_Base {
    @Inject AccountStore mAccountStore;
    @Inject SiteStore mSiteStore;

    static SiteModel sSite;

    private enum TestEvents {
        NONE,
        AUTHENTICATED,
        ACCOUNT_FETCHED,
        SITE_CHANGED,
    }

    private TestEvents mNextEvent;
    private AuthenticatePayload mAuthenticatePayload;

    @BeforeClass
    public static void beforeClass() {
        sSite = null;
    }

    @AfterClass
    public static void afterClass() {
        // Clear the token after the entire class finishes
        // This ensures that the token is not re-used by other tests that don't extend this class,
        // and are supposed to use a different WordPress.com account
        Context context = getInstrumentation().getTargetContext().getApplicationContext();
        context.getSharedPreferences(context.getPackageName() + "_fluxc-preferences", Context.MODE_PRIVATE)
               .edit().putString("ACCOUNT_TOKEN_PREF_KEY", null).apply();
    }

    @Override
    protected void init() throws Exception {
        super.init();
        internalInit(false);
    }

    protected void init(boolean testRequiresUserId) throws Exception {
        super.init();
        internalInit(testRequiresUserId);
    }

    private void internalInit(boolean testRequiresUserId) throws Exception {
        mNextEvent = TestEvents.NONE;

        if (!mAccountStore.getAccessToken().isEmpty() && sSite != null
            && (!testRequiresUserId || mAccountStore.getAccount().getUserId() != 0)) {
            // We have all we need, move on (the AccountStore is probably empty, but we don't need it)
            return;
        }

        if (mAccountStore.getAccessToken().isEmpty() || mAccountStore.getAccount().getUserId() == 0) {
            authenticate();
        }

        if (sSite == null) {
            fetchSites();
            sSite = getSiteFromDb();
        }
    }

    protected SiteModel getSiteFromDb() {
        return mSiteStore.getSites().get(0);
    }

    protected AuthenticatePayload buildAuthenticatePayload() {
        if (mAuthenticatePayload == null) {
            mAuthenticatePayload = new AuthenticatePayload(BuildConfig.TEST_WPCOM_USERNAME_TEST1,
                    BuildConfig.TEST_WPCOM_PASSWORD_TEST1);
        }
        return mAuthenticatePayload;
    }

    private void authenticate() throws InterruptedException {
        // Authenticate a test user (actual credentials declared in gradle.properties)
        AuthenticatePayload payload = buildAuthenticatePayload();

        // Correct user we should get an OnAuthenticationChanged message
        mCountDownLatch = new CountDownLatch(1);
        mNextEvent = TestEvents.AUTHENTICATED;
        mDispatcher.dispatch(AuthenticationActionBuilder.newAuthenticateAction(payload));
        // Wait for a network response / onChanged event
        assertTrue(mCountDownLatch.await(TestUtils.DEFAULT_TIMEOUT_MS, TimeUnit.MILLISECONDS));

        // Also fetch the account - it's required before WP.com sites can be stored in the database
        mCountDownLatch = new CountDownLatch(1);
        mNextEvent = TestEvents.ACCOUNT_FETCHED;
        mDispatcher.dispatch(AccountActionBuilder.newFetchAccountAction());
        // Wait for a network response / onChanged event
        assertTrue(mCountDownLatch.await(TestUtils.DEFAULT_TIMEOUT_MS, TimeUnit.MILLISECONDS));
    }

    private void fetchSites() throws InterruptedException {
        // Fetch sites from REST API, and wait for onSiteChanged event
        mCountDownLatch = new CountDownLatch(1);
        mNextEvent = TestEvents.SITE_CHANGED;
        mDispatcher.dispatch(SiteActionBuilder.newFetchSitesAction(new FetchSitesPayload()));

        assertTrue(mCountDownLatch.await(TestUtils.DEFAULT_TIMEOUT_MS, TimeUnit.MILLISECONDS));
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onAuthenticationChanged(OnAuthenticationChanged event) {
        assertFalse("!!!!! Authentication failed - verify tests.properties contains valid credentials !!!!! ",
                event.isError());
        assertEquals(TestEvents.AUTHENTICATED, mNextEvent);
        mCountDownLatch.countDown();
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onAccountChanged(OnAccountChanged event) {
        assertFalse(event.isError());
        assertEquals(TestEvents.ACCOUNT_FETCHED, mNextEvent);
        mCountDownLatch.countDown();
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onSiteChanged(OnSiteChanged event) {
        AppLog.i(T.TESTS, "site count " + mSiteStore.getSitesCount());
        if (event.isError()) {
            throw new AssertionError("event error type: " + event.error.type);
        }
        assertTrue(mSiteStore.hasSite());
        assertTrue(mSiteStore.hasSitesAccessedViaWPComRest());
        assertEquals(TestEvents.SITE_CHANGED, mNextEvent);
        mCountDownLatch.countDown();
    }
}
