import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC02: Fork - Include Related Branches
 * 
 * Test Flow:
 * 1. Click Fork menu button
 * 2. Click "Include related branches" option
 * 3. Verify fork success
 */

WebUI.comment('=== TC02: Fork (Include Related Branches) ===')

try {
    WebUI.comment('Clicking Fork menu button...')
    TestObject forkMenuButton = new TestObject('dynamic_fork_menu_button')
    forkMenuButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[contains(@aria-label, 'Fork') or contains(@title, 'Fork')]")
    
    WebUI.waitForElementVisible(forkMenuButton, 5)
    WebUI.click(forkMenuButton)
    WebUI.comment('Fork menu button clicked')

    WebUI.comment('Clicking "Include related branches" option...')
    TestObject includeRelatedOption = new TestObject('dynamic_include_related_option')
    includeRelatedOption.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='Include related branches']")
    
    WebUI.waitForElementVisible(includeRelatedOption, 5)
    WebUI.click(includeRelatedOption)
    WebUI.comment('"Include related branches" option clicked')

    WebUI.comment('Verifying fork success...')
    TestObject forkSuccessMessage = findTestObject('Object Repository/Core Chat/Action/Fork/Fork Test Message')
    WebUI.waitForElementVisible(forkSuccessMessage, 5)
    WebUI.comment('Fork success message displayed')
    WebUI.takeScreenshot('TC02_Fork_IncludeRelated_Success.png')

    WebUI.comment('TC02 PASSED')

} catch (Exception e) {
    WebUI.comment('TC02 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC02_Fork_IncludeRelated_Error.png')
    throw e
}