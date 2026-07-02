import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC26A_Parameters_Reasoning_Summary_Default
 * Verify Reasoning Summary slider default value is Unset
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Get Reasoning Summary slider and verify default value is Unset
 */

WebUI.comment('=== TC26A: Reasoning Summary - Default Value ===')

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
    
    TestObject parametersButton = findTestObject('Object Repository/Core Chat/nav/nav_items/button_Parameters')
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
    // STEP 2: GET REASONING SUMMARY SLIDER
    // ============================================================
    WebUI.comment('Step 2: Getting Reasoning Summary slider...')
    
    TestObject sliderThumb = findTestObject('Object Repository/Core Chat/nav/Parameter/slider_Reasoning Summary_thumb')
    TestObject inputDisplay = findTestObject('Object Repository/Core Chat/nav/Parameter/input_Reasoning Summary')
    
    WebUI.waitForElementVisible(sliderThumb, 10)
    WebUI.waitForElementVisible(inputDisplay, 10)
    WebUI.comment('Reasoning Summary slider and input found')

    // ============================================================
    // STEP 3: CHECK DEFAULT VALUE
    // ============================================================
    WebUI.comment('Step 3: Checking default value...')
    
    WebUI.click(sliderThumb)
    WebUI.delay(0.5)
    
    String defaultValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('Default Reasoning Summary: ' + defaultValue)
    
    if (defaultValue == 'Unset') {
        WebUI.comment('TC26A PASSED - Default value is Unset')
    } else {
        WebUI.comment('FAILED: Default value should be Unset but got: ' + defaultValue)
        KeywordUtil.markFailed("FAILED: Default value should be 'Unset' but is '" + defaultValue + "'")
    }

    WebUI.takeScreenshot('TC26A_ReasoningSummary_Default.png')
    WebUI.comment('=== TC26A Completed ===')

} catch (Exception e) {
    WebUI.comment('TC26A FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC26A_ReasoningSummary_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}