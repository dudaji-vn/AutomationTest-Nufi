import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import org.openqa.selenium.Keys as Keys

/**
 * TC03C: Parameters - Frequency Penalty Input Field
 * Verify direct input of Frequency Penalty values (-2.0 to 2.0)
 */

WebUI.comment('=== TC03C: Frequency Penalty - Input Field Test ===')

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

    // === FREQUENCY PENALTY INPUT ===
    WebUI.comment('Step 2: Testing Frequency Penalty input field...')
    TestObject inputField = findTestObject('Object Repository/Core Chat/nav/Parameter/input_Frequency Penalty')
    WebUI.waitForElementVisible(inputField, 10)
    WebUI.comment('Frequency Penalty input field found')

    // ================== HELPER FUNCTIONS ==================
    def setValueAndBlur = { String value ->
        WebUI.comment("→ Entering: " + value)
        WebUI.click(inputField)
        WebUI.sendKeys(inputField, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(inputField, value)
        WebUI.clickOffset(inputField, 250, 0)   // Trigger onblur
        WebUI.delay(1.2)
    }

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

    // === TEST 1: VALID VALUES (-2.0 to 2.0) ===
    WebUI.comment('=== Test 1: Valid values (-2.0 to 2.0) ===')
    String[][] validValues = [
        ["-2.0", "-2.0"],
        ["-1.0", "-1.0"],
        ["0.0",  "0.0"],
        ["1.0",  "1.0"],
        ["2.0",  "2.0"]
    ]

    for (String[] pair : validValues) {
        setValueAndBlur(pair[0])
        verifyNumericValue(pair[1], "Valid value test")
        WebUI.takeScreenshot("TC11_FreqPenalty_Valid_${pair[0].replace('-', 'neg')}.png")
    }

    // === TEST 2: Exceed max ===
    WebUI.comment('=== Test 2: Exceed max (3.0) → should clamp to 2.0 ===')
    setValueAndBlur("3.0")
    verifyNumericValue("2.0", "Exceed max test")

    // === TEST 3: Below min ===
    WebUI.comment('=== Test 3: Below min (-3.0) → should clamp to -2.0 ===')
    setValueAndBlur("-3.0")
    verifyNumericValue("-2.0", "Below min test")

    // === TEST 4: Invalid input (letters) ===
    WebUI.comment('=== Test 4: Invalid input (non-numeric) ===')

    // Set base valid value
    WebUI.comment('Step 4.1: Set initial valid value')
    setValueAndBlur("0.5")
    verifyNumericValue("0.5", "Set initial valid value")

    String[] invalidInputs = ["abc", "xyz123", "hello!"]
    
    for (String invalid : invalidInputs) {
        WebUI.comment("--- Testing invalid input: " + invalid + " ---")

        // Step 2: Enter invalid text
        WebUI.comment("Step 4.2: Entering invalid text: " + invalid)
        WebUI.click(inputField)
        WebUI.sendKeys(inputField, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(inputField, invalid)

        // Step 3: Trigger onblur
        WebUI.comment("Step 4.3: Trigger onblur by clicking outside")
        WebUI.clickOffset(inputField, 250, 0)
        WebUI.delay(1.5)

        // Step 4: Check value after blur
        WebUI.comment("Step 4.4: Checking value after onblur")
        String actualAfterInvalid = WebUI.getAttribute(inputField, 'value').trim()

        try {
            BigDecimal actualNum = new BigDecimal(actualAfterInvalid)
            if (actualNum.compareTo(new BigDecimal("0.5")) == 0) {
                WebUI.comment("PASSED: Invalid input '" + invalid + "' correctly reverted to previous valid value (0.5)")
            } else {
                KeywordUtil.markFailed("FAILED: Invalid input '" + invalid + "' did not revert. Actual: " + actualAfterInvalid)
            }
        } catch (Exception e) {
            KeywordUtil.markFailed("FAILED: Invalid input resulted in non-numeric value: " + actualAfterInvalid)
        }

        WebUI.takeScreenshot("TC11_FreqPenalty_Invalid_" + invalid + ".png")
    }

    WebUI.takeScreenshot("TC11_FrequencyPenalty_Final.png")
    WebUI.comment('=== TC11 Automation Test Completed Successfully ===')

} catch (Exception e) {
    WebUI.comment('TC11 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC11_FrequencyPenalty_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}