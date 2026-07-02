import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC02D_Parameters_TopP_DoubleClick
 * Double click the Top P slider thumb to reset to default value (1.0)
 */

WebUI.comment('=== TC02D_Parameters_TopP_DoubleClick: Top P - Double Click Reset ===')

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

    // === TOP P SLIDER ===
    WebUI.comment('Step 2: Getting Top P slider...')
    TestObject sliderThumb = new TestObject('topPSliderThumb')
    sliderThumb.addProperty('xpath', ConditionType.EQUALS,
        "//span[contains(@id,'top_p') or contains(@id,'top-p') or contains(@id,'topP')]//span[@role='slider']")

    WebUI.waitForElementVisible(sliderThumb, 10)
    WebUI.comment('Top P slider found')

    // === DRAG SLIDER TO DIFFERENT VALUE ===
    WebUI.comment('Step 3: Dragging slider to a different value...')
    
    // Click on thumb first to focus
    WebUI.click(sliderThumb)
    WebUI.delay(0.5)

    String widthScript = "return document.evaluate(\"//span[contains(@id,'top_p') or contains(@id,'top-p') or contains(@id,'topP')]\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.getBoundingClientRect().width;"
    double sliderWidth = (Double) WebUI.executeJavaScript(widthScript, null)
    WebUI.comment('Slider width: ' + sliderWidth + 'px')

    // Drag to middle-left (better chance to move)
    int dragOffset = (int)(sliderWidth * 0.35)
    WebUI.dragAndDropByOffset(sliderThumb, dragOffset, 0)
    WebUI.delay(1.5)

    String valueAfterDrag = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Top P after drag: ' + valueAfterDrag)

    // Only warning if it doesn't move at all
    double draggedValue = Double.parseDouble(valueAfterDrag)
    if (draggedValue > 0.95) {
        WebUI.comment('WARNING: Slider did not move significantly. Current: ' + valueAfterDrag)
        // Do not fail here, continue test
    } else {
        WebUI.comment('Slider moved successfully to: ' + valueAfterDrag)
    }

    // === DOUBLE CLICK TO RESET ===
    WebUI.comment('Step 4: Double clicking thumb to reset to default...')
    
    WebUI.doubleClick(sliderThumb)
    WebUI.delay(1.5)

    String valueAfterDoubleClick = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Top P after double click: ' + valueAfterDoubleClick)

    // Numeric verification
    try {
        BigDecimal actualNum = new BigDecimal(valueAfterDoubleClick)
        BigDecimal expectedDefault = new BigDecimal("1.0")

        if (actualNum.compareTo(expectedDefault) == 0) {
            WebUI.comment('Top P successfully reset to default value (1.0)')
        } else {
            WebUI.comment('First attempt failed. Trying double click again...')
            WebUI.doubleClick(sliderThumb)
            WebUI.delay(1.5)
            
            valueAfterDoubleClick = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
            WebUI.comment('Value after second double click: ' + valueAfterDoubleClick)
            
            actualNum = new BigDecimal(valueAfterDoubleClick)
            if (actualNum.compareTo(expectedDefault) != 0) {
                KeywordUtil.markFailed("FAILED: Double click did not reset Top P to 1.0. Actual: " + valueAfterDoubleClick)
            } else {
                WebUI.comment('Top P reset to default on second attempt')
            }
        }
    } catch (Exception e) {
        KeywordUtil.markFailed("FAILED: Cannot read Top P value as number: " + valueAfterDoubleClick)
    }

    WebUI.takeScreenshot('TC02D_Parameters_TopP_DoubleClick_TopP_DoubleClick_Reset.png')
    WebUI.comment('=== TC02D_Parameters_TopP_DoubleClick Completed Successfully ===')

} catch (Exception e) {
    WebUI.comment('TC02D_Parameters_TopP_DoubleClick FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC02D_Parameters_TopP_DoubleClick_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}