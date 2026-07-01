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

    if (WebUI.getAttribute(navSidebar, 'aria-hidden') == 'true') {
        WebUI.comment('Navbar is closed, opening sidebar...')
        TestObject openBtn = new TestObject('openSidebarButton')
        openBtn.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
        WebUI.click(openBtn)
        WebUI.delay(1)
    }

    // === OPEN PARAMETERS TAB ===
    WebUI.comment('Step 1: Opening Parameters tab...')
    TestObject parametersButton = findTestObject('Object Repository/Core Chat/nav/nav_items/button_Parameters')
    WebUI.waitForElementVisible(parametersButton, 10)

    if (WebUI.getAttribute(parametersButton, 'aria-pressed') != 'true') {
        WebUI.click(parametersButton)
        WebUI.delay(2)
    }

    // === PRESENCE PENALTY SLIDER ===
    WebUI.comment('Step 2: Getting Presence Penalty slider...')
    TestObject sliderThumb = new TestObject('presencePenaltySliderThumb')
    sliderThumb.addProperty('xpath', ConditionType.EQUALS,
        "//span[contains(@id,'presence_penalty') or contains(@id,'presence-penalty') or contains(@id,'presencePenalty')]//span[@role='slider']")

    WebUI.waitForElementVisible(sliderThumb, 10)
    WebUI.comment('Presence Penalty slider found')

    // === DRAG SLIDER TO DIFFERENT VALUE ===
    WebUI.comment('Step 3: Dragging slider to a different value...')
    WebUI.click(sliderThumb)   // Focus first
    WebUI.delay(0.5)

    String widthScript = """
        var element = document.evaluate(
            "//span[contains(@id,'presence_penalty') or contains(@id,'presence-penalty') or contains(@id,'presencePenalty')]",
            document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null
        ).singleNodeValue;
        return element ? element.getBoundingClientRect().width : 0;
    """
    double sliderWidth = (Double) WebUI.executeJavaScript(widthScript, null)
    WebUI.comment('Slider width: ' + sliderWidth + 'px')

    WebUI.dragAndDropByOffset(sliderThumb, (int)(sliderWidth * 0.6), 0)
    WebUI.delay(1.5)

    String valueAfterDrag = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Presence Penalty after drag: ' + valueAfterDrag)

    // === DOUBLE CLICK TO RESET ===
    WebUI.comment('Step 4: Double clicking thumb to reset to default...')
    WebUI.doubleClick(sliderThumb)
    WebUI.delay(1.5)

    String valueAfterDoubleClick = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Presence Penalty after double click: ' + valueAfterDoubleClick)

    // Numeric verification
    boolean isResetSuccess = false
    try {
        BigDecimal actualNum = new BigDecimal(valueAfterDoubleClick)
        BigDecimal expectedDefault = new BigDecimal("0.0")

        if (actualNum.compareTo(expectedDefault) == 0) {
            WebUI.comment('Presence Penalty successfully reset to default value (0.0)')
            isResetSuccess = true
        }
    } catch (Exception e) {
        WebUI.comment('Cannot convert value to number: ' + valueAfterDoubleClick)
    }

    // Retry if first attempt failed
    if (!isResetSuccess) {
        WebUI.comment('Reset failed on first attempt. Trying double click again...')
        WebUI.doubleClick(sliderThumb)
        WebUI.delay(1.5)

        valueAfterDoubleClick = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
        WebUI.comment('Value after second double click: ' + valueAfterDoubleClick)

        try {
            BigDecimal actualNum = new BigDecimal(valueAfterDoubleClick)
            if (actualNum.compareTo(new BigDecimal("0.0")) == 0) {
                WebUI.comment('Presence Penalty reset to default on second attempt')
                isResetSuccess = true
            }
        } catch (Exception e) {}
    }

    if (!isResetSuccess) {
        KeywordUtil.markFailed("FAILED: Presence Penalty did not reset to 0.0. Actual: " + valueAfterDoubleClick)
    }

    WebUI.takeScreenshot('TC16_PresencePenalty_DoubleClick_Reset.png')
    WebUI.comment('=== TC16 Completed Successfully ===')

} catch (Exception e) {
    WebUI.comment('TC16 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC16_PresencePenalty_DoubleClick_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}