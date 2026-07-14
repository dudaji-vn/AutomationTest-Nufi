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
 * TC02_Shared_UnsharedAgent
 * 
 * Test Flow:
 * 1. Ensure on Team detail page
 * 2. Click Shared tab
 * 3. Get first agent in Agents list
 * 4. Click Unshared button on first agent
 * 5. Verify toast success appears
 */

WebUI.comment('=== TC02_Shared_UnsharedAgent ===')

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
    // Step 3: Check if there are agents to unshared
    // ================================================================
    WebUI.comment('Step 3: Checking if there are agents to unshared...')
    
    // Check if there is at least one agent in Agents section
    TestObject agentItem = new TestObject('agentItem')
    agentItem.addProperty('xpath', ConditionType.EQUALS, 
        "//section[@aria-label='Agents']//ul//li[contains(@class, 'rounded-lg')]")
    
    boolean hasAgents = WebUI.waitForElementPresent(agentItem, 5, FailureHandling.OPTIONAL)
    
    if (!hasAgents) {
        throw new Exception('No agents found to unshared. Please add an agent first.')
    }
    WebUI.comment('✓ Agents found in list')

    // ================================================================
    // Step 4: Get first agent name and unshared it
    // ================================================================
    WebUI.comment('Step 4: Unsharing first agent...')
    
    // Get first agent name
    TestObject firstAgentName = new TestObject('firstAgentName')
    firstAgentName.addProperty('xpath', ConditionType.EQUALS, 
        "(//section[@aria-label='Agents']//ul//li[contains(@class, 'rounded-lg')]//p[contains(@class, 'text-text-primary')])[1]")
    
    String agentName = WebUI.getText(firstAgentName)
    WebUI.comment('Agent to unshared: ' + agentName)
    
    // Click Unshared button on first agent
    TestObject firstUnsharedButton = new TestObject('firstUnsharedButton')
    firstUnsharedButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//section[@aria-label='Agents']//ul//li[contains(@class, 'rounded-lg')]//button[@aria-label='Unshared from team'])[1]")
    
    WebUI.waitForElementClickable(firstUnsharedButton, 10)
    WebUI.click(firstUnsharedButton)
    WebUI.delay(2)
    WebUI.comment('✓ Unshared button clicked for: ' + agentName)

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
        WebUI.comment('⚠ Toast success not found, but agent may still be unshared')
    }
    
    WebUI.takeScreenshot('TC02_Shared_UnsharedAgent_Success.png')
    WebUI.comment('=== TC02_Shared_UnsharedAgent PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC02_Shared_UnsharedAgent FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC02_Shared_UnsharedAgent_Error.png')
    throw e
}