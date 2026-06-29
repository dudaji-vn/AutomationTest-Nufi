import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC06: Nufi Console - Create Chat and Switch to Console
 * 
 * Test Flow:
 * 1. Switch back to main chat tab
 * 2. Select Gemini endpoint
 * 3. Send a test message to create conversation
 * 4. Verify response received
 * 5. Switch back to Console tab
 * 6. Refresh Console page
 */

WebUI.comment('=== TC06: Nufi Console - Create Chat and Switch to Console ===')

try {
    // Step 1: Switch back to main chat tab
    WebUI.comment('Step 1: Switching back to main chat tab...')
    WebDriver driver = DriverFactory.getWebDriver()
    Set<String> windowHandles = driver.getWindowHandles()
    
    for (String handle : windowHandles) {
        driver.switchTo().window(handle)
        String url = driver.getCurrentUrl()
        WebUI.comment('Current URL: ' + url)
        
        if (url.contains(GlobalVariable.Base_URL) && !url.contains('console.nufi.me')) {
            WebUI.comment('Found main chat tab: ' + url)
            break
        }
    }
    WebUI.delay(2)
    
    // Step 2: Select Gemini endpoint
    WebUI.comment('Step 2: Selecting Gemini endpoint...')
    CustomKeywords.'keywords.ChatKeywords.selectEndpointAndModel'('Gemini', 'gemini')
    WebUI.delay(2)
    WebUI.comment('Selected Gemini endpoint')
    
    // Step 3: Send a test message
    WebUI.comment('Step 3: Sending test message...')
    String testMessage = 'Console test message - ' + System.currentTimeMillis()
    String response = CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(testMessage)
    WebUI.comment('Response received: ' + (response.length() > 100 ? response.substring(0, 100) + '...' : response))
    
    // Step 4: Verify response received
    WebUI.comment('Step 4: Verifying response received...')
    if (response != null && !response.trim().isEmpty()) {
        WebUI.comment('✓ Response received successfully')
        WebUI.takeScreenshot('TC06_CreateChat_Success.png')
    } else {
        WebUI.comment('✗ Response is empty')
        throw new Exception('Response is empty')
    }
    
    // Step 5: Switch back to Console tab
    WebUI.comment('Step 5: Switching back to Console tab...')
    Set<String> handles = driver.getWindowHandles()
    for (String handle : handles) {
        driver.switchTo().window(handle)
        String url = driver.getCurrentUrl()
        if (url.contains('console.nufi.me')) {
            WebUI.comment('Switched to Console tab: ' + url)
            break
        }
    }
    WebUI.delay(2)
    
    // Step 6: Refresh Console page
    WebUI.comment('Step 6: Refreshing Console page...')
    WebUI.refresh()
    WebUI.delay(3)
    WebUI.comment('Console page refreshed - ready for verification in subsequent TCs')
    
    WebUI.takeScreenshot('TC06_Console_AfterRefresh.png')
    
    WebUI.comment('TC06 PASSED')

} catch (Exception e) {
    WebUI.comment('TC06 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC06_Console_CreateChat_Error.png')
    throw e
}