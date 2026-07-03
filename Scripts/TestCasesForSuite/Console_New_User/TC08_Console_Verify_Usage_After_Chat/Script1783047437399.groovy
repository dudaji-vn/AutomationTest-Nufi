import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC08: Nufi Console - Usage Tab (Existing User with Data)
 * 
 * Test Flow:
 * 1. Ensure on Console tab
 * 2. Click Usage tab
 * 3. Verify Usage page loads
 * 4. Verify metrics have data:
 *    - Total cost > $0.00
 *    - Requests > 0
 *    - Models used > 0
 *    - Primary hardware has data (not "—")
 * 5. Verify Last 7 days has data (requests > 0 AND cost > $0.00)
 * 6. Verify By model section has data (not empty message)
 * 7. Verify By hardware section has data (not empty)
 * 8. Verify Recent requests table has at least 1 row
 */

WebUI.comment('=== TC08: Nufi Console - Usage Tab (Existing User) ===')

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
    
    // Step 2: Click Usage tab
    WebUI.comment('Step 2: Clicking Usage tab...')
    TestObject usageTab = new TestObject('usageTab')
    usageTab.addProperty('xpath', ConditionType.EQUALS, 
        "//a[@href='/usage' and contains(text(), 'Usage')]")
    WebUI.waitForElementClickable(usageTab, 10)
    WebUI.click(usageTab)
    WebUI.delay(2)
    WebUI.comment('Usage tab clicked')
    
    // Step 3: Verify Usage page loads
    WebUI.comment('Step 3: Verifying Usage page loads...')
    String currentUrl = driver.getCurrentUrl()
    WebUI.comment('Current URL: ' + currentUrl)
    
    if (!currentUrl.contains('/usage')) {
        throw new Exception('Failed to navigate to Usage page. Current URL: ' + currentUrl)
    }
    WebUI.comment('✓ Usage page loaded successfully')
    
    // Verify page title
    TestObject pageTitle = new TestObject('pageTitle')
    pageTitle.addProperty('xpath', ConditionType.EQUALS, 
        "//h1[contains(text(), 'Usage')]")
    WebUI.waitForElementVisible(pageTitle, 10)
    String title = WebUI.getText(pageTitle)
    WebUI.comment('Page title: ' + title)
    
    // Step 4: Verify metrics have data
    WebUI.comment('Step 4: Verifying usage metrics...')
    
    // 4.1 Verify Total cost > $0.00
    WebUI.comment('--- Verifying Total cost ---')
    TestObject totalCost = new TestObject('totalCost')
    totalCost.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(text(), 'Total cost')]/following-sibling::p[contains(@class, 'font-mono')]")
    WebUI.waitForElementVisible(totalCost, 5)
    String costText = WebUI.getText(totalCost)
    WebUI.comment('Total cost: ' + costText)
    
    if (costText == null || costText.trim().isEmpty()) {
        throw new Exception('Total cost not found')
    }
    
    String costNum = costText.replace('$', '').replace(',', '').trim()
    double costValue = Double.parseDouble(costNum)
    WebUI.comment('Parsed cost: ' + costValue)
    
    if (costValue <= 0.0) {
        throw new Exception('Total cost is $0.00 - expected > $0.00 for existing user. Actual: ' + costText)
    }
    WebUI.comment('✓ Total cost > $0.00: ' + costText)
    
    // 4.2 Verify Requests > 0
    WebUI.comment('--- Verifying Requests ---')
    TestObject requestsCount = new TestObject('requestsCount')
    requestsCount.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(text(), 'Requests')]/following-sibling::p[contains(@class, 'font-mono')]")
    WebUI.waitForElementVisible(requestsCount, 5)
    String requestsText = WebUI.getText(requestsCount)
    WebUI.comment('Requests: ' + requestsText)
    
    if (requestsText == null || requestsText.trim().isEmpty()) {
        throw new Exception('Requests count not found')
    }
    
    int requestsValue = Integer.parseInt(requestsText.trim())
    WebUI.comment('Parsed requests: ' + requestsValue)
    
    if (requestsValue <= 0) {
        throw new Exception('Requests is 0 - expected > 0 for existing user. Actual: ' + requestsText)
    }
    WebUI.comment('✓ Requests > 0: ' + requestsValue)
    
    // 4.3 Verify Models used > 0
    WebUI.comment('--- Verifying Models used ---')
    TestObject modelsUsed = new TestObject('modelsUsed')
    modelsUsed.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(text(), 'Models used')]/following-sibling::p[contains(@class, 'font-mono')]")
    WebUI.waitForElementVisible(modelsUsed, 5)
    String modelsText = WebUI.getText(modelsUsed)
    WebUI.comment('Models used: ' + modelsText)
    
    if (modelsText == null || modelsText.trim().isEmpty()) {
        throw new Exception('Models used not found')
    }
    
    int modelsValue = Integer.parseInt(modelsText.trim())
    WebUI.comment('Parsed models: ' + modelsValue)
    
    if (modelsValue <= 0) {
        throw new Exception('Models used is 0 - expected > 0 for existing user. Actual: ' + modelsText)
    }
    WebUI.comment('✓ Models used > 0: ' + modelsValue)
    
    // 4.4 Verify Primary hardware has data (not "—")
    WebUI.comment('--- Verifying Primary hardware ---')
    TestObject primaryHardware = new TestObject('primaryHardware')
    primaryHardware.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(text(), 'Primary hardware')]/following-sibling::p[contains(@class, 'font-mono')]//span[contains(@class, 'font-mono')]")
    WebUI.waitForElementVisible(primaryHardware, 5)
    String hardwareText = WebUI.getText(primaryHardware)
    WebUI.comment('Primary hardware: ' + hardwareText)
    
    if (hardwareText == null || hardwareText.trim().isEmpty() || hardwareText.equals('—')) {
        throw new Exception('Primary hardware has no data - expected hardware name. Actual: ' + hardwareText)
    }
    WebUI.comment('✓ Primary hardware has data: ' + hardwareText)
    
    // Check hardware badge
    TestObject hardwareBadge = new TestObject('hardwareBadge')
    hardwareBadge.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(text(), 'Primary hardware')]/following-sibling::div//span[contains(@data-slot, 'badge')]")
    boolean hasBadge = WebUI.waitForElementVisible(hardwareBadge, 3, FailureHandling.OPTIONAL)
    if (hasBadge) {
        String badgeText = WebUI.getText(hardwareBadge)
        WebUI.comment('Hardware badge: ' + badgeText)
        WebUI.comment('✓ Hardware badge found')
    } else {
        WebUI.comment('⚠ WARNING: Hardware badge not found')
    }
    
    // Step 5: Verify Last 7 days has data
    WebUI.comment('Step 5: Verifying Last 7 days has data...')
    
    // Get Last 7 days summary text
    TestObject last7DaysSummary = new TestObject('last7DaysSummary')
    last7DaysSummary.addProperty('xpath', ConditionType.EQUALS, 
        "//h3[contains(text(), 'Last 7 days')]/following-sibling::span[contains(@class, 'text-xs')]")
    WebUI.waitForElementVisible(last7DaysSummary, 5)
    String summaryText = WebUI.getText(last7DaysSummary)
    WebUI.comment('Last 7 days summary: ' + summaryText)
    
    if (summaryText == null || summaryText.trim().isEmpty()) {
        throw new Exception('Last 7 days summary not found')
    }
    
    // Extract requests count
    boolean hasRequests = false
    int last7Requests = 0
    double last7Cost = 0.0
    
    String[] parts = summaryText.split('·')
    if (parts.length >= 1) {
        String requestsPart = parts[0].trim()
        String requestsNum = requestsPart.replaceAll('[^0-9]', '').trim()
        if (!requestsNum.isEmpty()) {
            last7Requests = Integer.parseInt(requestsNum)
            WebUI.comment('Last 7 days requests: ' + last7Requests)
            if (last7Requests > 0) {
                hasRequests = true
            }
        }
    }
    
    if (parts.length >= 2) {
        String costPart = parts[1].trim()
        String costNumLast7 = costPart.replace('$', '').replace(',', '').trim()
        if (!costNumLast7.isEmpty()) {
            last7Cost = Double.parseDouble(costNumLast7)
            WebUI.comment('Last 7 days cost: ' + last7Cost)
        }
    }
    
    if (!hasRequests) {
        throw new Exception('Last 7 days has 0 requests - expected > 0. Summary: ' + summaryText)
    }
    WebUI.comment('✓ Last 7 days has requests: ' + last7Requests)
    
    if (last7Cost <= 0.0) {
        throw new Exception('Last 7 days cost is $0.00 - expected > $0.00. Summary: ' + summaryText)
    }
    WebUI.comment('✓ Last 7 days has cost: $' + last7Cost)
    
    // Step 6: Verify By model section has data
    WebUI.comment('Step 6: Verifying By model section...')
    
    // Check By model header exists
    TestObject byModelHeader = new TestObject('byModelHeader')
    byModelHeader.addProperty('xpath', ConditionType.EQUALS, 
        "//h3[contains(text(), 'By model')]")
    WebUI.waitForElementVisible(byModelHeader, 5)
    WebUI.comment('✓ By model section found')
    
    // CHECK: Must NOT show "No usage in this period."
    TestObject noModelUsage = new TestObject('noModelUsage')
    noModelUsage.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(text(), 'No usage in this period.')]")
    boolean isEmptyModel = WebUI.waitForElementVisible(noModelUsage, 2, FailureHandling.OPTIONAL)
    
    if (isEmptyModel) {
        throw new Exception('By model shows "No usage in this period." - expected data for existing user')
    }
    WebUI.comment('✓ By model does NOT show empty message')
    
    // CHECK: Must have at least 1 model with data
    TestObject modelItems = new TestObject('modelItems')
    modelItems.addProperty('xpath', ConditionType.EQUALS, 
        "//h3[contains(text(), 'By model')]/parent::div//ul//li")
    List<TestObject> modelList = WebUI.findWebElements(modelItems, 5)
    int modelCount = modelList.size()
    WebUI.comment('Number of models: ' + modelCount)
    
    if (modelCount <= 0) {
        throw new Exception('No models found in By model section - expected at least 1')
    }
    WebUI.comment('✓ By model has ' + modelCount + ' model(s)')
    
    // Verify first model has name
    TestObject firstModelName = new TestObject('firstModelName')
    firstModelName.addProperty('xpath', ConditionType.EQUALS, 
        "(//h3[contains(text(), 'By model')]/parent::div//ul//li//span[contains(@class, 'font-mono')])[1]")
    String firstModelNameText = WebUI.getText(firstModelName)
    WebUI.comment('First model: ' + firstModelNameText)
    
    if (firstModelNameText == null || firstModelNameText.trim().isEmpty()) {
        throw new Exception('First model name not found')
    }
    
    // Check progress bar exists for model
    TestObject modelProgressBar = new TestObject('modelProgressBar')
    modelProgressBar.addProperty('xpath', ConditionType.EQUALS, 
        "//h3[contains(text(), 'By model')]/parent::div//ul//li[1]//div[contains(@class, 'rounded-full')]")
    boolean hasProgress = WebUI.waitForElementVisible(modelProgressBar, 3, FailureHandling.OPTIONAL)
    if (hasProgress) {
        WebUI.comment('✓ Model progress bar found')
    }
    
    // Step 7: Verify By hardware section has data
    WebUI.comment('Step 7: Verifying By hardware section...')
    
    // Check By hardware header exists
    TestObject byHardwareHeader = new TestObject('byHardwareHeader')
    byHardwareHeader.addProperty('xpath', ConditionType.EQUALS, 
        "//h3[contains(text(), 'By hardware')]")
    WebUI.waitForElementVisible(byHardwareHeader, 5)
    WebUI.comment('✓ By hardware section found')
    
    // CHECK: Must have at least 1 hardware with data
    TestObject hardwareItems = new TestObject('hardwareItems')
    hardwareItems.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'bg-card') and .//h3[contains(text(), 'By hardware')]]")
    List<TestObject> hardwareList = WebUI.findWebElements(hardwareItems, 5)
    int hardwareCount = hardwareList.size()
    WebUI.comment('Number of hardware items: ' + hardwareCount)
    
    if (hardwareCount <= 0) {
        throw new Exception('No hardware found in By hardware section - expected at least 1')
    }
    WebUI.comment('✓ By hardware has ' + hardwareCount + ' hardware item(s)')
    
    // Verify first hardware has name
    TestObject firstHardwareName = new TestObject('firstHardwareName')
    firstHardwareName.addProperty('xpath', ConditionType.EQUALS, 
        "(//h3[contains(text(), 'By hardware')]/parent::div//ul//li//span[contains(@class, 'font-mono')])[1]")
    String firstHardwareNameText = WebUI.getText(firstHardwareName)
    WebUI.comment('First hardware: ' + firstHardwareNameText)
    
    if (firstHardwareNameText == null || firstHardwareNameText.trim().isEmpty()) {
        throw new Exception('First hardware name not found')
    }
    
    // Check hardware badge if applicable
    TestObject hardwareItemBadge = new TestObject('hardwareItemBadge')
    hardwareItemBadge.addProperty('xpath', ConditionType.EQUALS, 
        "(//h3[contains(text(), 'By hardware')]/parent::div//ul//li[1]//span[contains(@data-slot, 'badge')])[1]")
    boolean hasItemBadge = WebUI.waitForElementVisible(hardwareItemBadge, 2, FailureHandling.OPTIONAL)
    if (hasItemBadge) {
        String itemBadgeText = WebUI.getText(hardwareItemBadge)
        WebUI.comment('Hardware badge: ' + itemBadgeText)
    }
    
    // Check hardware progress bar
    TestObject hardwareProgressBar = new TestObject('hardwareProgressBar')
    hardwareProgressBar.addProperty('xpath', ConditionType.EQUALS, 
        "//h3[contains(text(), 'By hardware')]/parent::div//ul//li[1]//div[contains(@class, 'rounded-full')]")
    boolean hasHardwareProgress = WebUI.waitForElementVisible(hardwareProgressBar, 3, FailureHandling.OPTIONAL)
    if (hasHardwareProgress) {
        WebUI.comment('✓ Hardware progress bar found')
    }
    
    // Step 8: Verify Recent requests table has at least 1 row
    WebUI.comment('Step 8: Verifying Recent requests table...')
    
    // Check Recent requests header
    TestObject recentRequestsHeader = new TestObject('recentRequestsHeader')
    recentRequestsHeader.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'bg-card') and .//h3[text()='Recent requests']]//table")
    WebUI.waitForElementVisible(recentRequestsHeader, 5)
    WebUI.comment('✓ Recent requests section found')
    
    // Check table exists
    TestObject recentTable = new TestObject('recentTable')
    recentTable.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'bg-card') and .//h3[text()='Recent requests']]//table")
    WebUI.waitForElementVisible(recentTable, 5)
    WebUI.comment('✓ Recent requests table found')
    
    // Count rows in table
    TestObject tableRows = new TestObject('tableRows')
    tableRows.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'bg-card') and .//h3[text()='Recent requests']]//table//tbody//tr")
    List<TestObject> rows = WebUI.findWebElements(tableRows, 5)
    int rowCount = rows.size()
    WebUI.comment('Number of rows in Recent requests table: ' + rowCount)
    
    if (rowCount <= 0) {
        throw new Exception('Recent requests table has 0 rows - expected at least 1 for existing user')
    }
    WebUI.comment('✓ Recent requests table has ' + rowCount + ' row(s)')
    
    // Verify first row has data (When, Model, Source, Cost)
    TestObject firstRowWhen = new TestObject('firstRowWhen')
    firstRowWhen.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class, 'bg-card') and .//h3[text()='Recent requests']]//table//tbody//tr//td[1])[1]")
    String whenText = WebUI.getText(firstRowWhen)
    WebUI.comment('First request - When: ' + whenText)
    
    TestObject firstRowModel = new TestObject('firstRowModel')
    firstRowModel.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class, 'bg-card') and .//h3[text()='Recent requests']]//table//tbody//tr//td[2])[1]")
    String modelText = WebUI.getText(firstRowModel)
    WebUI.comment('First request - Model: ' + modelText)
    
    TestObject firstRowSource = new TestObject('firstRowSource')
    firstRowSource.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class, 'bg-card') and .//h3[text()='Recent requests']]//table//tbody//tr//td[3]//span[contains(@data-slot, 'badge')])[1]")
    String sourceText = WebUI.getText(firstRowSource)
    WebUI.comment('First request - Source: ' + sourceText)
    
    TestObject firstRowCost = new TestObject('firstRowCost')
    firstRowCost.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class, 'bg-card') and .//h3[text()='Recent requests']]//table//tbody//tr//td[4])[1]")
    String costRowText = WebUI.getText(firstRowCost)
    WebUI.comment('First request - Cost: ' + costRowText)
    
    WebUI.comment('✓ Recent requests table has data')
    
    WebUI.takeScreenshot('TC08_Console_Usage_ExistingUser_Success.png')
    WebUI.comment('=== TC08 PASSED - Usage tab verified for existing user ===')
    WebUI.comment('=== Summary: ===')
    WebUI.comment('  ✓ Total cost: ' + costText + ' (> $0.00)')
    WebUI.comment('  ✓ Requests: ' + requestsValue + ' (> 0)')
    WebUI.comment('  ✓ Models used: ' + modelsValue + ' (> 0)')
    WebUI.comment('  ✓ Primary hardware: ' + hardwareText + ' (has data)')
    WebUI.comment('  ✓ Last 7 days: ' + last7Requests + ' requests, $' + last7Cost + ' (> 0)')
    WebUI.comment('  ✓ By model: ' + modelCount + ' model(s) with data')
    WebUI.comment('  ✓ By hardware: ' + hardwareCount + ' hardware item(s) with data')
    WebUI.comment('  ✓ Recent requests: ' + rowCount + ' row(s)')

} catch (Exception e) {
    WebUI.comment('=== TC08 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC08_Console_Usage_ExistingUser_Error.png')
    throw e
}