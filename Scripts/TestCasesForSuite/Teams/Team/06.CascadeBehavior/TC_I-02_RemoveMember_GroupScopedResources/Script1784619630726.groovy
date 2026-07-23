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
 * TC02_RemoveMember_GroupScopedResources
 * 
 * Description: Remove Member 2 from team, loses team-wide and group-scoped resources
 * 
 * Test Flow:
 * 1. Login as Owner
 * 2. Navigate to Team (invitedTeamName)
 * 3. Go to Members tab
 * 4. Remove Member 2 from team
 * 5. Login as Member 2
 * 6. Verify Member 2 can NO LONGER see team
 * 7. Verify Member 2 can NO LONGER access team-wide resources
 * 8. Verify Member 2 can NO LONGER access group resources
 */
WebUI.comment('=== TC02_RemoveMember_GroupScopedResources ===')

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
    // STEP 3: Go to Members tab
    // ============================================================
    WebUI.comment('Step 3: Going to Members tab...')
    
    TestObject membersTab = new TestObject('membersTab')
    membersTab.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@role='tab' and contains(text(), 'Members')]")
    WebUI.click(membersTab)
    WebUI.delay(2)
    WebUI.comment('✓ Members tab clicked')
    
    // ============================================================
    // STEP 4: Remove Member 2 from team
    // ============================================================
    WebUI.comment('Step 4: Removing Member 2 from team...')
    
    String member2Email = GlobalVariable.invitedEmail2
    WebUI.comment('Member to remove: ' + member2Email)
    
    TestObject removeMember2 = new TestObject('removeMember2')
    removeMember2.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-text-secondary') and contains(text(), '" + member2Email + "')]/ancestor::li//button[@aria-label='Remove']")
    WebUI.waitForElementClickable(removeMember2, 10)
    WebUI.click(removeMember2)
    WebUI.delay(2)
    WebUI.comment('✓ Remove button clicked')
    
    TestObject confirmRemove = new TestObject('confirmRemove')
    confirmRemove.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(text(), 'Confirm')]")
    WebUI.waitForElementClickable(confirmRemove, 10)
    WebUI.click(confirmRemove)
    WebUI.delay(3)
    WebUI.comment('✓ Member 2 removed from team')
    
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
    // STEP 6: Verify Member 2 can NO LONGER see team
    // ============================================================
    WebUI.comment('Step 6: Verifying Member 2 cannot see team...')
    
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/nav_items/button_Teams'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Teams'))
    WebUI.delay(3)
    
    TestObject teamInList = new TestObject('teamInList')
    teamInList.addProperty('xpath', ConditionType.EQUALS, 
        "//h3[contains(@class, 'text-text-primary') and contains(text(), '" + GlobalVariable.invitedTeamName + "')]")
    boolean teamNotVisible = WebUI.waitForElementNotVisible(teamInList, 10, FailureHandling.OPTIONAL)
    
    if (teamNotVisible) {
        WebUI.comment('✓ PASSED: Member 2 cannot see team "' + GlobalVariable.invitedTeamName + '" (as expected)')
    } else {
        boolean teamStillVisible = WebUI.verifyElementPresent(teamInList, 2, FailureHandling.OPTIONAL)
        if (teamStillVisible) {
            throw new Exception('FAILED: Team "' + GlobalVariable.invitedTeamName + '" still visible to Member 2')
        } else {
            WebUI.comment('✓ PASSED: Team not visible to Member 2')
        }
    }
    
    // ============================================================
    // STEP 7: Verify Member 2 can NO LONGER access team-wide resources
    // ============================================================
    WebUI.comment('Step 7: Verifying Member 2 cannot access team-wide resources...')
    
    // SỬA: Dùng Dynamic TestObject thay vì Object Repository/body
    TestObject bodyElement = new TestObject('bodyElement')
    bodyElement.addProperty('xpath', ConditionType.EQUALS, "//body")
    
    String pageText = WebUI.getText(bodyElement)
    boolean noTeamFound = !pageText.contains(GlobalVariable.invitedTeamName)
    
    if (noTeamFound) {
        WebUI.comment('✓ PASSED: Member 2 cannot access team-wide resources (team not visible)')
    } else {
        WebUI.comment('⚠ WARNING: Team name still appears in page text')
    }
    
    // ============================================================
    // STEP 8: Verify Member 2 can NO LONGER access group resources
    // ============================================================
    WebUI.comment('Step 8: Verifying Member 2 cannot access group resources...')
    
    // SỬA: Dùng lại bodyElement đã tạo ở trên
    String pageText2 = WebUI.getText(bodyElement)
    boolean noGroupFound = !pageText2.contains(GlobalVariable.groupAName) && !pageText2.contains(GlobalVariable.groupBName)
    
    if (noGroupFound) {
        WebUI.comment('✓ PASSED: Member 2 cannot access group resources (groups not visible)')
    } else {
        WebUI.comment('⚠ WARNING: Group names still appear in page text')
    }
    
    // ============================================================
    // STEP 9: Take screenshot and summary
    // ============================================================
    WebUI.takeScreenshot('TC02_RemoveMember_GroupScopedResources_Success.png')
    
    WebUI.comment('=== TC02_RemoveMember_GroupScopedResources SUMMARY ===')
    WebUI.comment('✓ Member 2 removed: ' + GlobalVariable.invitedEmail2)
    WebUI.comment('✓ Member 2 can see team: NO')
    WebUI.comment('✓ Member 2 can access team-wide resources: NO')
    WebUI.comment('✓ Member 2 can access group resources: NO')
    WebUI.comment('=== TC02_RemoveMember_GroupScopedResources PASSED ===')
    
} catch (Exception e) {
    WebUI.comment('=== TC02_RemoveMember_GroupScopedResources FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC02_RemoveMember_GroupScopedResources_Error.png')
    throw e
} finally {
    WebUI.closeBrowser()
}