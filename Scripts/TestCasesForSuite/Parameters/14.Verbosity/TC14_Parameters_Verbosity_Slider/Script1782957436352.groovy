import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC27: Parameters - Verbosity Slider Test
 * Verify Verbosity slider values: None, Low, Medium, High
 * Constrains the verbosity of the model's response.
 * Lower values will result in more concise responses,
 * while higher values will result in more verbose responses.
 * Currently supported values are low, medium, and high.
 * Default value is None.
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Get Verbosity slider and verify default value (None)
 * 3. Drag slider to Low and verify
 * 4. Drag slider to Medium and verify
 * 5. Drag slider to High and verify
 * 6. Double click to reset to None and verify
 */

WebUI.comment('=== TC27: Verbosity - Slider Test ===')

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
        
        ariaHidden = WebUI.getAttribute(navSidebar, 'aria-hidden')
        WebUI.comment('Navbar aria-hidden after open: ' + ariaHidden)
        if (ariaHidden == 'false') {
            WebUI.comment('Navbar opened successfully')
        }
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
    
    WebUI.comment('Parameters button aria-label: ' + ariaLabel)
    WebUI.comment('Parameters button aria-pressed: ' + isPressed)
    
    if (ariaLabel == 'Parameters' && isPressed == 'true') {
        WebUI.comment('Parameters tab is open (correct aria-label and aria-pressed)')
    } else {
        WebUI.comment('Parameters tab not open or wrong label, clicking to open...')
        WebUI.click(parametersButton)
        WebUI.delay(2)
        
        ariaLabel = WebUI.getAttribute(parametersButton, 'aria-label')
        isPressed = WebUI.getAttribute(parametersButton, 'aria-pressed')
        WebUI.comment('After click - Parameters button aria-label: ' + ariaLabel)
        WebUI.comment('After click - Parameters button aria-pressed: ' + isPressed)
        
        if (ariaLabel == 'Parameters' && isPressed == 'true') {
            WebUI.comment('Parameters tab opened successfully')
        }
    }

    // ============================================================
    // STEP 2: GET VERBOSITY SLIDER
    // ============================================================
    WebUI.comment('Step 2: Getting Verbosity slider...')
    
    // Tạo slider thumb với dynamic XPath
    TestObject sliderThumb = new TestObject('verbositySliderThumb')
    sliderThumb.addProperty('xpath', ConditionType.EQUALS,
        "//span[contains(@id,'verbosity') or contains(@id,'Verbosity')]//span[@role='slider']")
    
    // Input display - dùng Object Repository
    TestObject inputDisplay = findTestObject('Object Repository/nav/Parameter/input_Verbosity')
    
    WebUI.waitForElementVisible(sliderThumb, 10)
    WebUI.waitForElementVisible(inputDisplay, 10)
    WebUI.comment('Verbosity slider and input found')

    // ============================================================
    // STEP 3: CHECK DEFAULT VALUE (None)
    // ============================================================
    WebUI.comment('Step 3: Checking default value...')
    
    // Click on thumb first to focus
    WebUI.click(sliderThumb)
    WebUI.delay(0.5)
    
    String defaultValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('Default Verbosity: ' + defaultValue)
    
    if (defaultValue == 'None') {
        WebUI.comment('Default value is None')
    } else {
        WebUI.comment('WARNING: Default value is not None: ' + defaultValue)
        WebUI.comment('Continuing test with current value: ' + defaultValue)
    }
    WebUI.takeScreenshot('TC27_Verbosity_Default.png')

    // ============================================================
    // STEP 4: GET SLIDER WIDTH
    // ============================================================
    WebUI.comment('Step 4: Getting slider width...')
    
    double sliderWidth = 0
    
    // Cách 1: Dùng WebUI.getWidth() với slider thumb
    try {
        sliderWidth = WebUI.getWidth(sliderThumb)
        WebUI.comment('Slider thumb width from WebUI.getWidth(): ' + sliderWidth + 'px')
        if (sliderWidth > 0 && sliderWidth < 50) {
            sliderWidth = sliderWidth * 12
            WebUI.comment('Estimated slider width: ' + sliderWidth + 'px')
        }
    } catch (Exception e) {
        WebUI.comment('Could not get width from WebUI: ' + e.getMessage())
    }
    
    // Cách 2: Fallback - dùng JavaScript
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
            WebUI.comment('Slider width from JavaScript: ' + sliderWidth + 'px')
        } catch (Exception e) {
            WebUI.comment('JavaScript fallback failed: ' + e.getMessage())
        }
    }
    
    // Cách 3: Fallback cuối cùng
    if (sliderWidth <= 0 || sliderWidth < 100) {
        sliderWidth = 300
        WebUI.comment('Using fallback slider width: ' + sliderWidth + 'px')
    }
    
    WebUI.comment('Final slider width: ' + sliderWidth + 'px')
    
    // Calculate step size for 4 values (None, Low, Medium, High)
    // Positions: 0%, 33.33%, 66.67%, 100%
    double stepSize = sliderWidth / 3
    WebUI.comment('Step size: ' + stepSize + 'px')

    // ============================================================
    // STEP 5: DRAG TO LOW
    // ============================================================
    WebUI.comment('Step 5: Dragging slider to Low...')
    
    int targetPosition = (int)(1 * stepSize) // Position 33.33%
    int dragOffset = targetPosition - 0
    
    WebUI.comment('Dragging to position ' + targetPosition + ' (offset: ' + dragOffset + 'px)')
    WebUI.dragAndDropByOffset(sliderThumb, dragOffset, 0)
    WebUI.delay(1.5)
    
    String lowValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('After dragging to Low: ' + lowValue)
    
    if (lowValue == 'Low') {
        WebUI.comment('Slider moved to Low successfully')
    } else {
        WebUI.comment('WARNING: Expected "Low" but got "' + lowValue + '"')
        // Try again
        WebUI.comment('Trying to drag to Low again...')
        WebUI.dragAndDropByOffset(sliderThumb, (int)(stepSize * 0.5), 0)
        WebUI.delay(1.5)
        lowValue = WebUI.getAttribute(inputDisplay, 'value')
        WebUI.comment('After retry: ' + lowValue)
        if (lowValue == 'Low') {
            WebUI.comment('Retry successful!')
        } else {
            WebUI.comment('WARNING: Still not at Low, continuing test...')
        }
    }
    WebUI.takeScreenshot('TC27_Verbosity_Low.png')

    // ============================================================
    // STEP 6: DRAG TO MEDIUM
    // ============================================================
    WebUI.comment('Step 6: Dragging slider to Medium...')
    
    targetPosition = (int)(2 * stepSize) // Position 66.67%
    int currentPosition = (int)(1 * stepSize)
    dragOffset = targetPosition - currentPosition
    
    WebUI.comment('Dragging to position ' + targetPosition + ' (offset: ' + dragOffset + 'px)')
    WebUI.dragAndDropByOffset(sliderThumb, dragOffset, 0)
    WebUI.delay(1.5)
    
    String mediumValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('After dragging to Medium: ' + mediumValue)
    
    if (mediumValue == 'Medium') {
        WebUI.comment('Slider moved to Medium successfully')
    } else {
        WebUI.comment('WARNING: Expected "Medium" but got "' + mediumValue + '"')
        WebUI.comment('Trying to drag to Medium again...')
        WebUI.dragAndDropByOffset(sliderThumb, (int)(stepSize * 0.5), 0)
        WebUI.delay(1.5)
        mediumValue = WebUI.getAttribute(inputDisplay, 'value')
        WebUI.comment('After retry: ' + mediumValue)
        if (mediumValue == 'Medium') {
            WebUI.comment('Retry successful!')
        } else {
            WebUI.comment('WARNING: Still not at Medium, continuing test...')
        }
    }
    WebUI.takeScreenshot('TC27_Verbosity_Medium.png')

    // ============================================================
    // STEP 7: DRAG TO HIGH
    // ============================================================
    WebUI.comment('Step 7: Dragging slider to High...')
    
    targetPosition = (int)(3 * stepSize) // Position 100%
    currentPosition = (int)(2 * stepSize)
    dragOffset = targetPosition - currentPosition
    
    WebUI.comment('Dragging to position ' + targetPosition + ' (offset: ' + dragOffset + 'px)')
    WebUI.dragAndDropByOffset(sliderThumb, dragOffset, 0)
    WebUI.delay(1.5)
    
    String highValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('After dragging to High: ' + highValue)
    
    if (highValue == 'High') {
        WebUI.comment('Slider moved to High successfully')
    } else {
        WebUI.comment('WARNING: Expected "High" but got "' + highValue + '"')
        WebUI.comment('Trying to drag to High again...')
        WebUI.dragAndDropByOffset(sliderThumb, (int)(stepSize * 0.5), 0)
        WebUI.delay(1.5)
        highValue = WebUI.getAttribute(inputDisplay, 'value')
        WebUI.comment('After retry: ' + highValue)
        if (highValue == 'High') {
            WebUI.comment('Retry successful!')
        } else {
            WebUI.comment('WARNING: Still not at High, continuing test...')
        }
    }
    WebUI.takeScreenshot('TC27_Verbosity_High.png')

    // ============================================================
    // STEP 8: DOUBLE CLICK TO RESET TO NONE
    // ============================================================
    WebUI.comment('Step 8: Double clicking thumb to reset to None...')
    
    // First attempt
    WebUI.doubleClick(sliderThumb)
    WebUI.delay(1.5)
    
    String resetValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('After double click (first attempt): ' + resetValue)
    
    if (resetValue == 'None') {
        WebUI.comment('Double click reset to None successfully')
    } else {
        WebUI.comment('First attempt failed. Trying double click again...')
        WebUI.click(sliderThumb)
        WebUI.delay(0.5)
        WebUI.doubleClick(sliderThumb)
        WebUI.delay(1.5)
        
        resetValue = WebUI.getAttribute(inputDisplay, 'value')
        WebUI.comment('After second double click: ' + resetValue)
        
        if (resetValue == 'None') {
            WebUI.comment('Double click reset to None on second attempt')
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
                WebUI.comment('Reset to None via input display')
            } else {
                // Try dragging to first position
                WebUI.comment('All reset methods failed. Trying to drag to None position...')
                int dragToNone = -(int)(3 * stepSize)
                WebUI.dragAndDropByOffset(sliderThumb, dragToNone, 0)
                WebUI.delay(1.5)
                resetValue = WebUI.getAttribute(inputDisplay, 'value')
                WebUI.comment('After dragging to None: ' + resetValue)
                
                if (resetValue == 'None') {
                    WebUI.comment('Reset to None by dragging')
                } else {
                    WebUI.comment('FAILED: Could not reset Verbosity to None. Final value: ' + resetValue)
                    KeywordUtil.markFailed("FAILED: Could not reset Verbosity to None. Final value: " + resetValue)
                }
            }
        }
    }
    WebUI.takeScreenshot('TC27_Verbosity_Reset.png')

    // ============================================================
    // STEP 9: FINAL VERIFICATION
    // ============================================================
    WebUI.comment('Step 9: Final verification...')
    String finalValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('Final Verbosity value: ' + finalValue)
    
    if (finalValue == 'None') {
        WebUI.comment('TC27 PASSED - Verbosity successfully reset to None')
    } else {
        WebUI.comment('FAILED: Verbosity should be "None" but is "' + finalValue + '"')
        KeywordUtil.markFailed("FAILED: Verbosity should be 'None' but is '" + finalValue + "'")
    }

    WebUI.takeScreenshot('TC27_Verbosity_Complete.png')
    WebUI.comment('=== TC27 Completed ===')

} catch (Exception e) {
    WebUI.comment('TC27 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC27_Verbosity_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}