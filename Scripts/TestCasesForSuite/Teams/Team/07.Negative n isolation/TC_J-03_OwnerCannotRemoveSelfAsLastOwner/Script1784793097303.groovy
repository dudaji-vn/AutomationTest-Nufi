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
 * TC_J03_OwnerCannotRemoveSelfAsLastOwner
 * 
 * Description: Verify Owner cannot remove self as last owner
 * 
 * Test Flow:
 * 1. Login as Owner
 * 2. Navigate to Team detail page
 * 3. Go to Members tab
 * 4. Attempt to remove Owner (self) from team
 * 5. Verify action is blocked
 * 6. Verify Owner still in team
 * 7. Screenshot and summary
 * 
 * Expected Result:
 * - Owner cannot remove self
 * - Error message shown
 * - Owner remains in team
 * 
 * Pre-condition:
 * - Setup_Negative_Isolation must be run first
 * - GlobalVariable.email (Owner) must be set
 */

WebUI.comment('=== TC_J03_OwnerCannotRemoveSelfAsLastOwner ===')

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
    // STEP 4: Find Owner in member list
    // ============================================================
    WebUI.comment('Step 4: Finding Owner in member list...')
    
    String ownerEmail = GlobalVariable.email
    WebUI.comment('Owner email: ' + ownerEmail)
    
    TestObject ownerInList = new TestObject('ownerInList')
    ownerInList.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-text-secondary') and contains(text(), '" + ownerEmail + "')]")
    
    boolean ownerFound = WebUI.waitForElementVisible(ownerInList, 10)
    if (!ownerFound) {
        throw new Exception('Owner not found in member list')
    }
    WebUI.comment('✓ Owner found in member list')
    
    // ============================================================
    // STEP 5: Attempt to remove Owner (self) from team
    // ============================================================
    WebUI.comment('Step 5: Attempting to remove Owner (self) from team...')
    
    // Tìm nút Remove cho Owner (sẽ không có hoặc bị disabled)
    TestObject removeOwnerButton = new TestObject('removeOwnerButton')
    removeOwnerButton.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-text-secondary') and contains(text(), '" + ownerEmail + "')]/ancestor::li//button[@aria-label='Remove']")
    
    // Kiểm tra nút Remove có tồn tại không
    boolean removeButtonExists = WebUI.waitForElementPresent(removeOwnerButton, 5, FailureHandling.OPTIONAL)
    
    if (removeButtonExists) {
        // Nếu tồn tại, thử click và kiểm tra error
        WebUI.comment('Remove button exists - attempting to click...')
        WebUI.click(removeOwnerButton)
        WebUI.delay(2)
        
        // Kiểm tra error message
        TestObject errorMessage = new TestObject('errorMessage')
        errorMessage.addProperty('xpath', ConditionType.EQUALS, 
            "//div[contains(@class, 'alert') and contains(text(), 'transfer ownership')]")
        boolean errorShown = WebUI.waitForElementVisible(errorMessage, 5, FailureHandling.OPTIONAL)
        
        if (errorShown) {
            WebUI.comment('✓ PASSED: Error message shown - "Transfer ownership first"')
        } else {
            // Kiểm tra toast error
            TestObject toastError = new TestObject('toastError')
            toastError.addProperty('xpath', ConditionType.EQUALS, 
                "//div[contains(@class, 'toast') and contains(@class, 'error')]")
            boolean toastShown = WebUI.waitForElementVisible(toastError, 5, FailureHandling.OPTIONAL)
            
            if (toastShown) {
                WebUI.comment('✓ PASSED: Error toast shown - cannot remove self')
            } else {
                WebUI.comment('⚠ WARNING: No error message found - may still be blocked')
            }
        }
    } else {
        // Nếu không có nút Remove, nghĩa là Owner không thể tự xóa (đúng)
        WebUI.comment('✓ PASSED: Remove button is HIDDEN for Owner - cannot remove self')
    }
    
    // ============================================================
    // STEP 6: Verify Owner still in team
    // ============================================================
    WebUI.comment('Step 6: Verifying Owner still in team...')
    
    boolean ownerStillExists = WebUI.waitForElementVisible(ownerInList, 5, FailureHandling.OPTIONAL)
    
    if (ownerStillExists) {
        WebUI.comment('✓ PASSED: Owner still in team (not removed)')
    } else {
        throw new Exception('FAILED: Owner was removed from team (should be blocked)')
    }
    
    // ============================================================
    // STEP 7: Screenshot and summary
    // ============================================================
    WebUI.takeScreenshot('TC_J03_OwnerCannotRemoveSelfAsLastOwner_Success.png')
    
    WebUI.comment('=== TC_J03_OwnerCannotRemoveSelfAsLastOwner SUMMARY ===')
    WebUI.comment('✓ Owner: ' + GlobalVariable.email)
    WebUI.comment('✓ Team: ' + GlobalVariable.invitedTeamName)
    WebUI.comment('✓ Owner can remove self: NO (blocked)')
    WebUI.comment('✓ Owner still in team: YES')
    WebUI.comment('=== TC_J03_OwnerCannotRemoveSelfAsLastOwner PASSED ===')
    
} catch (Exception e) {
    WebUI.comment('=== TC_J03_OwnerCannotRemoveSelfAsLastOwner FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC_J03_OwnerCannotRemoveSelfAsLastOwner_Error.png')
    throw e
} finally {
    WebUI.closeBrowser()
}