import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC24A_Parameters_Reasoning_Effort_Default
 * Verify Reasoning Effort slider default value is Auto
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Get Reasoning Effort slider and verify default value is Auto
 */

WebUI.comment('=== TC24A: Reasoning Effort - Default Value ===')

try {
    // ============================================================
    // STEP 0: CHECK NAVBAR
    // ============================================================
    WebUI.comment('Step 0: Checking Navbar state...')
    
    TestObject navSidebar = new TestObject('navSidebar')
    navSidebar.addProperty('xpath', ConditionType.EQUALS, "//nav")
    WebUI.waitForElementVisible(navSidebar, 10)
    
    if (WebUI.getAttribute(navSidebar, 'aria-hidden') == 'true') {
        WebUI.comment('Navbar is closed, opening sidebar...')
        TestObject openBtn = new TestObject('openSidebarButton')
        openBtn.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
        WebUI.click(openBtn)
        WebUI.delay(1)
        WebUI.comment('Sidebar opened')
    } else {
        WebUI.comment('Navbar is already open')
    }

    // ============================================================
    // STEP 1: OPEN PARAMETERS TAB
    // ============================================================
    WebUI.comment('Step 1: Opening Parameters tab...')
    
    TestObject parametersButton = findTestObject('Object Repository/nav/nav_items/button_Parameters')
    WebUI.waitForElementVisible(parametersButton, 10)
    
    if (WebUI.getAttribute(parametersButton, 'aria-pressed') != 'true') {
        WebUI.comment('Parameters tab is closed, clicking to open...')
        WebUI.click(parametersButton)
        WebUI.delay(2)
        WebUI.comment('Parameters tab opened')
    } else {
        WebUI.comment('Parameters tab is already open')
    }

    // ============================================================
    // STEP 2: GET REASONING EFFORT SLIDER
    // ============================================================
    WebUI.comment('Step 2: Getting Reasoning Effort slider...')
    
    TestObject sliderThumb = new TestObject('reasoningEffortSliderThumb')
    sliderThumb.addProperty('xpath', ConditionType.EQUALS,
        "//span[contains(@id,'reasoning') or contains(@id,'Reasoning')]//span[@role='slider']")
    
    TestObject inputDisplay = findTestObject('Object Repository/nav/Parameter/input_Reasoning Effort')
    
    WebUI.waitForElementVisible(sliderThumb, 10)
    WebUI.waitForElementVisible(inputDisplay, 10)
    WebUI.comment('Reasoning Effort slider and input found')

    // ============================================================
    // STEP 3: CHECK DEFAULT VALUE
    // ============================================================
    WebUI.comment('Step 3: Checking default value...')
    
    WebUI.click(sliderThumb)
    WebUI.delay(0.5)
    
    String defaultValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('Default Reasoning Effort: ' + defaultValue)
    
    if (defaultValue == 'Auto') {
        WebUI.comment('TC24A PASSED - Default value is Auto')
    } else {
        WebUI.comment('FAILED: Default value should be Auto but got: ' + defaultValue)
        KeywordUtil.markFailed("FAILED: Default value should be 'Auto' but is '" + defaultValue + "'")
    }

    WebUI.takeScreenshot('TC24A_ReasoningEffort_Default.png')
    WebUI.comment('=== TC24A Completed ===')

} catch (Exception e) {
    WebUI.comment('TC24A FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC24A_ReasoningEffort_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}