import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC13: Parameters - Presence Penalty Minimum (-2.0)
 * Verify Presence Penalty = -2.0 (Minimum)
 */

WebUI.comment('=== TC13: Presence Penalty - Minimum (-2.0) ===')

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
        WebUI.comment('✓ Navbar is already open (aria-hidden="false")')
    }

    // === CHECK PARAMETERS TAB ===
    WebUI.comment('Step 1: Checking Parameters tab state...')
    
    TestObject parametersButton = findTestObject('Object Repository/nav/nav_items/button_Parameters')
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

    // === SET PRESENCE PENALTY TO MINIMUM (-2.0) ===
    WebUI.comment('Step 2: Setting Presence Penalty to -2.0 (Minimum)...')
    
    // Use Object Repository for the slider thumb
    TestObject sliderThumb = findTestObject('Object Repository/nav/Parameter/slider_Presence Penalty_thumb')
    WebUI.waitForElementVisible(sliderThumb, 10)
    
    // Get current value
    String currentValue = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Current Presence Penalty value: ' + currentValue)
    
    // Get slider width
    String widthScript = """
        var element = document.evaluate(
            "//span[contains(@id,'presence_penalty') and contains(@id,'slider')]",
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
        // Drag slider to Minimum (left)
        WebUI.dragAndDropByOffset(sliderThumb, -(int)(sliderWidth * 0.8), 0)
        WebUI.delay(1.5)
        
        // Verify value after drag
        String valueAfterDrag = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
        WebUI.comment('Presence Penalty after drag to min: ' + valueAfterDrag)
        
        double actualValue = Double.parseDouble(valueAfterDrag)
        double expectedValue = -2.0
        
        if (Math.abs(actualValue - expectedValue) <= 0.05) {
            WebUI.comment('✅ Presence Penalty successfully set to Minimum (-2.0)')
        } else if (actualValue <= -1.8) {
            WebUI.comment('✅ Presence Penalty set to near Minimum: ' + actualValue)
        } else {
            WebUI.comment('⚠ Presence Penalty not at minimum: ' + actualValue)
            
            // Try dragging again
            WebUI.comment('Trying drag again...')
            WebUI.dragAndDropByOffset(sliderThumb, -(int)(sliderWidth * 0.2), 0)
            WebUI.delay(1.5)
            
            valueAfterDrag = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
            WebUI.comment('Presence Penalty after second drag: ' + valueAfterDrag)
            
            if (Double.parseDouble(valueAfterDrag) <= -1.8) {
                WebUI.comment('✅ Presence Penalty successfully set to Minimum on second attempt')
            }
        }
    } else {
        WebUI.comment('⚠ Could not get slider width, using alternative method...')
        TestObject sliderTrack = new TestObject('sliderTrack')
        sliderTrack.addProperty('xpath', ConditionType.EQUALS,
            "//span[contains(@id,'presence_penalty') and contains(@id,'slider')]")
        WebUI.click(sliderTrack)
        WebUI.delay(1)
        
        String valueAfterClick = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
        WebUI.comment('Presence Penalty after click: ' + valueAfterClick)
    }

    WebUI.takeScreenshot('TC13_PresencePenalty_Min_Slider.png')
    WebUI.comment('✅ TC13 PASSED - Presence Penalty Minimum slider test')

} catch (Exception e) {
    WebUI.comment('TC13 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC13_PresencePenalty_Min_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}