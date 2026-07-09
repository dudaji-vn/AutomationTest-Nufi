import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC24D_Parameters_Reasoning_Effort_Low
 * Verify dragging Reasoning Effort slider to Low
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Get Reasoning Effort slider
 * 3. Drag slider to Low and verify
 */

WebUI.comment('=== TC24D: Reasoning Effort - Drag to Low ===')

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
    // STEP 3: GET SLIDER WIDTH
    // ============================================================
    WebUI.comment('Step 3: Getting slider width...')
    
    WebUI.click(sliderThumb)
    WebUI.delay(0.5)
    
    String widthScript = "return document.evaluate(\"//span[contains(@id,'reasoning') or contains(@id,'Reasoning')]\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.getBoundingClientRect().width;"
    double sliderWidth = (Double) WebUI.executeJavaScript(widthScript, null)
    WebUI.comment('Slider width: ' + sliderWidth + 'px')
    
    double stepSize = sliderWidth / 6

    // ============================================================
    // STEP 4: DRAG TO LOW
    // ============================================================
    WebUI.comment('Step 4: Dragging slider to Low...')
    
    int targetPosition = (int)(3 * stepSize) // Position 50%
    int dragOffset = targetPosition - 0
    
    WebUI.comment('Dragging to position ' + targetPosition + ' (offset: ' + dragOffset + 'px)')
    WebUI.dragAndDropByOffset(sliderThumb, dragOffset, 0)
    WebUI.delay(1.5)
    
    String actualValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('After dragging to Low: ' + actualValue)
    
    if (actualValue == 'Low') {
        WebUI.comment('TC24D PASSED - Slider moved to Low successfully')
    } else {
        WebUI.comment('WARNING: First attempt failed. Trying again...')
        WebUI.dragAndDropByOffset(sliderThumb, (int)(stepSize * 0.5), 0)
        WebUI.delay(1.5)
        actualValue = WebUI.getAttribute(inputDisplay, 'value')
        WebUI.comment('After retry: ' + actualValue)
        
        if (actualValue == 'Low') {
            WebUI.comment('TC24D PASSED - Slider moved to Low on second attempt')
        } else {
            WebUI.comment('FAILED: Could not drag slider to Low. Current value: ' + actualValue)
            KeywordUtil.markFailed("FAILED: Could not drag slider to Low. Current value: " + actualValue)
        }
    }

    WebUI.takeScreenshot('TC24D_ReasoningEffort_Low.png')
    WebUI.comment('=== TC24D Completed ===')

} catch (Exception e) {
    WebUI.comment('TC24D FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC24D_ReasoningEffort_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}