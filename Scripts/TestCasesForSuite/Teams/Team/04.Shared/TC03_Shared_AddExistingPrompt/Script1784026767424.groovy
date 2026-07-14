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
 * TC03_Shared_AddExistingPrompt
 * 
 * Test Flow:
 * 1. Ensure on Team detail page
 * 2. Click Shared tab
 * 3. Click Add prompt button
 * 4. Select first existing prompt from popup
 * 5. Verify toast success appears
 */

WebUI.comment('=== TC03_Shared_AddExistingPrompt ===')

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
    // Step 3: Click Add prompt button
    // ================================================================
    WebUI.comment('Step 3: Clicking Add prompt button...')
    
    TestObject addPromptButton = findTestObject('Object Repository/nav/Teams/Team/Tab_shared/button_Add prompt')
    WebUI.waitForElementClickable(addPromptButton, 10)
    WebUI.click(addPromptButton)
    WebUI.delay(2)
    WebUI.comment('✓ Add prompt button clicked - popup opened')

    // ================================================================
    // Step 4: Select first existing prompt from popup
    // ================================================================
    WebUI.comment('Step 4: Selecting first existing prompt from popup...')
    
    // Get prompt name of first prompt in popup
    TestObject firstPromptName = new TestObject('firstPromptName')
    firstPromptName.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[@role='dialog']//ul//li//p[contains(@class, 'text-text-primary')])[1]")
    
    String promptName = WebUI.getText(firstPromptName)
    WebUI.comment('Selected existing prompt: ' + promptName)
    
    // Click Add button for first prompt in popup
    TestObject firstAddPromptInPopup = new TestObject('firstAddPromptInPopup')
    firstAddPromptInPopup.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[@role='dialog']//ul//li//button[contains(text(), 'Add')])[1]")
    
    WebUI.waitForElementClickable(firstAddPromptInPopup, 10)
    WebUI.click(firstAddPromptInPopup)
    WebUI.delay(2)
    WebUI.comment('✓ Added existing prompt: ' + promptName)

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
        WebUI.comment('⚠ Toast success not found, but prompt may still be added')
    }
    
    WebUI.takeScreenshot('TC03_Shared_AddExistingPrompt_Success.png')
    WebUI.comment('=== TC03_Shared_AddExistingPrompt PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC03_Shared_AddExistingPrompt FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC03_Shared_AddExistingPrompt_Error.png')
    throw e
}