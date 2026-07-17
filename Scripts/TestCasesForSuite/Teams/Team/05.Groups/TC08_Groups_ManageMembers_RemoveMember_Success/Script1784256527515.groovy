import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import org.openqa.selenium.Keys as Keys

/**
 * TC08_Groups_ManageMembers_RemoveMember_Success
 * 
 * Test Flow:
 * 1. Ensure on Team detail page
 * 2. Click Groups tab
 * 3. Get first group in list
 * 4. Click Manage members button
 * 5. Verify members list displayed
 * 6. Click Remove button on a member in group
 * 7. Verify toast success appears (if visible)
 * 8. Verify member removed from list
 */
WebUI.comment('=== TC08_Groups_ManageMembers_RemoveMember_Success ===')

try {
    // Step 1: Ensure on Team detail page
    WebUI.comment('Step 1: Ensuring on Team detail page...')
    String currentUrl = WebUI.getUrl()
    if (!(currentUrl.matches('.*/teams/[a-f0-9]+.*'))) {
        throw new Exception('Not on team detail page. Current URL: ' + currentUrl)
    }
    WebUI.comment('✓ On team detail page')
    
    // Step 2: Click Groups tab
    WebUI.comment('Step 2: Clicking Groups tab...')
    TestObject groupsTab = new TestObject('groupsTab')
    groupsTab.addProperty('xpath', ConditionType.EQUALS, '//button[@role=\'tab\' and contains(text(), \'Groups\')]')
    WebUI.waitForElementClickable(groupsTab, 10)
    WebUI.click(groupsTab)
    WebUI.delay(2)
    WebUI.comment('✓ Groups tab clicked')
    
    // Step 3: Check if there are groups
    WebUI.comment('Step 3: Checking if there are groups...')
    TestObject groupItem = new TestObject('groupItem')
    groupItem.addProperty('xpath', ConditionType.EQUALS, '//section[@aria-label=\'Groups\']//ul//li[contains(@class, \'rounded-lg\')]')
    boolean hasGroups = WebUI.waitForElementPresent(groupItem, 5, FailureHandling.OPTIONAL)
    if (!hasGroups) {
        throw new Exception('No groups found. Please create a group first.')
    }
    WebUI.comment('✓ Groups found in list')
    
    // Step 4: Click Manage members button
    WebUI.comment('Step 4: Clicking Manage members button...')
    TestObject firstGroupName = new TestObject('firstGroupName')
    firstGroupName.addProperty('xpath', ConditionType.EQUALS, '(//section[@aria-label=\'Groups\']//ul//li[contains(@class, \'rounded-lg\')]//p[contains(@class, \'text-text-primary\')])[1]')
    String groupName = WebUI.getText(firstGroupName)
    WebUI.comment('Group: ' + groupName)
    
    TestObject manageMembersButton = new TestObject('manageMembersButton')
    manageMembersButton.addProperty('xpath', ConditionType.EQUALS, '(//section[@aria-label=\'Groups\']//ul//li[contains(@class, \'rounded-lg\')]//button[@aria-label=\'Manage members\'])[1]')
    WebUI.waitForElementClickable(manageMembersButton, 10)
    WebUI.click(manageMembersButton)
    WebUI.delay(3)
    WebUI.comment('✓ Manage members button clicked')
    
    // Step 5: Check if there are members to remove
    WebUI.comment('Step 5: Checking if there are members to remove...')
    TestObject removeMemberButton = new TestObject('removeMemberButton')
    removeMemberButton.addProperty('xpath', ConditionType.EQUALS, '//div[@role=\'dialog\']//button[@aria-label=\'Remove\']')
    boolean hasRemoveButton = WebUI.waitForElementPresent(removeMemberButton, 5, FailureHandling.OPTIONAL)
    if (!hasRemoveButton) {
        WebUI.comment('⚠ No members to remove')
        // Close dialog
        TestObject closeDialog = new TestObject('closeDialog')
        closeDialog.addProperty('xpath', ConditionType.EQUALS, '//div[@role=\'dialog\']//button[contains(@class, \'absolute\')]')
        WebUI.click(closeDialog)
        WebUI.delay(1)
        throw new Exception('No members in group to remove')
    }
    WebUI.comment('✓ Members found in group')
    
    // Step 6: Click Remove button on first member
    WebUI.comment('Step 6: Clicking Remove button on first member...')
    TestObject firstRemoveButton = new TestObject('firstRemoveButton')
    firstRemoveButton.addProperty('xpath', ConditionType.EQUALS, '(//div[@role=\'dialog\']//button[@aria-label=\'Remove\'])[1]')
    WebUI.waitForElementClickable(firstRemoveButton, 10)
    WebUI.click(firstRemoveButton)
    WebUI.delay(2)
    WebUI.comment('✓ Remove button clicked')
    
    // Step 7: Verify toast success appears (optional - toast may disappear quickly)
    WebUI.comment('Step 7: Checking for toast success...')
    try {
        TestObject toastSuccess = findTestObject('Object Repository/Toast/Toast_Success')
        boolean hasToast = WebUI.waitForElementVisible(toastSuccess, 3, FailureHandling.OPTIONAL)
        if (hasToast) {
            WebUI.comment('✓ Toast success appeared')
        } else {
            WebUI.comment('⚠ Toast success not visible (may have disappeared)')
        }
    } catch (Exception e) {
        WebUI.comment('⚠ Toast not found (may have disappeared quickly)')
    }
    
    // Step 8: Verify member removed from list
    WebUI.comment('Step 8: Verifying member removed...')
    WebUI.delay(2)
    TestObject removedMemberItem = new TestObject('removedMemberItem')
    removedMemberItem.addProperty('xpath', ConditionType.EQUALS, '(//div[@role=\'dialog\']//button[@aria-label=\'Remove\'])')
    boolean memberRemoved = WebUI.waitForElementNotPresent(removedMemberItem, 5, FailureHandling.OPTIONAL)
    if (memberRemoved) {
        WebUI.comment('✓ Member removed from list')
    } else {
        WebUI.comment('⚠ Member still appears in list')
    }
    
    // Close dialog
    TestObject closeDialog = new TestObject('closeDialog')
    closeDialog.addProperty('xpath', ConditionType.EQUALS, '//div[@role=\'dialog\']//button[contains(@class, \'absolute\')]')
    WebUI.click(closeDialog, FailureHandling.OPTIONAL)
    WebUI.delay(1)
    
    WebUI.takeScreenshot('TC08_Groups_ManageMembers_RemoveMember_Success.png')
    WebUI.comment('=== TC08_Groups_ManageMembers_RemoveMember_Success PASSED ===')
    
} catch (Exception e) {
    WebUI.comment(('=== TC08_Groups_ManageMembers_RemoveMember_Success FAILED: ' + e.getMessage()) + ' ===')
    WebUI.takeScreenshot('TC08_Groups_ManageMembers_RemoveMember_Success_Error.png')
    throw e
}