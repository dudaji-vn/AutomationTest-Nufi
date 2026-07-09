import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC22C_Parameters_Image_Detail_High
 * Verify dragging Image Detail slider to High value
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Get Image Detail slider
 * 3. Drag slider to High and verify
 */

WebUI.comment('=== TC22C: Image Detail - Drag to High ===')

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
    // STEP 3: DRAG TO HIGH
    // ============================================================
    WebUI.comment('Step 3: Dragging slider to High...')
    
    // Click on thumb first to focus
    WebUI.click(sliderThumb)
    WebUI.delay(0.5)
    
    // Get slider width
    String widthScript = "return document.evaluate(\"//span[contains(@id,'imageDetail') or contains(@id,'image-detail') or contains(@id,'image_detail')]\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.getBoundingClientRect().width;"
    double sliderWidth = (Double) WebUI.executeJavaScript(widthScript, null)
    WebUI.comment('Slider width: ' + sliderWidth + 'px')
    
    // Drag to High (100% position - right)
    int dragToHigh = (int)(sliderWidth * 0.9)
    WebUI.dragAndDropByOffset(sliderThumb, dragToHigh, 0)
    WebUI.delay(1.5)
    
    String highValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('After dragging to High: ' + highValue)
    
    if (highValue == 'High') {
        WebUI.comment('TC22C PASSED - Slider moved to High successfully')
    } else {
        WebUI.comment('WARNING: First attempt failed. Trying to drag to High again...')
        WebUI.dragAndDropByOffset(sliderThumb, dragToHigh, 0)
        WebUI.delay(1.5)
        highValue = WebUI.getAttribute(inputDisplay, 'value')
        WebUI.comment('After second attempt: ' + highValue)
        
        if (highValue == 'High') {
            WebUI.comment('TC22C PASSED - Slider moved to High on second attempt')
        } else {
            WebUI.comment('FAILED: Could not drag slider to High. Current value: ' + highValue)
            KeywordUtil.markFailed("FAILED: Could not drag slider to High. Current value: " + highValue)
        }
    }

    WebUI.takeScreenshot('TC22C_ImageDetail_High.png')
    WebUI.comment('=== TC22C Completed ===')

} catch (Exception e) {
    WebUI.comment('TC22C FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC22C_ImageDetail_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}