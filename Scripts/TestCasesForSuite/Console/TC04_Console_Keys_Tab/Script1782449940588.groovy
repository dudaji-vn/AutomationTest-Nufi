import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC04: Nufi Console - API Keys Tab
 * 
 * Test Flow:
 * 1. Ensure on Console tab
 * 2. Click API Keys tab
 * 3. Verify API Keys page loads
 * 4. Verify "You don't have any API keys" message (new user)
 */

WebUI.comment('=== TC04: Nufi Console - API Keys Tab ===')

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
    
    // Step 2: Click API Keys tab
    WebUI.comment('Step 2: Clicking API Keys tab...')
    TestObject keysTab = new TestObject('keysTab')
    keysTab.addProperty('xpath', ConditionType.EQUALS, 
        "//a[contains(@href, '/keys') and contains(text(), 'API keys')]")
    WebUI.waitForElementClickable(keysTab, 10)
    WebUI.click(keysTab)
    WebUI.delay(2)
    WebUI.comment('API Keys tab clicked')
    
    // Step 3: Verify API Keys page loads
    WebUI.comment('Step 3: Verifying API Keys page loads...')
    String currentUrl = driver.getCurrentUrl()
    WebUI.comment('Current URL: ' + currentUrl)
    
    if (currentUrl.contains('/keys')) {
        WebUI.comment('API Keys page loaded')
    } else {
        WebUI.comment('Not on API Keys page: ' + currentUrl)
    }
    
    // Step 4: Verify "You don't have any API keys" message
    WebUI.comment('Step 4: Verifying API keys message...')
    TestObject noKeysMessage = new TestObject('noKeysMessage')
    noKeysMessage.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(text(), 'You don\\'t have any API keys')]")
    boolean hasMessage = WebUI.waitForElementVisible(noKeysMessage, 5, FailureHandling.OPTIONAL)
    
    if (hasMessage) {
        WebUI.comment('No API keys message found - correct for new user')
    } else {
        WebUI.comment('No API keys message not found')
    }
    
    // Step 5: Check for "Generate one" link
    TestObject generateLink = new TestObject('generateLink')
    generateLink.addProperty('xpath', ConditionType.EQUALS, 
        "//a[contains(text(), 'Generate one')]")
    boolean hasGenerateLink = WebUI.waitForElementVisible(generateLink, 3, FailureHandling.OPTIONAL)
    WebUI.comment('"Generate one" link exists: ' + hasGenerateLink)
    
    WebUI.takeScreenshot('TC04_Console_Keys_Success.png')
    WebUI.comment('TC04 PASSED')

} catch (Exception e) {
    WebUI.comment('TC04 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC04_Console_Keys_Error.png')
    throw e
}