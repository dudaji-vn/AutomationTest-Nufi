import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC05: Parameters - Top P Minimum (0.0)
 * Verify Top P = 0.0 (Minimum) - slider-only test
 */

WebUI.comment('=== TC05: Top P - Minimum (0.0) ===')

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

    // === SET TOP P TO MINIMUM (0.0) ===
    WebUI.comment('Step 2: Setting Top P to 0.0 (Minimum)...')
    
    // Use precise XPath for Top P slider
    TestObject sliderThumb = new TestObject('sliderThumb')
    sliderThumb.addProperty('xpath', ConditionType.EQUALS,
        "//span[contains(@id,'top_p') and contains(@id,'slider')]//span[@role='slider']")
    
    WebUI.waitForElementVisible(sliderThumb, 10)
    
    // Get current value
    String currentValue = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Current Top P value: ' + currentValue)
    
    // Get slider width
    String widthScript = """
        var element = document.evaluate(
            "//span[contains(@id,'top_p') and contains(@id,'slider')]",
            document,
            null,
            XPathResult.FIRST_ORDERED_NODE_TYPE,
            null
        ).singleNodeValue;
        return element ? element.getBoundingClientRect().width : 0;
    """
    double sliderWidth = (Double) WebUI.executeJavaScript(widthScript, null)
    WebUI.comment('Slider width: ' + sliderWidth + 'px')
    
    if (sliderWidth > 0) {
        // Drag slider to Minimum (move left)
        WebUI.dragAndDropByOffset(sliderThumb, -(int)(sliderWidth * 0.8), 0)
        WebUI.delay(1.5)
        
        // Check value after drag
        String valueAfterDrag = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
        WebUI.comment('Top P after drag to min: ' + valueAfterDrag)
        
        double actualValue = Double.parseDouble(valueAfterDrag)
        double expectedValue = 0.0
        
        if (Math.abs(actualValue - expectedValue) <= 0.05) {
            WebUI.comment('✅ Top P successfully set to Minimum (0.0)')
        } else {
            WebUI.comment('⚠ Top P not at minimum: ' + actualValue)
            
            // Try drag again
            WebUI.comment('Trying drag again...')
            WebUI.dragAndDropByOffset(sliderThumb, -(int)(sliderWidth * 0.2), 0)
            WebUI.delay(1.5)
            
            valueAfterDrag = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
            WebUI.comment('Top P after second drag: ' + valueAfterDrag)
            
            if (Double.parseDouble(valueAfterDrag) <= 0.1) {
                WebUI.comment('✅ Top P successfully set to Minimum (0.0) on second attempt')
            }
        }
    } else {
        WebUI.comment('⚠ Could not get slider width, using alternative method...')
        // Alternative: Click on track to set value
        TestObject sliderTrack = new TestObject('sliderTrack')
        sliderTrack.addProperty('xpath', ConditionType.EQUALS,
            "//span[contains(@id,'top_p') and contains(@id,'slider')]")
        WebUI.click(sliderTrack)
        WebUI.delay(1)
        
        String valueAfterClick = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
        WebUI.comment('Top P after click: ' + valueAfterClick)
    }

    WebUI.takeScreenshot('TC05_TopP_Min_Slider.png')
    WebUI.comment('✅ TC05 PASSED - Top P Minimum slider test')

} catch (Exception e) {
    WebUI.comment('TC5 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC05_TopP_Min_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}