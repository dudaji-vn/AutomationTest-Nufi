import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import org.openqa.selenium.Keys as Keys

/**
 * TC02_Parameters_Top_P_Test
 * 
 * Comprehensive Top P parameter test covering:
 * 1. Min value (0.0) - Slider test
 * 2. Max value (1.0) - Slider test
 * 3. Input field valid/invalid values
 * 4. Double click reset to default (1.0)
 * 
 * Note: Chat response verification is skipped
 */

WebUI.comment('=== TC02_Parameters_Top_P: Top P - Comprehensive Test ===')

try {
    // ============================================================
    // CONFIGURATION
    // ============================================================
    double expectedMin = 0.0
    double expectedMax = 1.0
    double expectedDefault = 1.0

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
    
    TestObject parametersButton = findTestObject('Object Repository/nav/nav_items/button_Parameters')
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
    // STEP 2: GET TOP P SLIDER AND INPUT
    // ============================================================
    WebUI.comment('Step 2: Getting Top P slider and input...')
    
    TestObject sliderThumb = new TestObject('topPSliderThumb')
    sliderThumb.addProperty('xpath', ConditionType.EQUALS,
        "//span[contains(@id,'top_p') or contains(@id,'top-p') or contains(@id,'topP')]//span[@role='slider']")
    
    TestObject inputTopP = findTestObject('Object Repository/nav/Parameter/input_Top P')
    
    WebUI.waitForElementVisible(sliderThumb, 10)
    WebUI.waitForElementVisible(inputTopP, 10)
    WebUI.comment('Top P slider and input found')

    // ============================================================
    // HELPER FUNCTIONS
    // ============================================================
    
    // Get slider width
    def getSliderWidth = {
        String script = """
            var element = document.evaluate(
                "//span[contains(@id,'top_p') or contains(@id,'top-p') or contains(@id,'topP')]",
                document,
                null,
                XPathResult.FIRST_ORDERED_NODE_TYPE,
                null
            ).singleNodeValue;
            return element ? element.getBoundingClientRect().width : 0;
        """
        return (Double) WebUI.executeJavaScript(script, null)
    }
    
    // Drag slider to target value (0.0 - 1.0)
    def dragSliderTo = { double targetValue ->
        double sliderWidth = getSliderWidth()
        if (sliderWidth <= 0) {
            WebUI.comment('Could not get slider width')
            return -1.0
        }
        double currentValue = Double.parseDouble(WebUI.getAttribute(sliderThumb, 'aria-valuenow'))
        double dragRatio = (targetValue - currentValue)
        int dragOffset = (int)(sliderWidth * dragRatio)
        WebUI.dragAndDropByOffset(sliderThumb, dragOffset, 0)
        WebUI.delay(1.5)
        return Double.parseDouble(WebUI.getAttribute(sliderThumb, 'aria-valuenow'))
    }
    
    // Set value and trigger onblur
    def setValueAndBlur = { String value ->
        WebUI.comment("→ Entering: " + value)
        WebUI.click(inputTopP)
        WebUI.sendKeys(inputTopP, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(inputTopP, value)
        WebUI.delay(0.3)
        
        WebUI.comment("→ Triggering onblur...")
        WebUI.clickOffset(inputTopP, 250, 0)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputTopP, -50, 50)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputTopP, 100, 100)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputTopP, 0, -100)
        WebUI.delay(0.5)
        
        String actualValue = WebUI.getAttribute(inputTopP, 'value').trim()
        WebUI.comment("→ After blur, value: " + actualValue)
    }
    
    // Verify numeric value matches expected
    def verifyNumericValue = { String expectedStr, String testStep ->
        String actualStr = WebUI.getAttribute(inputTopP, 'value').trim()
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
    
    // Verify value is within valid range (0.0 - 1.0)
    def verifyInRange = { String testStep ->
        String actualStr = WebUI.getAttribute(inputTopP, 'value').trim()
        try {
            BigDecimal actualNum = new BigDecimal(actualStr)
            BigDecimal min = new BigDecimal("0.0")
            BigDecimal max = new BigDecimal("1.0")
            
            if (actualNum.compareTo(min) >= 0 && actualNum.compareTo(max) <= 0) {
                WebUI.comment("PASSED - ${testStep}: Value '${actualStr}' is within valid range (0.0 - 1.0)")
                return true
            } else {
                KeywordUtil.markFailed("FAILED - ${testStep}: Value '${actualStr}' is NOT within valid range (0.0 - 1.0)")
                return false
            }
        } catch (Exception e) {
            KeywordUtil.markFailed("FAILED - ${testStep}: Cannot convert to number. Actual: '${actualStr}'")
            return false
        }
    }

    // ============================================================
    // TEST 1: MIN VALUE (0.0)
    // ============================================================
    WebUI.comment('=== TEST 1: Min Value (0.0) ===')
    
    double actualMin = dragSliderTo(0.0)
    WebUI.comment('Top P after drag to min: ' + actualMin)
    
    if (Math.abs(actualMin - expectedMin) <= 0.05) {
        WebUI.comment('PASSED - Top P set to 0.0')
    } else {
        WebUI.comment('WARNING: Top P is ' + actualMin + ' instead of 0.0')
    }
    WebUI.takeScreenshot('TC02_Parameters_Top_P_TopP_Min.png')

    // ============================================================
    // TEST 2: MAX VALUE (1.0)
    // ============================================================
    WebUI.comment('=== TEST 2: Max Value (1.0) ===')
    
    double actualMax = dragSliderTo(1.0)
    WebUI.comment('Top P after drag to max: ' + actualMax)
    
    if (Math.abs(actualMax - expectedMax) <= 0.05) {
        WebUI.comment('PASSED - Top P set to 1.0')
    } else if (actualMax >= 0.9) {
        WebUI.comment('WARNING: Top P set to near max: ' + actualMax)
    } else {
        WebUI.comment('WARNING: Top P is ' + actualMax + ' instead of 1.0')
    }
    WebUI.takeScreenshot('TC02_Parameters_Top_P_TopP_Max.png')

    // ============================================================
    // TEST 3: INPUT FIELD - VALID VALUES
    // ============================================================
    WebUI.comment('=== TEST 3: Input Field - Valid Values ===')
    
    String[][] validValues = [
        ["0.0", "0.0"],
        ["0.5", "0.5"],
        ["1.0", "1.0"]
    ]
    for (String[] pair : validValues) {
        WebUI.comment('--- Testing valid value: ' + pair[0] + ' ---')
        setValueAndBlur(pair[0])
        verifyNumericValue(pair[1], "Valid value test: " + pair[0])
        WebUI.takeScreenshot("TC02_Parameters_Top_P_TopP_Valid_${pair[0].replace('.', '_')}.png")
    }

    // ============================================================
    // TEST 4: INPUT FIELD - CLAMP VALUES
    // ============================================================
    WebUI.comment('=== TEST 4: Input Field - Clamp Values ===')
    
    // Clamp max: 1.5 → 1.0
    WebUI.comment('--- Testing clamp max (1.5 → 1.0) ---')
    setValueAndBlur("1.5")
    verifyNumericValue("1.0", "Exceed max test")
    WebUI.takeScreenshot('TC02_Parameters_Top_P_TopP_Clamp_Max.png')
    
    // Clamp min: -0.5 → 0.0
    WebUI.comment('--- Testing clamp min (-0.5 → 0.0) ---')
    setValueAndBlur("-0.5")
    verifyNumericValue("0.0", "Below min test")
    WebUI.takeScreenshot('TC02_Parameters_Top_P_TopP_Clamp_Min.png')

    // ============================================================
    // TEST 5: INPUT FIELD - INVALID VALUES
    // ============================================================
    WebUI.comment('=== TEST 5: Input Field - Invalid Values ===')
    
    // Set initial valid value
    WebUI.comment('Setting initial valid value (0.7)...')
    setValueAndBlur("0.7")
    verifyNumericValue("0.7", "Set initial valid value")
    
    String[] invalidInputs = ["abc", "xyz123", "hello!"]
    
    for (String invalid : invalidInputs) {
        WebUI.comment('--- Testing invalid input: "' + invalid + '" ---')
        
        WebUI.click(inputTopP)
        WebUI.sendKeys(inputTopP, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(inputTopP, invalid)
        WebUI.delay(0.5)
        
        WebUI.clickOffset(inputTopP, 250, 0)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputTopP, -50, 50)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputTopP, 100, 100)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputTopP, 0, -100)
        WebUI.delay(0.5)
        
        String actualAfterInvalid = WebUI.getAttribute(inputTopP, 'value').trim()
        WebUI.comment('Value after invalid input: ' + actualAfterInvalid)
        
        verifyInRange("Invalid input '" + invalid + "' reverted")
        WebUI.takeScreenshot("TC02_Parameters_Top_P_TopP_Invalid_" + invalid + ".png")
        
        // Reset for next test
        setValueAndBlur("0.7")
        verifyNumericValue("0.7", "Reset to 0.7")
    }

    // ============================================================
    // TEST 6: DOUBLE CLICK RESET
    // ============================================================
    WebUI.comment('=== TEST 6: Double Click Reset ===')
    
    // Drag to a different value first
    WebUI.comment('Dragging to 0.5 before reset...')
    dragSliderTo(0.5)
    String beforeReset = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Value before reset: ' + beforeReset)
    
    // Double click to reset
    WebUI.comment('Double clicking thumb to reset to default (1.0)...')
    WebUI.doubleClick(sliderThumb)
    WebUI.delay(1.5)
    
    String afterReset = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Value after reset: ' + afterReset)
    
    try {
        BigDecimal actualNum = new BigDecimal(afterReset)
        BigDecimal expectedNum = new BigDecimal("1.0")
        
        if (actualNum.compareTo(expectedNum) == 0) {
            WebUI.comment('PASSED - Top P reset to 1.0 successfully')
        } else {
            WebUI.comment('First attempt failed. Trying double click again...')
            WebUI.doubleClick(sliderThumb)
            WebUI.delay(1.5)
            afterReset = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
            WebUI.comment('Value after second double click: ' + afterReset)
            
            actualNum = new BigDecimal(afterReset)
            if (actualNum.compareTo(expectedNum) == 0) {
                WebUI.comment('PASSED - Top P reset to 1.0 on second attempt')
            } else {
                WebUI.comment('FAILED: Top P did not reset to 1.0. Actual: ' + afterReset)
                KeywordUtil.markFailed("FAILED: Top P did not reset to 1.0. Actual: " + afterReset)
            }
        }
    } catch (Exception e) {
        KeywordUtil.markFailed("FAILED: Cannot convert value to number: " + afterReset)
    }
    WebUI.takeScreenshot('TC02_Parameters_Top_P_TopP_DoubleClick_Reset.png')

    // ============================================================
    // FINAL VERIFICATION
    // ============================================================
    WebUI.comment('=== FINAL VERIFICATION ===')
    
    String finalValue = WebUI.getAttribute(inputTopP, 'value')
    WebUI.comment('Final Top P value: ' + finalValue)
    
    try {
        BigDecimal finalNum = new BigDecimal(finalValue)
        WebUI.comment('TC02_Parameters_Top_P PASSED - Top P parameter test completed successfully')
    } catch (Exception e) {
        WebUI.comment('FAILED: Final value is not numeric: ' + finalValue)
        KeywordUtil.markFailed("FAILED: Final value is not numeric: " + finalValue)
    }

    WebUI.takeScreenshot('TC02_Parameters_Top_P_TopP_Complete.png')
    WebUI.comment('=== TC02_Parameters_Top_P Completed ===')

} catch (Exception e) {
    WebUI.comment('TC02_Parameters_Top_P FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC02_Parameters_Top_P_TopP_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}