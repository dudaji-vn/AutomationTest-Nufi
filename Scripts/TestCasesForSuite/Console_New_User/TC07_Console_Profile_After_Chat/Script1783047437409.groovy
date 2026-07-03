import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC07: Nufi Console - Profile Tab (Existing User with Usage Data)
 * 
 * Test Flow:
 * 1. Ensure on Console tab
 * 2. Verify Profile tab is active
 * 3. Verify user ID and USER badge displayed
 * 4. Verify "You have unlimited usage" status (case-insensitive)
 * 5. Verify usage amount > $0.00 (has data)
 * 6. Verify Last 7 days has data (requests > 0 AND cost > $0.00)
 * 7. Verify Where it goes section has data (has Chat conversations OR Direct API calls)
 * 8. Verify NOT showing empty message "You haven't used anything yet this period."
 */

WebUI.comment('=== TC07: Nufi Console - Profile Tab (Existing User) ===')

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
    
    // Step 2: Verify Profile tab is active
    WebUI.comment('Step 2: Verifying Profile tab is active...')
    TestObject profileTab = new TestObject('profileTab')
    profileTab.addProperty('xpath', ConditionType.EQUALS, 
        "//a[contains(@class, 'text-foreground') and contains(text(), 'Profile')]")
    boolean isProfileActive = WebUI.waitForElementVisible(profileTab, 5)
    WebUI.comment('Profile tab active: ' + isProfileActive)
    
    if (!isProfileActive) {
        throw new Exception('Profile tab is not active')
    }
    WebUI.comment('✓ Profile tab is active')
    
    // Step 3: Verify user ID and USER badge
    WebUI.comment('Step 3: Verifying user ID and USER badge...')
    
    // Check User ID
    TestObject userId = new TestObject('userId')
    userId.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-muted-foreground')]//span[contains(@class, 'font-mono')]")
    String userIdText = WebUI.getText(userId)
    WebUI.comment('User ID: ' + userIdText)
    
    if (userIdText == null || userIdText.trim().isEmpty()) {
        throw new Exception('User ID not found or empty')
    }
    WebUI.comment('✓ User ID found: ' + userIdText)
    
    // Check USER badge - case-insensitive
    TestObject userBadge = new TestObject('userBadge')
    userBadge.addProperty('xpath', ConditionType.EQUALS, 
        "//span[contains(@data-slot, 'badge')]")
    String badgeText = WebUI.getText(userBadge)
    WebUI.comment('Badge text: ' + badgeText)
    
    if (badgeText == null || badgeText.trim().isEmpty()) {
        throw new Exception('USER badge not found')
    }
    
    // ✅ FIX: Case-insensitive check for USER badge
    if (!badgeText.toLowerCase().trim().contains('user')) {
        throw new Exception('USER badge not found. Actual: ' + badgeText)
    }
    WebUI.comment('✓ USER badge found: ' + badgeText)
    
    // Step 4: Verify "You have unlimited usage" status - case-insensitive
    WebUI.comment('Step 4: Verifying usage status...')
    
    // Tìm element chứa text không phân biệt hoa thường
    TestObject unlimitedStatus = new TestObject('unlimitedStatus')
    unlimitedStatus.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-xs') and contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'you have unlimited usage')]")
    WebUI.waitForElementVisible(unlimitedStatus, 5)
    String statusText = WebUI.getText(unlimitedStatus)
    WebUI.comment('Status: ' + statusText)
    
    // ✅ FIX: Case-insensitive check for status
    if (!statusText.toLowerCase().trim().contains('you have unlimited usage')) {
        throw new Exception('Unlimited usage status not found. Actual: ' + statusText)
    }
    WebUI.comment('✓ Unlimited usage status found: ' + statusText)
    
    // Step 5: Verify usage amount > $0.00 (has data)
    WebUI.comment('Step 5: Verifying usage amount > $0.00...')
    TestObject usageAmount = new TestObject('usageAmount')
    usageAmount.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'font-mono') and contains(@class, 'text-3xl')]")
    WebUI.waitForElementVisible(usageAmount, 5)
    String amountText = WebUI.getText(usageAmount)
    WebUI.comment('Usage amount: ' + amountText)
    
    if (amountText == null || amountText.trim().isEmpty()) {
        throw new Exception('Usage amount not found')
    }
    
    // Extract numeric value from $0.107 format
    String numericStr = amountText.replace('$', '').replace(',', '').trim()
    double amountValue = Double.parseDouble(numericStr)
    WebUI.comment('Parsed amount: ' + amountValue)
    
    // CHECK: Usage amount MUST be > 0.00
    if (amountValue <= 0.0) {
        throw new Exception('Usage amount is $0.00 - expected > $0.00 for existing user. Actual: ' + amountText)
    }
    WebUI.comment('✓ Usage amount is > $0.00: ' + amountText)
    
    // Check "used so far" text - case-insensitive
    TestObject usedSoFar = new TestObject('usedSoFar')
    usedSoFar.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-muted-foreground') and contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'used so far')]")
    WebUI.waitForElementVisible(usedSoFar, 3)
    String usedSoFarText = WebUI.getText(usedSoFar)
    WebUI.comment('Used so far text: ' + usedSoFarText)
    
    // ✅ FIX: Case-insensitive check
    if (usedSoFarText.toLowerCase().contains('used so far')) {
        WebUI.comment('✓ "used so far" label found')
    } else {
        WebUI.comment('⚠ WARNING: "used so far" label not found exactly: ' + usedSoFarText)
    }
    
    // Step 6: Verify Last 7 days HAS DATA (requests > 0 AND cost > $0.00)
    WebUI.comment('Step 6: Verifying Last 7 days has data...')
    
    // Get Last 7 days summary text
    TestObject last7DaysSummary = new TestObject('last7DaysSummary')
    last7DaysSummary.addProperty('xpath', ConditionType.EQUALS, 
        "//h3[contains(text(), 'Last 7 days')]/following-sibling::span[contains(@class, 'text-xs')]")
    WebUI.waitForElementVisible(last7DaysSummary, 5)
    String summaryText = WebUI.getText(last7DaysSummary)
    WebUI.comment('Last 7 days summary raw: ' + summaryText)
    
    if (summaryText == null || summaryText.trim().isEmpty()) {
        throw new Exception('Last 7 days summary not found')
    }
    
    // CHECK: Extract requests count (must be > 0)
    boolean hasRequests = false
    int requestsCount = 0
    double costValue = 0.0
    
    // Extract requests from text like "143 requests · $0.107"
    String[] parts = summaryText.split('·')
    if (parts.length >= 1) {
        String requestsPart = parts[0].trim()
        WebUI.comment('Requests part: ' + requestsPart)
        
        // Extract number from "143 requests"
        String requestsNum = requestsPart.replaceAll('[^0-9]', '').trim()
        if (!requestsNum.isEmpty()) {
            requestsCount = Integer.parseInt(requestsNum)
            WebUI.comment('Extracted requests: ' + requestsCount)
            if (requestsCount > 0) {
                hasRequests = true
            }
        }
    }
    
    // Extract cost from text
    if (parts.length >= 2) {
        String costPart = parts[1].trim()
        WebUI.comment('Cost part: ' + costPart)
        
        // Extract number from "$0.107"
        String costNum = costPart.replace('$', '').replace(',', '').trim()
        if (!costNum.isEmpty()) {
            costValue = Double.parseDouble(costNum)
            WebUI.comment('Extracted cost: ' + costValue)
        }
    }
    
    // CHECK: Must have requests > 0 AND cost > 0.00
    if (!hasRequests) {
        throw new Exception('Last 7 days has 0 requests - expected > 0 for existing user. Summary: ' + summaryText)
    }
    WebUI.comment('✓ Last 7 days has requests: ' + requestsCount)
    
    if (costValue <= 0.0) {
        throw new Exception('Last 7 days cost is $0.00 - expected > $0.00 for existing user. Summary: ' + summaryText)
    }
    WebUI.comment('✓ Last 7 days has cost: $' + costValue)
    
    // Step 6.1: Check chart bars - WARNING only (not fail)
    WebUI.comment('--- Checking chart bars (WARNING only) ---')
    TestObject dataBars = new TestObject('dataBars')
    dataBars.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'w-full') and contains(@class, 'rounded-t-sm') and not(contains(@style, 'height: 4px'))]")
    List<TestObject> barsWithData = WebUI.findWebElements(dataBars, 3)
    int barCount = barsWithData.size()
    WebUI.comment('Number of bars with data (> 0): ' + barCount)
    
    if (barCount > 0) {
        WebUI.comment('✓ Chart has ' + barCount + ' bars with data')
    } else {
        WebUI.comment('⚠ WARNING: Chart shows no data bars in last 7 days (user may have used > 7 days ago)')
    }
    
    // Check Peak day and Last request info - WARNING only
    TestObject peakDay = new TestObject('peakDay')
    peakDay.addProperty('xpath', ConditionType.EQUALS, 
        "//span[contains(text(), 'Peak day:')]")
    boolean hasPeakDay = WebUI.waitForElementVisible(peakDay, 3, FailureHandling.OPTIONAL)
    
    if (hasPeakDay) {
        String peakText = WebUI.getText(peakDay)
        WebUI.comment('Peak day: ' + peakText)
        
        TestObject lastRequest = new TestObject('lastRequest')
        lastRequest.addProperty('xpath', ConditionType.EQUALS, 
            "//span[contains(text(), 'Last request:')]")
        String lastRequestText = WebUI.getText(lastRequest)
        WebUI.comment('Last request: ' + lastRequestText)
        WebUI.comment('✓ Peak day and Last request information found')
    } else {
        WebUI.comment('ℹ Peak day / Last request info not found (may be acceptable)')
    }
    
    // Step 7: Verify Where it goes section HAS DATA (not empty message)
    WebUI.comment('Step 7: Verifying Where it goes section has data...')
    
    // Check Where it goes header exists
    TestObject whereItGoes = new TestObject('whereItGoes')
    whereItGoes.addProperty('xpath', ConditionType.EQUALS, 
        "//h3[contains(text(), 'Where it goes')]")
    WebUI.waitForElementVisible(whereItGoes, 5)
    WebUI.comment('✓ Where it goes section found')
    
    // CHECK: Must NOT show empty message "You haven't used anything yet this period."
    // Sử dụng translate để case-insensitive
    TestObject emptyMessage = new TestObject('emptyMessage')
    emptyMessage.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), \"you haven't used anything yet this period.\")]")
    boolean isEmptyState = WebUI.waitForElementVisible(emptyMessage, 2, FailureHandling.OPTIONAL)
    
    if (isEmptyState) {
        throw new Exception('Where it goes shows empty message "You haven\'t used anything yet this period." - expected data for existing user')
    }
    WebUI.comment('✓ Where it goes does NOT show empty message')
    
    // CHECK: Must have Chat conversations OR Direct API calls
    // Sử dụng translate cho case-insensitive
    TestObject chatConversations = new TestObject('chatConversations')
    chatConversations.addProperty('xpath', ConditionType.EQUALS, 
        "//span[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'chat conversations')]")
    boolean hasChat = WebUI.waitForElementVisible(chatConversations, 3, FailureHandling.OPTIONAL)
    
    TestObject directApiCalls = new TestObject('directApiCalls')
    directApiCalls.addProperty('xpath', ConditionType.EQUALS, 
        "//span[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'direct api calls')]")
    boolean hasApi = WebUI.waitForElementVisible(directApiCalls, 3, FailureHandling.OPTIONAL)
    
    if (!hasChat && !hasApi) {
        throw new Exception('Where it goes has no data - expected "Chat conversations" OR "Direct API calls" for existing user')
    }
    
    if (hasChat) {
        WebUI.comment('✓ Where it goes has Chat conversations')
        
        // Get chat amount
        TestObject chatAmount = new TestObject('chatAmount')
        chatAmount.addProperty('xpath', ConditionType.EQUALS, 
            "//span[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'chat conversations')]/following-sibling::span[contains(@class, 'font-mono')]")
        String chatAmountText = WebUI.getText(chatAmount)
        WebUI.comment('Chat amount: ' + chatAmountText)
        
        if (chatAmountText != null && !chatAmountText.trim().isEmpty()) {
            String chatNum = chatAmountText.replace('$', '').replace(',', '').trim()
            double chatValue = Double.parseDouble(chatNum)
            if (chatValue > 0.0) {
                WebUI.comment('✓ Chat amount > $0.00: ' + chatAmountText)
            } else {
                WebUI.comment('⚠ WARNING: Chat amount is $0.00')
            }
        }
        
        // Get chat percentage
        TestObject chatPercentage = new TestObject('chatPercentage')
        chatPercentage.addProperty('xpath', ConditionType.EQUALS, 
            "//span[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'chat conversations')]/following-sibling::span[contains(@class, 'text-muted-foreground')]")
        String chatPercentageText = WebUI.getText(chatPercentage)
        WebUI.comment('Chat percentage: ' + chatPercentageText)
    }
    
    if (hasApi) {
        WebUI.comment('✓ Where it goes has Direct API calls')
        
        // Get API amount
        TestObject apiAmount = new TestObject('apiAmount')
        apiAmount.addProperty('xpath', ConditionType.EQUALS, 
            "//span[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'direct api calls')]/following-sibling::span[contains(@class, 'font-mono')]")
        String apiAmountText = WebUI.getText(apiAmount)
        WebUI.comment('API amount: ' + apiAmountText)
        
        // Get API percentage
        TestObject apiPercentage = new TestObject('apiPercentage')
        apiPercentage.addProperty('xpath', ConditionType.EQUALS, 
            "//span[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'direct api calls')]/following-sibling::span[contains(@class, 'text-muted-foreground')]")
        String apiPercentageText = WebUI.getText(apiPercentage)
        WebUI.comment('API percentage: ' + apiPercentageText)
    }
    
    // Check progress bar exists
    TestObject progressBar = new TestObject('progressBar')
    progressBar.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'rounded-full') and contains(@class, 'bg-muted')]")
    boolean hasProgressBar = WebUI.waitForElementVisible(progressBar, 3, FailureHandling.OPTIONAL)
    if (hasProgressBar) {
        WebUI.comment('✓ Progress bar found in Where it goes section')
    }
    
    // Step 8: Verify Per-minute limits section exists
    WebUI.comment('Step 8: Verifying Per-minute limits section exists...')
    TestObject perMinuteHeader = new TestObject('perMinuteHeader')
    perMinuteHeader.addProperty('xpath', ConditionType.EQUALS, 
        "//h3[contains(text(), 'Per-minute limits')]")
    WebUI.waitForElementVisible(perMinuteHeader, 5)
    WebUI.comment('✓ Per-minute limits section found')
    
    WebUI.takeScreenshot('TC07_Console_Profile_ExistingUser_Success.png')
    WebUI.comment('=== TC07 PASSED - All profile elements verified for existing user ===')
    WebUI.comment('=== Summary: ===')
    WebUI.comment('  ✓ Unlimited usage status found: ' + statusText)
    WebUI.comment('  ✓ Usage amount: ' + amountText + ' (> $0.00)')
    WebUI.comment('  ✓ Last 7 days: ' + requestsCount + ' requests, $' + costValue + ' (> 0)')
    WebUI.comment('  ✓ Chart bars: ' + barCount + ' bars with data (WARNING only if 0)')
    WebUI.comment('  ✓ Where it goes has data (Chat/API) - not empty message')
    WebUI.comment('  ✓ Per-minute limits section exists')

} catch (Exception e) {
    WebUI.comment('=== TC07 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC07_Console_Profile_ExistingUser_Error.png')
    throw e
}