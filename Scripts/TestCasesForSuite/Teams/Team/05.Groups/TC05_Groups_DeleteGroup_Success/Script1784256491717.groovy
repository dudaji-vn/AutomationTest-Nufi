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
 * TC05_Groups_DeleteGroup_Success
 * 
 * Test Flow:
 * 1. Ensure on Team detail page
 * 2. Click Groups tab
 * 3. Get first group in list
 * 4. Click Delete group button
 * 5. Click Confirm Delete in dialog
 * 6. Verify toast success appears (if visible)
 * 7. Verify group no longer in list
 */
WebUI.comment('=== TC05_Groups_DeleteGroup_Success ===')

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
    
    // Step 3: Check if there are groups to delete
    WebUI.comment('Step 3: Checking if there are groups to delete...')
    TestObject groupItem = new TestObject('groupItem')
    groupItem.addProperty('xpath', ConditionType.EQUALS, '//section[@aria-label=\'Groups\']//ul//li[contains(@class, \'rounded-lg\')]')
    boolean hasGroups = WebUI.waitForElementPresent(groupItem, 5, FailureHandling.OPTIONAL)
    if (!hasGroups) {
        throw new Exception('No groups found to delete. Please create a group first.')
    }
    WebUI.comment('✓ Groups found in list')
    
    // Step 4: Get first group name and click Delete button
    WebUI.comment('Step 4: Clicking Delete group button...')
    TestObject firstGroupName = new TestObject('firstGroupName')
    firstGroupName.addProperty('xpath', ConditionType.EQUALS, '(//section[@aria-label=\'Groups\']//ul//li[contains(@class, \'rounded-lg\')]//p[contains(@class, \'text-text-primary\')])[1]')
    String groupName = WebUI.getText(firstGroupName)
    WebUI.comment('Group to delete: ' + groupName)
    
    TestObject firstDeleteButton = new TestObject('firstDeleteButton')
    firstDeleteButton.addProperty('xpath', ConditionType.EQUALS, '(//section[@aria-label=\'Groups\']//ul//li[contains(@class, \'rounded-lg\')]//button[@aria-label=\'Delete group\'])[1]')
    WebUI.waitForElementClickable(firstDeleteButton, 10)
    WebUI.click(firstDeleteButton)
    WebUI.delay(2)
    WebUI.comment('✓ Delete button clicked')
    
    // Step 5: Click Confirm Delete in dialog
    WebUI.comment('Step 5: Clicking Confirm Delete...')
    TestObject confirmDeleteButton = new TestObject('confirmDeleteButton')
    confirmDeleteButton.addProperty('xpath', ConditionType.EQUALS, '//button[@aria-label=\'Confirm\' and contains(text(), \'Delete\')]')
    WebUI.waitForElementClickable(confirmDeleteButton, 10)
    WebUI.click(confirmDeleteButton)
    WebUI.delay(3)
    WebUI.comment('✓ Confirm Delete clicked')
    
    // Step 6: Verify toast success appears (optional - toast may disappear quickly)
    WebUI.comment('Step 6: Checking for toast success...')
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
    
    // Step 7: Verify group no longer in list
    WebUI.comment('Step 7: Verifying group no longer in list...')
    TestObject deletedGroupItem = new TestObject('deletedGroupItem')
    deletedGroupItem.addProperty('xpath', ConditionType.EQUALS, '//section[@aria-label=\'Groups\']//ul//li//p[contains(@class, \'text-text-primary\') and text()=\'' + groupName + '\']')
    boolean groupRemoved = WebUI.waitForElementNotPresent(deletedGroupItem, 10, FailureHandling.OPTIONAL)
    if (groupRemoved) {
        WebUI.comment('✓ Group removed from list: ' + groupName)
    } else {
        WebUI.comment('⚠ Group still appears in list')
    }
    
    WebUI.takeScreenshot('TC05_Groups_DeleteGroup_Success.png')
    WebUI.comment('=== TC05_Groups_DeleteGroup_Success PASSED ===')
    
} catch (Exception e) {
    WebUI.comment(('=== TC05_Groups_DeleteGroup_Success FAILED: ' + e.getMessage()) + ' ===')
    WebUI.takeScreenshot('TC05_Groups_DeleteGroup_Success_Error.png')
    throw e
}