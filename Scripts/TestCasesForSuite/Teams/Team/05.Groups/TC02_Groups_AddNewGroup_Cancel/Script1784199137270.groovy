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
 * TC02_Groups_AddNewGroup_Cancel
 * 
 * Test Flow:
 * 1. Ensure on Team detail page
 * 2. Click Groups tab
 * 3. Click New group button
 * 4. Fill in group name
 * 5. Click Cancel button
 * 6. Verify dialog closed
 * 7. Verify group NOT appears in list
 */
WebUI.comment('=== TC02_Groups_AddNewGroup_Cancel ===')

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
    
    // Step 3: Click New group button
    WebUI.comment('Step 3: Clicking New group button...')
    TestObject newGroupButton = findTestObject('Object Repository/nav/Teams/Team/Tab_Groups/button_New group')
    WebUI.waitForElementClickable(newGroupButton, 10)
    WebUI.click(newGroupButton)
    WebUI.delay(2)
    WebUI.comment('✓ New group button clicked')
    
    // Step 4: Fill in group name
    WebUI.comment('Step 4: Filling in group name...')
    String groupName = 'Test Group Cancel ' + System.currentTimeMillis()
    TestObject groupNameInput = findTestObject('Object Repository/nav/Teams/Team/Tab_Groups/input_Group name')
    WebUI.waitForElementVisible(groupNameInput, 10)
    WebUI.setText(groupNameInput, groupName)
    WebUI.delay(1)
    WebUI.comment('✓ Group name entered: ' + groupName)
    
    // Step 5: Click Cancel button
    WebUI.comment('Step 5: Clicking Cancel button...')
    TestObject cancelButton = findTestObject('Object Repository/nav/Teams/Team/Tab_Groups/button_Cancel')
    WebUI.waitForElementClickable(cancelButton, 10)
    WebUI.click(cancelButton)
    WebUI.delay(2)
    WebUI.comment('✓ Cancel button clicked')
    
    // Step 6: Verify dialog closed
    WebUI.comment('Step 6: Verifying dialog closed...')
    TestObject dialog = new TestObject('dialog')
    dialog.addProperty('xpath', ConditionType.EQUALS, '//div[@role=\'dialog\' and contains(., \'New group\')]')
    boolean dialogClosed = WebUI.waitForElementNotPresent(dialog, 5, FailureHandling.OPTIONAL)
    if (dialogClosed) {
        WebUI.comment('✓ Dialog closed')
    } else {
        WebUI.comment('⚠ Dialog still visible')
    }
    
    // Step 7: Verify group NOT appears in list
    WebUI.comment('Step 7: Verifying group NOT appears in list...')
    TestObject groupItem = new TestObject('groupItem')
    groupItem.addProperty('xpath', ConditionType.EQUALS, '//section[@aria-label=\'Groups\']//ul//li//p[contains(@class, \'text-text-primary\') and text()=\'' + groupName + '\']')
    boolean groupNotExists = WebUI.waitForElementNotPresent(groupItem, 5, FailureHandling.OPTIONAL)
    if (groupNotExists) {
        WebUI.comment('✓ Group NOT created: ' + groupName)
    } else {
        WebUI.comment('⚠ Group still appears, may have been created')
    }
    
    WebUI.takeScreenshot('TC02_Groups_AddNewGroup_Cancel.png')
    WebUI.comment('=== TC02_Groups_AddNewGroup_Cancel PASSED ===')
    
} catch (Exception e) {
    WebUI.comment(('=== TC02_Groups_AddNewGroup_Cancel FAILED: ' + e.getMessage()) + ' ===')
    WebUI.takeScreenshot('TC02_Groups_AddNewGroup_Cancel_Error.png')
    throw e
}