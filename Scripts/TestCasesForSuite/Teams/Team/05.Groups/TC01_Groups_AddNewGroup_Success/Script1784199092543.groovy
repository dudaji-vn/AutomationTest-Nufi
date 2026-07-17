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
 * TC01_Groups_AddNewGroup_Success
 * 
 * Test Flow:
 * 1. Ensure on Team detail page
 * 2. Click Groups tab
 * 3. Click New group button
 * 4. Fill in group name
 * 5. Fill in group description
 * 6. Click Create button
 * 7. Verify toast success appears (if visible)
 * 8. Verify group appears in list
 */
WebUI.comment('=== TC01_Groups_AddNewGroup_Success ===')

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
    String groupName = 'Test Group ' + System.currentTimeMillis()
    TestObject groupNameInput = findTestObject('Object Repository/nav/Teams/Team/Tab_Groups/input_Group name')
    WebUI.waitForElementVisible(groupNameInput, 10)
    WebUI.setText(groupNameInput, groupName)
    WebUI.delay(1)
    WebUI.comment('✓ Group name entered: ' + groupName)
    
    // Step 5: Fill in group description
    WebUI.comment('Step 5: Filling in group description...')
    String groupDescription = 'Test description for group ' + System.currentTimeMillis()
    TestObject groupDescInput = findTestObject('Object Repository/nav/Teams/Team/Tab_Groups/textarea_Description')
    WebUI.waitForElementVisible(groupDescInput, 10)
    WebUI.setText(groupDescInput, groupDescription)
    WebUI.delay(1)
    WebUI.comment('✓ Group description entered')
    
    // Step 6: Click Create button
    WebUI.comment('Step 6: Clicking Create button...')
    TestObject createButton = findTestObject('Object Repository/nav/Teams/Team/Tab_Groups/button_Create')
    WebUI.waitForElementClickable(createButton, 10)
    WebUI.click(createButton)
    WebUI.delay(3)
    WebUI.comment('✓ Create button clicked')
    
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
    
    // Step 8: Verify group appears in list
    WebUI.comment('Step 8: Verifying group appears in list...')
    TestObject newGroupItem = new TestObject('newGroupItem')
    newGroupItem.addProperty('xpath', ConditionType.EQUALS, '//section[@aria-label=\'Groups\']//ul//li//p[contains(@class, \'text-text-primary\') and text()=\'' + groupName + '\']')
    boolean groupFound = WebUI.waitForElementPresent(newGroupItem, 10, FailureHandling.OPTIONAL)
    if (groupFound) {
        WebUI.comment('✓ New group found in list: ' + groupName)
    } else {
        throw new Exception('New group not found in list: ' + groupName)
    }
    
    WebUI.takeScreenshot('TC01_Groups_AddNewGroup_Success.png')
    WebUI.comment('=== TC01_Groups_AddNewGroup_Success PASSED ===')
    
} catch (Exception e) {
    WebUI.comment(('=== TC01_Groups_AddNewGroup_Success FAILED: ' + e.getMessage()) + ' ===')
    WebUI.takeScreenshot('TC01_Groups_AddNewGroup_Success_Error.png')
    throw e
}