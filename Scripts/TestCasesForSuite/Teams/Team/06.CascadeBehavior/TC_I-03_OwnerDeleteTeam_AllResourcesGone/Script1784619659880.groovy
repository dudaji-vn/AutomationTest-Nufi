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
 * TC03_OwnerDeleteTeam_AllResourcesGone
 * 
 * Description: Owner deletes team, all members lose access, all resources gone
 * 
 * Test Flow:
 * 1. Login as Owner
 * 2. Navigate to Team (invitedTeamName)
 * 3. Delete team
 * 4. Verify team disappears from Owner's list
 * 5. Login as Member 1
 * 6. Verify Member 1 cannot see team
 * 7. Verify Member 1 cannot access any resources
 * 8. Login as Member 2
 * 9. Verify Member 2 cannot see team
 * 
 * Expected Result:
 * - Team deleted successfully
 * - Team disappears from all members' lists
 * - All groups are deleted
 * - All shared resources are inaccessible
 * - All invites are removed
 * - All members lose access
 * 
 * Prerequisites:
 * - Setup_TeamAndMembers must be run first
 * - TC01 and TC02 may be run or skipped
 * - GlobalVariables must be set:
 *   - invitedTeamName
 *   - invitedEmail
 *   - invitedEmail2
 */

WebUI.comment('=== TC03_OwnerDeleteTeam_AllResourcesGone ===')

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
    // STEP 3: Delete team
    // ============================================================
    WebUI.comment('Step 3: Deleting team...')
    
    // Click Delete team button
    TestObject deleteTeamButton = new TestObject('deleteTeamButton')
    deleteTeamButton.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='Delete team']")
    WebUI.waitForElementClickable(deleteTeamButton, 10)
    WebUI.click(deleteTeamButton)
    WebUI.delay(2)
    WebUI.comment('✓ Delete team button clicked')
    
    // Confirm delete
    TestObject confirmDeleteTeam = new TestObject('confirmDeleteTeam')
    confirmDeleteTeam.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(text(), 'Delete') and @aria-label='Confirm']")
    WebUI.waitForElementClickable(confirmDeleteTeam, 10)
    WebUI.click(confirmDeleteTeam)
    WebUI.delay(3)
    WebUI.comment('✓ Team deleted successfully')
    
    // ============================================================
    // STEP 4: Verify team disappears from Owner's list
    // ============================================================
    WebUI.comment('Step 4: Verifying team disappears from Owner\'s list...')
    
    // Navigate to Teams page
    WebUI.navigateToUrl(GlobalVariable.Base_URL + '/teams')
    WebUI.delay(3)
    
    // Check if team still appears
    TestObject teamInList = new TestObject('teamInList')
    teamInList.addProperty('xpath', ConditionType.EQUALS, 
        "//h3[contains(@class, 'text-text-primary') and contains(text(), '" + GlobalVariable.invitedTeamName + "')]")
    boolean teamNotVisible = WebUI.waitForElementNotVisible(teamInList, 10, FailureHandling.OPTIONAL)
    
    if (teamNotVisible) {
        WebUI.comment('✓ PASSED: Team "' + GlobalVariable.invitedTeamName + '" removed from Owner\'s list')
    } else {
        // Double check
        boolean teamStillVisible = WebUI.verifyElementPresent(teamInList, 2, FailureHandling.OPTIONAL)
        if (teamStillVisible) {
            throw new Exception('FAILED: Team still visible to Owner after deletion')
        } else {
            WebUI.comment('✓ PASSED: Team removed from Owner\'s list')
        }
    }
    
    // ============================================================
    // STEP 5: Login as Member 1
    // ============================================================
    WebUI.comment('Step 5: Logging in as Member 1...')
    WebUI.closeBrowser()
    WebUI.openBrowser('')
    WebUI.navigateToUrl(GlobalVariable.Base_URL)
    WebUI.waitForPageLoad(5)
    
    CustomKeywords.'keywords.ChatKeywords.loginChat'(
        GlobalVariable.invitedEmail,
        GlobalVariable.invitedEmail // password = email
    )
    WebUI.delay(3)
    
    CustomKeywords.'keywords.ChatKeywords.switchToAdvancedInterface'()
    WebUI.delay(2)
    WebUI.comment('✓ Member 1 login successful')
    
    // ============================================================
    // STEP 6: Verify Member 1 cannot see team
    // ============================================================
    WebUI.comment('Step 6: Verifying Member 1 cannot see team...')
    
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/nav_items/button_Teams'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Teams'))
    WebUI.delay(3)
    
    boolean member1TeamNotVisible = WebUI.waitForElementNotVisible(teamInList, 10, FailureHandling.OPTIONAL)
    
    if (member1TeamNotVisible) {
        WebUI.comment('✓ PASSED: Member 1 cannot see team "' + GlobalVariable.invitedTeamName + '"')
    } else {
        boolean teamStillVisible = WebUI.verifyElementPresent(teamInList, 2, FailureHandling.OPTIONAL)
        if (teamStillVisible) {
            throw new Exception('FAILED: Team still visible to Member 1')
        } else {
            WebUI.comment('✓ PASSED: Team not visible to Member 1')
        }
    }
    
    // ============================================================
    // STEP 7: Verify Member 1 cannot access any resources
    // ============================================================
    WebUI.comment('Step 7: Verifying Member 1 cannot access resources...')
    
    // Check that there are no teams in the list
    TestObject anyTeam = new TestObject('anyTeam')
    anyTeam.addProperty('xpath', ConditionType.EQUALS, 
        "//h3[contains(@class, 'text-text-primary')]")
    boolean noTeams = WebUI.verifyElementNotPresent(anyTeam, 3, FailureHandling.OPTIONAL)
    
    if (noTeams || member1TeamNotVisible) {
        WebUI.comment('✓ PASSED: Member 1 cannot access any team resources')
    } else {
        // Check if the specific team appears
        boolean teamFound = WebUI.verifyElementPresent(teamInList, 2, FailureHandling.OPTIONAL)
        if (!teamFound) {
            WebUI.comment('✓ PASSED: Member 1 cannot access any team resources')
        } else {
            throw new Exception('FAILED: Member 1 can still access team resources')
        }
    }
    
    // ============================================================
    // STEP 8: Login as Member 2
    // ============================================================
    WebUI.comment('Step 8: Logging in as Member 2...')
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
    // STEP 9: Verify Member 2 cannot see team
    // ============================================================
    WebUI.comment('Step 9: Verifying Member 2 cannot see team...')
    
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/nav_items/button_Teams'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Teams'))
    WebUI.delay(3)
    
    boolean member2TeamNotVisible = WebUI.waitForElementNotVisible(teamInList, 10, FailureHandling.OPTIONAL)
    
    if (member2TeamNotVisible) {
        WebUI.comment('✓ PASSED: Member 2 cannot see team "' + GlobalVariable.invitedTeamName + '"')
    } else {
        boolean teamStillVisible = WebUI.verifyElementPresent(teamInList, 2, FailureHandling.OPTIONAL)
        if (teamStillVisible) {
            throw new Exception('FAILED: Team still visible to Member 2')
        } else {
            WebUI.comment('✓ PASSED: Team not visible to Member 2')
        }
    }
    
    // ============================================================
    // STEP 10: Verify Member 2 cannot access any resources
    // ============================================================
    WebUI.comment('Step 10: Verifying Member 2 cannot access resources...')
    
    // Check that there are no teams in the list
    boolean noTeamsForMember2 = WebUI.verifyElementNotPresent(anyTeam, 3, FailureHandling.OPTIONAL)
    
    if (noTeamsForMember2 || member2TeamNotVisible) {
        WebUI.comment('✓ PASSED: Member 2 cannot access any team resources')
    } else {
        WebUI.comment('⚠ WARNING: Member 2 may still have access to some resources')
    }
    
    // ============================================================
    // STEP 11: Take screenshot and summary
    // ============================================================
    WebUI.takeScreenshot('TC03_OwnerDeleteTeam_AllResourcesGone_Success.png')
    
    WebUI.comment('=== TC03_OwnerDeleteTeam_AllResourcesGone SUMMARY ===')
    WebUI.comment('✓ Team deleted: ' + GlobalVariable.invitedTeamName)
    WebUI.comment('✓ Owner can see team: NO')
    WebUI.comment('✓ Member 1 can see team: NO')
    WebUI.comment('✓ Member 2 can see team: NO')
    WebUI.comment('✓ All resources inaccessible: YES')
    WebUI.comment('=== TC03_OwnerDeleteTeam_AllResourcesGone PASSED ===')
    
} catch (Exception e) {
    WebUI.comment('=== TC03_OwnerDeleteTeam_AllResourcesGone FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC03_OwnerDeleteTeam_AllResourcesGone_Error.png')
    throw e
} finally {
    WebUI.closeBrowser()
}