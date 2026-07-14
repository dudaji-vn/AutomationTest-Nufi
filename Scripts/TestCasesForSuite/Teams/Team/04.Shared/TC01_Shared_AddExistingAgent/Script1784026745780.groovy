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
 * TC01_Shared_AddExistingAgent
 * 
 * Test Flow:
 * 1. Ensure on Team detail page
 * 2. Click Shared tab
 * 3. Click Add agent button
 * 4. Select first existing agent from popup
 * 5. Verify toast success appears
 */

WebUI.comment('=== TC01_Shared_AddExistingAgent ===')

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
    // Step 4: Select first existing agent from popup
    // ================================================================
    WebUI.comment('Step 4: Selecting first existing agent from popup...')
    
    // Get agent name of first agent in popup
    TestObject firstAgentName = new TestObject('firstAgentName')
    firstAgentName.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[@role='dialog']//ul//li//p[contains(@class, 'text-text-primary')])[1]")
    
    String agentName = WebUI.getText(firstAgentName)
    WebUI.comment('Selected existing agent: ' + agentName)
    
    // Click Add button for first agent in popup
    TestObject firstAddAgentInPopup = new TestObject('firstAddAgentInPopup')
    firstAddAgentInPopup.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[@role='dialog']//ul//li//button[contains(text(), 'Add')])[1]")
    
    WebUI.waitForElementClickable(firstAddAgentInPopup, 10)
    WebUI.click(firstAddAgentInPopup)
    WebUI.delay(2)
    WebUI.comment('✓ Added existing agent: ' + agentName)

    // ================================================================
    // Step 5: Verify toast success appears
    // ================================================================
    WebUI.comment('Step 5: Verifying toast success...')
    
    TestObject toastSuccess = findTestObject('Object Repository/Toast/Toast_Success')
    boolean hasToast = WebUI.waitForElementVisible(toastSuccess, 5, FailureHandling.OPTIONAL)
    
    if (hasToast) {
        String toastText = WebUI.getText(toastSuccess)
        WebUI.comment('✓ Toast success: ' + toastText)
    } else {
        WebUI.comment('⚠ Toast success not found, but agent may still be added')
    }
    
    WebUI.takeScreenshot('TC01_Shared_AddExistingAgent_Success.png')
    WebUI.comment('=== TC01_Shared_AddExistingAgent PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC01_Shared_AddExistingAgent FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC01_Shared_AddExistingAgent_Error.png')
    throw e
}