import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import org.openqa.selenium.Keys as Keys

/**
 * TC07: Parameters - Top P Input Field
 * Verify direct input of Top P values (0.0 - 1.0) with numeric comparison
 */

WebUI.comment('=== TC07: Top P - Input Field Test (Automation Only) ===')

try {
    // === CHECK NAVBAR & PARAMETERS TAB ===
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

    WebUI.comment('Step 1: Opening Parameters tab...')
    TestObject parametersButton = findTestObject('Object Repository/Core Chat/nav/nav_items/button_Parameters')
    WebUI.waitForElementVisible(parametersButton, 10)

    if (WebUI.getAttribute(parametersButton, 'aria-pressed') != 'true') {
        WebUI.click(parametersButton)
        WebUI.delay(2)
    }

    // === TOP P INPUT FIELD ===
    WebUI.comment('Step 2: Testing Top P input field...')
    TestObject inputTopP = findTestObject('Object Repository/Core Chat/nav/Parameter/input_Top P')
    WebUI.waitForElementVisible(inputTopP, 10)
    WebUI.comment('Top P input field found')

    // ================== HELPER FUNCTIONS ==================
    def setValueAndBlur = { String value ->
        WebUI.comment("→ Entering: " + value)
        WebUI.click(inputTopP)
        WebUI.sendKeys(inputTopP, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(inputTopP, value)
        WebUI.clickOffset(inputTopP, 250, 0)   // Trigger onblur
        WebUI.delay(1.2)
    }

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

    // === TEST 1: VALID VALUES ===
    WebUI.comment('=== Test 1: Valid values ===')
    String[][] validValues = [
        ["0.0", "0.0"],
        ["0.5", "0.5"],
        ["1.0", "1.0"]
    ]
    for (String[] pair : validValues) {
        setValueAndBlur(pair[0])
        verifyNumericValue(pair[1], "Valid value test")
        WebUI.takeScreenshot("TC07_TopP_Valid_${pair[0]}.png")
    }

    // === TEST 2: Exceed max ===
    WebUI.comment('=== Test 2: Exceed max (1.5) → clamp 1.0 ===')
    setValueAndBlur("1.5")
    verifyNumericValue("1.0", "Exceed max test")

    // === TEST 3: Below min ===
    WebUI.comment('=== Test 3: Below min (-0.5) → clamp 0.0 ===')
    setValueAndBlur("-0.5")
    verifyNumericValue("0.0", "Below min test")

    // === TEST 4: INVALID INPUT (TÁCH RÕ CÁC BƯỚC NHƯ TC03) ===
    WebUI.comment('=== Test 4: Invalid input (non-numeric) ===')

    // Bước 1: Thiết lập giá trị hợp lệ ban đầu
    WebUI.comment('Step 4.1: Set initial valid value')
    setValueAndBlur("0.7")
    verifyNumericValue("0.7", "Set initial valid value")

    // Bước 2-4: Test với chữ
    String[] invalidInputs = ["abc", "xyz123", "hello!"]

    for (String invalid : invalidInputs) {
        WebUI.comment("--- Testing invalid input: " + invalid + " ---")

        // Bước 2: Nhập chữ
        WebUI.comment("Step 4.2: Entering invalid text: " + invalid)
        WebUI.click(inputTopP)
        WebUI.sendKeys(inputTopP, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(inputTopP, invalid)

        // Bước 3: Trigger onblur
        WebUI.comment("Step 4.3: Trigger onblur by clicking outside")
        WebUI.clickOffset(inputTopP, 250, 0)
        WebUI.delay(1.5)   // Tăng delay một chút

        // Bước 4: Kiểm tra lại giá trị
        WebUI.comment("Step 4.4: Checking value after onblur")
        String actualAfterInvalid = WebUI.getAttribute(inputTopP, 'value').trim()

        try {
            BigDecimal actualNum = new BigDecimal(actualAfterInvalid)
            if (actualNum.compareTo(new BigDecimal("0.7")) == 0) {
                WebUI.comment("PASSED: Invalid input '" + invalid + "' correctly reverted to previous valid value (0.7)")
            } else {
                KeywordUtil.markFailed("FAILED: Invalid input '" + invalid + "' did not revert. Actual: " + actualAfterInvalid)
            }
        } catch (Exception e) {
            KeywordUtil.markFailed("FAILED: Invalid input resulted in non-numeric value: " + actualAfterInvalid)
        }

        WebUI.takeScreenshot("TC07_TopP_Invalid_" + invalid + ".png")
    }

    WebUI.takeScreenshot("TC07_TopP_Input_Final.png")
    WebUI.comment('=== TC07 Automation Test Completed Successfully ===')

} catch (Exception e) {
    WebUI.comment('TC07 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC07_TopP_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}