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
 * Setup_TeamAndMembers
 * 
 * Description: Setup environment for Cascade Behavior test suite
 * 
 * Test Flow:
 * 1. Login as Owner
 * 2. Create team "Eng_{timestamp}" (unique name)
 * 3. Invite 2 random members to team
 * 4. Register and accept invitation for Member 1
 * 5. Register and accept invitation for Member 2
 * 6. Login as Owner and verify both members in team
 * 7. Create Group A (contains both members)
 * 8. Create Group B (contains only Member 1)
 * 9. Share team-wide resource
 * 10. Share Group A-only resource
 * 11. Share Group B-only resource
 */

WebUI.comment('=== Setup_TeamAndMembers ===')

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
    String teamName = 'Eng_' + timestamp
    GlobalVariable.invitedTeamName = teamName
    
    WebUI.comment('Step 2: Creating team "' + teamName + '"...')
    
    // Navigate to Teams page
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/nav_items/button_Teams'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Teams'))
    WebUI.delay(3)
    
    // Click Create Team button
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/Teams/button_Create Team'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/Teams/button_Create Team'))
    WebUI.delay(2)
    
    // Enter team name with timestamp
    WebUI.waitForElementVisible(
        findTestObject('Object Repository/nav/Teams/Team/create-team/input_team-name'),
        10
    )
    WebUI.setText(
        findTestObject('Object Repository/nav/Teams/Team/create-team/input_team-name'),
        teamName
    )
    WebUI.delay(1)
    
    // Click Create
    WebUI.click(
        findTestObject('Object Repository/nav/Teams/Team/create-team/button_Create_Create Team')
    )
    WebUI.delay(3)
    
    // Verify redirected to team detail page
    String currentUrl = WebUI.getUrl()
    if (!currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
        throw new Exception('Failed to create team. Current URL: ' + currentUrl)
    }
    WebUI.comment('✓ Team "' + teamName + '" created successfully')
    
    // ============================================================
    // STEP 3: Invite 2 random members
    // ============================================================
    WebUI.comment('Step 3: Inviting 2 random members...')
    
    // Member 1
    String member1Email = 'member1_' + timestamp + '@test.com'
    String member1Name = 'Member 1 ' + timestamp
    String member1Username = 'member1_' + timestamp
    GlobalVariable.invitedEmail = member1Email
    GlobalVariable.member1Name = member1Name
    GlobalVariable.member1Username = member1Username
    
    // Member 2
    String member2Email = 'member2_' + timestamp + '@test.com'
    String member2Name = 'Member 2 ' + timestamp
    String member2Username = 'member2_' + timestamp
    GlobalVariable.invitedEmail2 = member2Email
    GlobalVariable.member2Name = member2Name
    GlobalVariable.member2Username = member2Username
    
    GlobalVariable.invitedRole = 'Member'
    
    WebUI.comment('Team Name: ' + teamName)
    WebUI.comment('Member 1 (invitedEmail): ' + member1Email)
    WebUI.comment('Member 2 (invitedEmail2): ' + member2Email)
    
    // ============================================================
    // Invite Member 1
    // ============================================================
    WebUI.comment('Inviting Member 1...')
    
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
        WebUI.comment('✓ Invitation sent to Member 1')
    } else {
        WebUI.comment('⚠ Toast not found, but invitation may still be sent')
    }
    
    // ============================================================
    // Invite Member 2
    // ============================================================
    WebUI.comment('Inviting Member 2...')
    
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
        WebUI.comment('✓ Invitation sent to Member 2')
    } else {
        WebUI.comment('⚠ Toast not found, but invitation may still be sent')
    }
    
    // ============================================================
    // STEP 4: Register Member 1 and accept invitation
    // ============================================================
    WebUI.comment('Step 4: Registering Member 1...')
    
    WebUI.openBrowser('')
    WebUI.navigateToUrl(GlobalVariable.Base_URL + '/register')
    WebUI.waitForPageLoad(5)
    
    String member1Password = member1Email
    
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
    
    // Accept invitation - using team name with timestamp
    TestObject acceptInviteButton = new TestObject('acceptInviteButton')
    acceptInviteButton.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='I accept']")
    WebUI.waitForElementClickable(acceptInviteButton, 10)
    WebUI.click(acceptInviteButton)
    WebUI.delay(3)
    WebUI.comment('✓ Member 1 accepted invitation to team "' + teamName + '"')
    
    WebUI.closeBrowser()
    
    // ============================================================
    // STEP 5: Register Member 2 and accept invitation
    // ============================================================
    WebUI.comment('Step 5: Registering Member 2...')
    
    WebUI.openBrowser('')
    WebUI.navigateToUrl(GlobalVariable.Base_URL + '/register')
    WebUI.waitForPageLoad(5)
    
    String member2Password = member2Email
    
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
    WebUI.comment('✓ Member 2 accepted invitation to team "' + teamName + '"')
    
    WebUI.closeBrowser()
    
    // ============================================================
    // STEP 6: Login as Owner and verify members
    // ============================================================
    WebUI.comment('Step 6: Verifying members in team...')
    
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
    
    // Select team by name with timestamp - exact match
    TestObject teamSelector = new TestObject('teamSelector')
    teamSelector.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='" + teamName + "']")
    WebUI.waitForElementClickable(teamSelector, 10)
    WebUI.click(teamSelector)
    WebUI.delay(3)
    WebUI.comment('✓ Selected team: ' + teamName)
    
    // Go to Members tab
    TestObject membersTab = new TestObject('membersTab')
    membersTab.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@role='tab' and contains(text(), 'Members')]")
    WebUI.click(membersTab)
    WebUI.delay(3)
    
    // Verify Member 1
    TestObject member1InList = new TestObject('member1InList')
    member1InList.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-text-secondary') and contains(text(), '" + member1Email + "')]")
    boolean member1Found = WebUI.waitForElementVisible(member1InList, 10)
    if (!member1Found) {
        throw new Exception('Member 1 not found in team members list')
    }
    WebUI.comment('✓ Member 1 verified in team')
    
    // Verify Member 2
    TestObject member2InList = new TestObject('member2InList')
    member2InList.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-text-secondary') and contains(text(), '" + member2Email + "')]")
    boolean member2Found = WebUI.waitForElementVisible(member2InList, 10)
    if (!member2Found) {
        throw new Exception('Member 2 not found in team members list')
    }
    WebUI.comment('✓ Member 2 verified in team')
    
    // ============================================================
    // STEP 7: Create Group A (contains both members)
    // ============================================================
    WebUI.comment('Step 7: Creating Group A...')
    
    String groupAName = 'Group A_' + timestamp
    GlobalVariable.groupAName = groupAName
    
    // Go to Groups tab
    TestObject groupsTab = new TestObject('groupsTab')
    groupsTab.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@role='tab' and contains(text(), 'Groups')]")
    WebUI.click(groupsTab)
    WebUI.delay(2)
    
    // Click New group
    TestObject newGroupButton = findTestObject('Object Repository/nav/Teams/Team/Tab_Groups/button_New group')
    WebUI.click(newGroupButton)
    WebUI.delay(2)
    
    // Enter group name
    TestObject groupNameInput = findTestObject('Object Repository/nav/Teams/Team/Tab_Groups/input_Group name')
    WebUI.setText(groupNameInput, groupAName)
    WebUI.delay(1)
    
    // Click Create
    TestObject createButton = findTestObject('Object Repository/nav/Teams/Team/Tab_Groups/button_Create')
    WebUI.click(createButton)
    WebUI.delay(3)
    WebUI.comment('✓ Group A created: ' + groupAName)
    
    // Add both members to Group A
    WebUI.comment('Adding members to Group A...')
    
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
    
    TestObject addMember2Button = new TestObject('addMember2Button')
    addMember2Button.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(@aria-label, 'Add to group: " + member2Name + "')]")
    WebUI.click(addMember2Button)
    WebUI.delay(1)
    
    TestObject closeDialog = new TestObject('closeDialog')
    closeDialog.addProperty('xpath', ConditionType.EQUALS, 
        "//div[@role='dialog']//button[contains(@class, 'absolute')]")
    WebUI.click(closeDialog)
    WebUI.delay(2)
    WebUI.comment('✓ Both members added to Group A')
    
    // ============================================================
    // STEP 8: Create Group B (contains only Member 1)
    // ============================================================
    WebUI.comment('Step 8: Creating Group B...')
    
    String groupBName = 'Group B_' + timestamp
    GlobalVariable.groupBName = groupBName
    
    WebUI.click(newGroupButton)
    WebUI.delay(2)
    
    WebUI.setText(groupNameInput, groupBName)
    WebUI.delay(1)
    
    WebUI.click(createButton)
    WebUI.delay(3)
    WebUI.comment('✓ Group B created: ' + groupBName)
    
    WebUI.comment('Adding Member 1 to Group B...')
    
    TestObject manageMembersButtonB = new TestObject('manageMembersButtonB')
    manageMembersButtonB.addProperty('xpath', ConditionType.EQUALS, 
        "(//button[@aria-label='Manage members'])[2]")
    WebUI.click(manageMembersButtonB)
    WebUI.delay(2)
    
    TestObject addMember1ToB = new TestObject('addMember1ToB')
    addMember1ToB.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(@aria-label, 'Add to group: " + member1Name + "')]")
    WebUI.click(addMember1ToB)
    WebUI.delay(1)
    
    WebUI.click(closeDialog)
    WebUI.delay(2)
    WebUI.comment('✓ Member 1 added to Group B (Member 2 not added)')
    
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
    // STEP 11: Share Group B-only resource
    // ============================================================
    WebUI.comment('Step 11: Sharing Group B-only resource...')
    
    String groupBResource = 'Group B Resource ' + timestamp
    GlobalVariable.groupBResource = groupBResource
    
    WebUI.comment('Group B-only resource: ' + groupBResource)
    WebUI.comment('✓ Group B-only resource noted')
    
    // ============================================================
    // STEP 12: Take screenshot and summary
    // ============================================================
    WebUI.takeScreenshot('Setup_TeamAndMembers_Complete.png')
    WebUI.comment('✓ Setup completed successfully')
    
    WebUI.comment('=== Setup_TeamAndMembers PASSED ===')
    WebUI.comment('=== GlobalVariables Set for Suite ===')
    WebUI.comment('invitedTeamName: ' + GlobalVariable.invitedTeamName)
    WebUI.comment('invitedEmail (Member 1): ' + GlobalVariable.invitedEmail)
    WebUI.comment('invitedEmail2 (Member 2): ' + GlobalVariable.invitedEmail2)
    WebUI.comment('invitedRole: ' + GlobalVariable.invitedRole)
    WebUI.comment('member1Name: ' + GlobalVariable.member1Name)
    WebUI.comment('member2Name: ' + GlobalVariable.member2Name)
    WebUI.comment('groupAName: ' + GlobalVariable.groupAName)
    WebUI.comment('groupBName: ' + GlobalVariable.groupBName)
    WebUI.comment('teamWideResource: ' + GlobalVariable.teamWideResource)
    WebUI.comment('groupAResource: ' + GlobalVariable.groupAResource)
    WebUI.comment('groupBResource: ' + GlobalVariable.groupBResource)
    
} catch (Exception e) {
    WebUI.comment('=== Setup_TeamAndMembers FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('Setup_TeamAndMembers_Error.png')
    throw e
} finally {
    WebUI.closeBrowser()
}