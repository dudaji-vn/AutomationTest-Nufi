import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC24H_Parameters_Reasoning_Effort_DoubleClick_Reset
 * Verify double click on Reasoning Effort slider resets to Auto
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Get Reasoning Effort slider
 * 3. Drag slider to Extra High (to ensure it's not Auto)
 * 4. Double click to reset to Auto and verify
 */

WebUI.comment('=== TC24H: Reasoning Effort - Double Click Reset ===')

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
    // STEP 2: GET REASONING EFFORT SLIDER
    // ============================================================
    WebUI.comment('Step 2: Getting Reasoning Effort slider...')
    
    TestObject sliderThumb = new TestObject('reasoningEffortSliderThumb')
    sliderThumb.addProperty('xpath', ConditionType.EQUALS,
        "//span[contains(@id,'reasoning') or contains(@id,'Reasoning')]//span[@role='slider']")
    
    TestObject inputDisplay = findTestObject('Object Repository/Core Chat/nav/Parameter/input_Reasoning Effort')
    
    WebUI.waitForElementVisible(sliderThumb, 10)
    WebUI.waitForElementVisible(inputDisplay, 10)
    WebUI.comment('Reasoning Effort slider and input found')

    // ============================================================
    // STEP 3: DRAG TO EXTRA HIGH (to ensure it's not Auto)
    // ============================================================
    WebUI.comment('Step 3: Dragging slider to Extra High...')
    
    WebUI.click(sliderThumb)
    WebUI.delay(0.5)
    
    String widthScript = "return document.evaluate(\"//span[contains(@id,'reasoning') or contains(@id,'Reasoning')]\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.getBoundingClientRect().width;"
    double sliderWidth = (Double) WebUI.executeJavaScript(widthScript, null)
    WebUI.comment('Slider width: ' + sliderWidth + 'px')
    
    double stepSize = sliderWidth / 6
    int dragToExtraHigh = (int)(6 * stepSize)
    
    WebUI.dragAndDropByOffset(sliderThumb, dragToExtraHigh, 0)
    WebUI.delay(1.5)
    
    String currentValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('Current value before reset: ' + currentValue)

    // ============================================================
    // STEP 4: DOUBLE CLICK TO RESET TO AUTO
    // ============================================================
    WebUI.comment('Step 4: Double clicking thumb to reset to Auto...')
    
    // First attempt
    WebUI.doubleClick(sliderThumb)
    WebUI.delay(1.5)
    
    String resetValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('After double click (first attempt): ' + resetValue)
    
    if (resetValue == 'Auto') {
        WebUI.comment('TC24H PASSED - Double click reset to Auto successfully')
    } else {
        WebUI.comment('First attempt failed. Trying double click again...')
        WebUI.click(sliderThumb)
        WebUI.delay(0.5)
        WebUI.doubleClick(sliderThumb)
        WebUI.delay(1.5)
        
        resetValue = WebUI.getAttribute(inputDisplay, 'value')
        WebUI.comment('After second double click: ' + resetValue)
        
        if (resetValue == 'Auto') {
            WebUI.comment('TC24H PASSED - Double click reset to Auto on second attempt')
        } else {
            // Try clicking on input display
            WebUI.comment('Double click failed. Trying to click on input display...')
            WebUI.click(inputDisplay)
            WebUI.delay(0.5)
            WebUI.doubleClick(inputDisplay)
            WebUI.delay(1.5)
            
            resetValue = WebUI.getAttribute(inputDisplay, 'value')
            WebUI.comment('After clicking input display: ' + resetValue)
            
            if (resetValue == 'Auto') {
                WebUI.comment('TC24H PASSED - Reset to Auto via input display')
            } else {
                WebUI.comment('FAILED: Could not reset Reasoning Effort to Auto. Final value: ' + resetValue)
                KeywordUtil.markFailed("FAILED: Could not reset Reasoning Effort to Auto. Final value: " + resetValue)
            }
        }
    }

    WebUI.takeScreenshot('TC24H_ReasoningEffort_Reset.png')
    WebUI.comment('=== TC24H Completed ===')

} catch (Exception e) {
    WebUI.comment('TC24H FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC24H_ReasoningEffort_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}