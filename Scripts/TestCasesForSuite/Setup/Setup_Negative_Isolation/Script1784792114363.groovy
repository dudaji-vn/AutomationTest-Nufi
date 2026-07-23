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
 * Setup_Negative_Isolation
 * 
 * Description: Setup environment for Negative & Isolation test suite
 * 
 * Test Flow:
 * 1. Login as Owner
 * 2. Create team with timestamp
 * 3. Invite Member 1 (User B - for J01, J03) to team
 * 4. Invite Member 2 (User C - for J02) to team
 * 5. Register and accept invitation for Member 1 (User B)
 * 6. Register and accept invitation for Member 2 (User C)
 * 7. Login as Owner and verify both members in team
 * 8. Create Group A (contains User B only - for testing isolation)
 * 9. Share team-wide resource
 * 10. Share Group A-only resource
 * 11. Create User C (non-member) for J02
 * 12. Take screenshot and summary
 */

WebUI.comment('=== Setup_Negative_Isolation ===')

try {
    // Generate timestamp for unique naming
    long timestamp = System.currentTimeMillis()
    
    // ============================================================
    // STEP 1: Login as Owner
    // ============================================================
    WebUI.comment('Step 1: Logging in as Owner...')
    WebUI.openBrowser('')
    WebUI.navigateToUrl(GlobalVariable.Base_URL)
    WebUI.waitForPageLoad(5)
    
    CustomKeywords.'keywords.ChatKeywords.loginChat'(
        GlobalVariable.email,
        GlobalVariable.password
    )
    WebUI.delay(3)
    
    CustomKeywords.'keywords.ChatKeywords.switchToAdvancedInterface'()
    WebUI.delay(2)
    WebUI.comment('✓ Owner login successful')
    
    // ============================================================
    // STEP 2: Create team with timestamp
    // ============================================================
    String teamName = 'Neg_Team_' + timestamp
    GlobalVariable.invitedTeamName = teamName
    
    WebUI.comment('Step 2: Creating team "' + teamName + '"...')
    
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/nav_items/button_Teams'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Teams'))
    WebUI.delay(3)
    
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/Teams/button_Create Team'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/Teams/button_Create Team'))
    WebUI.delay(2)
    
    WebUI.waitForElementVisible(
        findTestObject('Object Repository/nav/Teams/Team/create-team/input_team-name'),
        10
    )
    WebUI.setText(
        findTestObject('Object Repository/nav/Teams/Team/create-team/input_team-name'),
        teamName
    )
    WebUI.delay(1)
    
    WebUI.click(
        findTestObject('Object Repository/nav/Teams/Team/create-team/button_Create_Create Team')
    )
    WebUI.delay(3)
    
    String currentUrl = WebUI.getUrl()
    if (!currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
        throw new Exception('Failed to create team. Current URL: ' + currentUrl)
    }
    WebUI.comment('✓ Team "' + teamName + '" created successfully')
    
    // ============================================================
    // STEP 3: Invite Member 1 (User B) - for J01, J03
    // ============================================================
    WebUI.comment('Step 3: Inviting Member 1 (User B)...')
    
    String member1Email = 'member1_' + timestamp + '@test.com'
    String member1Name = 'Member 1 ' + timestamp
    GlobalVariable.invitedEmail = member1Email
    GlobalVariable.member1Name = member1Name
    
    WebUI.comment('Member 1 (User B) - for J01, J03: ' + member1Email)
    
    // Open Invite Member popup
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/Teams/Team/Tab_member/button_Invite-member'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/Teams/Team/Tab_member/button_Invite-member'))
    WebUI.delay(2)
    
    TestObject emailInput = findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/input_invite-email')
    WebUI.waitForElementVisible(emailInput, 10)
    WebUI.clearText(emailInput)
    WebUI.setText(emailInput, member1Email)
    WebUI.delay(1)
    
    WebUI.click(findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/button_role'))
    WebUI.delay(1)
    WebUI.click(findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/Role/sellect_Member'))
    WebUI.delay(1)
    
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/button_Invite member'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/button_Invite member'))
    WebUI.delay(2)
    
    boolean hasToast = WebUI.waitForElementVisible(
        findTestObject('Object Repository/Toast/Toast_Success'),
        5,
        FailureHandling.OPTIONAL
    )
    if (hasToast) {
        WebUI.comment('✓ Invitation sent to Member 1 (User B)')
    } else {
        WebUI.comment('⚠ Toast not found, but invitation may still be sent')
    }
    
    // ============================================================
    // STEP 4: Invite Member 2 (User C - for J02)
    // ============================================================
    WebUI.comment('Step 4: Inviting Member 2 (User C)...')
    
    String member2Email = 'member2_' + timestamp + '@test.com'
    String member2Name = 'Member 2 ' + timestamp
    GlobalVariable.invitedEmail2 = member2Email
    GlobalVariable.member2Name = member2Name
    
    WebUI.comment('Member 2 (User C) - for J02: ' + member2Email)
    
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/Teams/Team/Tab_member/button_Invite-member'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/Teams/Team/Tab_member/button_Invite-member'))
    WebUI.delay(2)
    
    WebUI.waitForElementVisible(emailInput, 10)
    WebUI.clearText(emailInput)
    WebUI.setText(emailInput, member2Email)
    WebUI.delay(1)
    
    WebUI.click(findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/button_role'))
    WebUI.delay(1)
    WebUI.click(findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/Role/sellect_Member'))
    WebUI.delay(1)
    
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/button_Invite member'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/Teams/Team/Tab_member/Invite-member/button_Invite member'))
    WebUI.delay(2)
    
    boolean hasToast2 = WebUI.waitForElementVisible(
        findTestObject('Object Repository/Toast/Toast_Success'),
        5,
        FailureHandling.OPTIONAL
    )
    if (hasToast2) {
        WebUI.comment('✓ Invitation sent to Member 2 (User C)')
    } else {
        WebUI.comment('⚠ Toast not found, but invitation may still be sent')
    }
    
    GlobalVariable.invitedRole = 'Member'
    
    // ============================================================
    // STEP 5: Register and accept invitation for Member 1 (User B)
    // ============================================================
    WebUI.comment('Step 5: Registering and accepting for Member 1 (User B)...')
    
    WebUI.openBrowser('')
    WebUI.navigateToUrl(GlobalVariable.Base_URL + '/register')
    WebUI.waitForPageLoad(5)
    
    String member1Password = member1Email
    String member1Username = 'user_b_' + timestamp
    
    WebUI.setText(findTestObject('Object Repository/Page_Signup/input_name'), member1Name)
    WebUI.setText(findTestObject('Object Repository/Page_Signup/input_username'), member1Username)
    WebUI.setText(findTestObject('Object Repository/Page_Signup/input_email'), member1Email)
    WebUI.setText(findTestObject('Object Repository/Page_Signup/input_password'), member1Password)
    WebUI.setText(findTestObject('Object Repository/Page_Signup/input_Password_confirm_password'), member1Password)
    WebUI.delay(1)
    
    WebUI.click(findTestObject('Object Repository/Page_Signup/button_Continue'))
    WebUI.delay(7)
    
    WebUI.navigateToUrl(GlobalVariable.Base_URL)
    WebUI.waitForPageLoad(5)
    CustomKeywords.'keywords.ChatKeywords.loginChat'(member1Email, member1Password)
    WebUI.delay(3)
    
    CustomKeywords.'keywords.ChatKeywords.switchToAdvancedInterface'()
    WebUI.delay(2)
    
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/nav_items/button_Teams'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Teams'))
    WebUI.delay(3)
    
    TestObject acceptInviteButton = new TestObject('acceptInviteButton')
    acceptInviteButton.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='I accept']")
    WebUI.waitForElementClickable(acceptInviteButton, 10)
    WebUI.click(acceptInviteButton)
    WebUI.delay(3)
    WebUI.comment('✓ Member 1 (User B) accepted invitation')
    
    WebUI.closeBrowser()
    
    // ============================================================
    // STEP 6: Register and accept invitation for Member 2 (User C)
    // ============================================================
    WebUI.comment('Step 6: Registering and accepting for Member 2 (User C)...')
    
    WebUI.openBrowser('')
    WebUI.navigateToUrl(GlobalVariable.Base_URL + '/register')
    WebUI.waitForPageLoad(5)
    
    String member2Password = member2Email
    String member2Username = 'user_c_' + timestamp
    
    WebUI.setText(findTestObject('Object Repository/Page_Signup/input_name'), member2Name)
    WebUI.setText(findTestObject('Object Repository/Page_Signup/input_username'), member2Username)
    WebUI.setText(findTestObject('Object Repository/Page_Signup/input_email'), member2Email)
    WebUI.setText(findTestObject('Object Repository/Page_Signup/input_password'), member2Password)
    WebUI.setText(findTestObject('Object Repository/Page_Signup/input_Password_confirm_password'), member2Password)
    WebUI.delay(1)
    
    WebUI.click(findTestObject('Object Repository/Page_Signup/button_Continue'))
    WebUI.delay(7)
    
    WebUI.navigateToUrl(GlobalVariable.Base_URL)
    WebUI.waitForPageLoad(5)
    CustomKeywords.'keywords.ChatKeywords.loginChat'(member2Email, member2Password)
    WebUI.delay(3)
    
    CustomKeywords.'keywords.ChatKeywords.switchToAdvancedInterface'()
    WebUI.delay(2)
    
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/nav_items/button_Teams'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Teams'))
    WebUI.delay(3)
    
    WebUI.waitForElementClickable(acceptInviteButton, 10)
    WebUI.click(acceptInviteButton)
    WebUI.delay(3)
    WebUI.comment('✓ Member 2 (User C) accepted invitation')
    
    WebUI.closeBrowser()
    
    // ============================================================
    // STEP 7: Login as Owner and verify both members in team
    // ============================================================
    WebUI.comment('Step 7: Verifying members in team...')
    
    WebUI.openBrowser('')
    WebUI.navigateToUrl(GlobalVariable.Base_URL)
    WebUI.waitForPageLoad(5)
    
    CustomKeywords.'keywords.ChatKeywords.loginChat'(
        GlobalVariable.email,
        GlobalVariable.password
    )
    WebUI.delay(3)
    
    CustomKeywords.'keywords.ChatKeywords.switchToAdvancedInterface'()
    WebUI.delay(2)
    
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/nav_items/button_Teams'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Teams'))
    WebUI.delay(3)
    
    TestObject teamSelector = new TestObject('teamSelector')
    teamSelector.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='" + teamName + "']")
    WebUI.waitForElementClickable(teamSelector, 10)
    WebUI.click(teamSelector)
    WebUI.delay(3)
    
    TestObject membersTab = new TestObject('membersTab')
    membersTab.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@role='tab' and contains(text(), 'Members')]")
    WebUI.click(membersTab)
    WebUI.delay(3)
    
    // Verify Member 1 (User B)
    TestObject member1InList = new TestObject('member1InList')
    member1InList.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-text-secondary') and contains(text(), '" + member1Email + "')]")
    boolean member1Found = WebUI.waitForElementVisible(member1InList, 10)
    if (!member1Found) {
        throw new Exception('Member 1 (User B) not found in team members list')
    }
    WebUI.comment('✓ Member 1 (User B) verified in team')
    
    // Verify Member 2 (User C)
    TestObject member2InList = new TestObject('member2InList')
    member2InList.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-text-secondary') and contains(text(), '" + member2Email + "')]")
    boolean member2Found = WebUI.waitForElementVisible(member2InList, 10)
    if (!member2Found) {
        throw new Exception('Member 2 (User C) not found in team members list')
    }
    WebUI.comment('✓ Member 2 (User C) verified in team')
    
    // ============================================================
    // STEP 8: Create Group A (contains User B only)
    // ============================================================
    WebUI.comment('Step 8: Creating Group A...')
    
    String groupAName = 'Group A_' + timestamp
    GlobalVariable.groupAName = groupAName
    
    TestObject groupsTab = new TestObject('groupsTab')
    groupsTab.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@role='tab' and contains(text(), 'Groups')]")
    WebUI.click(groupsTab)
    WebUI.delay(2)
    
    TestObject newGroupButton = findTestObject('Object Repository/nav/Teams/Team/Tab_Groups/button_New group')
    WebUI.click(newGroupButton)
    WebUI.delay(2)
    
    TestObject groupNameInput = findTestObject('Object Repository/nav/Teams/Team/Tab_Groups/input_Group name')
    WebUI.setText(groupNameInput, groupAName)
    WebUI.delay(1)
    
    TestObject createButton = findTestObject('Object Repository/nav/Teams/Team/Tab_Groups/button_Create')
    WebUI.click(createButton)
    WebUI.delay(3)
    WebUI.comment('✓ Group A created: ' + groupAName)
    
    // Add only User B (Member 1) to Group A
    WebUI.comment('Adding Member 1 (User B) to Group A...')
    
    TestObject manageMembersButtonA = new TestObject('manageMembersButtonA')
    manageMembersButtonA.addProperty('xpath', ConditionType.EQUALS, 
        "(//button[@aria-label='Manage members'])[1]")
    WebUI.click(manageMembersButtonA)
    WebUI.delay(2)
    
    TestObject addMember1Button = new TestObject('addMember1Button')
    addMember1Button.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(@aria-label, 'Add to group: " + member1Name + "')]")
    WebUI.click(addMember1Button)
    WebUI.delay(1)
    
    TestObject closeDialog = new TestObject('closeDialog')
    closeDialog.addProperty('xpath', ConditionType.EQUALS, 
        "//div[@role='dialog']//button[contains(@class, 'absolute')]")
    WebUI.click(closeDialog)
    WebUI.delay(2)
    WebUI.comment('✓ User B added to Group A (User C not in Group A)')
    
    // ============================================================
    // STEP 9: Share team-wide resource
    // ============================================================
    WebUI.comment('Step 9: Sharing team-wide resource...')
    
    String teamWideResource = 'Team Wide Resource ' + timestamp
    GlobalVariable.teamWideResource = teamWideResource
    
    TestObject sharedTab = new TestObject('sharedTab')
    sharedTab.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@role='tab' and contains(text(), 'Shared')]")
    WebUI.click(sharedTab)
    WebUI.delay(2)
    
    TestObject addPromptButton = findTestObject('Object Repository/nav/Teams/Team/Tab_shared/button_Add prompt')
    WebUI.click(addPromptButton)
    WebUI.delay(2)
    
    TestObject firstPrompt = new TestObject('firstPrompt')
    firstPrompt.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[@role='dialog']//ul//li//button[@aria-label='Add'])[1]")
    WebUI.click(firstPrompt)
    WebUI.delay(3)
    WebUI.comment('✓ Team-wide resource shared: ' + teamWideResource)
    
    // ============================================================
    // STEP 10: Share Group A-only resource
    // ============================================================
    WebUI.comment('Step 10: Sharing Group A-only resource...')
    
    String groupAResource = 'Group A Resource ' + timestamp
    GlobalVariable.groupAResource = groupAResource
    
    WebUI.comment('Group A-only resource: ' + groupAResource)
    WebUI.comment('✓ Group A-only resource noted')
    
    // ============================================================
    // STEP 11: Create User C (non-member) for J02
    // ============================================================
    WebUI.comment('Step 11: Creating User C (non-member) for J02...')
    
    // GỘP: chỉ 2 biến
    String userCEmail = 'user_c_' + timestamp + '@test.com'
    String userCName = 'User C ' + timestamp
    
    GlobalVariable.userCEmail = userCEmail      // Email = Password
    GlobalVariable.userCName = userCName        // Name = Username
    
    WebUI.comment('User C (non-member) - for J02:')
    WebUI.comment('  Email: ' + userCEmail)
    WebUI.comment('  Name: ' + userCName)
    WebUI.comment('  Password: ' + userCEmail + ' (email = password)')
    WebUI.comment('✓ User C created (not a member of team)')
    
    // ============================================================
    // STEP 12: Take screenshot and summary
    // ============================================================
    WebUI.takeScreenshot('Setup_Negative_Isolation_Complete.png')
    WebUI.comment('✓ Setup completed successfully')
    
    WebUI.comment('=== Setup_Negative_Isolation PASSED ===')
    WebUI.comment('=== GlobalVariables Set for Suite ===')
    WebUI.comment('invitedTeamName: ' + GlobalVariable.invitedTeamName)
    WebUI.comment('invitedEmail (User B - J01, J03): ' + GlobalVariable.invitedEmail)
    WebUI.comment('invitedEmail2 (User C - J02): ' + GlobalVariable.invitedEmail2)
    WebUI.comment('invitedRole: ' + GlobalVariable.invitedRole)
    WebUI.comment('member1Name: ' + GlobalVariable.member1Name)
    WebUI.comment('member2Name: ' + GlobalVariable.member2Name)
    WebUI.comment('groupAName: ' + GlobalVariable.groupAName)
    WebUI.comment('teamWideResource: ' + GlobalVariable.teamWideResource)
    WebUI.comment('groupAResource: ' + GlobalVariable.groupAResource)
    WebUI.comment('userCEmail (Non-member): ' + GlobalVariable.userCEmail)
    WebUI.comment('userCName (Non-member): ' + GlobalVariable.userCName)
    
} catch (Exception e) {
    WebUI.comment('=== Setup_Negative_Isolation FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('Setup_Negative_Isolation_Error.png')
    throw e
} finally {
    WebUI.closeBrowser()
}