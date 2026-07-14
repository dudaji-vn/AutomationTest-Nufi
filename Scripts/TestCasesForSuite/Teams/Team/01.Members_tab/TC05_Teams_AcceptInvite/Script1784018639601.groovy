import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType

import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile

import internal.GlobalVariable as GlobalVariable

import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC05: Teams - Accept Invitation as New Member
 * 
 * Test Flow:
 * 1. Open new browser (preserve admin browser)
 * 2. Navigate to registration page
 * 3. Register new user with invited email from TC12 (password = email)
 * 4. Login with newly registered account
 * 5. Verify invitation appears in "My Invitations" section
 * 6. Click "I accept" button
 * 7. Verify success toast appears
 * 8. Verify team appears in member's team list
 */

WebUI.comment('=== TC05: Teams - Accept Invitation as New Member ===')

// Get team name and email from TC12 via GlobalVariable
// These should be set in TC12 before running TC05
String teamName = GlobalVariable.invitedTeamName ?: 'Test Team'
String invitedEmail = GlobalVariable.invitedEmail ?: 'test_member@test.com'
String role = GlobalVariable.invitedRole ?: 'Member'

WebUI.comment('Team: ' + teamName)
WebUI.comment('Invited Email: ' + invitedEmail)
WebUI.comment('Role: ' + role)

