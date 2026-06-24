import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC03: Fork - Include From Here
 * 
 * Test Flow:
 * 1. Click Fork menu button
 * 2. Click "Include all to/from here" option
 * 3. Verify fork success
 */

WebUI.comment('=== TC03: Fork (Include From Here) ===')

try {
    WebUI.comment('Clicking Fork menu button...')
    TestObject forkMenuButton = new TestObject('dynamic_fork_menu_button')
    forkMenuButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[contains(@aria-label, 'Fork') or contains(@title, 'Fork')]")
    
    WebUI.waitForElementVisible(forkMenuButton, 5)
    WebUI.click(forkMenuButton)
    WebUI.comment('Fork menu button clicked')

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
    WebUI.takeScreenshot('TC03_Fork_IncludeFromHere_Success.png')

    WebUI.comment('TC03 PASSED')

} catch (Exception e) {
    WebUI.comment('TC03 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC03_Fork_IncludeFromHere_Error.png')
    throw e
}