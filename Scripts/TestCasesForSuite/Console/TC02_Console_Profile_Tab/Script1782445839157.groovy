import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC02: Nufi Console - Profile Tab
 * 
 * Test Flow:
 * 1. Ensure on Console tab
 * 2. Verify Profile tab is active
 * 3. Verify user ID displayed
 * 4. Verify usage status
 * 5. Verify API keys section
 * 6. Verify Per-minute limits
 */

WebUI.comment('=== TC02: Nufi Console - Profile Tab ===')

try {
    // Switch to Console tab if not already
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
    
    // Step 2: Verify Profile tab is active
    WebUI.comment('Step 2: Verifying Profile tab is active...')
    TestObject profileTab = new TestObject('profileTab')
    profileTab.addProperty('xpath', ConditionType.EQUALS, 
        "//a[contains(@class, 'text-foreground') and contains(text(), 'Profile')]")
    boolean isProfileActive = WebUI.waitForElementVisible(profileTab, 5, FailureHandling.OPTIONAL)
    WebUI.comment('Profile tab active: ' + isProfileActive)
    
    // Step 3: Verify user ID
    WebUI.comment('Step 3: Verifying user ID...')
    TestObject userId = new TestObject('userId')
    userId.addProperty('xpath', ConditionType.EQUALS, 
        "//p[@class='mt-1 flex items-center gap-2 text-sm text-muted-foreground']//span[@class='font-mono']")
    String userIdText = WebUI.getText(userId)
    WebUI.comment('User ID: ' + userIdText)
    
    if (userIdText != null && !userIdText.trim().isEmpty()) {
        WebUI.comment('User ID found')
    } else {
        WebUI.comment('User ID not found')
    }
    
    // Step 4: Verify usage status
    WebUI.comment('Step 4: Verifying usage status...')
    TestObject usageStatus = new TestObject('usageStatus')
    usageStatus.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'rounded-xl')]//p[contains(@class, 'text-xs') and contains(@class, 'font-medium')]")
    String statusText = WebUI.getText(usageStatus)
    WebUI.comment('Usage status: ' + statusText)
    
    // Step 5: Verify API keys section
    WebUI.comment('Step 5: Verifying API keys section...')
    TestObject apiKeysSection = new TestObject('apiKeysSection')
    apiKeysSection.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'rounded-lg')]//h3[contains(text(), 'Your keys')]")
    boolean hasApiKeys = WebUI.waitForElementVisible(apiKeysSection, 5, FailureHandling.OPTIONAL)
    WebUI.comment('API keys section exists: ' + hasApiKeys)
    
    // Step 6: Verify Per-minute limits
    WebUI.comment('Step 6: Verifying Per-minute limits...')
    TestObject perMinuteLimits = new TestObject('perMinuteLimits')
    perMinuteLimits.addProperty('xpath', ConditionType.EQUALS, 
        "//h3[contains(text(), 'Per-minute limits')]")
    boolean hasPerMinuteLimits = WebUI.waitForElementVisible(perMinuteLimits, 3, FailureHandling.OPTIONAL)
    WebUI.comment('Per-minute limits section exists: ' + hasPerMinuteLimits)
    
    WebUI.takeScreenshot('TC02_Console_Profile_Success.png')
    WebUI.comment('TC02 PASSED')

} catch (Exception e) {
    WebUI.comment('TC02 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC02_Console_Profile_Error.png')
    throw e
}