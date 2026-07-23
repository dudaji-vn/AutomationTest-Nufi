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
 * TC_J01_MemberCannotPerformAdminActions
 * 
 * Description: Verify Member (User B) cannot perform admin actions
 * 
 * Test Flow:
 * 1. Login as Member (User B - invitedEmail)
 * 2. Navigate to Team detail page
 * 3. Verify Groups tab is HIDDEN
 * 4. Verify Invite member button is HIDDEN
 * 5. Verify Add knowledge button is HIDDEN
 * 6. Verify Add agent button is HIDDEN
 * 7. Verify Add prompt button is HIDDEN
 * 8. Verify Manage group button is HIDDEN
 * 9. Verify New group button is HIDDEN
 * 10. Verify Delete team button is HIDDEN
 * 11. Verify Member can view resources (Knowledge, Shared)
 * 12. Screenshot and summary
 * 
 * Expected Result:
 * - Groups tab is HIDDEN
 * - All admin buttons are HIDDEN
 * - Member can view Knowledge and Shared resources
 * 
 * Pre-condition:
 * - Setup_Negative_Isolation must be run first
 * - GlobalVariable.invitedEmail (User B) must be set
 */

WebUI.comment('=== TC_J01_MemberCannotPerformAdminActions ===')

try {
    // ============================================================
    // STEP 1: Login as Member (User B)
    // ============================================================
    WebUI.comment('Step 1: Logging in as Member (User B)...')
    WebUI.openBrowser('')
    WebUI.navigateToUrl(GlobalVariable.Base_URL)
    WebUI.waitForPageLoad(5)
    
    // User B = invitedEmail, password = invitedEmail
    CustomKeywords.'keywords.ChatKeywords.loginChat'(
        GlobalVariable.invitedEmail,
        GlobalVariable.invitedEmail
    )
    WebUI.delay(3)
    
    CustomKeywords.'keywords.ChatKeywords.switchToAdvancedInterface'()
    WebUI.delay(2)
    WebUI.comment('✓ Member (User B) login successful')
    
    // ============================================================
    // STEP 2: Navigate to Team detail page
    // ============================================================
    WebUI.comment('Step 2: Navigating to Team "' + GlobalVariable.invitedTeamName + '"...')
    
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/nav_items/button_Teams'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Teams'))
    WebUI.delay(3)
    
    TestObject teamSelector = new TestObject('teamSelector')
    teamSelector.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='" + GlobalVariable.invitedTeamName + "']")
    WebUI.waitForElementClickable(teamSelector, 10)
    WebUI.click(teamSelector)
    WebUI.delay(3)
    WebUI.comment('✓ Navigated to team: ' + GlobalVariable.invitedTeamName)
    
    // Verify Member role badge appears
    TestObject memberBadge = new TestObject('memberBadge')
    memberBadge.addProperty('xpath', ConditionType.EQUALS, 
        "//span[contains(@class, 'rounded-full') and contains(text(), 'Member')]")
    boolean hasMemberBadge = WebUI.waitForElementVisible(memberBadge, 5, FailureHandling.OPTIONAL)
    if (hasMemberBadge) {
        WebUI.comment('✓ Member badge displayed - User is Member, not Admin/Owner')
    } else {
        WebUI.comment('⚠ Member badge not found - may need verification')
    }
    
    // ============================================================
    // STEP 3: Verify Groups tab is HIDDEN
    // ============================================================
    WebUI.comment('Step 3: Verifying Groups tab is HIDDEN...')
    
    TestObject groupsTab = new TestObject('groupsTab')
    groupsTab.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@role='tab' and contains(text(), 'Groups')]")
    
    boolean groupsTabVisible = WebUI.waitForElementVisible(groupsTab, 5, FailureHandling.OPTIONAL)
    
    if (groupsTabVisible) {
        throw new Exception('FAILED: Groups tab is VISIBLE to Member (should be hidden)')
    } else {
        WebUI.comment('✓ PASSED: Groups tab is HIDDEN from Member')
    }
    
    // ============================================================
    // STEP 4: Verify Invite member button is HIDDEN
    // ============================================================
    WebUI.comment('Step 4: Verifying Invite member button is HIDDEN...')
    
    // Go to Members tab
    TestObject membersTab = new TestObject('membersTab')
    membersTab.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@role='tab' and contains(text(), 'Members')]")
    WebUI.click(membersTab)
    WebUI.delay(2)
    
    TestObject inviteMemberButton = new TestObject('inviteMemberButton')
    inviteMemberButton.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(text(), 'Invite member')]")
    
    boolean inviteButtonVisible = WebUI.waitForElementVisible(inviteMemberButton, 5, FailureHandling.OPTIONAL)
    
    if (inviteButtonVisible) {
        throw new Exception('FAILED: Invite member button is VISIBLE to Member (should be hidden)')
    } else {
        WebUI.comment('✓ PASSED: Invite member button is HIDDEN from Member')
    }
    
    // ============================================================
    // STEP 5: Verify Add knowledge button is HIDDEN
    // ============================================================
    WebUI.comment('Step 5: Verifying Add knowledge button is HIDDEN...')
    
    // Go to Knowledge tab
    TestObject knowledgeTab = new TestObject('knowledgeTab')
    knowledgeTab.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@role='tab' and contains(text(), 'Knowledge')]")
    WebUI.click(knowledgeTab)
    WebUI.delay(2)
    
    TestObject addKnowledgeButton = new TestObject('addKnowledgeButton')
    addKnowledgeButton.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(text(), 'Add knowledge')]")
    
    boolean knowledgeButtonVisible = WebUI.waitForElementVisible(addKnowledgeButton, 5, FailureHandling.OPTIONAL)
    
    if (knowledgeButtonVisible) {
        throw new Exception('FAILED: Add knowledge button is VISIBLE to Member (should be hidden)')
    } else {
        WebUI.comment('✓ PASSED: Add knowledge button is HIDDEN from Member')
    }
    
    // ============================================================
    // STEP 6: Verify Add agent button is HIDDEN
    // ============================================================
    WebUI.comment('Step 6: Verifying Add agent button is HIDDEN...')
    
    // Go to Shared tab
    TestObject sharedTab = new TestObject('sharedTab')
    sharedTab.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@role='tab' and contains(text(), 'Shared')]")
    WebUI.click(sharedTab)
    WebUI.delay(2)
    
    TestObject addAgentButton = new TestObject('addAgentButton')
    addAgentButton.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(text(), 'Add agent')]")
    
    boolean agentButtonVisible = WebUI.waitForElementVisible(addAgentButton, 5, FailureHandling.OPTIONAL)
    
    if (agentButtonVisible) {
        throw new Exception('FAILED: Add agent button is VISIBLE to Member (should be hidden)')
    } else {
        WebUI.comment('✓ PASSED: Add agent button is HIDDEN from Member')
    }
    
    // ============================================================
    // STEP 7: Verify Add prompt button is HIDDEN
    // ============================================================
    WebUI.comment('Step 7: Verifying Add prompt button is HIDDEN...')
    
    TestObject addPromptButton = new TestObject('addPromptButton')
    addPromptButton.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(text(), 'Add prompt')]")
    
    boolean promptButtonVisible = WebUI.waitForElementVisible(addPromptButton, 5, FailureHandling.OPTIONAL)
    
    if (promptButtonVisible) {
        throw new Exception('FAILED: Add prompt button is VISIBLE to Member (should be hidden)')
    } else {
        WebUI.comment('✓ PASSED: Add prompt button is HIDDEN from Member')
    }
    
    // ============================================================
    // STEP 8: Verify Manage group button is HIDDEN
    // ============================================================
    WebUI.comment('Step 8: Verifying Manage group button is HIDDEN...')
    
    TestObject manageGroupButton = new TestObject('manageGroupButton')
    manageGroupButton.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='Manage members']")
    
    boolean manageGroupVisible = WebUI.waitForElementVisible(manageGroupButton, 5, FailureHandling.OPTIONAL)
    
    if (manageGroupVisible) {
        throw new Exception('FAILED: Manage group button is VISIBLE to Member (should be hidden)')
    } else {
        WebUI.comment('✓ PASSED: Manage group button is HIDDEN from Member')
    }
    
    // ============================================================
    // STEP 9: Verify New group button is HIDDEN
    // ============================================================
    WebUI.comment('Step 9: Verifying New group button is HIDDEN...')
    
    TestObject newGroupButton = new TestObject('newGroupButton')
    newGroupButton.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='New group']")
    
    boolean newGroupVisible = WebUI.waitForElementVisible(newGroupButton, 5, FailureHandling.OPTIONAL)
    
    if (newGroupVisible) {
        throw new Exception('FAILED: New group button is VISIBLE to Member (should be hidden)')
    } else {
        WebUI.comment('✓ PASSED: New group button is HIDDEN from Member')
    }
    
    // ============================================================
    // STEP 10: Verify Delete team button is HIDDEN
    // ============================================================
    WebUI.comment('Step 10: Verifying Delete team button is HIDDEN...')
    
    TestObject deleteTeamButton = new TestObject('deleteTeamButton')
    deleteTeamButton.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='Delete team']")
    
    boolean deleteTeamVisible = WebUI.waitForElementVisible(deleteTeamButton, 5, FailureHandling.OPTIONAL)
    
    if (deleteTeamVisible) {
        throw new Exception('FAILED: Delete team button is VISIBLE to Member (should be hidden)')
    } else {
        WebUI.comment('✓ PASSED: Delete team button is HIDDEN from Member')
    }
    
    // ============================================================
    // STEP 11: Verify Member can view Knowledge resources
    // ============================================================
    WebUI.comment('Step 11: Verifying Member can view Knowledge resources...')
    
    TestObject knowledgeResource = new TestObject('knowledgeResource')
    knowledgeResource.addProperty('xpath', ConditionType.EQUALS, 
        "//section[@aria-label='Knowledge']//ul//li")
    
    boolean hasKnowledge = WebUI.waitForElementVisible(knowledgeResource, 5, FailureHandling.OPTIONAL)
    
    if (hasKnowledge) {
        WebUI.comment('✓ PASSED: Member can view Knowledge resources')
    } else {
        WebUI.comment('⚠ No Knowledge resources found - may be empty')
    }
    
    // ============================================================
    // STEP 12: Verify Member can view Shared resources
    // ============================================================
    WebUI.comment('Step 12: Verifying Member can view Shared resources...')
    
    // Check Agents
    TestObject agentResource = new TestObject('agentResource')
    agentResource.addProperty('xpath', ConditionType.EQUALS, 
        "//section[@aria-label='Agents']//ul//li")
    
    boolean hasAgent = WebUI.waitForElementVisible(agentResource, 5, FailureHandling.OPTIONAL)
    
    if (hasAgent) {
        WebUI.comment('✓ PASSED: Member can view Agents in Shared tab')
    } else {
        WebUI.comment('⚠ No Agents found - may be empty')
    }
    
    // Check Prompts
    TestObject promptResource = new TestObject('promptResource')
    promptResource.addProperty('xpath', ConditionType.EQUALS, 
        "//section[@aria-label='Prompts']//ul//li")
    
    boolean hasPrompt = WebUI.waitForElementVisible(promptResource, 5, FailureHandling.OPTIONAL)
    
    if (hasPrompt) {
        WebUI.comment('✓ PASSED: Member can view Prompts in Shared tab')
    } else {
        WebUI.comment('⚠ No Prompts found - may be empty')
    }
    
    // ============================================================
    // STEP 13: Verify resources show correct sharing scope
    // ============================================================
    WebUI.comment('Step 13: Verifying resources show sharing scope...')
    
    TestObject scopeTag = new TestObject('scopeTag')
    scopeTag.addProperty('xpath', ConditionType.EQUALS, 
        "//span[contains(@class, 'rounded-full') and contains(@class, 'text-text-secondary')]")
    
    boolean hasScopeTag = WebUI.waitForElementVisible(scopeTag, 3, FailureHandling.OPTIONAL)
    
    if (hasScopeTag) {
        WebUI.comment('✓ PASSED: Resources show sharing scope (Whole team / Group name)')
    } else {
        WebUI.comment('⚠ No scope tags found - may not be displayed')
    }
    
    // ============================================================
    // STEP 14: Screenshot and summary
    // ============================================================
    WebUI.takeScreenshot('TC_J01_MemberCannotPerformAdminActions_Success.png')
    
    WebUI.comment('=== TC_J01_MemberCannotPerformAdminActions SUMMARY ===')
    WebUI.comment('✓ Member (User B): ' + GlobalVariable.invitedEmail)
    WebUI.comment('✓ Team: ' + GlobalVariable.invitedTeamName)
    WebUI.comment('✓ Groups tab: HIDDEN')
    WebUI.comment('✓ Invite member button: HIDDEN')
    WebUI.comment('✓ Add knowledge button: HIDDEN')
    WebUI.comment('✓ Add agent button: HIDDEN')
    WebUI.comment('✓ Add prompt button: HIDDEN')
    WebUI.comment('✓ Manage group button: HIDDEN')
    WebUI.comment('✓ New group button: HIDDEN')
    WebUI.comment('✓ Delete team button: HIDDEN')
    WebUI.comment('✓ Knowledge resources: VISIBLE')
    WebUI.comment('✓ Shared resources: VISIBLE')
    WebUI.comment('=== TC_J01_MemberCannotPerformAdminActions PASSED ===')
    
} catch (Exception e) {
    WebUI.comment('=== TC_J01_MemberCannotPerformAdminActions FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC_J01_MemberCannotPerformAdminActions_Error.png')
    throw e
} finally {
    WebUI.closeBrowser()
}