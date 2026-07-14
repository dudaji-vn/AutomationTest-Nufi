import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC02: Teams - Invite Member Validation (missing email & invalid email)
 * 
 * Test Flow:
 * 1. Create a new team successfully
 * 2. Open Invite Member popup
 * 3. Do NOT enter email -> Verify Invite button is disabled
 * 4. Enter invalid email -> Click Invite -> Verify error toast appears
 * 5. Close popup
 */

WebUI.comment('=== TC02: Teams - Invite Member Validation ===')

try {
    String invalidEmail = 'invalid-email'
//    String teamName = 'Test Team ' + System.currentTimeMillis()
//    
//    // Step 1: Create a new team
//    WebUI.comment('Step 1: Creating a new team...')
//    WebUI.click(findTestObject('Object Repository/nav/Teams/button_Create Team'))
//    WebUI.delay(2)
//    
//    WebUI.setText(
//        findTestObject('Object Repository/nav/Teams/Team/create-team/input_team-name'),
//        teamName
//    )
//    WebUI.click(findTestObject('Object Repository/nav/Teams/Team/create-team/button_Create_Create Team'))
//    WebUI.delay(3)
//    WebUI.comment('✓ Team created: ' + teamName)
//    
    // Step 2: Open Invite Member popup
    WebUI.comment('Step 2: Opening Invite Member popup...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/Teams/Team/Tab_member/button_Invite member'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/Teams/Team/Tab_member/button_Invite member'))
    WebUI.delay(2)
    WebUI.comment('✓ Invite Member popup opened')
    
    // Step 3: Do NOT enter email - Verify Invite button is disabled
    WebUI.comment('Step 3: Verifying Invite button is disabled without email...')
    TestObject inviteButton = findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/button_Invite member')
    String disabledAttr = WebUI.getAttribute(inviteButton, 'disabled')
    boolean isDisabled = (disabledAttr != null && disabledAttr == 'true') || disabledAttr == ''
    
    if (!isDisabled) {
        boolean isClickable = WebUI.waitForElementClickable(inviteButton, 2, FailureHandling.OPTIONAL)
        if (isClickable) {
            throw new Exception('Invite button is clickable without email!')
        }
    }
    WebUI.comment('✓ Invite button is disabled as expected (no email entered)')
    
    // Step 4: Enter invalid email and verify error toast
    WebUI.comment('Step 4: Entering invalid email...')
    TestObject emailInput = findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/input_invite-email')
	WebUI.waitForElementVisible(emailInput, 10)
	WebUI.clearText(emailInput) 
    WebUI.setText(
        findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/input_invite-email'),
        invalidEmail
    )
    WebUI.comment('✓ Invalid email entered: ' + invalidEmail)
    WebUI.delay(1)
    
    WebUI.comment('Step 5: Clicking Invite button...')
    WebUI.waitForElementClickable(inviteButton, 10)
    WebUI.click(inviteButton)
    WebUI.delay(2)
    WebUI.comment('✓ Invite button clicked')
    
    WebUI.comment('Step 6: Verifying error toast...')
    boolean hasErrorToast = WebUI.waitForElementVisible(
        findTestObject('Object Repository/Toast/Toast_Error'),
        5
    )
    
    if (!hasErrorToast) {
        throw new Exception('Error toast not displayed for invalid email')
    }
    
    String errorText = WebUI.getText(findTestObject('Object Repository/Toast/Toast_Error'))
    WebUI.comment('Error toast message: ' + errorText)
    WebUI.comment('✓ Error toast verified for invalid email')
    
    // Step 7: Close popup
    WebUI.comment('Step 7: Closing popup...')
    WebUI.click(findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/button_Cancel_invite'))
    WebUI.delay(1)
    WebUI.comment('✓ Popup closed')
    
    WebUI.takeScreenshot('TC02_Teams_InviteMember_Validation_Success.png')
    WebUI.comment('=== TC02 PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC02 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC02_Teams_InviteMember_Validation_Error.png')
    throw e
}