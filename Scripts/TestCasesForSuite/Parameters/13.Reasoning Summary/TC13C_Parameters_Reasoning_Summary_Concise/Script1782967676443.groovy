import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC26C_Parameters_Reasoning_Summary_Concise
 * Verify dragging Reasoning Summary slider to Concise
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Get Reasoning Summary slider
 * 3. Drag slider to Concise and verify
 */

WebUI.comment('=== TC26C: Reasoning Summary - Drag to Concise ===')

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
    // STEP 2: GET REASONING SUMMARY SLIDER
    // ============================================================
    WebUI.comment('Step 2: Getting Reasoning Summary slider...')
    
    TestObject sliderThumb = findTestObject('Object Repository/nav/Parameter/slider_Reasoning Summary_thumb')
    TestObject inputDisplay = findTestObject('Object Repository/nav/Parameter/input_Reasoning Summary')
    
    WebUI.waitForElementVisible(sliderThumb, 10)
    WebUI.waitForElementVisible(inputDisplay, 10)
    WebUI.comment('Reasoning Summary slider and input found')

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
                    "//span[contains(@id,'reasoningSummary') or contains(@id,'ReasoningSummary') or contains(@id,'reasoning-summary')]",
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
    // STEP 4: DRAG TO CONCISE
    // ============================================================
    WebUI.comment('Step 4: Dragging slider to Concise...')
    
    int targetPosition = (int)(2 * stepSize) // Position 66.67%
    int dragOffset = targetPosition - 0
    
    WebUI.comment('Dragging to position ' + targetPosition + ' (offset: ' + dragOffset + 'px)')
    WebUI.dragAndDropByOffset(sliderThumb, dragOffset, 0)
    WebUI.delay(1.5)
    
    String actualValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('After dragging to Concise: ' + actualValue)
    
    if (actualValue == 'Concise') {
        WebUI.comment('TC26C PASSED - Slider moved to Concise successfully')
    } else {
        WebUI.comment('WARNING: First attempt failed. Trying again...')
        WebUI.dragAndDropByOffset(sliderThumb, (int)(stepSize * 0.5), 0)
        WebUI.delay(1.5)
        actualValue = WebUI.getAttribute(inputDisplay, 'value')
        WebUI.comment('After retry: ' + actualValue)
        
        if (actualValue == 'Concise') {
            WebUI.comment('TC26C PASSED - Slider moved to Concise on second attempt')
        } else {
            WebUI.comment('FAILED: Could not drag slider to Concise. Current value: ' + actualValue)
            KeywordUtil.markFailed("FAILED: Could not drag slider to Concise. Current value: " + actualValue)
        }
    }

    WebUI.takeScreenshot('TC26C_ReasoningSummary_Concise.png')
    WebUI.comment('=== TC26C Completed ===')

} catch (Exception e) {
    WebUI.comment('TC26C FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC26C_ReasoningSummary_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}