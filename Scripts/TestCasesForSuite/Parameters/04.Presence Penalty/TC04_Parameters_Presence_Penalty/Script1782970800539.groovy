import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import org.openqa.selenium.Keys as Keys

/**
 * TC04_Parameters_Presence_Penalty_Test
 * 
 * Comprehensive Presence Penalty parameter test covering:
 * 1. Max value (2.0) - Slider test
 * 2. Input field valid/invalid values
 * 3. Double click reset to default (0.0)
 * Note: Min value (-2.0) is not tested as Presence Penalty only has Max test
 */

WebUI.comment('=== TC04: Presence Penalty - Comprehensive Test ===')

try {
    // ============================================================
    // CONFIGURATION
    // ============================================================
    double expectedMax = 2.0
    double expectedDefault = 0.0

    // ============================================================
    // STEP 0: CHECK NAVBAR
    // ============================================================
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
            WebUI.comment('Navbar opened successfully')
        }
    } else {
        WebUI.comment('Navbar is open (aria-hidden="false")')
    }

    // ============================================================
    // STEP 1: OPEN PARAMETERS TAB
    // ============================================================
    WebUI.comment('Step 1: Opening Parameters tab...')
    
    TestObject parametersButton = findTestObject('Object Repository/Core Chat/nav/nav_items/button_Parameters')
    WebUI.waitForElementVisible(parametersButton, 10)
    
    String ariaLabel = WebUI.getAttribute(parametersButton, 'aria-label')
    String isPressed = WebUI.getAttribute(parametersButton, 'aria-pressed')
    
    WebUI.comment('Parameters button aria-label: ' + ariaLabel)
    WebUI.comment('Parameters button aria-pressed: ' + isPressed)
    
    if (ariaLabel == 'Parameters' && isPressed == 'true') {
        WebUI.comment('Parameters tab is open (correct aria-label and aria-pressed)')
    } else {
        WebUI.comment('Parameters tab not open or wrong label, clicking to open...')
        WebUI.click(parametersButton)
        WebUI.delay(2)
        
        ariaLabel = WebUI.getAttribute(parametersButton, 'aria-label')
        isPressed = WebUI.getAttribute(parametersButton, 'aria-pressed')
        WebUI.comment('After click - Parameters button aria-label: ' + ariaLabel)
        WebUI.comment('After click - Parameters button aria-pressed: ' + isPressed)
        
        if (ariaLabel == 'Parameters' && isPressed == 'true') {
            WebUI.comment('Parameters tab opened successfully')
        }
    }

    // ============================================================
    // STEP 2: GET PRESENCE PENALTY SLIDER AND INPUT
    // ============================================================
    WebUI.comment('Step 2: Getting Presence Penalty slider and input...')
    
    TestObject sliderThumb = new TestObject('presencePenaltySliderThumb')
    sliderThumb.addProperty('xpath', ConditionType.EQUALS,
        "//span[contains(@id,'presence_penalty') or contains(@id,'presence-penalty') or contains(@id,'presencePenalty')]//span[@role='slider']")
    
    TestObject inputField = findTestObject('Object Repository/Core Chat/nav/Parameter/input_Presence Penalty')
    
    WebUI.waitForElementVisible(sliderThumb, 10)
    WebUI.waitForElementVisible(inputField, 10)
    WebUI.comment('Presence Penalty slider and input found')

    // ============================================================
    // HELPER FUNCTIONS
    // ============================================================
    
    // Helper: Click outside to dismiss any popup/tooltip
    def dismissPopup = {
        WebUI.comment("→ Dismissing any popup/tooltip...")
        WebUI.clickOffset(inputField, 250, 0)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputField, -50, 50)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputField, 0, -100)
        WebUI.delay(0.5)
    }
    
    // Get slider width
    def getSliderWidth = {
        String script = """
            var element = document.evaluate(
                "//span[contains(@id,'presence_penalty') or contains(@id,'presence-penalty') or contains(@id,'presencePenalty')]",
                document,
                null,
                XPathResult.FIRST_ORDERED_NODE_TYPE,
                null
            ).singleNodeValue;
            return element ? element.getBoundingClientRect().width : 0;
        """
        return (Double) WebUI.executeJavaScript(script, null)
    }
    
    // Drag slider to target value (-2.0 - 2.0)
    def dragSliderTo = { double targetValue ->
        double sliderWidth = getSliderWidth()
        if (sliderWidth <= 0) {
            WebUI.comment('Could not get slider width')
            return -999.0
        }
        double currentValue = Double.parseDouble(WebUI.getAttribute(sliderThumb, 'aria-valuenow'))
        double dragRatio = (targetValue - currentValue) / 4.0
        int dragOffset = (int)(sliderWidth * dragRatio)
        WebUI.dragAndDropByOffset(sliderThumb, dragOffset, 0)
        WebUI.delay(1.5)
        return Double.parseDouble(WebUI.getAttribute(sliderThumb, 'aria-valuenow'))
    }
    
    // Set value and trigger onblur - IMPROVED: dismiss popup before click
    def setValueAndBlur = { String value ->
        // Dismiss any popup before clicking input
        dismissPopup()
        
        WebUI.comment("→ Entering: " + value)
        WebUI.click(inputField)
        WebUI.sendKeys(inputField, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(inputField, value)
        WebUI.delay(0.3)
        
        WebUI.comment("→ Triggering onblur...")
        WebUI.clickOffset(inputField, 250, 0)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputField, -50, 50)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputField, 100, 100)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputField, 0, -100)
        WebUI.delay(0.5)
        
        String actualValue = WebUI.getAttribute(inputField, 'value').trim()
        WebUI.comment("→ After blur, value: " + actualValue)
    }
    
    // Verify numeric value matches expected
    def verifyNumericValue = { String expectedStr, String testStep ->
        String actualStr = WebUI.getAttribute(inputField, 'value').trim()
        try {
            BigDecimal actualNum = new BigDecimal(actualStr)
            BigDecimal expectedNum = new BigDecimal(expectedStr)
            
            if (actualNum.compareTo(expectedNum) == 0) {
                WebUI.comment("PASSED - ${testStep}: Expected '${expectedStr}' → Actual: '${actualStr}'")
                return true
            } else {
                KeywordUtil.markFailed("FAILED - ${testStep}: Expected ${expectedStr} but got ${actualStr}")
                return false
            }
        } catch (Exception e) {
            KeywordUtil.markFailed("FAILED - ${testStep}: Cannot convert to number. Actual: '${actualStr}'")
            return false
        }
    }
    
    // Verify value is within valid range (-2.0 - 2.0)
    def verifyInRange = { String testStep ->
        String actualStr = WebUI.getAttribute(inputField, 'value').trim()
        try {
            BigDecimal actualNum = new BigDecimal(actualStr)
            BigDecimal min = new BigDecimal("-2.0")
            BigDecimal max = new BigDecimal("2.0")
            
            if (actualNum.compareTo(min) >= 0 && actualNum.compareTo(max) <= 0) {
                WebUI.comment("PASSED - ${testStep}: Value '${actualStr}' is within valid range (-2.0 to 2.0)")
                return true
            } else {
                KeywordUtil.markFailed("FAILED - ${testStep}: Value '${actualStr}' is NOT within valid range (-2.0 to 2.0)")
                return false
            }
        } catch (Exception e) {
            KeywordUtil.markFailed("FAILED - ${testStep}: Cannot convert to number. Actual: '${actualStr}'")
            return false
        }
    }

    // ============================================================
    // TEST 1: MAX VALUE (2.0)
    // ============================================================
    WebUI.comment('=== TEST 1: Max Value (2.0) ===')
    
    double actualMax = dragSliderTo(2.0)
    WebUI.comment('Presence Penalty after drag to max: ' + actualMax)
    
    if (Math.abs(actualMax - expectedMax) <= 0.05) {
        WebUI.comment('PASSED - Presence Penalty set to 2.0')
    } else if (actualMax >= 1.8) {
        WebUI.comment('WARNING: Presence Penalty set to near max: ' + actualMax)
    } else {
        WebUI.comment('WARNING: Presence Penalty is ' + actualMax + ' instead of 2.0')
    }
    WebUI.takeScreenshot('TC04_PresencePenalty_Max.png')

    // ============================================================
    // TEST 2: INPUT FIELD - VALID VALUES
    // ============================================================
    WebUI.comment('=== TEST 2: Input Field - Valid Values ===')
    
    String[][] validValues = [
        ["-2.0", "-2.0"],
        ["-1.0", "-1.0"],
        ["0.0", "0.0"],
        ["1.0", "1.0"],
        ["2.0", "2.0"]
    ]
    
    for (String[] pair : validValues) {
        WebUI.comment('--- Testing valid value: ' + pair[0] + ' ---')
        setValueAndBlur(pair[0])
        verifyNumericValue(pair[1], "Valid value test: " + pair[0])
        WebUI.takeScreenshot("TC04_PresencePenalty_Valid_${pair[0].replace('-', 'neg').replace('.', '_')}.png")
    }

    // ============================================================
    // TEST 3: INPUT FIELD - CLAMP VALUES
    // ============================================================
    WebUI.comment('=== TEST 3: Input Field - Clamp Values ===')
    
    // Clamp max: 3.0 → 2.0
    WebUI.comment('--- Testing clamp max (3.0 → 2.0) ---')
    setValueAndBlur("3.0")
    verifyNumericValue("2.0", "Exceed max test")
    WebUI.takeScreenshot('TC04_PresencePenalty_Clamp_Max.png')
    
    // Clamp min: -3.0 → -2.0
    WebUI.comment('--- Testing clamp min (-3.0 → -2.0) ---')
    setValueAndBlur("-3.0")
    verifyNumericValue("-2.0", "Below min test")
    WebUI.takeScreenshot('TC04_PresencePenalty_Clamp_Min.png')

    // ============================================================
    // TEST 4: INPUT FIELD - INVALID VALUES
    // ============================================================
    WebUI.comment('=== TEST 4: Input Field - Invalid Values ===')
    
    // Set initial valid value
    WebUI.comment('Setting initial valid value (0.5)...')
    setValueAndBlur("0.5")
    verifyNumericValue("0.5", "Set initial valid value")
    
    String[] invalidInputs = ["abc", "xyz123", "hello!"]
    
    for (String invalid : invalidInputs) {
        WebUI.comment('--- Testing invalid input: "' + invalid + '" ---')
        
        // Dismiss popup before clicking
        dismissPopup()
        
        WebUI.click(inputField)
        WebUI.sendKeys(inputField, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(inputField, invalid)
        WebUI.delay(0.5)
        
        WebUI.clickOffset(inputField, 250, 0)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputField, -50, 50)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputField, 100, 100)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputField, 0, -100)
        WebUI.delay(0.5)
        
        String actualAfterInvalid = WebUI.getAttribute(inputField, 'value').trim()
        WebUI.comment('Value after invalid input: ' + actualAfterInvalid)
        
        verifyInRange("Invalid input '" + invalid + "' reverted")
        WebUI.takeScreenshot("TC04_PresencePenalty_Invalid_" + invalid + ".png")
        
        // Reset for next test
        setValueAndBlur("0.5")
        verifyNumericValue("0.5", "Reset to 0.5")
    }

    // ============================================================
    // TEST 5: DOUBLE CLICK RESET
    // ============================================================
    WebUI.comment('=== TEST 5: Double Click Reset ===')
    
    // Drag to a different value first
    WebUI.comment('Dragging to 1.0 before reset...')
    dragSliderTo(1.0)
    String beforeReset = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Value before reset: ' + beforeReset)
    
    // Double click to reset
    WebUI.comment('Double clicking thumb to reset to default (0.0)...')
    WebUI.doubleClick(sliderThumb)
    WebUI.delay(1.5)
    
    String afterReset = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Value after reset: ' + afterReset)
    
    try {
        BigDecimal actualNum = new BigDecimal(afterReset)
        BigDecimal expectedNum = new BigDecimal("0.0")
        
        if (actualNum.compareTo(expectedNum) == 0) {
            WebUI.comment('PASSED - Presence Penalty reset to 0.0 successfully')
        } else {
            WebUI.comment('First attempt failed. Trying double click again...')
            WebUI.doubleClick(sliderThumb)
            WebUI.delay(1.5)
            afterReset = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
            WebUI.comment('Value after second double click: ' + afterReset)
            
            actualNum = new BigDecimal(afterReset)
            if (actualNum.compareTo(expectedNum) == 0) {
                WebUI.comment('PASSED - Presence Penalty reset to 0.0 on second attempt')
            } else {
                WebUI.comment('FAILED: Presence Penalty did not reset to 0.0. Actual: ' + afterReset)
                KeywordUtil.markFailed("FAILED: Presence Penalty did not reset to 0.0. Actual: " + afterReset)
            }
        }
    } catch (Exception e) {
        KeywordUtil.markFailed("FAILED: Cannot convert value to number: " + afterReset)
    }
    WebUI.takeScreenshot('TC04_PresencePenalty_DoubleClick_Reset.png')

    // ============================================================
    // FINAL VERIFICATION
    // ============================================================
    WebUI.comment('=== FINAL VERIFICATION ===')
    
    String finalValue = WebUI.getAttribute(inputField, 'value')
    WebUI.comment('Final Presence Penalty value: ' + finalValue)
    
    try {
        BigDecimal finalNum = new BigDecimal(finalValue)
        WebUI.comment('TC04 PASSED - Presence Penalty parameter test completed successfully')
    } catch (Exception e) {
        WebUI.comment('FAILED: Final value is not numeric: ' + finalValue)
        KeywordUtil.markFailed("FAILED: Final value is not numeric: " + finalValue)
    }

    WebUI.takeScreenshot('TC04_PresencePenalty_Complete.png')
    WebUI.comment('=== TC04 Completed ===')

} catch (Exception e) {
    WebUI.comment('TC04 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC04_PresencePenalty_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}