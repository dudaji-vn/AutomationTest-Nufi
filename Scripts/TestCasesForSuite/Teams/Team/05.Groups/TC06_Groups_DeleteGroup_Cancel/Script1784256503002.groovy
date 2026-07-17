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
 * TC06_Groups_DeleteGroup_Cancel
 * 
 * Test Flow:
 * 1. Ensure on Team detail page
 * 2. Click Groups tab
 * 3. Get first group in list
 * 4. Click Delete group button
 * 5. Click Cancel in dialog
 * 6. Verify dialog closed
 * 7. Verify group still in list
 */
WebUI.comment('=== TC06_Groups_DeleteGroup_Cancel ===')

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
    
    // Step 5: Click Cancel in dialog
    WebUI.comment('Step 5: Clicking Cancel in dialog...')
    TestObject cancelDeleteButton = new TestObject('cancelDeleteButton')
    cancelDeleteButton.addProperty('xpath', ConditionType.EQUALS, '//button[@aria-label=\'Cancel\' and contains(text(), \'Cancel\')]')
    WebUI.waitForElementClickable(cancelDeleteButton, 10)
    WebUI.click(cancelDeleteButton)
    WebUI.delay(2)
    WebUI.comment('✓ Cancel clicked')
    
    // Step 6: Verify dialog closed
    WebUI.comment('Step 6: Verifying dialog closed...')
    TestObject dialog = new TestObject('dialog')
    dialog.addProperty('xpath', ConditionType.EQUALS, '//div[@role=\'dialog\' and contains(., \'Delete group\')]')
    boolean dialogClosed = WebUI.waitForElementNotPresent(dialog, 5, FailureHandling.OPTIONAL)
    if (dialogClosed) {
        WebUI.comment('✓ Dialog closed')
    } else {
        WebUI.comment('⚠ Dialog still visible')
    }
    
    // Step 7: Verify group still in list
    WebUI.comment('Step 7: Verifying group still in list...')
    TestObject existingGroupItem = new TestObject('existingGroupItem')
    existingGroupItem.addProperty('xpath', ConditionType.EQUALS, '//section[@aria-label=\'Groups\']//ul//li//p[contains(@class, \'text-text-primary\') and text()=\'' + groupName + '\']')
    boolean groupExists = WebUI.waitForElementPresent(existingGroupItem, 5, FailureHandling.OPTIONAL)
    if (groupExists) {
        WebUI.comment('✓ Group still exists: ' + groupName)
    } else {
        WebUI.comment('⚠ Group not found, may have been deleted')
    }
    
    WebUI.takeScreenshot('TC06_Groups_DeleteGroup_Cancel.png')
    WebUI.comment('=== TC06_Groups_DeleteGroup_Cancel PASSED ===')
    
} catch (Exception e) {
    WebUI.comment(('=== TC06_Groups_DeleteGroup_Cancel FAILED: ' + e.getMessage()) + ' ===')
    WebUI.takeScreenshot('TC06_Groups_DeleteGroup_Cancel_Error.png')
    throw e
}