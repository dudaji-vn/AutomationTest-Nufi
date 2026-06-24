import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC05: Fork - Start Fork Here + Include Related Branches
 * 
 * Test Flow:
 * 1. Click Fork menu button
 * 2. Check "Start fork here" checkbox
 * 3. Uncheck "Remember" (if checked)
 * 4. Click "Include related branches" option
 * 5. Verify fork success
 */

WebUI.comment('=== TC05: Fork (Start Fork Here + Include Related Branches) ===')

try {
    WebUI.comment('Clicking Fork menu button...')
    TestObject forkMenuButton = new TestObject('dynamic_fork_menu_button')
    forkMenuButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[contains(@aria-label, 'Fork') or contains(@title, 'Fork')]")
    
    WebUI.waitForElementVisible(forkMenuButton, 5)
    WebUI.click(forkMenuButton)
    WebUI.comment('Fork menu button clicked')

    WebUI.comment('Checking "Start fork here" checkbox...')
    TestObject startForkCheckbox = new TestObject('dynamic_start_fork_checkbox')
    startForkCheckbox.addProperty('xpath', ConditionType.EQUALS, 
        "//label[contains(text(),'Start fork here')]//input[@type='checkbox']")
    WebUI.waitForElementVisible(startForkCheckbox, 5)
    WebUI.click(startForkCheckbox)
    WebUI.comment('"Start fork here" checkbox checked')

    WebUI.comment('Ensuring "Remember" is unchecked...')
    TestObject rememberCheckbox = new TestObject('dynamic_remember_checkbox')
    rememberCheckbox.addProperty('xpath', ConditionType.EQUALS, 
        "//label[contains(text(),'Remember')]//input[@type='checkbox']")
    WebUI.waitForElementVisible(rememberCheckbox, 5)
    boolean isChecked = WebUI.verifyElementChecked(rememberCheckbox, 2, FailureHandling.OPTIONAL)
    if (isChecked) {
        WebUI.click(rememberCheckbox)
        WebUI.comment('"Remember" unchecked')
    }

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
    WebUI.takeScreenshot('TC05_Fork_StartForkHere_IncludeRelated_Success.png')

    WebUI.comment('TC05 PASSED')

} catch (Exception e) {
    WebUI.comment('TC05 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC05_Fork_StartForkHere_IncludeRelated_Error.png')
    throw e
}