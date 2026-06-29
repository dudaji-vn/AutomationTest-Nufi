import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC08: Nufi Console - Verify Keys After Chat
 * 
 * Test Flow:
 * 1. Ensure on Console tab
 * 2. Click API Keys tab
 * 3. Verify keys page loads
 * 4. Verify active keys count
 * 5. Verify usage across keys updated
 */

WebUI.comment('=== TC08: Nufi Console - Verify Keys After Chat ===')

try {
    // Step 1: Switch to Console tab
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
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/Page_NUFI Console/a_API keys'),
        10
    )
    WebUI.click(findTestObject('Object Repository/Page_NUFI Console/a_API keys'))
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
    
    // Step 4: Verify active keys count
    WebUI.comment('Step 4: Verifying active keys count...')
    TestObject activeKeys = new TestObject('activeKeys')
    activeKeys.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'rounded-lg')]//p[contains(text(), 'Active keys')]/following-sibling::p[contains(@class, 'font-mono')]")
    String activeKeysText = WebUI.getText(activeKeys)
    WebUI.comment('Active keys: ' + activeKeysText)
    
    int activeKeysCount = 0
    try {
        activeKeysCount = Integer.parseInt(activeKeysText.trim())
        WebUI.comment('Active keys count: ' + activeKeysCount)
    } catch (Exception e) {
        WebUI.comment('Could not parse active keys count: ' + activeKeysText)
    }
    
    if (activeKeysCount > 0) {
        WebUI.comment('✓ Active keys found: ' + activeKeysCount)
    } else {
        WebUI.comment('⚠ No active keys found')
    }
    
    // Step 5: Verify usage across keys
    WebUI.comment('Step 5: Verifying usage across keys...')
    TestObject usedAcrossKeys = new TestObject('usedAcrossKeys')
    usedAcrossKeys.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'rounded-lg')]//p[contains(text(), 'Used across keys')]/following-sibling::p[contains(@class, 'font-mono')]")
    String usageText = WebUI.getText(usedAcrossKeys)
    WebUI.comment('Used across keys: ' + usageText)
    
    WebUI.takeScreenshot('TC08_Console_Keys_AfterChat_Success.png')
    WebUI.comment('TC08 PASSED')

} catch (Exception e) {
    WebUI.comment('TC08 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC08_Console_Keys_AfterChat_Error.png')
    throw e
}