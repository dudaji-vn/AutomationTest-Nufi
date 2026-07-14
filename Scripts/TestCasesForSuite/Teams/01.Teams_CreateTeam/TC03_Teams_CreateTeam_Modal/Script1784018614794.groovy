import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC03: Teams - Open Create Team modal
 * 
 * Test Flow:
 * 1. At page /teams
 * 2. Click Create Team button
 * 3. Verify Create Team popup appears
 */

WebUI.comment('=== TC03: Teams - Open Create Team Modal ===')

try {
    // Step 1: Ensure on Teams page
    WebUI.comment('Step 1: Ensuring on Teams page...')
    String currentUrl = WebUI.getUrl()
    if (!currentUrl.contains('/teams')) {
        WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Teams'))
        WebUI.delay(2)
    }
    WebUI.comment('✓ On Teams page: ' + WebUI.getUrl())
    
    // Step 2: Click Create Team button
    WebUI.comment('Step 2: Clicking Create Team button...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/Teams/button_Create Team'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/Teams/button_Create Team'))
    WebUI.delay(2)
    WebUI.comment('✓ Create Team button clicked')
    
    // Step 3: Verify Create Team popup appears
    WebUI.comment('Step 3: Verifying Create Team popup...')
    boolean hasPopup = WebUI.waitForElementVisible(
        findTestObject('Object Repository/nav/Teams/Team/create-team/popup_Create Team'),
        10
    )
    if (!hasPopup) {
        throw new Exception('Create Team popup not found')
    }
    WebUI.comment('✓ Create Team popup opened')
    
    // Step 4: Close popup
    WebUI.comment('Step 4: Closing popup...')
    WebUI.click(findTestObject('Object Repository/nav/Teams/Team/create-team/button_Cancel_Create Team'))
    WebUI.delay(1)
    WebUI.comment('✓ Popup closed')
    
    WebUI.takeScreenshot('TC03_Teams_CreateTeam_Modal_Success.png')
    WebUI.comment('=== TC03 PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC03 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC03_Teams_CreateTeam_Modal_Error.png')
    throw e
}