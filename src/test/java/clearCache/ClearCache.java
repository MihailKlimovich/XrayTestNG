package clearCache;

import org.testng.annotations.Test;
import tests.BaseTest;

public class ClearCache extends BaseTest {

    @Test(priority = 1, description="Clear Cache")
    public void clearCache() throws InterruptedException{
        driver.get("chrome://settings/clearBrowserData");
        Thread.sleep(5000);
        account.enter();
        Thread.sleep(5000);
        account.enter();
        Thread.sleep(10000);

    }
}