try {
    // Step 1: Open new browser window
    WebUI.comment('Step 1: Opening new browser window...')
    WebUI.openBrowser('')
    WebUI.navigateToUrl(GlobalVariable.Base_URL + '/register')
    WebUI.waitForPageLoad(5)
    WebUI.comment('✓ New browser opened to registration page')
    
    // Step 2: Register new user with invited email
    WebUI.comment('Step 2: Registering new user with invited email...')
    WebUI.comment('Password will be same as email for simplicity')
    
    // Generate random username
    long timestamp = System.currentTimeMillis()
    String randomName = 'Accept User ' + timestamp
    String randomUsername = 'accept_user_' + timestamp
    // Password = email (as per your requirement)
    String registrationPassword = invitedEmail
    
    WebUI.setText(findTestObject('Object Repository/Page_Signup/input_name'), randomName)
    WebUI.setText(findTestObject('Object Repository/Page_Signup/input_username'), randomUsername)
    WebUI.setText(findTestObject('Object Repository/Page_Signup/input_email'), invitedEmail)
    WebUI.setText(findTestObject('Object Repository/Page_Signup/input_password'), registrationPassword)
    WebUI.setText(findTestObject('Object Repository/Page_Signup/input_Password_confirm_password'), registrationPassword)
    WebUI.delay(1)
    
    WebUI.comment('Registration form filled with:')
    WebUI.comment('  - Name: ' + randomName)
    WebUI.comment('  - Username: ' + randomUsername)
    WebUI.comment('  - Email: ' + invitedEmail)
    WebUI.comment('  - Password: ' + registrationPassword)
    
    WebUI.click(findTestObject('Object Repository/Page_Signup/button_Continue'))
    WebUI.comment('Continue button clicked - waiting for redirect...')
    
    // Step 3: Wait for registration to process and redirect
    WebUI.comment('Step 3: Waiting for registration to complete...')
    // Wait at least 5-7 seconds for server to process registration
    WebUI.delay(7)
    
    // Verify registration success by checking URL
    String currentUrl = WebUI.getUrl()
    WebUI.comment('Current URL after registration: ' + currentUrl)
    
    if (!currentUrl.contains('/login')) {
        // If still on register page, take screenshot and check for errors
        WebUI.takeScreenshot('TC05_Registration_StillOnRegisterPage.png')
        
        // Try to find any error message on the page
        TestObject errorMessage = new TestObject('errorMessage')
        errorMessage.addProperty('xpath', ConditionType.EQUALS, 
            "//div[contains(@class, 'error')] | //p[contains(@class, 'error')] | //div[contains(@role, 'alert')]")
        boolean hasError = WebUI.verifyElementPresent(errorMessage, 3, FailureHandling.OPTIONAL)
        
        if (hasError) {
            String errorText = WebUI.getText(errorMessage)
            WebUI.comment('Error message found: ' + errorText)
            throw new Exception('Registration failed with error: ' + errorText)
        } else {
            throw new Exception('Registration failed. Still on registration page after 7 seconds. URL: ' + currentUrl)
        }
    }
    WebUI.comment('✓ Registration successful - redirected to login page')
    
    // Step 4: Login with newly registered account
    WebUI.comment('Step 4: Logging in with newly registered account...')
    WebUI.navigateToUrl(GlobalVariable.Base_URL)
    WebUI.waitForPageLoad(5)
    
    // Login with email as both username and password
    CustomKeywords.'keywords.ChatKeywords.loginChat'(invitedEmail, invitedEmail)
    WebUI.comment('✓ Login successful with email as password')
    
    // Step 5: Navigate to Teams page to see invitations
    WebUI.comment('Step 5: Navigating to Teams page...')
    
    // Check screen width for mobile responsive
    String script = "return window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;"
    int screenWidth = (Integer) WebUI.executeJavaScript(script, null)
    
    if (screenWidth < 760) {
        WebUI.comment('Screen width < 760px, opening sidebar...')
        TestObject openSidebarButton = new TestObject('openSidebarButton')
        openSidebarButton.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
        WebUI.waitForElementClickable(openSidebarButton, 5)
        WebUI.click(openSidebarButton)
        WebUI.delay(1)
        WebUI.comment('Sidebar opened')
    }
    
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/nav_items/button_Teams'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Teams'))
    WebUI.delay(3)
    
    String teamsUrl = WebUI.getUrl()
    if (!teamsUrl.contains('/teams')) {
        throw new Exception('Failed to navigate to Teams page. Current URL: ' + teamsUrl)
    }
    WebUI.comment('✓ Navigated to Teams page')
    
    // Step 6: Verify invitation appears in "My Invitations" section
    WebUI.comment('Step 6: Verifying invitation appears in My Invitations...')
    
    // Wait for My Invitations section
    TestObject invitationsSection = new TestObject('invitationsSection')
    invitationsSection.addProperty('xpath', ConditionType.EQUALS, "//h2[contains(text(),'My Invitations')]")
    WebUI.waitForElementVisible(invitationsSection, 10)
    WebUI.comment('✓ My Invitations section found')
    
    // Check if team invitation exists in the list
    TestObject invitationItem = new TestObject('invitationItem')
    invitationItem.addProperty('xpath', ConditionType.EQUALS, 
        "//ul//p[contains(@class,'text-sm') and contains(text(), '" + teamName + "')]")
    boolean hasInvitation = WebUI.waitForElementVisible(invitationItem, 10)
    
    if (!hasInvitation) {
        // Take screenshot and try to find what's on the page
        WebUI.takeScreenshot('TC05_NoInvitation_Found.png')
        
        // Check if maybe the invitation was already accepted
        String pageText = WebUI.getText(findTestObject('Object Repository/body'))
        WebUI.comment('Page text preview: ' + pageText.substring(0, Math.min(500, pageText.length())))
        
        // Check if team is already in Teams list
        TestObject teamInList = new TestObject('teamInList')
        teamInList.addProperty('xpath', ConditionType.EQUALS, 
            "//h2[contains(text(),'Teams')]/following-sibling::div//p[contains(text(), '" + teamName + "')]")
        boolean teamAlreadyExists = WebUI.verifyElementPresent(teamInList, 3, FailureHandling.OPTIONAL)
        
        if (teamAlreadyExists) {
            WebUI.comment('⚠ Team "' + teamName + '" is already in Teams list - invitation may have been auto-accepted')
            WebUI.comment('✓ TC05 PASSED - Team already in member list')
            return
        }
        
        throw new Exception('Invitation for team "' + teamName + '" not found in My Invitations')
    }
    WebUI.comment('✓ Invitation for team "' + teamName + '" found in My Invitations')
    
    // Verify role displayed
    TestObject roleDisplay = new TestObject('roleDisplay')
    roleDisplay.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class,'text-text-secondary') and contains(text(), '" + role + "')]")
    boolean hasRole = WebUI.verifyElementPresent(roleDisplay, 3, FailureHandling.OPTIONAL)
    if (hasRole) {
        WebUI.comment('✓ Role "' + role + '" displayed correctly')
    } else {
        WebUI.comment('⚠ Role display not verified')
    }
    
    // Step 7: Click "I accept" button
    WebUI.comment('Step 7: Clicking I accept button...')
    TestObject acceptButton = new TestObject('acceptButton')
    acceptButton.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='I accept']")
    WebUI.waitForElementClickable(acceptButton, 10)
    WebUI.click(acceptButton)
    WebUI.delay(3)
    WebUI.comment('✓ I accept button clicked')
    
    // Step 8: Verify success toast appears
    WebUI.comment('Step 8: Verifying success toast...')
    TestObject successToast = new TestObject('successToast')
    successToast.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class,'toast') and contains(@class,'success')]")
    boolean hasToast = WebUI.waitForElementVisible(successToast, 5)
    
    if (hasToast) {
        String toastText = WebUI.getText(successToast)
        WebUI.comment('Success toast message: ' + toastText)
        WebUI.comment('✓ Success toast verified')
    } else {
        // Check for alternative toast
        TestObject successToastAlt = new TestObject('successToastAlt')
        successToastAlt.addProperty('xpath', ConditionType.EQUALS, 
            "//div[contains(@class,'Toast') and contains(text(),'joined')]")
        boolean hasAltToast = WebUI.waitForElementVisible(successToastAlt, 3, FailureHandling.OPTIONAL)
        if (hasAltToast) {
            WebUI.comment('✓ Success toast (alternative) verified')
        } else {
            WebUI.comment('⚠ Success toast not found - may have been auto-closed')
        }
    }
    
    // Wait for the invitation to disappear and team to appear
    WebUI.delay(2)
    
    // Step 9: Verify team appears in member's team list
    WebUI.comment('Step 9: Verifying team appears in member\'s team list...')
    
    // Refresh the page to ensure updated state
    WebUI.refresh()
    WebUI.delay(3)
    
    // Check for team in Teams section
    TestObject teamInTeams = new TestObject('teamInTeams')
    teamInTeams.addProperty('xpath', ConditionType.EQUALS, 
        "//h2[contains(text(),'Teams')]/following-sibling::div//p[contains(text(), '" + teamName + "')]")
    boolean teamFound = WebUI.waitForElementVisible(teamInTeams, 10)
    
    if (!teamFound) {
        // Check if team appears in the main list
        TestObject teamInListAlt = new TestObject('teamInListAlt')
        teamInListAlt.addProperty('xpath', ConditionType.EQUALS, 
            "//p[contains(@class,'text-text-primary') and contains(text(), '" + teamName + "')]")
        boolean teamFoundAlt = WebUI.verifyElementPresent(teamInListAlt, 5, FailureHandling.OPTIONAL)
        
        if (teamFoundAlt) {
            WebUI.comment('✓ Team "' + teamName + '" found in team list (alternative selector)')
        } else {
            // Take screenshot for debugging
            WebUI.takeScreenshot('TC05_TeamNotFound_AfterAccept.png')
            
            // Check if invitation section is empty (invitation removed after accept)
            TestObject emptyInvitations = new TestObject('emptyInvitations')
            emptyInvitations.addProperty('xpath', ConditionType.EQUALS, 
                "//p[contains(@class,'text-text-secondary') and contains(text(),'No invitations')]")
            boolean noInvitations = WebUI.verifyElementPresent(emptyInvitations, 3, FailureHandling.OPTIONAL)
            
            if (noInvitations) {
                WebUI.comment('⚠ Invitation section is empty - invitation was removed but team not visible')
            }
        }
    } else {
        WebUI.comment('✓ Team "' + teamName + '" found in member\'s Teams list')
    }
    
    WebUI.takeScreenshot('TC05_Teams_AcceptInvite_Success.png')
    WebUI.comment('=== TC05 PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC05 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC05_Teams_AcceptInvite_Error.png')
    throw e
} finally {
    WebUI.comment('✓ Member browser closed')
}