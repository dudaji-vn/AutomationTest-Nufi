import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC09: Nufi Console - Generate New API Key
 * 
 * Test Flow:
 * 1. Switch to Console tab
 * 2. Go to API Keys tab
 * 3. Record initial Active keys count and table rows
 * 4. Click Generate Key
 * 5. Fill all required fields in popup
 * 6. Submit and verify key created successfully
 * 7. Verify Active keys increased by 1
 * 8. Verify table rows increased by 1
 */

WebUI.comment('=== TC09: Nufi Console - Generate New API Key ===')

try {
    // Step 1: Switch to Console tab
    WebUI.comment('Step 1: Switching to Console tab...')
    WebDriver driver = DriverFactory.getWebDriver()
    Set<String> windowHandles = driver.getWindowHandles()
    boolean foundConsoleTab = false
    
    for (String handle : windowHandles) {
        driver.switchTo().window(handle)
        String url = driver.getCurrentUrl()
        if (url.contains('console.nufi.me')) {
            WebUI.comment('On Console tab: ' + url)
            foundConsoleTab = true
            break
        }
    }
    
    if (!foundConsoleTab) {
        throw new Exception('Console tab not found')
    }
    WebUI.delay(2)

    // Step 2: Click API Keys tab
    WebUI.comment('Step 2: Clicking API Keys tab...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/Page_NUFI Console/a_API keys'),
        10
    )
    WebUI.click(findTestObject('Object Repository/Page_NUFI Console/a_API keys'))
    WebUI.delay(2)
    WebUI.comment('✓ API Keys tab clicked')
    
    // Step 3: Record initial Active keys count and table rows
    WebUI.comment('Step 3: Recording initial metrics...')
    
    // 3.1 Get initial Active keys count
    TestObject activeKeysInitial = new TestObject('activeKeysInitial')
    activeKeysInitial.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(text(), 'Active keys')]/following-sibling::p[contains(@class, 'font-mono')]")
    WebUI.waitForElementVisible(activeKeysInitial, 5)
    String initialActiveKeysText = WebUI.getText(activeKeysInitial)
    int initialActiveKeys = Integer.parseInt(initialActiveKeysText.trim())
    WebUI.comment('Initial Active keys: ' + initialActiveKeys)
    
    // 3.2 Get initial table rows count
    TestObject initialRows = new TestObject('initialRows')
    initialRows.addProperty('xpath', ConditionType.EQUALS, 
        "//table[contains(@class, 'w-full')]//tbody//tr")
    List<TestObject> initialRowList = WebUI.findWebElements(initialRows, 5)
    int initialRowCount = initialRowList.size()
    WebUI.comment('Initial table rows: ' + initialRowCount)

    // Step 4: Click Generate Key button
    WebUI.comment('Step 4: Clicking Generate Key button...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/Page_NUFI Console/API keys/button_Generate Key'),
        10
    )
    WebUI.click(findTestObject('Object Repository/Page_NUFI Console/API keys/button_Generate Key'))
    WebUI.delay(2)
    WebUI.comment('✓ Generate API Key popup opened')

    // Step 5: Fill Alias
    WebUI.comment('Step 5: Entering Alias...')
    String alias = 'TestKey_' + System.currentTimeMillis()
    WebUI.setText(
        findTestObject('Object Repository/Page_NUFI Console/API keys/Generate API key/input_Alias_alias'),
        alias
    )
    WebUI.comment('Alias: ' + alias)

    // Step 6: Fill Max Budget
    WebUI.comment('Step 6: Entering Max Budget...')
    WebUI.setText(
        findTestObject('Object Repository/Page_NUFI Console/API keys/Generate API key/input_Max budget (USD)_budget'),
        '10.00'
    )
    WebUI.comment('✓ Max Budget: 10.00')

    // Step 7: Fill TPM & RPM limits
    WebUI.comment('Step 7: Setting TPM & RPM limits...')
    WebUI.setText(
        findTestObject('Object Repository/Page_NUFI Console/API keys/Generate API key/input_TPM limit_tpm'),
        '1000000'
    )
    WebUI.comment('✓ TPM: 1000000')
    
    WebUI.setText(
        findTestObject('Object Repository/Page_NUFI Console/API keys/Generate API key/input_RPM limit_rpm'),
        '1000'
    )
    WebUI.comment('✓ RPM: 1000')

    // Step 8: Click Generate button (Submit)
    WebUI.comment('Step 8: Clicking Generate button...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/Page_NUFI Console/API keys/Generate API key/button_Generate'),
        10
    )
    WebUI.click(findTestObject('Object Repository/Page_NUFI Console/API keys/Generate API key/button_Generate'))
    WebUI.delay(3)
    WebUI.comment('✓ Generate button clicked')

    // Step 9: Verify key created successfully
    WebUI.comment('Step 9: Verifying API Key created...')
    WebUI.delay(2)
    
    // Tìm key mới trong table bằng alias
    TestObject newKeyRow = new TestObject('newKeyRow')
    newKeyRow.addProperty('xpath', ConditionType.EQUALS, 
        "//td//span[contains(text(), '${alias}')]")
    
    boolean keyExists = WebUI.waitForElementVisible(newKeyRow, 15, FailureHandling.OPTIONAL)
    
    if (!keyExists) {
        // Thử XPath khác nếu không tìm thấy
        TestObject newKeyRowAlt = new TestObject('newKeyRowAlt')
        newKeyRowAlt.addProperty('xpath', ConditionType.EQUALS, 
            "//tr[contains(., '${alias}')]")
        keyExists = WebUI.waitForElementVisible(newKeyRowAlt, 5, FailureHandling.OPTIONAL)
    }
    
    if (keyExists) {
        WebUI.comment('✓ New API Key found in table: ' + alias)
    } else {
        throw new Exception('API Key creation verification failed - key not found in table')
    }

    // Step 10: Verify Active keys increased by 1
    WebUI.comment('Step 10: Verifying Active keys increased by 1...')
    
    TestObject activeKeysAfter = new TestObject('activeKeysAfter')
    activeKeysAfter.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(text(), 'Active keys')]/following-sibling::p[contains(@class, 'font-mono')]")
    WebUI.waitForElementVisible(activeKeysAfter, 5)
    String afterActiveKeysText = WebUI.getText(activeKeysAfter)
    int afterActiveKeys = Integer.parseInt(afterActiveKeysText.trim())
    WebUI.comment('Active keys after creation: ' + afterActiveKeys)
    
    int expectedActiveKeys = initialActiveKeys + 1
    if (afterActiveKeys == expectedActiveKeys) {
        WebUI.comment('✓ Active keys increased from ' + initialActiveKeys + ' to ' + afterActiveKeys + ' (+1)')
    } else {
        throw new Exception('Active keys count mismatch. Before: ' + initialActiveKeys + ', After: ' + afterActiveKeys + ', Expected: ' + expectedActiveKeys)
    }

    // Step 11: Verify table rows increased by 1
    WebUI.comment('Step 11: Verifying table rows increased by 1...')
    
    TestObject afterRows = new TestObject('afterRows')
    afterRows.addProperty('xpath', ConditionType.EQUALS, 
        "//table[contains(@class, 'w-full')]//tbody//tr")
    List<TestObject> afterRowList = WebUI.findWebElements(afterRows, 5)
    int afterRowCount = afterRowList.size()
    WebUI.comment('Table rows after creation: ' + afterRowCount)
    
    int expectedRowCount = initialRowCount + 1
    if (afterRowCount == expectedRowCount) {
        WebUI.comment('✓ Table rows increased from ' + initialRowCount + ' to ' + afterRowCount + ' (+1)')
    } else {
        throw new Exception('Table row count mismatch. Before: ' + initialRowCount + ', After: ' + afterRowCount + ', Expected: ' + expectedRowCount)
    }

    WebUI.takeScreenshot('TC09_Console_Generate_API_Key_Success.png')
    WebUI.comment('=== TC09 PASSED - API Key generated successfully ===')
    WebUI.comment('=== Summary: ===')
    WebUI.comment('  ✓ Alias: ' + alias)
    WebUI.comment('  ✓ Active keys: ' + initialActiveKeys + ' → ' + afterActiveKeys + ' (+1)')
    WebUI.comment('  ✓ Table rows: ' + initialRowCount + ' → ' + afterRowCount + ' (+1)')

} catch (Exception e) {
    WebUI.comment('=== TC09 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC09_Console_Generate_API_Key_Error.png')
    throw e
}