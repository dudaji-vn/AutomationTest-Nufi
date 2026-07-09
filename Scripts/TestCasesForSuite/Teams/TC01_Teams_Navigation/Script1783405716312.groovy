import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC01: Teams - Navigation to Teams tab
 * 
 * Test Flow:
 * 1. Click Teams button trên navbar
 * 2. Verify URL contains '/teams'
 */

WebUI.comment('=== TC01: Teams - Navigation ===')

try {
    // Step 1: Click Teams button on navbar
    WebUI.comment('Step 1: Clicking Teams button on navbar...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/nav_items/button_Teams'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Teams'))
    WebUI.delay(2)
    WebUI.comment('✓ Teams button clicked')
    
    // Step 2: Verify URL contains '/teams'
    WebUI.comment('Step 2: Verifying URL contains /teams...')
    String currentUrl = WebUI.getUrl()
    WebUI.comment('Current URL: ' + currentUrl)
    
    if (!currentUrl.contains('/teams')) {
        throw new Exception('Not navigated to Teams page. Current URL: ' + currentUrl)
    }
    WebUI.comment('✓ Navigated to Teams page: ' + currentUrl)
    
    WebUI.takeScreenshot('TC01_Teams_Navigation_Success.png')
    WebUI.comment('=== TC01 PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC01 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC01_Teams_Navigation_Error.png')
    throw e
}