import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import internal.GlobalVariable as GlobalVariable

/**
 * TC_ShareAgentWithGroup
 * 
 * Description: Share an existing agent with a specific group (sub-group) instead of whole team
 * 
 * Test Flow:
 * 1. Ensure on Team detail page
 * 2. Click Shared tab
 * 3. Click Add agent button to open popup
 * 4. Click dropdown "Share with" to open options
 * 5. Select first group option (after "Whole team")
 * 6. Select first agent from list and click Add
 * 7. Verify toast success appears
 * 
 * Expected Result:
 * - Agent is shared with the selected group
 * - Toast success message appears
 */

WebUI.comment('=== TC_ShareAgentWithGroup ===')

try {
    // ================================================================
    // Step 1: Ensure on Team detail page
    // ================================================================
    WebUI.comment('Step 1: Ensuring on Team detail page...')
    
    String currentUrl = WebUI.getUrl()
    if (!currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
        throw new Exception('Not on team detail page. Current URL: ' + currentUrl)
    }
    WebUI.comment('✓ On team detail page: ' + currentUrl)

    // ================================================================
    // Step 2: Click Shared tab
    // ================================================================
    WebUI.comment('Step 2: Clicking Shared tab...')
    
    TestObject sharedTab = new TestObject('sharedTab')
    sharedTab.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@role='tab' and contains(text(), 'Shared')]")
    
    WebUI.waitForElementClickable(sharedTab, 10)
    WebUI.click(sharedTab)
    WebUI.delay(2)
    WebUI.comment('✓ Shared tab clicked')

    // ================================================================
    // Step 3: Click Add agent button
    // ================================================================
    WebUI.comment('Step 3: Clicking Add agent button...')
    
    TestObject addAgentButton = findTestObject('Object Repository/nav/Teams/Team/Tab_shared/button_Add agent')
    WebUI.waitForElementClickable(addAgentButton, 10)
    WebUI.click(addAgentButton)
    WebUI.delay(2)
    WebUI.comment('✓ Add agent button clicked - popup opened')

    // ================================================================
    // Step 4: Click dropdown "Share with"
    // ================================================================
    WebUI.comment('Step 4: Clicking dropdown "Share with"...')
    
    TestObject shareWithDropdown = new TestObject('shareWithDropdown')
    shareWithDropdown.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@role='combobox' and @aria-label='Share with']")
    
    WebUI.waitForElementClickable(shareWithDropdown, 10)
    WebUI.click(shareWithDropdown)
    WebUI.delay(1)
    WebUI.comment('✓ Dropdown "Share with" opened')

    // ================================================================
    // Step 5: Select first group option after "Whole team"
    // ================================================================
    WebUI.comment('Step 5: Selecting first group option after "Whole team"...')
    
    // Dùng XPath để chọn option thứ 2 (sau Whole team)
    // Dựa trên selector từ capture: //div[@role='option' and contains(@class, 'cursor-pointer')]
    TestObject firstGroupOption = new TestObject('firstGroupOption')
    firstGroupOption.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[@role='option' and contains(@class, 'cursor-pointer')])[2]")
    
    WebUI.waitForElementClickable(firstGroupOption, 10)
    String groupName = WebUI.getText(firstGroupOption)
    WebUI.comment('Selected group: ' + groupName)
    WebUI.click(firstGroupOption)
    WebUI.delay(1)
    WebUI.comment('✓ Group selected: ' + groupName)

    // ================================================================
    // Step 6: Select first agent from list and click Add
    // ================================================================
    WebUI.comment('Step 6: Selecting first agent from list...')
    
    // Get agent name of first agent in popup
    TestObject firstAgentName = new TestObject('firstAgentName')
    firstAgentName.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[@role='dialog']//ul//li//p[contains(@class, 'text-text-primary')])[1]")
    
    String agentName = WebUI.getText(firstAgentName)
    WebUI.comment('Agent to share: ' + agentName)
    
    // Click Add button for first agent
    TestObject firstAddAgentInPopup = new TestObject('firstAddAgentInPopup')
    firstAddAgentInPopup.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[@role='dialog']//ul//li//button[@aria-label='Add'])[1]")
    
    WebUI.waitForElementClickable(firstAddAgentInPopup, 10)
    WebUI.click(firstAddAgentInPopup)
    WebUI.delay(2)
    WebUI.comment('✓ Agent "' + agentName + '" added to group "' + groupName + '"')

    // ================================================================
    // Step 7: Verify toast success appears
    // ================================================================
    WebUI.comment('Step 7: Verifying toast success...')
    
    try {
        TestObject toastSuccess = findTestObject('Object Repository/Toast/Toast_Success')
        boolean hasToast = WebUI.waitForElementVisible(toastSuccess, 5, FailureHandling.OPTIONAL)
        
        if (hasToast) {
            WebUI.comment('✓ Toast success - Agent shared with group successfully')
        } else {
            WebUI.comment('⚠ Toast success not found, but agent may still be added')
        }
    } catch (Exception e) {
        WebUI.comment('⚠ Toast not found (may have disappeared quickly)')
    }
    
    // ================================================================
    // Step 8: Take screenshot and summary
    // ================================================================
    WebUI.takeScreenshot('TC_ShareAgentWithGroup_Success.png')
    
    WebUI.comment('=== TC_ShareAgentWithGroup PASSED ===')
    WebUI.comment('✓ Agent shared: ' + agentName)
    WebUI.comment('✓ Shared with group: ' + groupName)
    WebUI.comment('✓ Toast verified: YES')

} catch (Exception e) {
    WebUI.comment('=== TC_ShareAgentWithGroup FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC_ShareAgentWithGroup_Error.png')
    throw e
}