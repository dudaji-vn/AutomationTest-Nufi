import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import org.openqa.selenium.Keys as Keys

/**
 * TC03: Parameters - Temperature Input Field
 * Verify direct input of Temperature values (0.0 - 2.0) with numeric comparison
 */

WebUI.comment('=== TC03: Temperature - Input Field Test (Automation Only) ===')

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

    // === TEMPERATURE INPUT ===
    WebUI.comment('Step 2: Testing Temperature input field...')
    TestObject tempInput = findTestObject('Object Repository/Core Chat/nav/Parameter/input_Temperature')
    WebUI.waitForElementVisible(tempInput, 10)

    // ================== HELPER FUNCTIONS ==================
    def setValueAndBlur = { String value ->
        WebUI.comment("→ Entering: " + value)
        WebUI.click(tempInput)
        WebUI.sendKeys(tempInput, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(tempInput, value)
        WebUI.clickOffset(tempInput, 250, 0)   // Trigger onblur
        WebUI.delay(1.2)
    }

    def verifyNumericValue = { String expectedStr, String testStep ->
        String actualStr = WebUI.getAttribute(tempInput, 'value').trim()
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

    // === TEST 1-4: VALID & CLAMPING CASES (giữ nguyên) ===
    WebUI.comment('=== Test 1: Valid values ===')
    String[][] validValues = [["0.75", "0.75"], ["2", "2"], ["0.0", "0.0"]]
    for (String[] pair : validValues) {
        setValueAndBlur(pair[0])
        verifyNumericValue(pair[1], "Valid value test")
        WebUI.takeScreenshot("TC03_Temp_Valid_${pair[0]}.png")
    }

    WebUI.comment('=== Test 2: Exceed max (2.5) → clamp 2.0 ===')
    setValueAndBlur("2.5")
    verifyNumericValue("2.0", "Exceed max test")

    WebUI.comment('=== Test 3: Below min (-0.5) → clamp 0.0 ===')
    setValueAndBlur("-0.5")
    verifyNumericValue("0.0", "Below min test")

    WebUI.comment('=== Test 4: Very large (99) → clamp 2.0 ===')
    setValueAndBlur("99")
    verifyNumericValue("2.0", "Very large value test")

    // === TEST 5: INVALID INPUT (TÁCH RÕ CÁC BƯỚC) ===
    WebUI.comment('=== Test 5: Invalid input (non-numeric) ===')
    
    // Bước 1: Thiết lập giá trị hợp lệ ban đầu
    WebUI.comment('Step 5.1: Set initial valid value')
    setValueAndBlur("1.5")
    verifyNumericValue("1.5", "Set initial valid value")

    // Bước 2-4: Test với chữ
    String[] invalidInputs = ["abc", "xyz123", "hello!"]

    for (String invalid : invalidInputs) {
        WebUI.comment("--- Testing invalid input: " + invalid + " ---")
        
        // Bước 2: Nhập chữ
        WebUI.comment("Step 5.2: Entering invalid text: " + invalid)
        WebUI.click(tempInput)
        WebUI.sendKeys(tempInput, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(tempInput, invalid)
        
        // Bước 3: Trigger onblur (click ra ngoài)
        WebUI.comment("Step 5.3: Trigger onblur by clicking outside")
        WebUI.clickOffset(tempInput, 250, 0)
        WebUI.delay(1.2)
        
        // Bước 4: Kiểm tra lại giá trị trong input
        WebUI.comment("Step 5.4: Checking value after onblur")
        String actualAfterInvalid = WebUI.getAttribute(tempInput, 'value').trim()
        
        try {
            BigDecimal actualNum = new BigDecimal(actualAfterInvalid)
            if (actualNum.compareTo(new BigDecimal("1.5")) == 0) {
                WebUI.comment("PASSED: Invalid input '" + invalid + "' correctly reverted to previous valid value (1.5)")
            } else {
                KeywordUtil.markFailed("FAILED: Invalid input '" + invalid + "' did not revert. Actual: " + actualAfterInvalid)
            }
        } catch (Exception e) {
            KeywordUtil.markFailed("FAILED: Invalid input resulted in non-numeric value: " + actualAfterInvalid)
        }
        
        WebUI.takeScreenshot("TC03_Temp_Invalid_" + invalid + ".png")
    }

    WebUI.takeScreenshot("TC03_Temperature_Input_Final.png")
    WebUI.comment('=== TC03 Automation Test Completed Successfully ===')

} catch (Exception e) {
    WebUI.comment('TC03 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC03_Temperature_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}