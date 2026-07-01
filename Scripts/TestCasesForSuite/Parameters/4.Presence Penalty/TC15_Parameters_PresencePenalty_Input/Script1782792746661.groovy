import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import org.openqa.selenium.Keys as Keys

/**
 * TC15: Parameters - Presence Penalty Input Field
 * Verify direct input of Presence Penalty values (-2.0 to 2.0)
 */

WebUI.comment('=== TC15: Presence Penalty - Input Field Test ===')

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

    // === PRESENCE PENALTY INPUT ===
    WebUI.comment('Step 2: Testing Presence Penalty input field...')
    TestObject inputField = findTestObject('Object Repository/Core Chat/nav/Parameter/input_Presence Penalty')
    WebUI.waitForElementVisible(inputField, 10)
    WebUI.comment('Presence Penalty input field found')

    // ================== HELPER FUNCTIONS ==================
    def setValueAndBlur = { String value ->
        WebUI.comment("→ Entering: " + value)
        WebUI.click(inputField)
        WebUI.sendKeys(inputField, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(inputField, value)
        WebUI.clickOffset(inputField, 250, 0)
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

    // === TEST 1: VALID VALUES ===
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
        WebUI.takeScreenshot("TC15_PresencePenalty_Valid_${pair[0].replace('-', 'neg')}.png")
    }

    // === TEST 2: Exceed max ===
    WebUI.comment('=== Test 2: Exceed max (3.0) → clamp to 2.0 ===')
    setValueAndBlur("3.0")
    verifyNumericValue("2.0", "Exceed max test")

    // === TEST 3: Below min ===
    WebUI.comment('=== Test 3: Below min (-3.0) → clamp to -2.0 ===')
    setValueAndBlur("-3.0")
    verifyNumericValue("-2.0", "Below min test")

    // === TEST 4: Invalid input ===
    WebUI.comment('=== Test 4: Invalid input (non-numeric) ===')
    setValueAndBlur("0.5")
    verifyNumericValue("0.5", "Set initial valid value")

    String[] invalidInputs = ["abc", "xyz123", "hello!"]
    for (String invalid : invalidInputs) {
        WebUI.comment("--- Testing invalid input: " + invalid + " ---")
        
        WebUI.click(inputField)
        WebUI.sendKeys(inputField, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(inputField, invalid)
        WebUI.clickOffset(inputField, 250, 0)
        WebUI.delay(1.5)

        String actualAfter = WebUI.getAttribute(inputField, 'value').trim()
        WebUI.comment("After invalid input → Actual: " + actualAfter)

        try {
            BigDecimal actualNum = new BigDecimal(actualAfter)
            if (actualNum.compareTo(new BigDecimal("0.5")) == 0) {
                WebUI.comment("PASSED: Invalid input correctly reverted to previous value")
            } else {
                KeywordUtil.markFailed("FAILED: Invalid input did not revert. Actual: " + actualAfter)
            }
        } catch (Exception e) {
            KeywordUtil.markFailed("FAILED: Non-numeric value after invalid input: " + actualAfter)
        }

        WebUI.takeScreenshot("TC15_PresencePenalty_Invalid_" + invalid + ".png")
    }

    WebUI.takeScreenshot("TC15_PresencePenalty_Final.png")
    WebUI.comment('=== TC15 Completed Successfully ===')

} catch (Exception e) {
    WebUI.comment('TC15 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC15_PresencePenalty_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}