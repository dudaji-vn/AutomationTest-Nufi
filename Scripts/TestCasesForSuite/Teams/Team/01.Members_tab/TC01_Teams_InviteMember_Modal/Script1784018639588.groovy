import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC01: Teams - Open Invite Member Modal
 * 
 * Test Flow:
 * 1. Create a new team successfully
 * 2. Verify redirect to team detail page (URL contains /teams/{teamId})
 * 3. Verify Members tab is active by default
 * 4. Click Invite Member button
 * 5. Verify Invite Member popup appears
 * 6. Close popup
 */

WebUI.comment('=== TC01: Teams - Open Invite Member Modal ===')

try {
//    String teamName = 'Test Team ' + System.currentTimeMillis()
//    String teamDescription = 'Description for ' + teamName
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
//    WebUI.setText(
//        findTestObject('Object Repository/nav/Teams/Team/create-team/textarea_team-description'),
//        teamDescription
//    )
//    WebUI.click(findTestObject('Object Repository/nav/Teams/Team/create-team/button_Create_Create Team'))
//    WebUI.delay(3)
//    WebUI.comment('✓ Team created: ' + teamName)
    
    // Step 2: Verify redirect to team detail page
    WebUI.comment('Step 2: Verifying redirect to team detail page...')
    String currentUrl = WebUI.getUrl()
    WebUI.comment('Current URL: ' + currentUrl)
    
    if (!currentUrl.contains('/teams/')) {
        throw new Exception('Not redirected to team detail page. Current URL: ' + currentUrl)
    }
    WebUI.comment('✓ Redirected to team detail page: ' + currentUrl)
    
    // Step 3: Verify Members tab is active by default
    WebUI.comment('Step 3: Verifying Members tab is active...')
    TestObject membersTab = new TestObject('membersTab')
    membersTab.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@role='tab' and contains(text(), 'Members') and @aria-selected='true']")
    boolean isMembersActive = WebUI.waitForElementVisible(membersTab, 5)
    
    if (!isMembersActive) {
        throw new Exception('Members tab is not active by default')
    }
    WebUI.comment('✓ Members tab is active')
    
    // Step 4: Click Invite Member button
    WebUI.comment('Step 4: Clicking Invite Member button...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/Teams/Team/Tab_member/button_Invite-member'),
        01
    )
    WebUI.click(findTestObject('Object Repository/nav/Teams/Team/Tab_member/button_Invite-member'))
    WebUI.delay(2)
    WebUI.comment('✓ Invite Member button clicked')
    
    // Step 5: Verify Invite Member popup appears
    WebUI.comment('Step 5: Verifying Invite Member popup...')
    boolean hasPopup = WebUI.waitForElementVisible(
        findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/popup_Invite member'),
        01
    )
    if (!hasPopup) {
        throw new Exception('Invite Member popup not found')
    }
    WebUI.comment('✓ Invite Member popup opened')
    
    // Step 6: Close popup
    WebUI.comment('Step 6: Closing popup...')
    WebUI.click(findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/button_Cancel_invite'))
    WebUI.delay(1)
    WebUI.comment('✓ Popup closed')
    
    WebUI.takeScreenshot('TC01_Teams_InviteMember_Modal_Success.png')
    WebUI.comment('=== TC01 PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC01 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC01_Teams_InviteMember_Modal_Error.png')
    throw e
}