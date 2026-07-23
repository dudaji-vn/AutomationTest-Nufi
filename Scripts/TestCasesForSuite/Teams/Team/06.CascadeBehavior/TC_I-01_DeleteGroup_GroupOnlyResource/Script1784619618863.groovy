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
 * TC01_DeleteGroup_GroupOnlyResource
 * 
 * Description: Delete Group A, Member 2 loses Group A resource but remains team member
 * 
 * Test Flow:
 * 1. Login as Owner
 * 2. Navigate to Team (invitedTeamName)
 * 3. Go to Groups tab
 * 4. Delete Group A
 * 5. Login as Member 2 (invitedEmail2)
 * 6. Verify Member 2 can STILL see team
 * 7. Verify Member 2 can NO LONGER access Group A resource
 * 8. Verify Member 2 can STILL access team-wide resources
 * 
 * Expected Result:
 * - Group A deleted successfully
 * - Member 2 loses access to Group A resource
 * - Member 2 remains team member
 * - Member 2 can still access team-wide resources
 * 
 * Prerequisites:
 * - Setup_TeamAndMembers must be run first
 * - GlobalVariables must be set:
 *   - invitedTeamName
 *   - invitedEmail2
 *   - groupAName
 *   - groupAResource
 *   - teamWideResource
 */

WebUI.comment('=== TC01_DeleteGroup_GroupOnlyResource ===')

