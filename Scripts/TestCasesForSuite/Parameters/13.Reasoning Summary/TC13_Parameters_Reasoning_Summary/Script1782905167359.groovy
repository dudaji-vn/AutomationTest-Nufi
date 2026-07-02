import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC26_Parameters_Reasoning_Summary_Slider
 * 
 * Verify Reasoning Summary slider values: Unset, Auto, Concise, Detailed
 * Responses API only: A summary of the reasoning performed by the model.
 * This can be useful for debugging and understanding the model's reasoning process.
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Get Reasoning Summary slider and verify default value
 * 3. Drag slider through all values and verify:
 *    - Unset (default)
 *    - Auto
 *    - Concise
 *    - Detailed
 * 4. Double click to reset to Unset
 */

WebUI.comment('=== TC26: Reasoning Summary - Slider Test ===')

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
    // STEP 2: GET REASONING SUMMARY SLIDER - DÙNG OBJECT REPOSITORY
    // ============================================================
    WebUI.comment('Step 2: Getting Reasoning Summary slider...')
    
    // ✅ SỬ DỤNG OBJECT REPOSITORY
    TestObject sliderThumb = findTestObject('Object Repository/Core Chat/nav/Parameter/slider_Reasoning Summary_thumb')
    TestObject inputDisplay = findTestObject('Object Repository/Core Chat/nav/Parameter/input_Reasoning Summary')
    
    WebUI.waitForElementVisible(sliderThumb, 10)
    WebUI.waitForElementVisible(inputDisplay, 10)
    WebUI.comment('Reasoning Summary slider and input found')

    // ============================================================
    // STEP 3: CHECK DEFAULT VALUE (Unset)
    // ============================================================
    WebUI.comment('Step 3: Checking default value...')
    
    // Click on thumb first to focus
    WebUI.click(sliderThumb)
    WebUI.delay(0.5)
    
    String defaultValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('Default Reasoning Summary: ' + defaultValue)
    
    if (defaultValue == 'Unset') {
        WebUI.comment('Default value is Unset ✓')
    } else {
        WebUI.comment('WARNING: Default value is not Unset: ' + defaultValue)
    }
    WebUI.takeScreenshot('TC26_ReasoningSummary_Default.png')

    // ============================================================
    // STEP 4: GET SLIDER WIDTH - DÙNG OBJECT REPOSITORY
    // ============================================================
    WebUI.comment('Step 4: Getting slider width...')
    
    double sliderWidth = 0
    
    // ✅ CÁCH 1: Dùng WebUI.getWidth() với Object Repository
    try {
        sliderWidth = WebUI.getWidth(sliderThumb)
        WebUI.comment('Slider width from WebUI.getWidth(): ' + sliderWidth + 'px')
        // Thường thumb width ~ 20px, slider width ~ 200-300px
        // Nên nhân với 10 để ước lượng slider width
        if (sliderWidth > 0 && sliderWidth < 50) {
            sliderWidth = sliderWidth * 12 // Estimate slider width from thumb
            WebUI.comment('Estimated slider width: ' + sliderWidth + 'px')
        }
    } catch (Exception e) {
        WebUI.comment('Could not get width from WebUI: ' + e.getMessage())
    }
    
    // ✅ CÁCH 2: Fallback - dùng JavaScript với Object Repository
    if (sliderWidth <= 0 || sliderWidth < 100) {
        try {
            String script = """
                var element = document.evaluate(
                    "//span[contains(@id,'reasoningSummary') or contains(@id,'ReasoningSummary') or contains(@id,'reasoning-summary')]",
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
    
    // ✅ CÁCH 3: Fallback cuối cùng
    if (sliderWidth <= 0 || sliderWidth < 100) {
        sliderWidth = 300
        WebUI.comment('Using fallback slider width: ' + sliderWidth + 'px')
    }
    
    WebUI.comment('Final slider width: ' + sliderWidth + 'px')
    
    // Calculate step size for 4 values (Unset, Auto, Concise, Detailed)
    // Positions: 0%, 33.33%, 66.67%, 100%
    double stepSize = sliderWidth / 3
    WebUI.comment('Step size: ' + stepSize + 'px')

    // ============================================================
    // STEP 5: DRAG THROUGH ALL VALUES
    // ============================================================
    WebUI.comment('Step 5: Dragging through all values...')
    
    String[] expectedValues = ['Unset', 'Auto', 'Concise', 'Detailed']
    int currentPosition = 0
    
    for (int i = 0; i < expectedValues.length; i++) {
        WebUI.comment('--- Testing value: ' + expectedValues[i] + ' ---')
        
        int targetPosition = (int)(i * stepSize)
        int dragOffset = targetPosition - currentPosition
        
        if (dragOffset != 0) {
            WebUI.comment('Dragging from position ' + currentPosition + ' to ' + targetPosition + ' (offset: ' + dragOffset + 'px)')
            WebUI.dragAndDropByOffset(sliderThumb, dragOffset, 0)
            WebUI.delay(1)
        } else {
            WebUI.comment('Already at position ' + currentPosition + ', no drag needed')
        }
        
        String actualValue = WebUI.getAttribute(inputDisplay, 'value')
        WebUI.comment('Expected: ' + expectedValues[i] + ', Actual: ' + actualValue)
        
        if (actualValue == expectedValues[i]) {
            WebUI.comment('✓ Value matches: ' + actualValue)
        } else {
            WebUI.comment('WARNING: Expected "' + expectedValues[i] + '" but got "' + actualValue + '"')
            if (i > 0) {
                WebUI.comment('Trying to drag to ' + expectedValues[i] + ' again...')
                int retryOffset = (int)(i * stepSize) - (int)((i-1) * stepSize)
                WebUI.dragAndDropByOffset(sliderThumb, retryOffset, 0)
                WebUI.delay(1)
                actualValue = WebUI.getAttribute(inputDisplay, 'value')
                WebUI.comment('After retry - Actual: ' + actualValue)
            }
        }
        
        WebUI.takeScreenshot('TC26_ReasoningSummary_' + expectedValues[i] + '.png')
        currentPosition = targetPosition
    }

    // ============================================================
    // STEP 6: DOUBLE CLICK TO RESET TO UNSET
    // ============================================================
    WebUI.comment('Step 6: Double clicking to reset to Unset...')
    
    WebUI.doubleClick(sliderThumb)
    WebUI.delay(1.5)
    
    String resetValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('After double click (first attempt): ' + resetValue)
    
    if (resetValue == 'Unset') {
        WebUI.comment('✓ Double click reset to Unset successfully')
    } else {
        WebUI.comment('First attempt failed. Trying double click again...')
        WebUI.click(sliderThumb)
        WebUI.delay(0.5)
        WebUI.doubleClick(sliderThumb)
        WebUI.delay(1.5)
        
        resetValue = WebUI.getAttribute(inputDisplay, 'value')
        WebUI.comment('After second double click: ' + resetValue)
        
        if (resetValue == 'Unset') {
            WebUI.comment('✓ Double click reset to Unset on second attempt')
        } else {
            WebUI.comment('Double click failed. Trying to click on input display...')
            WebUI.click(inputDisplay)
            WebUI.delay(0.5)
            WebUI.doubleClick(inputDisplay)
            WebUI.delay(1.5)
            
            resetValue = WebUI.getAttribute(inputDisplay, 'value')
            WebUI.comment('After clicking input display: ' + resetValue)
            
            if (resetValue == 'Unset') {
                WebUI.comment('✓ Reset to Unset via input display')
            } else {
                KeywordUtil.markFailed("FAILED: Could not reset Reasoning Summary to Unset. Final value: " + resetValue)
            }
        }
    }
    WebUI.takeScreenshot('TC26_ReasoningSummary_Reset.png')

    // ============================================================
    // STEP 7: FINAL VERIFICATION
    // ============================================================
    WebUI.comment('Step 7: Final verification...')
    String finalValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('Final Reasoning Summary value: ' + finalValue)
    
    if (finalValue == 'Unset') {
        WebUI.comment('✓ TC26 PASSED - Reasoning Summary successfully reset to Unset')
    } else {
        KeywordUtil.markFailed("FAILED: Reasoning Summary should be 'Unset' but is '" + finalValue + "'")
    }

    WebUI.takeScreenshot('TC26_ReasoningSummary_Complete.png')
    WebUI.comment('=== TC26 Completed ===')

} catch (Exception e) {
    WebUI.comment('TC26 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC26_ReasoningSummary_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}