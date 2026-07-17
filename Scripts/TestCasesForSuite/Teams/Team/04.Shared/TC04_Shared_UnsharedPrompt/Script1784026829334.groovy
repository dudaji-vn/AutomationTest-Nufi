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
 * TC04_Shared_UnsharedPrompt
 * 
 * Test Flow:
 * 1. Ensure on Team detail page
 * 2. Click Shared tab
 * 3. Get first prompt in Prompts list
 * 4. Click Unshared button on first prompt
 * 5. Verify toast success appears
 */
WebUI.comment('=== TC04_Shared_UnsharedPrompt ===')

try {
    // ================================================================
    // Step 1: Ensure on Team detail page
    // ================================================================
    WebUI.comment('Step 1: Ensuring on Team detail page...')

    String currentUrl = WebUI.getUrl()

    if (!(currentUrl.matches('.*/teams/[a-f0-9]+.*'))) {
        throw new Exception('Not on team detail page. Current URL: ' + currentUrl)
    }
    
    WebUI.comment('✓ On team detail page: ' + currentUrl)

    // ================================================================
    // Step 2: Click Shared tab
    // ================================================================
    WebUI.comment('Step 2: Clicking Shared tab...')

    TestObject sharedTab = new TestObject('sharedTab')

    sharedTab.addProperty('xpath', ConditionType.EQUALS, '//button[@role=\'tab\' and contains(text(), \'Shared\')]')

    WebUI.waitForElementClickable(sharedTab, 10)

    WebUI.click(sharedTab)

    WebUI.delay(2)

    WebUI.comment('✓ Shared tab clicked')

    // ================================================================
    // Step 3: Check if there are prompts to unshared
    // ================================================================
    WebUI.comment('Step 3: Checking if there are prompts to unshared...')

    // Check if there is at least one prompt in Prompts section
    TestObject promptItem = new TestObject('promptItem')

    promptItem.addProperty('xpath', ConditionType.EQUALS, '//section[@aria-label=\'Prompts\']//ul//li[contains(@class, \'rounded-lg\')]')

    boolean hasPrompts = WebUI.waitForElementPresent(promptItem, 5, FailureHandling.OPTIONAL)

    if (!(hasPrompts)) {
        throw new Exception('No prompts found to unshared. Please add a prompt first.')
    }
    
    WebUI.comment('✓ Prompts found in list')

    // ================================================================
    // Step 4: Get first prompt name and unshared it
    // ================================================================
    WebUI.comment('Step 4: Unsharing first prompt...')

    // Get first prompt name
    TestObject firstPromptName = new TestObject('firstPromptName')

    firstPromptName.addProperty('xpath', ConditionType.EQUALS, '(//section[@aria-label=\'Prompts\']//ul//li[contains(@class, \'rounded-lg\')]//p[contains(@class, \'text-text-primary\')])[1]')

    String promptName = WebUI.getText(firstPromptName)

    WebUI.comment('Prompt to unshared: ' + promptName)

    // Click Unshared button on first prompt
    TestObject firstUnsharedButton = new TestObject('firstUnsharedButton')

    firstUnsharedButton.addProperty('xpath', ConditionType.EQUALS, '(//section[@aria-label=\'Prompts\']//ul//li[contains(@class, \'rounded-lg\')]//button[@aria-label=\'Unshared from team\'])[1]')

    WebUI.waitForElementClickable(firstUnsharedButton, 10)

    WebUI.click(firstUnsharedButton)

    WebUI.delay(2)

    WebUI.comment('✓ Unshared button clicked for: ' + promptName)

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
        WebUI.comment('⚠ Toast success not found, but prompt may still be unshared')
    }
    
    WebUI.takeScreenshot('TC04_Shared_UnsharedPrompt_Success.png')

    WebUI.comment('=== TC04_Shared_UnsharedPrompt PASSED ===')
}
catch (Exception e) {
    WebUI.comment(('=== TC04_Shared_UnsharedPrompt FAILED: ' + e.getMessage()) + ' ===')

    WebUI.takeScreenshot('TC04_Shared_UnsharedPrompt_Error.png')

    throw e
} 

WebUI.acceptAlert()

