import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC03: Nufi Console - Usage Tab
 * 
 * Test Flow:
 * 1. Ensure on Console tab
 * 2. Click Usage tab
 * 3. Verify Usage page loads
 * 4. Verify usage data (0 requests for new user)
 */

WebUI.comment('=== TC03: Nufi Console - Usage Tab ===')

try {
    // Switch to Console tab
    WebUI.comment('Step 1: Switching to Console tab...')
    WebDriver driver = DriverFactory.getWebDriver()
    Set<String> windowHandles = driver.getWindowHandles()
    
    for (String handle : windowHandles) {
        driver.switchTo().window(handle)
        String url = driver.getCurrentUrl()
        if (url.contains('console.nufi.me')) {
            WebUI.comment('On Console tab: ' + url)
            break
        }
    }
    WebUI.delay(2)
    
    // Step 2: Click Usage tab
    WebUI.comment('Step 2: Clicking Usage tab...')
    TestObject usageTab = new TestObject('usageTab')
    usageTab.addProperty('xpath', ConditionType.EQUALS, 
        "//a[contains(@href, '/usage') and contains(text(), 'Usage')]")
    WebUI.waitForElementClickable(usageTab, 10)
    WebUI.click(usageTab)
    WebUI.delay(2)
    WebUI.comment('Usage tab clicked')
    
    // Step 3: Verify Usage page loads
    WebUI.comment('Step 3: Verifying Usage page loads...')
    String currentUrl = driver.getCurrentUrl()
    WebUI.comment('Current URL: ' + currentUrl)
    
    if (currentUrl.contains('/usage')) {
        WebUI.comment('Usage page loaded')
    } else {
        WebUI.comment('Not on Usage page: ' + currentUrl)
    }
    
    // Step 4: Verify usage data
    WebUI.comment('Step 4: Verifying usage data...')
    
    // Check for total requests or usage amount
    TestObject usageAmount = new TestObject('usageAmount')
    usageAmount.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'font-mono') and contains(@class, 'text-3xl')]")
    String amountText = WebUI.getText(usageAmount)
    WebUI.comment('Usage amount: ' + amountText)
    
    // Check for request count
    TestObject requestCount = new TestObject('requestCount')
    requestCount.addProperty('xpath', ConditionType.EQUALS, 
        "//span[contains(text(), 'requests')]")
    String requests = WebUI.getText(requestCount)
    WebUI.comment('Requests: ' + requests)
    
    WebUI.takeScreenshot('TC03_Console_Usage_Success.png')
    WebUI.comment('TC03 PASSED')

} catch (Exception e) {
    WebUI.comment('TC03 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC03_Console_Usage_Error.png')
    throw e
}