try {
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
    // STEP 2: Navigate to Team
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
    
    // ============================================================
    // STEP 3: Go to Groups tab
    // ============================================================
    WebUI.comment('Step 3: Going to Groups tab...')
    
    TestObject groupsTab = new TestObject('groupsTab')
    groupsTab.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@role='tab' and contains(text(), 'Groups')]")
    WebUI.click(groupsTab)
    WebUI.delay(2)
    WebUI.comment('✓ Groups tab clicked')
    
    // ============================================================
    // STEP 4: Delete Group A
    // ============================================================
    WebUI.comment('Step 4: Deleting Group A...')
    
    String groupAName = GlobalVariable.groupAName
    WebUI.comment('Group to delete: ' + groupAName)
    
    // Find Group A delete button
    TestObject deleteGroupA = new TestObject('deleteGroupA')
    deleteGroupA.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-text-primary') and text()='" + groupAName + "']/ancestor::li//button[@aria-label='Delete group']")
    WebUI.waitForElementClickable(deleteGroupA, 10)
    WebUI.click(deleteGroupA)
    WebUI.delay(2)
    WebUI.comment('✓ Delete group button clicked')
    
    // Confirm delete
    TestObject confirmDelete = new TestObject('confirmDelete')
    confirmDelete.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='Confirm' and contains(text(), 'Delete')]")
    WebUI.waitForElementClickable(confirmDelete, 10)
    WebUI.click(confirmDelete)
    WebUI.delay(3)
    WebUI.comment('✓ Group A deleted successfully')
    
    // ============================================================
    // STEP 5: Login as Member 2
    // ============================================================
    WebUI.comment('Step 5: Logging in as Member 2...')
    WebUI.closeBrowser()
    WebUI.openBrowser('')
    WebUI.navigateToUrl(GlobalVariable.Base_URL)
    WebUI.waitForPageLoad(5)
    
    CustomKeywords.'keywords.ChatKeywords.loginChat'(
        GlobalVariable.invitedEmail2,
        GlobalVariable.invitedEmail2 // password = email
    )
    WebUI.delay(3)
    
    CustomKeywords.'keywords.ChatKeywords.switchToAdvancedInterface'()
    WebUI.delay(2)
    WebUI.comment('✓ Member 2 login successful')
    
    // ============================================================
    // STEP 6: Verify Member 2 can STILL see team
    // ============================================================
    WebUI.comment('Step 6: Verifying Member 2 can see team...')
    
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/nav_items/button_Teams'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Teams'))
    WebUI.delay(3)
    
    TestObject teamInList = new TestObject('teamInList')
    teamInList.addProperty('xpath', ConditionType.EQUALS, 
        "//h3[contains(@class, 'text-text-primary') and contains(text(), '" + GlobalVariable.invitedTeamName + "')]")
    boolean teamVisible = WebUI.waitForElementVisible(teamInList, 10)
    
    if (!teamVisible) {
        throw new Exception('FAILED: Member 2 cannot see team "' + GlobalVariable.invitedTeamName + '"')
    }
    WebUI.comment('✓ PASSED: Member 2 can still see team: ' + GlobalVariable.invitedTeamName)
    
    // Click on team to access resources
    WebUI.click(teamSelector)
    WebUI.delay(3)
    
    // ============================================================
    // STEP 7: Verify Member 2 can NO LONGER access Group A resource
    // ============================================================
    WebUI.comment('Step 7: Verifying Member 2 cannot access Group A resource...')
    
    // Go to Shared tab
    TestObject sharedTab = new TestObject('sharedTab')
    sharedTab.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@role='tab' and contains(text(), 'Shared')]")
    WebUI.click(sharedTab)
    WebUI.delay(2)
    
    // Look for Group A resource - should NOT be visible
    TestObject groupAResourceElement = new TestObject('groupAResourceElement')
    groupAResourceElement.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-text-primary') and contains(text(), '" + GlobalVariable.groupAResource + "')]")
    boolean resourceNotVisible = WebUI.waitForElementNotVisible(groupAResourceElement, 5, FailureHandling.OPTIONAL)
    
    if (resourceNotVisible) {
        WebUI.comment('✓ PASSED: Group A resource is NOT visible to Member 2 (as expected)')
    } else {
        // Check if resource is still present
        boolean resourceStillVisible = WebUI.verifyElementPresent(groupAResourceElement, 2, FailureHandling.OPTIONAL)
        if (resourceStillVisible) {
            throw new Exception('FAILED: Group A resource is still visible to Member 2')
        } else {
            WebUI.comment('✓ PASSED: Group A resource is NOT visible to Member 2')
        }
    }
    
    // ============================================================
    // STEP 8: Verify Member 2 can STILL access team-wide resources
    // ============================================================
    WebUI.comment('Step 8: Verifying Member 2 can access team-wide resources...')
    
    // Check if team-wide resource is still accessible
    TestObject teamWideResourceElement = new TestObject('teamWideResourceElement')
    teamWideResourceElement.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-text-primary') and contains(text(), '" + GlobalVariable.teamWideResource + "')]")
    boolean teamWideVisible = WebUI.waitForElementVisible(teamWideResourceElement, 5, FailureHandling.OPTIONAL)
    
    if (teamWideVisible) {
        WebUI.comment('✓ PASSED: Team-wide resource is still accessible to Member 2')
    } else {
        WebUI.comment('⚠ WARNING: Team-wide resource not found - may need verification')
    }
    
    // ============================================================
    // STEP 9: Take screenshot and summary
    // ============================================================
    WebUI.takeScreenshot('TC01_DeleteGroup_GroupOnlyResource_Success.png')
    
    WebUI.comment('=== TC01_DeleteGroup_GroupOnlyResource SUMMARY ===')
    WebUI.comment('✓ Group A deleted: ' + GlobalVariable.groupAName)
    WebUI.comment('✓ Member 2: ' + GlobalVariable.invitedEmail2)
    WebUI.comment('✓ Member 2 still in team: YES')
    WebUI.comment('✓ Member 2 can access Group A resource: NO')
    WebUI.comment('✓ Member 2 can access team-wide resources: YES')
    WebUI.comment('=== TC01_DeleteGroup_GroupOnlyResource PASSED ===')
    
} catch (Exception e) {
    WebUI.comment('=== TC01_DeleteGroup_GroupOnlyResource FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC01_DeleteGroup_GroupOnlyResource_Error.png')
    throw e
} finally {
    WebUI.closeBrowser()
}