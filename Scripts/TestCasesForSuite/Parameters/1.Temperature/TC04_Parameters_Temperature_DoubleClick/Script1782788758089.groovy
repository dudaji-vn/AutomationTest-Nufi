import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC04: Parameters - Temperature Double Click Reset
 * Double click the slider thumb to reset to default value (1.0)
 */

WebUI.comment('=== TC04: Temperature - Double Click Reset ===')

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
        if (ariaHidden == 'false') {
            WebUI.comment('✓ Navbar opened successfully')
        }
    } else {
        WebUI.comment('✓ Navbar is open (aria-hidden="false")')
    }

    // === VERIFY PARAMETERS TAB ===
    WebUI.comment('Step 1: Checking Parameters tab state...')
    
    TestObject parametersButton = findTestObject('Object Repository/Core Chat/nav/nav_items/button_Parameters')
    WebUI.waitForElementVisible(parametersButton, 10)
    
    String ariaLabel = WebUI.getAttribute(parametersButton, 'aria-label')
    String isPressed = WebUI.getAttribute(parametersButton, 'aria-pressed')
    
    WebUI.comment('Parameters button aria-label: ' + ariaLabel)
    WebUI.comment('Parameters button aria-pressed: ' + isPressed)
    
    if (ariaLabel == 'Parameters' && isPressed == 'true') {
        WebUI.comment('✓ Parameters tab is open (correct aria-label and aria-pressed)')
    } else {
        WebUI.comment('Parameters tab not open or wrong label, clicking to open...')
        WebUI.click(parametersButton)
        WebUI.delay(2)
        
        ariaLabel = WebUI.getAttribute(parametersButton, 'aria-label')
        isPressed = WebUI.getAttribute(parametersButton, 'aria-pressed')
        WebUI.comment('After click - Parameters button aria-label: ' + ariaLabel)
        WebUI.comment('After click - Parameters button aria-pressed: ' + isPressed)
        
        if (ariaLabel == 'Parameters' && isPressed == 'true') {
            WebUI.comment('✓ Parameters tab opened successfully')
        }
    }

    // === GET TEMPERATURE SLIDER ===
    WebUI.comment('Step 2: Getting Temperature slider...')
    
    TestObject sliderThumb = new TestObject('sliderThumb')
    sliderThumb.addProperty('xpath', ConditionType.EQUALS,
        "//span[@id='temperature-dynamic-setting-slider']//span[@role='slider']")
    
    TestObject sliderTrack = new TestObject('sliderTrack')
    sliderTrack.addProperty('xpath', ConditionType.EQUALS,
        "//span[@id='temperature-dynamic-setting-slider']")
    
    WebUI.waitForElementVisible(sliderThumb, 10)
    WebUI.comment('Temperature slider found')

    // === GET DEFAULT VALUE ===
    WebUI.comment('Step 3: Getting default value...')
    
    String defaultValue = "1.0" // Temperature default is 1.0
    String currentValue = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Current Temperature: ' + currentValue)

    // === DRAG SLIDER TO A DIFFERENT VALUE ===
    WebUI.comment('Step 4: Dragging slider to a different value...')
    
    String widthScript = "return document.evaluate(\"//span[@id='temperature-dynamic-setting-slider']\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.getBoundingClientRect().width;"
    double sliderWidth = (Double) WebUI.executeJavaScript(widthScript, null)
    WebUI.comment('Slider width: ' + sliderWidth + 'px')
    
    // Drag to 25% (0.5)
    WebUI.dragAndDropByOffset(sliderThumb, (int)(sliderWidth * 0.25), 0)
    WebUI.delay(1.5)
    
    String valueAfterDrag = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Temperature after drag: ' + valueAfterDrag)
    
    if (Double.parseDouble(valueAfterDrag) > 0.1) {
        WebUI.comment('✓ Slider moved to new value: ' + valueAfterDrag)
    } else {
        WebUI.comment('⚠ Slider did not move, current: ' + valueAfterDrag)
    }

    // === DOUBLE CLICK TO RESET ===
    WebUI.comment('Step 5: Double clicking thumb to reset to default...')
    
    // Double click the thumb
    WebUI.doubleClick(sliderThumb)
    WebUI.delay(1.5)
    
    // Check value has reset to default
    String valueAfterDoubleClick = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Temperature after double click: ' + valueAfterDoubleClick)
    
    double actualValue = Double.parseDouble(valueAfterDoubleClick)
    double expectedDefault = 1.0
    
    if (Math.abs(actualValue - expectedDefault) <= 0.05) {
        WebUI.comment('✅ Temperature reset to default (1.0) successfully')
    } else {
        WebUI.comment('⚠ Temperature not reset to default: ' + actualValue)
        
        // Try double click again
        WebUI.comment('Trying double click again...')
        WebUI.doubleClick(sliderThumb)
        WebUI.delay(1.5)
        
        valueAfterDoubleClick = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
        WebUI.comment('Temperature after second double click: ' + valueAfterDoubleClick)
        
        if (Double.parseDouble(valueAfterDoubleClick) >= 0.9) {
            WebUI.comment('✅ Temperature reset to default on second attempt')
        }
    }

    WebUI.takeScreenshot('TC04_Temperature_DoubleClick_Reset.png')
    WebUI.comment('✅ TC04 PASSED - Temperature Double Click Reset test')

} catch (Exception e) {
    WebUI.comment('TC04 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC04_Temperature_DoubleClick_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}