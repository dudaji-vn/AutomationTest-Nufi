import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import org.openqa.selenium.Keys as Keys

/**
 * TC02C_Parameters_Top_P_Input_Field
 * 
 * Verify direct input of Top P values (0.0 - 1.0) with numeric comparison
 * When invalid input is entered, the input field should revert to a valid number (0.0 - 1.0)
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Test valid values: 0.0, 0.5, 1.0
 * 3. Test clamp max: 1.5 → 1.0
 * 4. Test clamp min: -0.5 → 0.0
 * 5. Test invalid input: abc, xyz123, hello!
 * 6. Verify invalid input reverts to a valid number (0.0 - 1.0)
 */

WebUI.comment('=== TC02C_Parameters_Top_P_Input_Field: Top P - Input Field Test ===')

try {
    // ============================================================
    // STEP 0: CHECK NAVBAR
    // ============================================================
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
        WebUI.comment('Sidebar opened')
    } else {
        WebUI.comment('Navbar is already open')
    }

    // ============================================================
    // STEP 1: OPEN PARAMETERS TAB
    // ============================================================
    WebUI.comment('Step 1: Opening Parameters tab...')
    TestObject parametersButton = findTestObject('Object Repository/nav/nav_items/button_Parameters')
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
    // STEP 2: GET TOP P INPUT
    // ============================================================
    WebUI.comment('Step 2: Getting Top P input field...')
    TestObject inputTopP = findTestObject('Object Repository/nav/Parameter/input_Top P')
    WebUI.waitForElementVisible(inputTopP, 10)
    WebUI.comment('Top P input field found')

    // ============================================================
    // STEP 3: CHECK DEFAULT VALUE
    // ============================================================
    WebUI.comment('Step 3: Checking default value...')
    String defaultValue = WebUI.getAttribute(inputTopP, 'value')
    WebUI.comment('Default Top P: ' + defaultValue)
    
    try {
        BigDecimal defaultNum = new BigDecimal(defaultValue)
        BigDecimal min = new BigDecimal("0.0")
        BigDecimal max = new BigDecimal("1.0")
        
        if (defaultNum.compareTo(min) >= 0 && defaultNum.compareTo(max) <= 0) {
            WebUI.comment('Default value is within valid range (0.0 - 1.0) ✓')
        } else {
            WebUI.comment('WARNING: Default value is out of range: ' + defaultValue)
        }
    } catch (Exception e) {
        WebUI.comment('WARNING: Default value is not numeric: ' + defaultValue)
    }
    WebUI.takeScreenshot('TC02C_Parameters_Top_P_Input_Field_TopP_Default.png')

    // ============================================================
    // HELPER FUNCTIONS
    // ============================================================
    
    // Helper: Set value and trigger onblur
    def setValueAndBlur = { String value ->
        WebUI.comment("→ Entering: " + value)
        WebUI.click(inputTopP)
        WebUI.sendKeys(inputTopP, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(inputTopP, value)
        WebUI.clickOffset(inputTopP, 250, 0)   // Trigger onblur
        WebUI.delay(1.2)
    }

    // Helper: Verify numeric value matches expected
    def verifyNumericValue = { String expectedStr, String testStep ->
        String actualStr = WebUI.getAttribute(inputTopP, 'value').trim()
        try {
            BigDecimal actualNum = new BigDecimal(actualStr)
            BigDecimal expectedNum = new BigDecimal(expectedStr)

            if (actualNum.compareTo(expectedNum) == 0) {
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

    // Helper: Verify value is within valid range (0.0 - 1.0)
    def verifyInRange = { String testStep ->
        String actualStr = WebUI.getAttribute(inputTopP, 'value').trim()
        try {
            BigDecimal actualNum = new BigDecimal(actualStr)
            BigDecimal min = new BigDecimal("0.0")
            BigDecimal max = new BigDecimal("1.0")
            
            if (actualNum.compareTo(min) >= 0 && actualNum.compareTo(max) <= 0) {
                WebUI.comment("PASSED - ${testStep}: Value '${actualStr}' is within valid range (0.0 - 1.0) ✓")
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
    // STEP 4: TEST VALID VALUES
    // ============================================================
    WebUI.comment('Step 4: Testing valid values...')
    
    String[][] validValues = [
        ["0.0", "0.0"],
        ["0.5", "0.5"],
        ["1.0", "1.0"]
    ]
    for (String[] pair : validValues) {
        WebUI.comment('--- Testing valid value: ' + pair[0] + ' ---')
        setValueAndBlur(pair[0])
        verifyNumericValue(pair[1], "Valid value test: " + pair[0])
        WebUI.takeScreenshot("TC02C_Parameters_Top_P_Input_Field_TopP_Valid_${pair[0].replace('.', '_')}.png")
    }

    // ============================================================
    // STEP 5: TEST CLAMP MAX (1.5 → 1.0)
    // ============================================================
    WebUI.comment('Step 5: Testing clamp max (1.5 → 1.0)...')
    setValueAndBlur("1.5")
    verifyNumericValue("1.0", "Exceed max test (1.5 → 1.0)")
    WebUI.takeScreenshot('TC02C_Parameters_Top_P_Input_Field_TopP_Clamp_Max.png')

    // ============================================================
    // STEP 6: TEST CLAMP MIN (-0.5 → 0.0)
    // ============================================================
    WebUI.comment('Step 6: Testing clamp min (-0.5 → 0.0)...')
    setValueAndBlur("-0.5")
    verifyNumericValue("0.0", "Below min test (-0.5 → 0.0)")
    WebUI.takeScreenshot('TC02C_Parameters_Top_P_Input_Field_TopP_Clamp_Min.png')

    // ============================================================
    // STEP 7: TEST INVALID INPUT
    // ============================================================
    WebUI.comment('Step 7: Testing invalid input...')
    
    // Set initial valid value
    WebUI.comment('Step 7.1: Setting initial valid value (0.7)...')
    setValueAndBlur("0.7")
    verifyNumericValue("0.7", "Set initial valid value")
    WebUI.comment('Initial value: 0.7')

    // Test invalid inputs
    String[] invalidInputs = ["abc", "xyz123", "hello!"]
    
    for (String invalid : invalidInputs) {
        WebUI.comment('--- Testing invalid input: "' + invalid + '" ---')
        
        // Step 7.2: Enter invalid text
        WebUI.comment('Step 7.2: Entering invalid text: ' + invalid)
        WebUI.click(inputTopP)
        WebUI.sendKeys(inputTopP, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(inputTopP, invalid)
        WebUI.delay(0.5)
        
        // Step 7.3: Trigger onblur by clicking outside
        WebUI.comment('Step 7.3: Triggering onblur by clicking outside...')
        WebUI.clickOffset(inputTopP, 250, 0)
        WebUI.delay(1.5)
        
        // Step 7.4: Verify value is within valid range (0.0 - 1.0)
        WebUI.comment('Step 7.4: Verifying value is within valid range (0.0 - 1.0)...')
        String actualAfterInvalid = WebUI.getAttribute(inputTopP, 'value').trim()
        WebUI.comment('Value after invalid input: ' + actualAfterInvalid)
        
        // Check if value is a valid number
        try {
            BigDecimal actualNum = new BigDecimal(actualAfterInvalid)
            BigDecimal min = new BigDecimal("0.0")
            BigDecimal max = new BigDecimal("1.0")
            
            if (actualNum.compareTo(min) >= 0 && actualNum.compareTo(max) <= 0) {
                WebUI.comment('✓ PASSED: Invalid input "' + invalid + '" correctly reverted to valid number: ' + actualAfterInvalid)
            } else {
                KeywordUtil.markFailed("FAILED: Invalid input '" + invalid + "' resulted in out-of-range value: " + actualAfterInvalid)
            }
        } catch (Exception e) {
            KeywordUtil.markFailed("FAILED: Invalid input '" + invalid + "' resulted in non-numeric value: " + actualAfterInvalid)
        }
        
        WebUI.takeScreenshot("TC02C_Parameters_Top_P_Input_Field_TopP_Invalid_" + invalid + ".png")
        
        // Reset to valid value for next test
        WebUI.comment('Resetting to valid value (0.7) for next test...')
        setValueAndBlur("0.7")
        verifyNumericValue("0.7", "Reset to 0.7")
    }

    // ============================================================
    // STEP 8: FINAL VERIFICATION
    // ============================================================
    WebUI.comment('Step 8: Final verification...')
    String finalValue = WebUI.getAttribute(inputTopP, 'value').trim()
    WebUI.comment('Final Top P value: ' + finalValue)
    
    try {
        BigDecimal finalNum = new BigDecimal(finalValue)
        BigDecimal min = new BigDecimal("0.0")
        BigDecimal max = new BigDecimal("1.0")
        
        if (finalNum.compareTo(min) >= 0 && finalNum.compareTo(max) <= 0) {
            WebUI.comment('✓ TC02C_Parameters_Top_P_Input_Field PASSED - Top P input field works correctly')
        } else {
            KeywordUtil.markFailed("FAILED: Final value is out of range: " + finalValue)
        }
    } catch (Exception e) {
        KeywordUtil.markFailed("FAILED: Final value is not numeric: " + finalValue)
    }

    WebUI.takeScreenshot("TC02C_Parameters_Top_P_Input_Field_TopP_Input_Final.png")
    WebUI.comment('=== TC02C_Parameters_Top_P_Input_Field Completed ===')

} catch (Exception e) {
    WebUI.comment('TC02C_Parameters_Top_P_Input_Field FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC02C_Parameters_Top_P_Input_Field_TopP_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}