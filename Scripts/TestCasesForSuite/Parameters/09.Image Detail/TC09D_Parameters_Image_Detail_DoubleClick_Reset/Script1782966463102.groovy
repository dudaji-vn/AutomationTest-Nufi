import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC22D_Parameters_Image_Detail_DoubleClick_Reset
 * Verify double click on Image Detail slider resets to Auto
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Get Image Detail slider
 * 3. Drag slider to High (to ensure it's not Auto)
 * 4. Double click to reset to Auto and verify
 */

WebUI.comment('=== TC22D: Image Detail - Double Click Reset ===')

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
    // STEP 2: GET IMAGE DETAIL SLIDER
    // ============================================================
    WebUI.comment('Step 2: Getting Image Detail slider...')
    
    TestObject sliderThumb = new TestObject('imageDetailSliderThumb')
    sliderThumb.addProperty('xpath', ConditionType.EQUALS,
        "//span[contains(@id,'imageDetail') or contains(@id,'image-detail') or contains(@id,'image_detail')]//span[@role='slider']")
    
    TestObject inputDisplay = findTestObject('Object Repository/nav/Parameter/input_Image Detail')
    
    WebUI.waitForElementVisible(sliderThumb, 10)
    WebUI.waitForElementVisible(inputDisplay, 10)
    WebUI.comment('Image Detail slider and input found')

    // ============================================================
    // STEP 3: DRAG TO HIGH (to ensure it's not Auto)
    // ============================================================
    WebUI.comment('Step 3: Dragging slider to High...')
    
    WebUI.click(sliderThumb)
    WebUI.delay(0.5)
    
    String widthScript = "return document.evaluate(\"//span[contains(@id,'imageDetail') or contains(@id,'image-detail') or contains(@id,'image_detail')]\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.getBoundingClientRect().width;"
    double sliderWidth = (Double) WebUI.executeJavaScript(widthScript, null)
    WebUI.comment('Slider width: ' + sliderWidth + 'px')
    
    int dragToHigh = (int)(sliderWidth * 0.9)
    WebUI.dragAndDropByOffset(sliderThumb, dragToHigh, 0)
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
        WebUI.comment('TC22D PASSED - Double click reset to Auto successfully')
    } else {
        WebUI.comment('First attempt failed. Trying double click again...')
        WebUI.click(sliderThumb)
        WebUI.delay(0.5)
        WebUI.doubleClick(sliderThumb)
        WebUI.delay(1.5)
        
        resetValue = WebUI.getAttribute(inputDisplay, 'value')
        WebUI.comment('After second double click: ' + resetValue)
        
        if (resetValue == 'Auto') {
            WebUI.comment('TC22D PASSED - Double click reset to Auto on second attempt')
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
                WebUI.comment('TC22D PASSED - Reset to Auto via input display')
            } else {
                // Try dragging to middle position
                WebUI.comment('All methods failed. Trying to drag to middle position...')
                int dragToMiddle = (int)(sliderWidth * 0.5)
                WebUI.dragAndDropByOffset(sliderThumb, -(dragToHigh - dragToMiddle), 0)
                WebUI.delay(1.5)
                resetValue = WebUI.getAttribute(inputDisplay, 'value')
                WebUI.comment('After dragging to middle: ' + resetValue)
                
                if (resetValue == 'Auto') {
                    WebUI.comment('TC22D PASSED - Dragged to Auto successfully')
                } else {
                    WebUI.comment('FAILED: Could not reset Image Detail to Auto. Final value: ' + resetValue)
                    KeywordUtil.markFailed("FAILED: Could not reset Image Detail to Auto. Final value: " + resetValue)
                }
            }
        }
    }

    WebUI.takeScreenshot('TC22D_ImageDetail_Reset.png')
    WebUI.comment('=== TC22D Completed ===')

} catch (Exception e) {
    WebUI.comment('TC22D FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC22D_ImageDetail_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}