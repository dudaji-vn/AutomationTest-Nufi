import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import org.openqa.selenium.Keys as Keys

/**
 * TC01_Parameters_Temperature_Test
 * 
 * Comprehensive Temperature parameter test covering:
 * 1. Default value check (1.0)
 * 2. Min value (0.0) - Deterministic responses
 * 3. Max value (2.0) - Diverse responses
 * 4. Input field valid/invalid values
 * 5. Double click reset
 * 
 * Note: Chat response verification is skipped in this combined test
 */

WebUI.comment('=== TC01: Temperature - Comprehensive Test ===')

try {
    // ============================================================
    // CONFIGURATION
    // ============================================================
    double expectedDefault = 1.0
    double expectedMin = 0.0
    double expectedMax = 2.0
    String testMessage = "how are you"

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
    } else {
        WebUI.comment('Navbar is open (aria-hidden="false")')
    }

    // ============================================================
    // STEP 1: OPEN PARAMETERS TAB
    // ============================================================
    WebUI.comment('Step 1: Opening Parameters tab...')
    
    TestObject parametersButton = findTestObject('Object Repository/Core Chat/nav/nav_items/button_Parameters')
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
    // STEP 2: GET TEMPERATURE SLIDER & INPUT
    // ============================================================
    WebUI.comment('Step 2: Getting Temperature slider and input...')
    
    TestObject sliderThumb = new TestObject('sliderThumb')
    sliderThumb.addProperty('xpath', ConditionType.EQUALS,
        "//span[@id='temperature-dynamic-setting-slider']//span[@role='slider']")
    
    TestObject tempInput = findTestObject('Object Repository/Core Chat/nav/Parameter/input_Temperature')
    
    WebUI.waitForElementVisible(sliderThumb, 10)
    WebUI.waitForElementVisible(tempInput, 10)
    WebUI.comment('Temperature slider and input found')

    // ============================================================
    // HELPER FUNCTIONS
    // ============================================================
    
    // Get slider width
    def getSliderWidth = {
        String widthScript = "return document.evaluate(\"//span[@id='temperature-dynamic-setting-slider']\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.getBoundingClientRect().width;"
        return (Double) WebUI.executeJavaScript(widthScript, null)
    }
    
    // Drag slider to target value (0.0 - 2.0)
    def dragSliderTo = { double targetValue ->
        double sliderWidth = getSliderWidth()
        double currentValue = Double.parseDouble(WebUI.getAttribute(sliderThumb, 'aria-valuenow'))
        double dragRatio = (targetValue - currentValue) / 2.0
        int dragOffset = (int)(sliderWidth * dragRatio)
        WebUI.dragAndDropByOffset(sliderThumb, dragOffset, 0)
        WebUI.delay(1.5)
        return Double.parseDouble(WebUI.getAttribute(sliderThumb, 'aria-valuenow'))
    }
    
    // Set value and trigger onblur
    def setValueAndBlur = { String value ->
        WebUI.comment("→ Entering: " + value)
        WebUI.click(tempInput)
        WebUI.sendKeys(tempInput, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(tempInput, value)
        WebUI.delay(0.3)
        
        WebUI.comment("→ Triggering onblur...")
        WebUI.clickOffset(tempInput, 250, 0)
        WebUI.delay(0.3)
        WebUI.clickOffset(tempInput, -50, 50)
        WebUI.delay(0.3)
        WebUI.clickOffset(tempInput, 100, 100)
        WebUI.delay(0.3)
        WebUI.clickOffset(tempInput, 0, -100)
        WebUI.delay(0.5)
        
        String actualValue = WebUI.getAttribute(tempInput, 'value').trim()
        WebUI.comment("→ After blur, value: " + actualValue)
    }
    
    // Verify numeric value with tolerance
    def verifyNumericValue = { String expectedStr, String testStep, double toleranceValue = 0.01 ->
        String actualStr = WebUI.getAttribute(tempInput, 'value').trim()
        try {
            BigDecimal actualNum = new BigDecimal(actualStr)
            BigDecimal expectedNum = new BigDecimal(expectedStr)
            BigDecimal tolerance = new BigDecimal(toleranceValue)
            BigDecimal diff = actualNum.subtract(expectedNum).abs()
            
            if (diff.compareTo(tolerance) <= 0) {
                WebUI.comment("PASSED - ${testStep}: Expected '${expectedStr}' → Actual: '${actualStr}' ✓")
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
    
    // Verify value in range
    def verifyInRange = { double minVal, double maxVal, String testStep ->
        String actualStr = WebUI.getAttribute(tempInput, 'value').trim()
        try {
            BigDecimal actualNum = new BigDecimal(actualStr)
            BigDecimal min = new BigDecimal(minVal)
            BigDecimal max = new BigDecimal(maxVal)
            
            if (actualNum.compareTo(min) >= 0 && actualNum.compareTo(max) <= 0) {
                WebUI.comment("PASSED - ${testStep}: Value '${actualStr}' is within range (${minVal} - ${maxVal}) ✓")
                return true
            } else {
                KeywordUtil.markFailed("FAILED - ${testStep}: Value '${actualStr}' is NOT within range")
                return false
            }
        } catch (Exception e) {
            KeywordUtil.markFailed("FAILED - ${testStep}: Cannot convert to number. Actual: '${actualStr}'")
            return false
        }
    }

    // ============================================================
    // TEST 1: CHECK DEFAULT VALUE
    // ============================================================
    WebUI.comment('=== TEST 1: Default Value (1.0) ===')
    
    WebUI.click(sliderThumb)
    WebUI.delay(0.5)
    
    String defaultValue = WebUI.getAttribute(tempInput, 'value')
    WebUI.comment('Default Temperature: ' + defaultValue)
    
    if (defaultValue == '1.0' || defaultValue == '1') {
        WebUI.comment('PASSED - Default value is 1.0 ✓')
    } else {
        WebUI.comment('WARNING: Default value is not 1.0: ' + defaultValue)
    }
    WebUI.takeScreenshot('TC01_Temp_Default.png')

    // ============================================================
    // TEST 2: MIN VALUE (0.0)
    // ============================================================
    WebUI.comment('=== TEST 2: Min Value (0.0) ===')
    
    double actualMin = dragSliderTo(0.0)
    WebUI.comment('Temperature after drag to min: ' + actualMin)
    
    if (Math.abs(actualMin - expectedMin) <= 0.05) {
        WebUI.comment('PASSED - Temperature set to 0.0 ✓')
    } else {
        WebUI.comment('WARNING: Temperature is ' + actualMin + ' instead of 0.0')
    }
    WebUI.takeScreenshot('TC01_Temp_Min.png')

    // ============================================================
    // TEST 3: MAX VALUE (2.0)
    // ============================================================
    WebUI.comment('=== TEST 3: Max Value (2.0) ===')
    
    double actualMax = dragSliderTo(2.0)
    WebUI.comment('Temperature after drag to max: ' + actualMax)
    
    if (Math.abs(actualMax - expectedMax) <= 0.05) {
        WebUI.comment('PASSED - Temperature set to 2.0 ✓')
    } else {
        WebUI.comment('WARNING: Temperature is ' + actualMax + ' instead of 2.0')
    }
    WebUI.takeScreenshot('TC01_Temp_Max.png')

    // ============================================================
    // TEST 4: INPUT FIELD - VALID VALUES
    // ============================================================
    WebUI.comment('=== TEST 4: Input Field - Valid Values ===')
    
    String[][] validValues = [["0.75", "0.75"], ["2", "2.0"], ["0.0", "0.0"]]
    for (String[] pair : validValues) {
        WebUI.comment('--- Testing valid value: ' + pair[0] + ' ---')
        setValueAndBlur(pair[0])
        verifyNumericValue(pair[1], "Valid value test: " + pair[0])
        WebUI.takeScreenshot("TC01_Temp_Valid_${pair[0].replace('.', '_')}.png")
    }

    // ============================================================
    // TEST 5: INPUT FIELD - CLAMP VALUES
    // ============================================================
    WebUI.comment('=== TEST 5: Input Field - Clamp Values ===')
    
    // Clamp max: 2.5 → 2.0
    WebUI.comment('--- Testing clamp max (2.5 → 2.0) ---')
    setValueAndBlur("2.5")
    verifyNumericValue("2.0", "Exceed max test")
    WebUI.takeScreenshot('TC01_Temp_Clamp_Max.png')
    
    // Clamp min: -0.5 → 0.0
    WebUI.comment('--- Testing clamp min (-0.5 → 0.0) ---')
    setValueAndBlur("-0.5")
    verifyNumericValue("0.0", "Below min test")
    WebUI.takeScreenshot('TC01_Temp_Clamp_Min.png')
    
    // Very large: 99 → 2.0
    WebUI.comment('--- Testing very large (99 → 2.0) ---')
    setValueAndBlur("99")
    verifyNumericValue("2.0", "Very large test")
    WebUI.takeScreenshot('TC01_Temp_Very_Large.png')

    // ============================================================
    // TEST 6: INPUT FIELD - INVALID VALUES
    // ============================================================
    WebUI.comment('=== TEST 6: Input Field - Invalid Values ===')
    
    // Set initial valid value
    WebUI.comment('Setting initial valid value (1.5)...')
    setValueAndBlur("1.5")
    verifyNumericValue("1.5", "Set initial valid value")
    
    String[] invalidInputs = ["abc", "@", "1.2.3", "hello", "test"]
    
    for (String invalid : invalidInputs) {
        WebUI.comment('--- Testing invalid input: "' + invalid + '" ---')
        
        WebUI.click(tempInput)
        WebUI.sendKeys(tempInput, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(tempInput, invalid)
        WebUI.delay(0.5)
        
        WebUI.clickOffset(tempInput, 250, 0)
        WebUI.delay(0.3)
        WebUI.clickOffset(tempInput, -50, 50)
        WebUI.delay(0.3)
        WebUI.clickOffset(tempInput, 100, 100)
        WebUI.delay(0.3)
        WebUI.clickOffset(tempInput, 0, -100)
        WebUI.delay(0.5)
        
        String actualAfterInvalid = WebUI.getAttribute(tempInput, 'value').trim()
        WebUI.comment('Value after invalid input: ' + actualAfterInvalid)
        
        verifyInRange(0.0, 2.0, "Invalid input '" + invalid + "' reverted")
        WebUI.takeScreenshot("TC01_Temp_Invalid_" + invalid + ".png")
        
        // Reset for next test
        setValueAndBlur("1.5")
        verifyNumericValue("1.5", "Reset to 1.5")
    }

    // ============================================================
    // TEST 7: DOUBLE CLICK RESET
    // ============================================================
    WebUI.comment('=== TEST 7: Double Click Reset ===')
    
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
            WebUI.comment('PASSED - Temperature reset to 1.0 successfully ✓')
        } else {
            WebUI.comment('First attempt failed. Trying double click again...')
            WebUI.doubleClick(sliderThumb)
            WebUI.delay(1.5)
            afterReset = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
            WebUI.comment('Value after second double click: ' + afterReset)
            
            actualNum = new BigDecimal(afterReset)
            if (actualNum.compareTo(expectedNum) == 0) {
                WebUI.comment('PASSED - Temperature reset to 1.0 on second attempt ✓')
            } else {
                WebUI.comment('FAILED: Temperature did not reset to 1.0. Actual: ' + afterReset)
                KeywordUtil.markFailed("FAILED: Temperature did not reset to 1.0. Actual: " + afterReset)
            }
        }
    } catch (Exception e) {
        KeywordUtil.markFailed("FAILED: Cannot convert value to number: " + afterReset)
    }
    WebUI.takeScreenshot('TC01_Temp_DoubleClick_Reset.png')

    // ============================================================
    // FINAL VERIFICATION
    // ============================================================
    WebUI.comment('=== FINAL VERIFICATION ===')
    
    String finalValue = WebUI.getAttribute(tempInput, 'value')
    WebUI.comment('Final Temperature value: ' + finalValue)
    
    try {
        BigDecimal finalNum = new BigDecimal(finalValue)
        WebUI.comment('TC01 PASSED - Temperature parameter test completed successfully ✓')
    } catch (Exception e) {
        WebUI.comment('FAILED: Final value is not numeric: ' + finalValue)
        KeywordUtil.markFailed("FAILED: Final value is not numeric: " + finalValue)
    }

    WebUI.takeScreenshot('TC01_Temperature_Complete.png')
    WebUI.comment('=== TC01 Completed ===')

} catch (Exception e) {
    WebUI.comment('TC01 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC01_Temperature_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}