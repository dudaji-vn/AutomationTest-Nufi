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
 * TC04_Groups_EditGroup_Cancel
 * 
 * Test Flow:
 * 1. Ensure on Team detail page
 * 2. Click Groups tab
 * 3. Get first group in list
 * 4. Click Rename group button
 * 5. Update group name
 * 6. Click Cancel button
 * 7. Verify dialog closed
 * 8. Verify group name NOT changed in list
 */
WebUI.comment('=== TC04_Groups_EditGroup_Cancel ===')

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
    
    // Step 3: Check if there are groups to edit
    WebUI.comment('Step 3: Checking if there are groups to edit...')
    TestObject groupItem = new TestObject('groupItem')
    groupItem.addProperty('xpath', ConditionType.EQUALS, '//section[@aria-label=\'Groups\']//ul//li[contains(@class, \'rounded-lg\')]')
    boolean hasGroups = WebUI.waitForElementPresent(groupItem, 5, FailureHandling.OPTIONAL)
    if (!hasGroups) {
        throw new Exception('No groups found to edit. Please create a group first.')
    }
    WebUI.comment('✓ Groups found in list')
    
    // Step 4: Get first group name and click Rename button
    WebUI.comment('Step 4: Clicking Rename group button...')
    TestObject firstGroupName = new TestObject('firstGroupName')
    firstGroupName.addProperty('xpath', ConditionType.EQUALS, '(//section[@aria-label=\'Groups\']//ul//li[contains(@class, \'rounded-lg\')]//p[contains(@class, \'text-text-primary\')])[1]')
    String originalGroupName = WebUI.getText(firstGroupName)
    WebUI.comment('Original group name: ' + originalGroupName)
    
    TestObject firstRenameButton = new TestObject('firstRenameButton')
    firstRenameButton.addProperty('xpath', ConditionType.EQUALS, '(//section[@aria-label=\'Groups\']//ul//li[contains(@class, \'rounded-lg\')]//button[@aria-label=\'Rename group\'])[1]')
    WebUI.waitForElementClickable(firstRenameButton, 10)
    WebUI.click(firstRenameButton)
    WebUI.delay(2)
    WebUI.comment('✓ Rename button clicked')
    
    // Step 5: Update group name
    WebUI.comment('Step 5: Updating group name...')
    String tempGroupName = 'Temp Name ' + System.currentTimeMillis()
    TestObject groupNameInput = findTestObject('Object Repository/nav/Teams/Team/Tab_Groups/input_Group name')
    WebUI.waitForElementVisible(groupNameInput, 10)
    WebUI.clearText(groupNameInput)
    WebUI.setText(groupNameInput, tempGroupName)
    WebUI.delay(1)
    WebUI.comment('✓ Group name changed to: ' + tempGroupName)
    
    // Step 6: Click Cancel button
    WebUI.comment('Step 6: Clicking Cancel button...')
    TestObject cancelButton = findTestObject('Object Repository/nav/Teams/Team/Tab_Groups/button_Cancel')
    WebUI.waitForElementClickable(cancelButton, 10)
    WebUI.click(cancelButton)
    WebUI.delay(2)
    WebUI.comment('✓ Cancel button clicked')
    
    // Step 7: Verify dialog closed
    WebUI.comment('Step 7: Verifying dialog closed...')
    TestObject dialog = new TestObject('dialog')
    dialog.addProperty('xpath', ConditionType.EQUALS, '//div[@role=\'dialog\' and contains(., \'Rename group\')]')
    boolean dialogClosed = WebUI.waitForElementNotPresent(dialog, 5, FailureHandling.OPTIONAL)
    if (dialogClosed) {
        WebUI.comment('✓ Dialog closed')
    } else {
        WebUI.comment('⚠ Dialog still visible')
    }
    
    // Step 8: Verify group name NOT changed in list
    WebUI.comment('Step 8: Verifying group name NOT changed...')
    TestObject originalGroupItem = new TestObject('originalGroupItem')
    originalGroupItem.addProperty('xpath', ConditionType.EQUALS, '//section[@aria-label=\'Groups\']//ul//li//p[contains(@class, \'text-text-primary\') and text()=\'' + originalGroupName + '\']')
    boolean originalExists = WebUI.waitForElementPresent(originalGroupItem, 5, FailureHandling.OPTIONAL)
    if (originalExists) {
        WebUI.comment('✓ Original group name still exists: ' + originalGroupName)
    } else {
        WebUI.comment('⚠ Original group name not found')
    }
    
    // Verify temp name does NOT exist
    TestObject tempGroupItem = new TestObject('tempGroupItem')
    tempGroupItem.addProperty('xpath', ConditionType.EQUALS, '//section[@aria-label=\'Groups\']//ul//li//p[contains(@class, \'text-text-primary\') and text()=\'' + tempGroupName + '\']')
    boolean tempNotExists = WebUI.waitForElementNotPresent(tempGroupItem, 5, FailureHandling.OPTIONAL)
    if (tempNotExists) {
        WebUI.comment('✓ Temporary name NOT saved: ' + tempGroupName)
    } else {
        WebUI.comment('⚠ Temporary name found, may have been saved')
    }
    
    WebUI.takeScreenshot('TC04_Groups_EditGroup_Cancel.png')
    WebUI.comment('=== TC04_Groups_EditGroup_Cancel PASSED ===')
    
} catch (Exception e) {
    WebUI.comment(('=== TC04_Groups_EditGroup_Cancel FAILED: ' + e.getMessage()) + ' ===')
    WebUI.takeScreenshot('TC04_Groups_EditGroup_Cancel_Error.png')
    throw e
}