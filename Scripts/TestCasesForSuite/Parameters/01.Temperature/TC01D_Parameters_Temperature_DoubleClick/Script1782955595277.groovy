import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC01D: Parameters - Temperature Double Click Reset
 * Double click the slider thumb to reset to default value (1.0)
 */

WebUI.comment('=== TC01D: Temperature - Double Click Reset ===')

try {
    // === CHECK NAVBAR ===
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
    } else {
        WebUI.comment('Navbar is open (aria-hidden="false")')
    }

    // === VERIFY PARAMETERS TAB ===
    WebUI.comment('Step 1: Checking Parameters tab state...')
   
    TestObject parametersButton = findTestObject('Object Repository/Core Chat/nav/nav_items/button_Parameters')
    WebUI.waitForElementVisible(parametersButton, 10)
   
    String ariaLabel = WebUI.getAttribute(parametersButton, 'aria-label')
    String isPressed = WebUI.getAttribute(parametersButton, 'aria-pressed')
   
    if (ariaLabel == 'Parameters' && isPressed == 'true') {
        WebUI.comment('Parameters tab is open')
    } else {
        WebUI.comment('Parameters tab not open, clicking to open...')
        WebUI.click(parametersButton)
        WebUI.delay(2)
    }

    // === GET TEMPERATURE SLIDER ===
    WebUI.comment('Step 2: Getting Temperature slider...')
   
    TestObject sliderThumb = new TestObject('sliderThumb')
    sliderThumb.addProperty('xpath', ConditionType.EQUALS,
        "//span[@id='temperature-dynamic-setting-slider']//span[@role='slider']")
   
    WebUI.waitForElementVisible(sliderThumb, 10)
    WebUI.comment('Temperature slider found')

    // === DRAG SLIDER TO DIFFERENT VALUE ===
    WebUI.comment('Step 3: Dragging slider to a different value...')
   
    String widthScript = "return document.evaluate(\"//span[@id='temperature-dynamic-setting-slider']\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.getBoundingClientRect().width;"
    double sliderWidth = (Double) WebUI.executeJavaScript(widthScript, null)
    WebUI.comment('Slider width: ' + sliderWidth + 'px')
   
    WebUI.dragAndDropByOffset(sliderThumb, (int)(sliderWidth * 0.25), 0)
    WebUI.delay(1.5)
   
    String valueAfterDrag = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Temperature after drag: ' + valueAfterDrag)

    // === DOUBLE CLICK TO RESET ===
    WebUI.comment('Step 4: Double clicking thumb to reset to default...')
   
    WebUI.doubleClick(sliderThumb)
    WebUI.delay(1.5)
   
    String valueAfterDoubleClick = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Temperature after double click: ' + valueAfterDoubleClick)
   
    // Numeric comparison (supports 1, 1.0, 1.00, 1.000...)
    boolean isResetCorrect = false
    try {
        BigDecimal actualNum = new BigDecimal(valueAfterDoubleClick)
        BigDecimal expectedNum = new BigDecimal("1.0")
        
        if (actualNum.compareTo(expectedNum) == 0) {
            WebUI.comment('Temperature reset to default (1.0) successfully')
            isResetCorrect = true
        }
    } catch (Exception e) {
        WebUI.comment('Cannot convert value to number: ' + valueAfterDoubleClick)
    }
   
    // Retry if first attempt failed
    if (!isResetCorrect) {
        WebUI.comment('Temperature not reset on first attempt. Trying again...')
        WebUI.doubleClick(sliderThumb)
        WebUI.delay(1.5)
       
        valueAfterDoubleClick = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
        WebUI.comment('Temperature after second double click: ' + valueAfterDoubleClick)
       
        try {
            BigDecimal actualNum = new BigDecimal(valueAfterDoubleClick)
            BigDecimal expectedNum = new BigDecimal("1.0")
            
            if (actualNum.compareTo(expectedNum) == 0) {
                WebUI.comment('Temperature reset to default on second attempt')
                isResetCorrect = true
            } else {
                KeywordUtil.markFailed("FAILED: Temperature did not reset to 1.0. Actual: " + valueAfterDoubleClick)
            }
        } catch (Exception e) {
            KeywordUtil.markFailed("FAILED: Cannot convert value to number after retry")
        }
    }
    
    WebUI.takeScreenshot('TC04_Temperature_DoubleClick_Reset.png')
    WebUI.comment('TC04 PASSED - Temperature Double Click Reset test completed')

} catch (Exception e) {
    WebUI.comment('TC04 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC04_Temperature_DoubleClick_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}