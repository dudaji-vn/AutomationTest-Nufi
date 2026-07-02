import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import org.openqa.selenium.Keys as Keys

/**
 * TC16: Parameters - File Token Limit Input Field
 * Set maximum token limit for file processing to control costs and resource usage
 * When invalid input is entered, the input field should show NaN
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Test valid value: 500
 * 3. Test clamp min: 0 → 0
 * 4. Test negative: -1 → 0
 * 5. Test invalid input: abc, @, 1.2.3 → should show NaN
 * 6. Final verification
 */

WebUI.comment('=== TC16: File Token Limit - Input Field Test ===')

try {
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
    // STEP 1: VERIFY PARAMETERS TAB
    // ============================================================
    WebUI.comment('Step 1: Checking Parameters tab state...')
    
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
    // STEP 2: GET FILE TOKEN LIMIT INPUT
    // ============================================================
    WebUI.comment('Step 2: Getting File Token Limit input field...')
    
    TestObject inputField = findTestObject('Object Repository/Core Chat/nav/Parameter/input_File Token Limit')
    WebUI.waitForElementVisible(inputField, 10)
    WebUI.comment('File Token Limit input found')

    // ============================================================
    // HELPER FUNCTIONS
    // ============================================================
    
    // Helper: Set value and trigger onblur
    def setValueAndBlur = { String value ->
        WebUI.comment("→ Entering: " + value)
        WebUI.click(inputField)
        WebUI.sendKeys(inputField, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(inputField, value)
        WebUI.delay(0.3)
        
        // Trigger onblur - multiple positions to avoid popup
        WebUI.comment("→ Triggering onblur...")
        WebUI.clickOffset(inputField, 250, 0)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputField, -50, 50)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputField, 100, 100)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputField, 0, -100)
        WebUI.delay(0.5)
        
        // Log value after blur
        String actualValue = WebUI.getAttribute(inputField, 'value').trim()
        WebUI.comment("→ After blur, value: " + actualValue)
    }

    // Helper: Verify numeric value matches expected
    def verifyNumericValue = { String expectedStr, String testStep ->
        String actualStr = WebUI.getAttribute(inputField, 'value').trim()
        try {
            BigDecimal actualNum = new BigDecimal(actualStr)
            BigDecimal expectedNum = new BigDecimal(expectedStr)
            
            if (actualNum.compareTo(expectedNum) == 0) {
                WebUI.comment("PASSED - ${testStep}: Expected '${expectedStr}' → Actual: '${actualStr}'")
                return true
            } else {
                WebUI.comment("FAILED - ${testStep}: Expected ${expectedStr} but got ${actualStr}")
                KeywordUtil.markFailed("FAILED - ${testStep}: Expected ${expectedStr} but got ${actualStr}")
                return false
            }
        } catch (Exception e) {
            WebUI.comment("FAILED - ${testStep}: Cannot convert to number. Actual: '${actualStr}'")
            KeywordUtil.markFailed("FAILED - ${testStep}: Cannot convert to number. Actual: '${actualStr}'")
            return false
        }
    }

    // Helper: Verify value is NaN
    def verifyNaN = { String testStep ->
        String actualStr = WebUI.getAttribute(inputField, 'value').trim()
        if (actualStr == "NaN" || actualStr == "nan" || actualStr == "") {
            WebUI.comment("PASSED - ${testStep}: Value '${actualStr}' is NaN")
            return true
        } else {
            WebUI.comment("FAILED - ${testStep}: Expected NaN but got '${actualStr}'")
            KeywordUtil.markFailed("FAILED - ${testStep}: Expected NaN but got '${actualStr}'")
            return false
        }
    }

    // ============================================================
    // STEP 3: TEST VALID VALUE (500)
    // ============================================================
    WebUI.comment('Step 3: Testing valid value (500)...')
    setValueAndBlur("500")
    verifyNumericValue("500", "Valid value test: 500")
    WebUI.takeScreenshot("TC16_FileTokenLimit_Valid.png")

    // ============================================================
    // STEP 4: TEST MIN VALUE (0)
    // ============================================================
    WebUI.comment('Step 4: Testing min value (0)...')
    setValueAndBlur("0")
    verifyNumericValue("0", "Min value test (0)")
    WebUI.takeScreenshot('TC16_FileTokenLimit_Min.png')

    // ============================================================
    // STEP 5: TEST NEGATIVE VALUE (-1 → 0)
    // ============================================================
    WebUI.comment('Step 5: Testing negative value (-1 → 0)...')
    setValueAndBlur("-1")
    verifyNumericValue("0", "Below min test (-1 → 0)")
    WebUI.takeScreenshot('TC16_FileTokenLimit_Negative.png')

    // ============================================================
    // STEP 6: TEST INVALID INPUT → NaN
    // ============================================================
    WebUI.comment('Step 6: Testing invalid input → should show NaN...')
    
    // Set initial valid value
    WebUI.comment('Step 6.1: Setting initial valid value (500)...')
    setValueAndBlur("500")
    verifyNumericValue("500", "Set initial valid value")
    WebUI.comment('Initial value: 500')

    // Test invalid inputs
    String[] invalidInputs = ["abc", "@", "1.2.3"]
    
    for (String invalid : invalidInputs) {
        WebUI.comment('--- Testing invalid input: "' + invalid + '" ---')
        
        // Step 6.2: Enter invalid text
        WebUI.comment('Step 6.2: Entering invalid text: ' + invalid)
        WebUI.click(inputField)
        WebUI.sendKeys(inputField, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(inputField, invalid)
        WebUI.delay(0.5)
        
        // Step 6.3: Trigger onblur
        WebUI.comment('Step 6.3: Triggering onblur...')
        WebUI.clickOffset(inputField, 250, 0)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputField, -50, 50)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputField, 100, 100)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputField, 0, -100)
        WebUI.delay(0.5)
        
        // Step 6.4: Verify value is NaN
        WebUI.comment('Step 6.4: Verifying value is NaN...')
        String actualAfterInvalid = WebUI.getAttribute(inputField, 'value').trim()
        WebUI.comment('Input "' + invalid + '" → Actual: "' + actualAfterInvalid + '"')
        
        if (actualAfterInvalid == "NaN" || actualAfterInvalid == "nan" || actualAfterInvalid == "") {
            WebUI.comment('PASSED: Invalid input "' + invalid + '" correctly handled as NaN')
        } else {
            WebUI.comment('FAILED: Invalid input "' + invalid + '" not handled correctly. Actual: ' + actualAfterInvalid)
            KeywordUtil.markFailed("FAILED: Invalid input '" + invalid + "' not handled correctly. Actual: " + actualAfterInvalid)
        }
        
        WebUI.takeScreenshot("TC16_FileTokenLimit_Invalid_" + invalid + ".png")
        
        // Reset to valid value for next test
        WebUI.comment('Resetting to valid value (500) for next test...')
        setValueAndBlur("500")
        verifyNumericValue("500", "Reset to 500")
    }

    // ============================================================
    // STEP 7: FINAL VERIFICATION
    // ============================================================
    WebUI.comment('Step 7: Final verification...')
    
    String finalValue = WebUI.getAttribute(inputField, 'value').trim()
    WebUI.comment('Final File Token Limit value: ' + finalValue)
    
    try {
        BigDecimal finalNum = new BigDecimal(finalValue)
        WebUI.comment('TC16 PASSED - File Token Limit input field works correctly')
    } catch (Exception e) {
        WebUI.comment("FAILED: Final value is not numeric: " + finalValue)
        KeywordUtil.markFailed("FAILED: Final value is not numeric: " + finalValue)
    }

    WebUI.takeScreenshot("TC16_FileTokenLimit_Complete.png")
    WebUI.comment('=== TC16 Completed ===')

} catch (Exception e) {
    WebUI.comment('TC16 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC16_FileTokenLimit_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}