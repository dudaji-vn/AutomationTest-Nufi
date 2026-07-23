import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC03: Teams - Invite Member Successfully
 * 
 * Test Flow:
 * 1. Create a new team successfully
 * 2. Open Invite Member popup
 * 3. Enter valid email
 * 4. Select role (Member or Admin)
 * 5. Click Invite button
 * 6. Verify success toast appears
 * 7. Verify invited member appears in list
 */

WebUI.comment('=== TC03: Teams - Invite Member Success ===')

try {
//    String teamName = 'Test Team ' + System.currentTimeMillis()
	String teamName =  WebUI.getText(findTestObject('Object Repository/nav/Teams/Team/team-name'))
    String validEmail = 'test_member_' + System.currentTimeMillis() + '@test.com'
    String role = 'Member' // or 'Admin'
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
    
    // Step 4: Select role
    WebUI.comment('Step 4: Selecting role...')
    WebUI.click(findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/button_role'))
    WebUI.delay(1)
    
    if (role == 'Admin') {
        WebUI.click(findTestObject('Object Repository/nav/Teams/Tab_member/Invite-member/Role/select_Admin'))
        WebUI.comment('✓ Role selected: Admin')
    } else {
        WebUI.click(findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/Role/sellect_Member'))
        WebUI.comment('✓ Role selected: Member')
    }
    WebUI.delay(1)
    
    // Step 5: Click Invite button
    WebUI.comment('Step 5: Clicking Invite button...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/button_Invite member'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/button_Invite member'))
    WebUI.delay(2)
    WebUI.comment('✓ Invite button clicked')
    
    // Step 6: Verify success toast appears
    WebUI.comment('Step 6: Verifying success toast...')
    boolean hasToast = WebUI.waitForElementVisible(
        findTestObject('Object Repository/Toast/Toast_Success'),
        5
    )
    
    if (!hasToast) {
        throw new Exception('Success toast not displayed')
    }
    
    String toastText = WebUI.getText(findTestObject('Object Repository/Toast/Toast_Success'))
    WebUI.comment('Success toast message: ' + toastText)
    WebUI.comment('✓ Success toast verified')
    
    // Step 7: Verify invited member appears in list
//    WebUI.comment('Step 7: Verifying invited member appears in list...')
//    TestObject invitedMember = new TestObject('invitedMember')
//    invitedMember.addProperty('xpath', ConditionType.EQUALS, 
//        "//p[contains(@class, 'text-text-secondary') and contains(text(), '" + validEmail + "')]")
//    boolean hasMember = WebUI.waitForElementVisible(invitedMember, 10)
//    
//    if (!hasMember) {
//        throw new Exception('Invited member "' + validEmail + '" not found in list')
//    }
//    WebUI.comment('✓ Invited member "' + validEmail + '" found in list')
//    
//    // Verify member role
//    TestObject memberRole = new TestObject('memberRole')
//    memberRole.addProperty('xpath', ConditionType.EQUALS, 
//        "//p[contains(@class, 'text-text-secondary') and contains(text(), '" + validEmail + "')]/ancestor::li//span[contains(@class, 'text-text-secondary') and contains(text(), '" + role + "')]")
//    boolean hasRole = WebUI.waitForElementVisible(memberRole, 5)
//    
//    if (!hasRole) {
//        WebUI.comment('⚠ Warning: Role "' + role + '" not found for invited member')
//    } else {
//        WebUI.comment('✓ Role "' + role + '" verified for invited member')
//    }
	// Set GlobalVariables for TC14
	GlobalVariable.invitedTeamName = teamName
	GlobalVariable.invitedEmail = validEmail
	GlobalVariable.invitedRole = role
	
	WebUI.comment('✓ GlobalVariables set for TC14: invitedTeamName=' + teamName + ', invitedEmail=' + validEmail)
	
    WebUI.takeScreenshot('TC03_Teams_InviteMember_Success.png')
    WebUI.comment('=== TC03 PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC03 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC03_Teams_InviteMember_Error.png')
    throw e
}