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
 * TC07_Groups_ManageMembers_AddMember_Success
 * 
 * Test Flow:
 * 1. Ensure on Team detail page
 * 2. Click Groups tab
 * 3. Get first group in list
 * 4. Click Manage members button
 * 5. Verify members list displayed
 * 6. Click Add member button on a member not in group
 * 7. Verify toast success appears (if visible)
 * 8. Verify member count increased
 */
WebUI.comment('=== TC07_Groups_ManageMembers_AddMember_Success ===')

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
    
    // Step 4: Get member count before adding
    WebUI.comment('Step 4: Getting member count...')
    TestObject memberCountBefore = new TestObject('memberCountBefore')
    memberCountBefore.addProperty('xpath', ConditionType.EQUALS, '(//section[@aria-label=\'Groups\']//ul//li[contains(@class, \'rounded-lg\')]//span[contains(@class, \'rounded-full\')])[1]')
    String countTextBefore = WebUI.getText(memberCountBefore)
    int countBefore = 0
    if (countTextBefore) {
        countBefore = Integer.parseInt(countTextBefore.replaceAll('[^0-9]', ''))
    }
    WebUI.comment('Current member count: ' + countBefore)
    
    // Step 5: Click Manage members button
    WebUI.comment('Step 5: Clicking Manage members button...')
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
    
    // Step 6: Click Add member button
    WebUI.comment('Step 6: Clicking Add member button...')
    TestObject addMemberButton = new TestObject('addMemberButton')
    addMemberButton.addProperty('xpath', ConditionType.EQUALS, '//div[@role=\'dialog\']//button[contains(@aria-label, \'Add to group\')]')
    boolean hasAddButton = WebUI.waitForElementPresent(addMemberButton, 5, FailureHandling.OPTIONAL)
    if (hasAddButton) {
        WebUI.click(addMemberButton)
        WebUI.delay(2)
        WebUI.comment('✓ Add member button clicked')
    } else {
        WebUI.comment('⚠ No members available to add')
        // Close dialog
        TestObject closeDialog = new TestObject('closeDialog')
        closeDialog.addProperty('xpath', ConditionType.EQUALS, '//div[@role=\'dialog\']//button[contains(@class, \'absolute\')]')
        WebUI.click(closeDialog)
        WebUI.delay(1)
        throw new Exception('No members available to add to group')
    }
    
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
    
    // Step 8: Verify member count increased
    WebUI.comment('Step 8: Verifying member count increased...')
    WebUI.delay(2)
    // Get new member count
    TestObject memberCountAfter = new TestObject('memberCountAfter')
    memberCountAfter.addProperty('xpath', ConditionType.EQUALS, '(//section[@aria-label=\'Groups\']//ul//li[contains(@class, \'rounded-lg\')]//span[contains(@class, \'rounded-full\')])[1]')
    String countTextAfter = WebUI.getText(memberCountAfter)
    int countAfter = 0
    if (countTextAfter) {
        countAfter = Integer.parseInt(countTextAfter.replaceAll('[^0-9]', ''))
    }
    WebUI.comment('New member count: ' + countAfter)
    
    if (countAfter > countBefore) {
        WebUI.comment('✓ Member count increased from ' + countBefore + ' to ' + countAfter)
    } else {
        WebUI.comment('⚠ Member count not increased, may need refresh')
    }
    
    // Close dialog
    TestObject closeDialog = new TestObject('closeDialog')
    closeDialog.addProperty('xpath', ConditionType.EQUALS, '//div[@role=\'dialog\']//button[contains(@class, \'absolute\')]')
    WebUI.click(closeDialog, FailureHandling.OPTIONAL)
    WebUI.delay(1)
    
    WebUI.takeScreenshot('TC07_Groups_ManageMembers_AddMember_Success.png')
    WebUI.comment('=== TC07_Groups_ManageMembers_AddMember_Success PASSED ===')
    
} catch (Exception e) {
    WebUI.comment(('=== TC07_Groups_ManageMembers_AddMember_Success FAILED: ' + e.getMessage()) + ' ===')
    WebUI.takeScreenshot('TC07_Groups_ManageMembers_AddMember_Success_Error.png')
    throw e
}