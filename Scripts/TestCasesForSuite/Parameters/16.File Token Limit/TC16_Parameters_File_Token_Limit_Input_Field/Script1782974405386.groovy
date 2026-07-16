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
 * When invalid input is entered, the input field should show NaN or any valid number >= 0
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Test valid value: 500
 * 3. Test min value: 0
 * 4. Test negative: -1 → should be NaN or any number >= 0
 * 5. Test invalid input: abc, @, 1.2.3 → should show NaN or any number >= 0
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
    }

    // ============================================================
    // STEP 1: VERIFY PARAMETERS TAB
    // ============================================================
    WebUI.comment('Step 1: Checking Parameters tab state...')
    
    TestObject parametersButton = findTestObject('Object Repository/nav/nav_items/button_Parameters')
    WebUI.waitForElementVisible(parametersButton, 10)
    
    String isPressed = WebUI.getAttribute(parametersButton, 'aria-pressed')
    
    if (isPressed != 'true') {
        WebUI.comment('Parameters tab not open, clicking to open...')
        WebUI.click(parametersButton)
        WebUI.delay(2)
        WebUI.comment('Parameters tab opened')
    } else {
        WebUI.comment('Parameters tab already open')
    }

    // ============================================================
    // STEP 2: GET FILE TOKEN LIMIT INPUT
    // ============================================================
    WebUI.comment('Step 2: Getting File Token Limit input field...')
    
    TestObject inputField = findTestObject('Object Repository/nav/Parameter/input_File Token Limit')
    WebUI.waitForElementVisible(inputField, 10)
    WebUI.comment('File Token Limit input found')

    // ============================================================
    // CLICK OUTSIDE ELEMENT (to trigger onblur safely)
    // ============================================================
    TestObject clickOutside = new TestObject('clickOutside')
    clickOutside.addProperty('xpath', ConditionType.EQUALS, "//body")

    // ============================================================
    // HELPER FUNCTIONS
    // ============================================================
    
    // Helper: Set value and trigger onblur
    def setValueAndBlur = { String value ->
        WebUI.comment("→ Entering: " + value)
        WebUI.click(inputField)
        WebUI.sendKeys(inputField, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(inputField, value)
        WebUI.delay(0.5)
        WebUI.click(clickOutside)
        WebUI.delay(0.5)
    }

    // Helper: Verify value is valid (NaN or any number >= 0)
    def verifyValid = { String testStep ->
        String actualStr = WebUI.getAttribute(inputField, 'value').trim()
        WebUI.comment("Actual value: '" + actualStr + "'")
        
        // Case 1: Value is NaN - PASS
        if (actualStr == "NaN" || actualStr == "nan" || actualStr == "") {
            WebUI.comment("PASSED - ${testStep}: Value is NaN")
            return true
        }
        
        // Case 2: Value is number >= 0 - PASS
        try {
            BigDecimal actualNum = new BigDecimal(actualStr)
            if (actualNum.compareTo(BigDecimal.ZERO) >= 0) {
                WebUI.comment("PASSED - ${testStep}: Value '${actualStr}' is valid number >= 0")
                return true
            } else {
                WebUI.comment("FAILED - ${testStep}: Value '${actualStr}' is negative number (invalid)")
                KeywordUtil.markFailed("FAILED - ${testStep}: Value '${actualStr}' is negative number (invalid)")
                return false
            }
        } catch (Exception e) {
            WebUI.comment("FAILED - ${testStep}: Value '${actualStr}' is not NaN and not a valid number")
            KeywordUtil.markFailed("FAILED - ${testStep}: Value '${actualStr}' is not NaN and not a valid number")
            return false
        }
    }

    // ============================================================
    // STEP 3: TEST VALID VALUE (500)
    // ============================================================
    WebUI.comment('Step 3: Testing valid value (500)...')
    setValueAndBlur("500")
    verifyValid("Valid value test: 500")
    WebUI.takeScreenshot("TC16_FileTokenLimit_Valid.png")

    // ============================================================
    // STEP 4: TEST MIN VALUE (0)
    // ============================================================
    WebUI.comment('Step 4: Testing min value (0)...')
    setValueAndBlur("0")
    verifyValid("Min value test: 0")
    WebUI.takeScreenshot('TC16_FileTokenLimit_Min.png')

    // ============================================================
    // STEP 5: TEST NEGATIVE VALUE (-1)
    // ============================================================
    WebUI.comment('Step 5: Testing negative value (-1)...')
    setValueAndBlur("-1")
    verifyValid("Negative test: -1")
    WebUI.takeScreenshot('TC16_FileTokenLimit_Negative.png')

    // ============================================================
    // STEP 6: TEST INVALID INPUTS
    // ============================================================
    WebUI.comment('Step 6: Testing invalid inputs...')
    
    String[] invalidInputs = ["abc", "@", "1.2.3"]
    
    for (String invalid : invalidInputs) {
        WebUI.comment('--- Testing invalid input: "' + invalid + '" ---')
        
        setValueAndBlur(invalid)
        verifyValid("Invalid input: " + invalid)
        WebUI.takeScreenshot("TC16_FileTokenLimit_Invalid_" + invalid + ".png")
        
        // Reset to valid value for next test
        WebUI.comment('Resetting to 500...')
        setValueAndBlur("500")
        verifyValid("Reset after invalid")
    }

    // ============================================================
    // STEP 7: FINAL VERIFICATION
    // ============================================================
    WebUI.comment('Step 7: Final verification...')
    
    String finalValue = WebUI.getAttribute(inputField, 'value').trim()
    WebUI.comment('Final File Token Limit value: ' + finalValue)
    
    // Check final value is valid (NaN or number >= 0)
    if (finalValue == "NaN" || finalValue == "nan" || finalValue == "") {
        WebUI.comment('TC16 PASSED - Final value is NaN')
    } else {
        try {
            BigDecimal finalNum = new BigDecimal(finalValue)
            if (finalNum.compareTo(BigDecimal.ZERO) >= 0) {
                WebUI.comment('TC16 PASSED - Final value is valid number >= 0')
            } else {
                WebUI.comment("FAILED: Final value is negative: " + finalValue)
                KeywordUtil.markFailed("FAILED: Final value is negative: " + finalValue)
            }
        } catch (Exception e) {
            WebUI.comment("FAILED: Final value is invalid: " + finalValue)
            KeywordUtil.markFailed("FAILED: Final value is invalid: " + finalValue)
        }
    }

    WebUI.takeScreenshot("TC16_FileTokenLimit_Complete.png")
    WebUI.comment('=== TC16 Completed ===')

} catch (Exception e) {
    WebUI.comment('TC16 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC16_FileTokenLimit_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}