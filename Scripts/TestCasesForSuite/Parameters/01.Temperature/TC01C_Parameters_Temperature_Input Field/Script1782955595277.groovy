import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import org.openqa.selenium.Keys as Keys

/**
 * TC01C_Parameters_Temperature_Input_Field
 * 
 * Verify direct input of Temperature values (0.0 - 2.0) with numeric comparison
 * When invalid input is entered, the input field should revert to a valid number (0.0 - 2.0)
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Test valid values: 0.75, 2, 0.0
 * 3. Test clamp max: 2.5 → 2.0
 * 4. Test clamp min: -0.5 → 0.0
 * 5. Test clamp very large: 99 → 2.0
 * 6. Test invalid input: abc, @, 1.2.3
 * 7. Verify invalid input reverts to a valid number (0.0 - 2.0)
 */

WebUI.comment('=== TC01C: Temperature - Input Field Test ===')

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
    // STEP 2: GET TEMPERATURE INPUT
    // ============================================================
    WebUI.comment('Step 2: Getting Temperature input field...')
    TestObject tempInput = findTestObject('Object Repository/Core Chat/nav/Parameter/input_Temperature')
    WebUI.waitForElementVisible(tempInput, 10)
    WebUI.comment('Temperature input found')

    // ============================================================
    // HELPER FUNCTIONS
    // ============================================================
    
    // Helper: Set value and trigger onblur - MULTIPLE CLICKS TO AVOID POPUP
    def setValueAndBlur = { String value ->
        WebUI.comment("→ Entering: " + value)
        WebUI.click(tempInput)
        WebUI.sendKeys(tempInput, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(tempInput, value)
        WebUI.delay(0.3)
        
        // Trigger onblur by clicking outside - multiple positions to avoid popup
        WebUI.comment("→ Triggering onblur...")
        WebUI.clickOffset(tempInput, 250, 0)    // Click phải
        WebUI.delay(0.3)
        WebUI.clickOffset(tempInput, -50, 50)   // Click trái xuống
        WebUI.delay(0.3)
        WebUI.clickOffset(tempInput, 100, 100)  // Click phải xuống
        WebUI.delay(0.3)
        WebUI.clickOffset(tempInput, 0, -100)   // Click lên trên
        WebUI.delay(0.5)
        
        // Verify after blur - read value
        String actualValue = WebUI.getAttribute(tempInput, 'value').trim()
        WebUI.comment("→ After blur, value: " + actualValue)
    }

    // Helper: Verify numeric value with tolerance (for floating point)
    def verifyNumericValueWithTolerance = { String expectedStr, String testStep, double toleranceValue = 0.01 ->
        // Đọc giá trị thực tế từ input
        String actualStr = WebUI.getAttribute(tempInput, 'value').trim()
        try {
            BigDecimal actualNum = new BigDecimal(actualStr)
            BigDecimal expectedNum = new BigDecimal(expectedStr)
            BigDecimal tolerance = new BigDecimal(toleranceValue)
            BigDecimal diff = actualNum.subtract(expectedNum).abs()
            
            if (diff.compareTo(tolerance) <= 0) {
                WebUI.comment("PASSED - ${testStep}: Expected '${expectedStr}' → Actual: '${actualStr}' (diff: ${diff}) ✓")
                return true
            } else {
                KeywordUtil.markFailed("FAILED - ${testStep}: Expected ${expectedStr} but got ${actualStr} (diff: ${diff})")
                return false
            }
        } catch (Exception e) {
            KeywordUtil.markFailed("FAILED - ${testStep}: Cannot convert to number. Actual: '${actualStr}'")
            return false
        }
    }

    // Helper: Verify value is within valid range (0.0 - 2.0)
    def verifyInRange = { String testStep ->
        String actualStr = WebUI.getAttribute(tempInput, 'value').trim()
        try {
            BigDecimal actualNum = new BigDecimal(actualStr)
            BigDecimal min = new BigDecimal("0.0")
            BigDecimal max = new BigDecimal("2.0")
            
            if (actualNum.compareTo(min) >= 0 && actualNum.compareTo(max) <= 0) {
                WebUI.comment("PASSED - ${testStep}: Value '${actualStr}' is within valid range (0.0 - 2.0) ✓")
                return true
            } else {
                KeywordUtil.markFailed("FAILED - ${testStep}: Value '${actualStr}' is NOT within valid range (0.0 - 2.0)")
                return false
            }
        } catch (Exception e) {
            KeywordUtil.markFailed("FAILED - ${testStep}: Cannot convert to number. Actual: '${actualStr}'")
            return false
        }
    }

    // ============================================================
    // STEP 3: TEST VALID VALUES
    // ============================================================
    WebUI.comment('Step 3: Testing valid values...')
    
    String[][] validValues = [["0.75", "0.75"], ["2", "2.0"], ["0.0", "0.0"]]
    for (String[] pair : validValues) {
        WebUI.comment('--- Testing valid value: ' + pair[0] + ' ---')
        setValueAndBlur(pair[0])
        verifyNumericValueWithTolerance(pair[1], "Valid value test: " + pair[0])
        WebUI.takeScreenshot("TC03_Temp_Valid_${pair[0].replace('.', '_')}.png")
    }

    // ============================================================
    // STEP 4: TEST CLAMP MAX (2.5 → 2.0)
    // ============================================================
    WebUI.comment('Step 4: Testing clamp max (2.5 → 2.0)...')
    setValueAndBlur("2.5")
    verifyNumericValueWithTolerance("2.0", "Exceed max test (2.5 → 2.0)")
    WebUI.takeScreenshot('TC03_Temp_Clamp_Max.png')

    // ============================================================
    // STEP 5: TEST CLAMP MIN (-0.5 → 0.0)
    // ============================================================
    WebUI.comment('Step 5: Testing clamp min (-0.5 → 0.0)...')
    setValueAndBlur("-0.5")
    verifyNumericValueWithTolerance("0.0", "Below min test (-0.5 → 0.0)")
    WebUI.takeScreenshot('TC03_Temp_Clamp_Min.png')

    // ============================================================
    // STEP 6: TEST VERY LARGE (99 → 2.0)
    // ============================================================
    WebUI.comment('Step 6: Testing very large value (99 → 2.0)...')
    setValueAndBlur("99")
    verifyNumericValueWithTolerance("2.0", "Very large value test (99 → 2.0)")
    WebUI.takeScreenshot('TC03_Temp_Very_Large.png')

    // ============================================================
    // STEP 7: TEST INVALID INPUT
    // ============================================================
    WebUI.comment('Step 7: Testing invalid input...')
    
    // Set initial valid value
    WebUI.comment('Step 7.1: Setting initial valid value (1.5)...')
    setValueAndBlur("1.5")
    verifyNumericValueWithTolerance("1.5", "Set initial valid value")
    WebUI.comment('Initial value: 1.5')

    // Test invalid inputs
    String[] invalidInputs = ["abc", "@", "1.2.3", "hello", "test"]
    
    for (String invalid : invalidInputs) {
        WebUI.comment('--- Testing invalid input: "' + invalid + '" ---')
        
        // Step 7.2: Enter invalid text
        WebUI.comment('Step 7.2: Entering invalid text: ' + invalid)
        WebUI.click(tempInput)
        WebUI.sendKeys(tempInput, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(tempInput, invalid)
        WebUI.delay(0.5)
        
        // Step 7.3: Trigger onblur - MULTIPLE CLICKS
        WebUI.comment('Step 7.3: Triggering onblur...')
        WebUI.clickOffset(tempInput, 250, 0)
        WebUI.delay(0.3)
        WebUI.clickOffset(tempInput, -50, 50)
        WebUI.delay(0.3)
        WebUI.clickOffset(tempInput, 100, 100)
        WebUI.delay(0.3)
        WebUI.clickOffset(tempInput, 0, -100)
        WebUI.delay(0.5)
        
        // Step 7.4: Verify value is within valid range (0.0 - 2.0)
        WebUI.comment('Step 7.4: Verifying value is within valid range (0.0 - 2.0)...')
        String actualAfterInvalid = WebUI.getAttribute(tempInput, 'value').trim()
        WebUI.comment('Value after invalid input: ' + actualAfterInvalid)
        
        // Check if value is a valid number
        try {
            BigDecimal actualNum = new BigDecimal(actualAfterInvalid)
            BigDecimal min = new BigDecimal("0.0")
            BigDecimal max = new BigDecimal("2.0")
            
            if (actualNum.compareTo(min) >= 0 && actualNum.compareTo(max) <= 0) {
                WebUI.comment('✓ PASSED: Invalid input "' + invalid + '" correctly reverted to valid number: ' + actualAfterInvalid)
            } else {
                KeywordUtil.markFailed("FAILED: Invalid input '" + invalid + "' resulted in out-of-range value: " + actualAfterInvalid)
            }
        } catch (Exception e) {
            KeywordUtil.markFailed("FAILED: Invalid input '" + invalid + "' resulted in non-numeric value: " + actualAfterInvalid)
        }
        
        WebUI.takeScreenshot("TC03_Temp_Invalid_" + invalid + ".png")
        
        // Reset to valid value for next test
        WebUI.comment('Resetting to valid value (1.5) for next test...')
        setValueAndBlur("1.5")
        verifyNumericValueWithTolerance("1.5", "Reset to 1.5")
    }

    // ============================================================
    // STEP 8: FINAL VERIFICATION
    // ============================================================
    WebUI.comment('Step 8: Final verification...')
    String finalValue = WebUI.getAttribute(tempInput, 'value').trim()
    WebUI.comment('Final Temperature value: ' + finalValue)
    
    try {
        BigDecimal finalNum = new BigDecimal(finalValue)
        BigDecimal min = new BigDecimal("0.0")
        BigDecimal max = new BigDecimal("2.0")
        
        if (finalNum.compareTo(min) >= 0 && finalNum.compareTo(max) <= 0) {
            WebUI.comment('✓ TC03 PASSED - Temperature input field works correctly')
        } else {
            KeywordUtil.markFailed("FAILED: Final value is out of range: " + finalValue)
        }
    } catch (Exception e) {
        KeywordUtil.markFailed("FAILED: Final value is not numeric: " + finalValue)
    }

    WebUI.takeScreenshot("TC03_Temperature_Input_Final.png")
    WebUI.comment('=== TC03 Completed ===')

} catch (Exception e) {
    WebUI.comment('TC03 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC03_Temperature_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}