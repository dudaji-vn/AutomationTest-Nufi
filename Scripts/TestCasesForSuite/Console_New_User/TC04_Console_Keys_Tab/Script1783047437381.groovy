import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC04: Nufi Console - API Keys Tab
 * 
 * Test Flow:
 * 1. Ensure on Console tab
 * 2. Click API Keys tab
 * 3. Verify API Keys page loads
 * 4. Verify API Keys metrics:
 *    - Active keys = 1 (default key created when account is created)
 *    - Used across keys = $0.00
 *    - Total budget = — (No caps set)
 *    - Expiring this week = 0
 * 5. Verify Generate Key button exists
 * 6. Verify API key table:
 *    - Table has exactly 1 row (only default key)
 *    - Default key details: (unnamed), $0.00, limits, created date, expires never
 * 7. Verify Revoke button exists
 */

WebUI.comment('=== TC04: Nufi Console - API Keys Tab ===')

try {
    // Switch to Console tab
    WebUI.comment('Step 1: Switching to Console tab...')
    WebDriver driver = DriverFactory.getWebDriver()
    Set<String> windowHandles = driver.getWindowHandles()
    
    boolean foundConsole = false
    for (String handle : windowHandles) {
        driver.switchTo().window(handle)
        String url = driver.getCurrentUrl()
        if (url.contains('console.nufi.me')) {
            WebUI.comment('On Console tab: ' + url)
            foundConsole = true
            break
        }
    }
    
    if (!foundConsole) {
        throw new Exception('Console tab not found')
    }
    WebUI.delay(2)
    
    // Step 2: Click API Keys tab
    WebUI.comment('Step 2: Clicking API Keys tab...')
    TestObject keysTab = new TestObject('keysTab')
    keysTab.addProperty('xpath', ConditionType.EQUALS, 
        "//a[@href='/keys' and contains(text(), 'API keys')]")
    WebUI.waitForElementClickable(keysTab, 10)
    WebUI.click(keysTab)
    WebUI.delay(2)
    WebUI.comment('API Keys tab clicked')
    
    // Step 3: Verify API Keys page loads
    WebUI.comment('Step 3: Verifying API Keys page loads...')
    String currentUrl = driver.getCurrentUrl()
    WebUI.comment('Current URL: ' + currentUrl)
    
    if (!currentUrl.contains('/keys')) {
        throw new Exception('Failed to navigate to API Keys page. Current URL: ' + currentUrl)
    }
    WebUI.comment('API Keys page loaded successfully')
    
    // Verify page title
    TestObject pageTitle = new TestObject('pageTitle')
    pageTitle.addProperty('xpath', ConditionType.EQUALS, 
        "//h1[contains(text(), 'API keys')]")
    WebUI.waitForElementVisible(pageTitle, 10)
    String title = WebUI.getText(pageTitle)
    WebUI.comment('Page title: ' + title)
    
    // Step 4: Verify API Keys metrics
    WebUI.comment('Step 4: Verifying API Keys metrics...')
    
    // 4.1 Verify Active keys = 1
    WebUI.comment('--- Verifying Active keys ---')
    TestObject activeKeys = new TestObject('activeKeys')
    activeKeys.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(text(), 'Active keys')]/following-sibling::p[contains(@class, 'font-mono')]")
    WebUI.waitForElementVisible(activeKeys, 5)
    String activeKeysText = WebUI.getText(activeKeys)
    WebUI.comment('Active keys: ' + activeKeysText)
    
    if (activeKeysText.equals('1')) {
        WebUI.comment('✓ Active keys is 1 as expected (default key)')
    } else {
        WebUI.comment('✗ Active keys is ' + activeKeysText + ', expected 1')
        throw new Exception('Active keys mismatch. Actual: ' + activeKeysText + ', Expected: 1')
    }
    
    // 4.2 Verify Used across keys = $0.00
    WebUI.comment('--- Verifying Used across keys ---')
    TestObject usedAcrossKeys = new TestObject('usedAcrossKeys')
    usedAcrossKeys.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(text(), 'Used across keys')]/following-sibling::p[contains(@class, 'font-mono')]")
    WebUI.waitForElementVisible(usedAcrossKeys, 5)
    String usedText = WebUI.getText(usedAcrossKeys)
    WebUI.comment('Used across keys: ' + usedText)
    
    if (usedText.equals('$0.00')) {
        WebUI.comment('✓ Used across keys is $0.00 as expected')
    } else {
        WebUI.comment('✗ Used across keys is ' + usedText + ', expected $0.00')
        throw new Exception('Used across keys mismatch. Actual: ' + usedText + ', Expected: $0.00')
    }
    
    // 4.3 Verify Total budget = — and "No caps set"
    WebUI.comment('--- Verifying Total budget ---')
    TestObject totalBudget = new TestObject('totalBudget')
    totalBudget.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(text(), 'Total budget')]/following-sibling::p[contains(@class, 'font-mono')]")
    WebUI.waitForElementVisible(totalBudget, 5)
    String budgetText = WebUI.getText(totalBudget)
    WebUI.comment('Total budget: ' + budgetText)
    
    if (budgetText.equals('—')) {
        WebUI.comment('✓ Total budget is — as expected')
    } else {
        WebUI.comment('✗ Total budget is ' + budgetText + ', expected —')
        throw new Exception('Total budget mismatch. Actual: ' + budgetText + ', Expected: —')
    }
    
    // Verify "No caps set" message
    TestObject noCapsSet = new TestObject('noCapsSet')
    noCapsSet.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(text(), 'Total budget')]/following-sibling::p[contains(@class, 'text-muted-foreground') and contains(text(), 'No caps set')]")
    WebUI.waitForElementVisible(noCapsSet, 5)
    String noCapsText = WebUI.getText(noCapsSet)
    WebUI.comment('No caps set message: ' + noCapsText)
    
    if (noCapsText.contains('No caps set')) {
        WebUI.comment('✓ No caps set message displayed')
    } else {
        WebUI.comment('✗ No caps set message not found')
        throw new Exception('No caps set message not found')
    }
    
    // 4.4 Verify Expiring this week = 0
    WebUI.comment('--- Verifying Expiring this week ---')
    TestObject expiringThisWeek = new TestObject('expiringThisWeek')
    expiringThisWeek.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(text(), 'Expiring this week')]/following-sibling::p[contains(@class, 'font-mono')]")
    WebUI.waitForElementVisible(expiringThisWeek, 5)
    String expiringText = WebUI.getText(expiringThisWeek)
    WebUI.comment('Expiring this week: ' + expiringText)
    
    if (expiringText.equals('0')) {
        WebUI.comment('✓ Expiring this week is 0 as expected')
    } else {
        WebUI.comment('✗ Expiring this week is ' + expiringText + ', expected 0')
        throw new Exception('Expiring this week mismatch. Actual: ' + expiringText + ', Expected: 0')
    }
    
    // Step 5: Verify Generate Key button exists
    WebUI.comment('Step 5: Verifying Generate Key button...')
    TestObject generateKeyBtn = new TestObject('generateKeyBtn')
    generateKeyBtn.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(@class, 'bg-primary') and contains(., 'Generate Key')]")
    WebUI.waitForElementVisible(generateKeyBtn, 5)
    WebUI.comment('✓ Generate Key button exists')
    
    // Step 6: Verify API key table
    WebUI.comment('Step 6: Verifying API key table...')
    
    // 6.1 Verify table exists
    TestObject keyTable = new TestObject('keyTable')
    keyTable.addProperty('xpath', ConditionType.EQUALS, 
        "//table[contains(@class, 'w-full')]")
    WebUI.waitForElementVisible(keyTable, 5)
    WebUI.comment('✓ API key table exists')
    
    // 6.2 Count number of rows in table body
    WebUI.comment('--- Counting rows in table ---')
    TestObject tableRows = new TestObject('tableRows')
    tableRows.addProperty('xpath', ConditionType.EQUALS, 
        "//table[contains(@class, 'w-full')]//tbody//tr")
    
    // Get all rows in tbody
    List<TestObject> rows = WebUI.findWebElements(tableRows, 5)
    int rowCount = rows.size()
    WebUI.comment('Number of rows in table: ' + rowCount)
    
    // VERIFY: Table has exactly 1 row (only default key)
    if (rowCount == 1) {
        WebUI.comment('✓ Table has exactly 1 row (default key only)')
    } else {
        WebUI.comment('✗ Table has ' + rowCount + ' rows, expected 1')
        throw new Exception('Table row count mismatch. Actual: ' + rowCount + ', Expected: 1')
    }
    
    // 6.3 Verify default key row exists
    WebUI.comment('--- Verifying default key details ---')
    TestObject defaultKeyRow = new TestObject('defaultKeyRow')
    defaultKeyRow.addProperty('xpath', ConditionType.EQUALS, 
        "//td//span[contains(text(), '(unnamed)')]")
    WebUI.waitForElementVisible(defaultKeyRow, 5)
    String keyName = WebUI.getText(defaultKeyRow)
    WebUI.comment('Default key name: ' + keyName)
    
    if (keyName.contains('(unnamed)')) {
        WebUI.comment('✓ Default key found with name: (unnamed)')
    } else {
        WebUI.comment('✗ Default key not found or different name: ' + keyName)
        throw new Exception('Default key not found')
    }
    
    // 6.4 Verify key usage is $0.00
    TestObject keyUsage = new TestObject('keyUsage')
    keyUsage.addProperty('xpath', ConditionType.EQUALS, 
        "//td//span[contains(text(), '(unnamed)')]/ancestor::tr//span[contains(@class, 'font-mono') and contains(text(), '\$0.00')]")
    WebUI.waitForElementVisible(keyUsage, 5)
    String usageText = WebUI.getText(keyUsage)
    WebUI.comment('Key usage: ' + usageText)
    
    if (usageText.equals('$0.00')) {
        WebUI.comment('✓ Key usage is $0.00')
    } else {
        WebUI.comment('✗ Key usage is ' + usageText + ', expected $0.00')
        throw new Exception('Key usage mismatch. Actual: ' + usageText + ', Expected: $0.00')
    }
    
    // 6.5 Verify key limits (10K tok/min, 60 req/min)
    WebUI.comment('--- Verifying key limits ---')
    TestObject tokenLimit = new TestObject('tokenLimit')
    tokenLimit.addProperty('xpath', ConditionType.EQUALS, 
        "//span[contains(@class, 'font-mono') and contains(text(), '10K')]")
    WebUI.waitForElementVisible(tokenLimit, 5)
    String tokenLimitText = WebUI.getText(tokenLimit)
    WebUI.comment('Token limit: ' + tokenLimitText)
    
    TestObject reqLimit = new TestObject('reqLimit')
    reqLimit.addProperty('xpath', ConditionType.EQUALS, 
        "//span[contains(@class, 'font-mono') and contains(text(), '60')]")
    WebUI.waitForElementVisible(reqLimit, 5)
    String reqLimitText = WebUI.getText(reqLimit)
    WebUI.comment('Request limit: ' + reqLimitText)
    
    // 6.6 Verify Created date
    TestObject createdDate = new TestObject('createdDate')
    createdDate.addProperty('xpath', ConditionType.EQUALS, 
        "//td[contains(@class, 'hidden lg:table-cell') and contains(text(), 'Jul')]")
    WebUI.waitForElementVisible(createdDate, 5)
    String createdText = WebUI.getText(createdDate)
    WebUI.comment('Created date: ' + createdText)
    
    // 6.7 Verify Expires = never
    TestObject expiresNever = new TestObject('expiresNever')
    expiresNever.addProperty('xpath', ConditionType.EQUALS, 
        "//td[contains(@class, 'hidden lg:table-cell')]//span[contains(@class, 'italic') and contains(text(), 'never')]")
    WebUI.waitForElementVisible(expiresNever, 5)
    String expiresText = WebUI.getText(expiresNever)
    WebUI.comment('Expires: ' + expiresText)
    
    if (expiresText.equals('never')) {
        WebUI.comment('✓ Expires is never (no expiration)')
    } else {
        WebUI.comment('✗ Expires is ' + expiresText + ', expected never')
        throw new Exception('Expires mismatch. Actual: ' + expiresText + ', Expected: never')
    }
    
    // 6.8 Verify Revoke button exists
    WebUI.comment('--- Verifying Revoke button ---')
    TestObject revokeBtn = new TestObject('revokeBtn')
    revokeBtn.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='Revoke key']")
    WebUI.waitForElementVisible(revokeBtn, 5)
    WebUI.comment('✓ Revoke key button exists')
    
    // Step 7: Verify there is NO additional row (double check)
    WebUI.comment('Step 7: Double check no additional rows...')
    // Verify only 1 row by checking there are no other rows
    TestObject additionalRows = new TestObject('additionalRows')
    additionalRows.addProperty('xpath', ConditionType.EQUALS, 
        "//table[contains(@class, 'w-full')]//tbody//tr[position()>1]")
    
    List<TestObject> additionalRowsList = WebUI.findWebElements(additionalRows, 3)
    int additionalRowCount = additionalRowsList.size()
    WebUI.comment('Additional rows beyond first: ' + additionalRowCount)
    
    if (additionalRowCount == 0) {
        WebUI.comment('✓ No additional rows found - only default key exists')
    } else {
        WebUI.comment('✗ Found ' + additionalRowCount + ' additional rows')
        throw new Exception('Additional rows found. Expected: 0, Actual: ' + additionalRowCount)
    }
    
    WebUI.takeScreenshot('TC04_Console_Keys_Success.png')
    WebUI.comment('=== TC04 PASSED - All API Keys data verified successfully ===')
    WebUI.comment('=== Summary of verification: ===')
    WebUI.comment('  ✓ Active keys: 1')
    WebUI.comment('  ✓ Used across keys: $0.00')
    WebUI.comment('  ✓ Total budget: —')
    WebUI.comment('  ✓ No caps set message displayed')
    WebUI.comment('  ✓ Expiring this week: 0')
    WebUI.comment('  ✓ Generate Key button exists')
    WebUI.comment('  ✓ Table has exactly 1 row')
    WebUI.comment('  ✓ Default key exists: (unnamed)')
    WebUI.comment('  ✓ Key usage: $0.00')
    WebUI.comment('  ✓ Key limits: 10K tok/min, 60 req/min')
    WebUI.comment('  ✓ Created date: ' + createdText)
    WebUI.comment('  ✓ Expires: never')
    WebUI.comment('  ✓ Revoke button exists')
    WebUI.comment('  ✓ No additional rows found')

} catch (Exception e) {
    WebUI.comment('=== TC04 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC04_Console_Keys_Error.png')
    throw e
}