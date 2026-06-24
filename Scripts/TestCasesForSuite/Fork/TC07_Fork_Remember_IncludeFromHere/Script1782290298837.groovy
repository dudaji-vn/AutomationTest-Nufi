import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC07: Fork - Remember + Include From Here
 * 
 * Test Flow:
 * 1. Click Fork menu button
 * 2. Check "Start fork here" checkbox
 * 3. Check "Remember" checkbox
 * 4. Click "Include all to/from here" option
 * 5. Verify fork success
 */

WebUI.comment('=== TC07: Fork (Remember + Include From Here) ===')

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

    WebUI.comment('Checking "Remember" checkbox...')
    TestObject rememberCheckbox = new TestObject('dynamic_remember_checkbox')
    rememberCheckbox.addProperty('xpath', ConditionType.EQUALS, 
        "//label[contains(text(),'Remember')]//input[@type='checkbox']")
    WebUI.waitForElementVisible(rememberCheckbox, 5)
    WebUI.click(rememberCheckbox)
    WebUI.comment('"Remember" checkbox checked')

    WebUI.comment('Clicking "Include all to/from here" option...')
    TestObject includeFromHereOption = new TestObject('dynamic_include_from_here_option')
    includeFromHereOption.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='Include all to/from here']")
    
    WebUI.waitForElementVisible(includeFromHereOption, 5)
    WebUI.click(includeFromHereOption)
    WebUI.comment('"Include all to/from here" option clicked')

    WebUI.comment('Verifying fork success...')
    TestObject forkSuccessMessage = findTestObject('Object Repository/Core Chat/Action/Fork/Fork Test Message')
    WebUI.waitForElementVisible(forkSuccessMessage, 5)
    WebUI.comment('Fork success message displayed')
    WebUI.takeScreenshot('TC07_Fork_Remember_IncludeFromHere_Success.png')

    WebUI.comment('TC07 PASSED')

} catch (Exception e) {
    WebUI.comment('TC07 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC07_Fork_Remember_IncludeFromHere_Error.png')
    throw e
}