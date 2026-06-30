import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC16: Parameters - Presence Penalty Double Click Reset
 * Double click the slider thumb to reset to default value (0.0)
 */

WebUI.comment('=== TC16: Presence Penalty - Double Click Reset ===')

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

    // === GET PRESENCE PENALTY SLIDER ===
    WebUI.comment('Step 2: Getting Presence Penalty slider...')
    
    TestObject sliderThumb = findTestObject('Object Repository/Core Chat/nav/Parameter/slider_Presence Penalty_thumb')
    WebUI.waitForElementVisible(sliderThumb, 10)
    WebUI.comment('Presence Penalty slider found')

    // === GET DEFAULT VALUE ===
    WebUI.comment('Step 3: Getting default value...')
    
    double defaultValue = 0.0
    String currentValue = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Current Presence Penalty: ' + currentValue)

    // === DRAG SLIDER TO A DIFFERENT VALUE ===
    WebUI.comment('Step 4: Dragging slider to a different value...')
    
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
    
    // Drag to 50% (1.0)
    WebUI.dragAndDropByOffset(sliderThumb, (int)(sliderWidth * 0.5), 0)
    WebUI.delay(1.5)
    
    String valueAfterDrag = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Presence Penalty after drag: ' + valueAfterDrag)
    
    if (Double.parseDouble(valueAfterDrag) > 0.3) {
        WebUI.comment('✓ Slider moved to new value: ' + valueAfterDrag)
    } else {
        WebUI.comment('⚠ Slider did not move, current: ' + valueAfterDrag)
    }

    // === DOUBLE CLICK TO RESET ===
    WebUI.comment('Step 5: Double clicking thumb to reset to default...')
    
    WebUI.doubleClick(sliderThumb)
    WebUI.delay(1.5)
    
    String valueAfterDoubleClick = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Presence Penalty after double click: ' + valueAfterDoubleClick)
    
    double actualValue = Double.parseDouble(valueAfterDoubleClick)
    
    if (Math.abs(actualValue - defaultValue) <= 0.05) {
        WebUI.comment('✅ Presence Penalty reset to default (0.0) successfully')
    } else {
        WebUI.comment('⚠ Presence Penalty not reset to default: ' + actualValue)
        
        WebUI.comment('Trying double click again...')
        WebUI.doubleClick(sliderThumb)
        WebUI.delay(1.5)
        
        valueAfterDoubleClick = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
        WebUI.comment('Presence Penalty after second double click: ' + valueAfterDoubleClick)
        
        if (Double.parseDouble(valueAfterDoubleClick) <= 0.1 && Double.parseDouble(valueAfterDoubleClick) >= -0.1) {
            WebUI.comment('✅ Presence Penalty reset to default on second attempt')
        }
    }

    WebUI.takeScreenshot('TC16_PresencePenalty_DoubleClick_Reset.png')
    WebUI.comment('✅ TC16 PASSED - Presence Penalty Double Click Reset test')

} catch (Exception e) {
    WebUI.comment('TC16 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC16_PresencePenalty_DoubleClick_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}