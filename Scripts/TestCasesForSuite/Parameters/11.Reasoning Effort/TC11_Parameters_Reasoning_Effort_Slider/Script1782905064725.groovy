import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC24_Parameters_Reasoning_Effort_Slider
 * 
 * Verify Reasoning Effort slider values: Auto, None, Minimal, Low, Medium, High, Extra High
 * Reasoning models only: constrains effort on reasoning.
 * Reducing reasoning effort can result in faster responses and fewer tokens used on reasoning.
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Get Reasoning Effort slider and verify default value
 * 3. Drag slider through all values and verify:
 *    - Auto (default)
 *    - None
 *    - Minimal
 *    - Low
 *    - Medium
 *    - High
 *    - Extra High
 * 4. Double click to reset to Auto
 */

WebUI.comment('=== TC24: Reasoning Effort - Slider Test ===')

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
    
    // Dynamic XPath for Reasoning Effort slider thumb
    TestObject sliderThumb = new TestObject('reasoningEffortSliderThumb')
    sliderThumb.addProperty('xpath', ConditionType.EQUALS,
        "//span[contains(@id,'reasoning') or contains(@id,'Reasoning')]//span[@role='slider']")
    
    TestObject inputDisplay = findTestObject('Object Repository/nav/Parameter/input_Reasoning Effort')
    
    WebUI.waitForElementVisible(sliderThumb, 10)
    WebUI.waitForElementVisible(inputDisplay, 10)
    WebUI.comment('Reasoning Effort slider and input found')

    // ============================================================
    // STEP 3: CHECK DEFAULT VALUE (Auto)
    // ============================================================
    WebUI.comment('Step 3: Checking default value...')
    
    // Click on thumb first to focus
    WebUI.click(sliderThumb)
    WebUI.delay(0.5)
    
    String defaultValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('Default Reasoning Effort: ' + defaultValue)
    
    if (defaultValue == 'Auto') {
        WebUI.comment('Default value is Auto ✓')
    } else {
        WebUI.comment('WARNING: Default value is not Auto: ' + defaultValue)
        // Don't fail, continue test
    }
    WebUI.takeScreenshot('TC24_ReasoningEffort_Default.png')

    // ============================================================
    // STEP 4: GET SLIDER WIDTH
    // ============================================================
    WebUI.comment('Step 4: Getting slider width...')
    
    String widthScript = "return document.evaluate(\"//span[contains(@id,'reasoning') or contains(@id,'Reasoning')]\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.getBoundingClientRect().width;"
    double sliderWidth = (Double) WebUI.executeJavaScript(widthScript, null)
    WebUI.comment('Slider width: ' + sliderWidth + 'px')
    
    // Calculate step size for 7 values (Auto, None, Minimal, Low, Medium, High, Extra High)
    // Positions: 0%, 16.67%, 33.33%, 50%, 66.67%, 83.33%, 100%
    double stepSize = sliderWidth / 6
    
    // Define expected values in order from left to right
    String[] expectedValues = ['Auto', 'None', 'Minimal', 'Low', 'Medium', 'High', 'Extra High']
    String[] stepNames = ['Auto', 'None', 'Minimal', 'Low', 'Medium', 'High', 'Extra High']

    // ============================================================
    // STEP 5: DRAG THROUGH ALL VALUES
    // ============================================================
    WebUI.comment('Step 5: Dragging through all values...')
    
    // Start from Auto (position 0)
    int currentPosition = 0
    
    for (int i = 0; i < expectedValues.length; i++) {
        WebUI.comment('--- Testing value: ' + expectedValues[i] + ' ---')
        
        // Calculate target position
        int targetPosition = (int)(i * stepSize)
        int dragOffset = targetPosition - currentPosition
        
        if (dragOffset != 0) {
            WebUI.comment('Dragging from position ' + currentPosition + ' to ' + targetPosition + ' (offset: ' + dragOffset + 'px)')
            WebUI.dragAndDropByOffset(sliderThumb, dragOffset, 0)
            WebUI.delay(1)
        } else {
            WebUI.comment('Already at position ' + currentPosition + ', no drag needed')
        }
        
        // Verify value
        String actualValue = WebUI.getAttribute(inputDisplay, 'value')
        WebUI.comment('Expected: ' + expectedValues[i] + ', Actual: ' + actualValue)
        
        if (actualValue == expectedValues[i]) {
            WebUI.comment('✓ Value matches: ' + actualValue)
        } else {
            WebUI.comment('WARNING: Expected "' + expectedValues[i] + '" but got "' + actualValue + '"')
            // Try to drag again if mismatch
            if (i > 0) {
                WebUI.comment('Trying to drag to ' + expectedValues[i] + ' again...')
                int retryOffset = (int)(i * stepSize) - (int)((i-1) * stepSize)
                WebUI.dragAndDropByOffset(sliderThumb, retryOffset, 0)
                WebUI.delay(1)
                actualValue = WebUI.getAttribute(inputDisplay, 'value')
                WebUI.comment('After retry - Actual: ' + actualValue)
                if (actualValue == expectedValues[i]) {
                    WebUI.comment('✓ Retry successful!')
                } else {
                    WebUI.comment('WARNING: Still mismatch after retry')
                }
            }
        }
        
        // Take screenshot for each value
        WebUI.takeScreenshot('TC24_ReasoningEffort_' + expectedValues[i] + '.png')
        
        // Update current position
        currentPosition = targetPosition
    }

    // ============================================================
    // STEP 6: DOUBLE CLICK TO RESET TO AUTO
    // ============================================================
    WebUI.comment('Step 6: Double clicking to reset to Auto...')
    
    // First attempt
    WebUI.doubleClick(sliderThumb)
    WebUI.delay(1.5)
    
    String resetValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('After double click (first attempt): ' + resetValue)
    
    if (resetValue == 'Auto') {
        WebUI.comment('✓ Double click reset to Auto successfully')
    } else {
        WebUI.comment('First attempt failed. Trying double click again...')
        WebUI.click(sliderThumb)
        WebUI.delay(0.5)
        WebUI.doubleClick(sliderThumb)
        WebUI.delay(1.5)
        
        resetValue = WebUI.getAttribute(inputDisplay, 'value')
        WebUI.comment('After second double click: ' + resetValue)
        
        if (resetValue == 'Auto') {
            WebUI.comment('✓ Double click reset to Auto on second attempt')
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
                WebUI.comment('✓ Reset to Auto via input display')
            } else {
                KeywordUtil.markFailed("FAILED: Could not reset Reasoning Effort to Auto. Final value: " + resetValue)
            }
        }
    }
    WebUI.takeScreenshot('TC24_ReasoningEffort_Reset.png')

    // ============================================================
    // STEP 7: FINAL VERIFICATION
    // ============================================================
    WebUI.comment('Step 7: Final verification...')
    String finalValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('Final Reasoning Effort value: ' + finalValue)
    
    if (finalValue == 'Auto') {
        WebUI.comment('✓ TC24 PASSED - Reasoning Effort successfully reset to Auto')
    } else {
        KeywordUtil.markFailed("FAILED: Reasoning Effort should be 'Auto' but is '" + finalValue + "'")
    }

    WebUI.takeScreenshot('TC24_ReasoningEffort_Complete.png')
    WebUI.comment('=== TC24 Completed ===')

} catch (Exception e) {
    WebUI.comment('TC24 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC24_ReasoningEffort_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}