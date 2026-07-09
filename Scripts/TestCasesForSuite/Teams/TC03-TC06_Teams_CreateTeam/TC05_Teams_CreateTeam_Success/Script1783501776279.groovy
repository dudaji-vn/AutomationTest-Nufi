import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC05: Teams - Create Team Validation (missing required fields)
 * 
 * Test Flow:
 * 1. Ensure on Teams page (/teams)
 * 2. Click Create Team button
 * 3. Verify Create Team popup appears
 * 4. Verify Create button is disabled (not clickable without required fields)
 * 5. Close/Cancel popup
 */

WebUI.comment('=== TC05: Teams - Create Team Validation ===')

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
        findTestObject('Object Repository/nav/Teams/create-team/popup_Create Team'),
        10
    )
    if (!hasPopup) {
        throw new Exception('Create Team popup not found')
    }
    WebUI.comment('✓ Create Team popup opened')
    
    // Step 4: Verify Create button is disabled (no required fields filled)
    WebUI.comment('Step 4: Verifying Create button is disabled...')
    TestObject createButton = findTestObject('Object Repository/nav/Teams/create-team/button_Create_Create Team')
    String disabledAttr = WebUI.getAttribute(createButton, 'disabled')
    boolean isDisabled = (disabledAttr != null && disabledAttr == 'true') || disabledAttr == ''
    
    if (!isDisabled) {
        boolean isClickable = WebUI.waitForElementClickable(createButton, 2, FailureHandling.OPTIONAL)
        if (isClickable) {
            WebUI.comment('⚠ Warning: Create button is clickable without required fields')
            WebUI.click(createButton)
            WebUI.delay(1)
            
            // Check for error toast
            boolean hasErrorToast = WebUI.waitForElementVisible(
                findTestObject('Object Repository/Toast/Toast_Error'),
                3,
                FailureHandling.OPTIONAL
            )
            
            if (hasErrorToast) {
                String errorText = WebUI.getText(findTestObject('Object Repository/Toast/Toast_Error'))
                WebUI.comment('Error toast message: ' + errorText)
                WebUI.comment('✓ Validation prevented team creation - error toast shown')
            } else {
                boolean popupStillOpen = WebUI.waitForElementVisible(
                    findTestObject('Object Repository/nav/Teams/create-team/popup_Create Team'),
                    2,
                    FailureHandling.OPTIONAL
                )
                if (!popupStillOpen) {
                    throw new Exception('Team was created without required fields!')
                }
                WebUI.comment('✓ Validation prevented team creation without required fields')
            }
        } else {
            WebUI.comment('✓ Create button is not clickable (disabled by validation)')
        }
    } else {
        WebUI.comment('✓ Create button is disabled as expected (no required fields filled)')
    }
    
    // Step 5: Close popup by clicking Cancel
    WebUI.comment('Step 5: Closing popup with Cancel button...')
    WebUI.click(findTestObject('Object Repository/nav/Teams/create-team/button_Cancel_Create Team'))
    WebUI.delay(1)
    WebUI.comment('✓ Popup closed')
    
    WebUI.takeScreenshot('TC05_Teams_CreateTeam_Validation_Success.png')
    WebUI.comment('=== TC05 PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC05 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC05_Teams_CreateTeam_Validation_Error.png')
    throw e
}