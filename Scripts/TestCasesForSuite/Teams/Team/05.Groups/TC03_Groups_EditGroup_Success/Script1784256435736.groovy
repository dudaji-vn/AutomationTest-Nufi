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
 * TC03_Groups_EditGroup_Success
 * 
 * Test Flow:
 * 1. Ensure on Team detail page
 * 2. Click Groups tab
 * 3. Get first group in list
 * 4. Click Rename group button
 * 5. Update group name
 * 6. Update group description
 * 7. Click Save button
 * 8. Verify toast success appears (if visible)
 * 9. Verify group name updated in list
 */
WebUI.comment('=== TC03_Groups_EditGroup_Success ===')

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
    String oldGroupName = WebUI.getText(firstGroupName)
    WebUI.comment('Group to edit: ' + oldGroupName)
    
    TestObject firstRenameButton = new TestObject('firstRenameButton')
    firstRenameButton.addProperty('xpath', ConditionType.EQUALS, '(//section[@aria-label=\'Groups\']//ul//li[contains(@class, \'rounded-lg\')]//button[@aria-label=\'Rename group\'])[1]')
    WebUI.waitForElementClickable(firstRenameButton, 10)
    WebUI.click(firstRenameButton)
    WebUI.delay(2)
    WebUI.comment('✓ Rename button clicked')
    
    // Step 5: Update group name
    WebUI.comment('Step 5: Updating group name...')
    String newGroupName = 'Updated Group ' + System.currentTimeMillis()
    TestObject groupNameInput = findTestObject('Object Repository/nav/Teams/Team/Tab_Groups/input_Group name')
    WebUI.waitForElementVisible(groupNameInput, 10)
    WebUI.clearText(groupNameInput)
    WebUI.setText(groupNameInput, newGroupName)
    WebUI.delay(1)
    WebUI.comment('✓ Group name updated to: ' + newGroupName)
    
    // Step 6: Update group description
    WebUI.comment('Step 6: Updating group description...')
    String newGroupDescription = 'Updated description ' + System.currentTimeMillis()
    TestObject groupDescInput = findTestObject('Object Repository/nav/Teams/Team/Tab_Groups/textarea_Description')
    WebUI.waitForElementVisible(groupDescInput, 10)
    WebUI.clearText(groupDescInput)
    WebUI.setText(groupDescInput, newGroupDescription)
    WebUI.delay(1)
    WebUI.comment('✓ Group description updated')
    
    // Step 7: Click Save button
    WebUI.comment('Step 7: Clicking Save button...')
    TestObject saveButton = new TestObject('saveButton')
    saveButton.addProperty('xpath', ConditionType.EQUALS, '//button[@aria-label=\'Rename group\' and contains(text(), \'Save\')]')
    WebUI.waitForElementClickable(saveButton, 10)
    WebUI.click(saveButton)
    WebUI.delay(3)
    WebUI.comment('✓ Save button clicked')
    
    // Step 8: Verify toast success appears (optional - toast may disappear quickly)
    WebUI.comment('Step 8: Checking for toast success...')
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
    
    // Step 9: Verify group name updated in list
    WebUI.comment('Step 9: Verifying group name updated...')
    TestObject updatedGroupItem = new TestObject('updatedGroupItem')
    updatedGroupItem.addProperty('xpath', ConditionType.EQUALS, '//section[@aria-label=\'Groups\']//ul//li//p[contains(@class, \'text-text-primary\') and text()=\'' + newGroupName + '\']')
    boolean groupFound = WebUI.waitForElementPresent(updatedGroupItem, 10, FailureHandling.OPTIONAL)
    if (groupFound) {
        WebUI.comment('✓ Group name updated in list: ' + newGroupName)
    } else {
        throw new Exception('Updated group not found in list: ' + newGroupName)
    }
    
    WebUI.takeScreenshot('TC03_Groups_EditGroup_Success.png')
    WebUI.comment('=== TC03_Groups_EditGroup_Success PASSED ===')
    
} catch (Exception e) {
    WebUI.comment(('=== TC03_Groups_EditGroup_Success FAILED: ' + e.getMessage()) + ' ===')
    WebUI.takeScreenshot('TC03_Groups_EditGroup_Success_Error.png')
    throw e
}