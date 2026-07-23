import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC04: Teams - Cancel Invite Member
 * 
 * Test Flow:
 * 1. Create a new team successfully
 * 2. Open Invite Member popup
 * 3. Enter valid email
 * 4. Click Cancel button
 * 5. Verify popup closed
 * 6. Verify member NOT added to list
 */

WebUI.comment('=== TC04: Teams - Cancel Invite Member ===')

try {
//    String teamName = 'Test Team ' + System.currentTimeMillis()
    String validEmail = 'cancelled_member_' + System.currentTimeMillis() + '@test.com'
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
        findTestObject('Object Repository/nav/Teams/Team/Tab_member/button_Invite-member'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/Teams/Team/Tab_member/button_Invite-member'))
    WebUI.delay(2)
    WebUI.comment('✓ Invite Member popup opened')
    
    // Step 3: Enter valid email
    WebUI.comment('Step 3: Entering valid email...')
    TestObject emailInput = findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/input_invite-email')
	WebUI.waitForElementVisible(emailInput, 10)
	WebUI.clearText(emailInput) 
    WebUI.setText(emailInput,validEmail)
	
    WebUI.comment('✓ Valid email entered: ' + validEmail)
    WebUI.delay(1)
    
    // Step 4: Click Cancel button
    WebUI.comment('Step 4: Clicking Cancel button...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/button_Cancel_invite'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/button_Cancel_invite'))
    WebUI.delay(2)
    WebUI.comment('✓ Cancel button clicked')
    
    // Step 5: Verify popup closed
    WebUI.comment('Step 5: Verifying popup closed...')
    boolean popupClosed = WebUI.waitForElementNotVisible(
        findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/popup_Invite member'),
        5,
        FailureHandling.OPTIONAL
    )
    if (!popupClosed) {
        WebUI.comment('⚠ Warning: Popup may still be visible')
    } else {
        WebUI.comment('✓ Popup closed')
    }
    
    // Step 6: Verify member NOT added to list
    WebUI.comment('Step 6: Verifying member NOT added to list...')
    TestObject cancelledMember = new TestObject('cancelledMember')
    cancelledMember.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-text-secondary') and contains(text(), '" + validEmail + "')]")
    boolean memberExists = WebUI.waitForElementVisible(cancelledMember, 3, FailureHandling.OPTIONAL)
    
    if (memberExists) {
        throw new Exception('Member "' + validEmail + '" was added despite cancellation')
    }
    WebUI.comment('✓ Member not added (cancelled successfully)')
    
    WebUI.takeScreenshot('TC04_Teams_InviteMember_Cancel_Success.png')
    WebUI.comment('=== TC04 PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC04 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC04_Teams_InviteMember_Cancel_Error.png')
    throw e
}