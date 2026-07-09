import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC27B_Parameters_Verbosity_Low
 * Verify dragging Verbosity slider to Low
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Get Verbosity slider
 * 3. Drag slider to Low and verify
 */

WebUI.comment('=== TC27B: Verbosity - Drag to Low ===')

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
    // STEP 3: GET SLIDER WIDTH
    // ============================================================
    WebUI.comment('Step 3: Getting slider width...')
    
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
    
    WebUI.comment('Final slider width: ' + sliderWidth + 'px')
    double stepSize = sliderWidth / 3
    WebUI.comment('Step size: ' + stepSize + 'px')

    // ============================================================
    // STEP 4: DRAG TO LOW
    // ============================================================
    WebUI.comment('Step 4: Dragging slider to Low...')
    
    int targetPosition = (int)(1 * stepSize)
    int dragOffset = targetPosition - 0
    
    WebUI.comment('Dragging to position ' + targetPosition + ' (offset: ' + dragOffset + 'px)')
    WebUI.dragAndDropByOffset(sliderThumb, dragOffset, 0)
    WebUI.delay(1.5)
    
    String actualValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('After dragging to Low: ' + actualValue)
    
    if (actualValue == 'Low') {
        WebUI.comment('TC27B PASSED - Slider moved to Low successfully')
    } else {
        WebUI.comment('WARNING: First attempt failed. Trying again...')
        WebUI.dragAndDropByOffset(sliderThumb, (int)(stepSize * 0.5), 0)
        WebUI.delay(1.5)
        actualValue = WebUI.getAttribute(inputDisplay, 'value')
        WebUI.comment('After retry: ' + actualValue)
        
        if (actualValue == 'Low') {
            WebUI.comment('TC27B PASSED - Slider moved to Low on second attempt')
        } else {
            WebUI.comment('FAILED: Could not drag slider to Low. Current value: ' + actualValue)
            KeywordUtil.markFailed("FAILED: Could not drag slider to Low. Current value: " + actualValue)
        }
    }

    WebUI.takeScreenshot('TC27B_Verbosity_Low.png')
    WebUI.comment('=== TC27B Completed ===')

} catch (Exception e) {
    WebUI.comment('TC27B FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC27B_Verbosity_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}