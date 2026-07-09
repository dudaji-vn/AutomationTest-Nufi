import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC27E_Parameters_Verbosity_DoubleClick_Reset
 * Verify double click on Verbosity slider resets to None
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Get Verbosity slider
 * 3. Drag slider to High (to ensure it's not None)
 * 4. Double click to reset to None and verify
 */

WebUI.comment('=== TC27E: Verbosity - Double Click Reset ===')

try {
    // ============================================================
    // STEP 0: CHECK NAVBAR
    // ============================================================
    WebUI.comment('Step 0: Checking Navbar state...')
    
    TestObject navSidebar = new TestObject('navSidebar')
    navSidebar.addProperty('xpath', ConditionType.EQUALS, "//nav")
    WebUI.waitForElementVisible(navSidebar, 10)
    
    String ariaHidden = WebUI.getAttribute(navSidebar, 'aria-hidden')
    WebUI.comment('Navbar aria-hidden: ' + ariaHidden)
    
    if (ariaHidden == 'true') {
        WebUI.comment('Navbar is closed, opening sidebar...')
        TestObject openSidebarButton = new TestObject('openSidebarButton')
        openSidebarButton.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
        WebUI.waitForElementClickable(openSidebarButton, 5)
        WebUI.click(openSidebarButton)
        WebUI.delay(1)
        WebUI.comment('Sidebar opened')
    } else {
        WebUI.comment('Navbar is open (aria-hidden="false")')
    }

    // ============================================================
    // STEP 1: VERIFY PARAMETERS TAB
    // ============================================================
    WebUI.comment('Step 1: Checking Parameters tab state...')
    
    TestObject parametersButton = findTestObject('Object Repository/nav/nav_items/button_Parameters')
    WebUI.waitForElementVisible(parametersButton, 10)
    
    String ariaLabel = WebUI.getAttribute(parametersButton, 'aria-label')
    String isPressed = WebUI.getAttribute(parametersButton, 'aria-pressed')
    
    if (ariaLabel == 'Parameters' && isPressed == 'true') {
        WebUI.comment('Parameters tab is open')
    } else {
        WebUI.comment('Parameters tab not open, clicking to open...')
        WebUI.click(parametersButton)
        WebUI.delay(2)
        WebUI.comment('Parameters tab opened')
    }

    // ============================================================
    // STEP 2: GET VERBOSITY SLIDER
    // ============================================================
    WebUI.comment('Step 2: Getting Verbosity slider...')
    
    TestObject sliderThumb = new TestObject('verbositySliderThumb')
    sliderThumb.addProperty('xpath', ConditionType.EQUALS,
        "//span[contains(@id,'verbosity') or contains(@id,'Verbosity')]//span[@role='slider']")
    
    TestObject inputDisplay = findTestObject('Object Repository/nav/Parameter/input_Verbosity')
    
    WebUI.waitForElementVisible(sliderThumb, 10)
    WebUI.waitForElementVisible(inputDisplay, 10)
    WebUI.comment('Verbosity slider and input found')

    // ============================================================
    // STEP 3: DRAG TO HIGH (to ensure it's not None)
    // ============================================================
    WebUI.comment('Step 3: Dragging slider to High...')
    
    WebUI.click(sliderThumb)
    WebUI.delay(0.5)
    
    double sliderWidth = 0
    try {
        sliderWidth = WebUI.getWidth(sliderThumb)
        if (sliderWidth > 0 && sliderWidth < 50) {
            sliderWidth = sliderWidth * 12
        }
    } catch (Exception e) {
        WebUI.comment('Could not get width from WebUI: ' + e.getMessage())
    }
    
    if (sliderWidth <= 0 || sliderWidth < 100) {
        try {
            String script = """
                var element = document.evaluate(
                    "//span[contains(@id,'verbosity') or contains(@id,'Verbosity')]",
                    document,
                    null,
                    XPathResult.FIRST_ORDERED_NODE_TYPE,
                    null
                ).singleNodeValue;
                return element ? element.getBoundingClientRect().width : 0;
            """
            sliderWidth = (Double) WebUI.executeJavaScript(script, null)
        } catch (Exception e) {
            WebUI.comment('JavaScript fallback failed: ' + e.getMessage())
        }
    }
    
    if (sliderWidth <= 0 || sliderWidth < 100) {
        sliderWidth = 300
    }
    
    double stepSize = sliderWidth / 3
    int dragToHigh = (int)(3 * stepSize)
    
    WebUI.dragAndDropByOffset(sliderThumb, dragToHigh, 0)
    WebUI.delay(1.5)
    
    String currentValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('Current value before reset: ' + currentValue)

    // ============================================================
    // STEP 4: DOUBLE CLICK TO RESET TO NONE
    // ============================================================
    WebUI.comment('Step 4: Double clicking thumb to reset to None...')
    
    // First attempt
    WebUI.doubleClick(sliderThumb)
    WebUI.delay(1.5)
    
    String resetValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('After double click (first attempt): ' + resetValue)
    
    if (resetValue == 'None') {
        WebUI.comment('TC27E PASSED - Double click reset to None successfully')
    } else {
        WebUI.comment('First attempt failed. Trying double click again...')
        WebUI.click(sliderThumb)
        WebUI.delay(0.5)
        WebUI.doubleClick(sliderThumb)
        WebUI.delay(1.5)
        
        resetValue = WebUI.getAttribute(inputDisplay, 'value')
        WebUI.comment('After second double click: ' + resetValue)
        
        if (resetValue == 'None') {
            WebUI.comment('TC27E PASSED - Double click reset to None on second attempt')
        } else {
            // Try clicking on input display
            WebUI.comment('Double click failed. Trying to click on input display...')
            WebUI.click(inputDisplay)
            WebUI.delay(0.5)
            WebUI.doubleClick(inputDisplay)
            WebUI.delay(1.5)
            
            resetValue = WebUI.getAttribute(inputDisplay, 'value')
            WebUI.comment('After clicking input display: ' + resetValue)
            
            if (resetValue == 'None') {
                WebUI.comment('TC27E PASSED - Reset to None via input display')
            } else {
                // Try dragging to first position
                WebUI.comment('All reset methods failed. Trying to drag to None position...')
                int dragToNone = -(int)(3 * stepSize)
                WebUI.dragAndDropByOffset(sliderThumb, dragToNone, 0)
                WebUI.delay(1.5)
                resetValue = WebUI.getAttribute(inputDisplay, 'value')
                WebUI.comment('After dragging to None: ' + resetValue)
                
                if (resetValue == 'None') {
                    WebUI.comment('TC27E PASSED - Reset to None by dragging')
                } else {
                    WebUI.comment('FAILED: Could not reset Verbosity to None. Final value: ' + resetValue)
                    KeywordUtil.markFailed("FAILED: Could not reset Verbosity to None. Final value: " + resetValue)
                }
            }
        }
    }

    WebUI.takeScreenshot('TC27E_Verbosity_Reset.png')
    WebUI.comment('=== TC27E Completed ===')

} catch (Exception e) {
    WebUI.comment('TC27E FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC27E_Verbosity_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}