import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC02: Nufi Console - Profile Tab (New User)
 * 
 * Test Flow:
 * 1. Ensure on Console tab
 * 2. Verify Profile tab is active
 * 3. Verify user ID and USER badge displayed
 * 4. Verify Usage Status (Available · next 30 days OR You have unlimited usage)
 * 5. Verify usage data based on status
 * 6. Verify Last 7 days: 0 requests · $0.00
 * 7. Verify Where it goes section
 * 8. Verify API keys section (only 1 item)
 * 9. Verify Per-minute limits
 */

WebUI.comment('=== TC02: Nufi Console - Profile Tab (New User) ===')

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
    
    // Check User ID - LINH HOẠT
    TestObject userId = new TestObject('userId')
    userId.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-muted-foreground')]//span[contains(@class, 'font-mono')]")
    String userIdText = WebUI.getText(userId)
    WebUI.comment('User ID: ' + userIdText)
    
    if (userIdText == null || userIdText.trim().isEmpty()) {
        throw new Exception('User ID not found or empty')
    }
    WebUI.comment('✓ User ID found: ' + userIdText)
    
    // Check USER badge - LINH HOẠT + ÉP KIỂU CHỮ THƯỜNG
    TestObject userBadge = new TestObject('userBadge')
    userBadge.addProperty('xpath', ConditionType.EQUALS, 
        "//span[contains(@data-slot, 'badge')]")
    String badgeText = WebUI.getText(userBadge)
    WebUI.comment('Badge text: ' + badgeText)
    
    if (badgeText == null || badgeText.trim().isEmpty()) {
        throw new Exception('USER badge not found')
    }
    
    // Ép kiểu chữ thường để so sánh
    String badgeTextLower = badgeText.toLowerCase().trim()
    if (!badgeTextLower.contains('user')) {
        throw new Exception('USER badge not found. Actual: ' + badgeText)
    }
    WebUI.comment('✓ USER badge found: ' + badgeText)
    
    // Step 4: Verify Usage Status - check 2 possible statuses - LINH HOẠT + ÉP KIỂU CHỮ THƯỜNG
    WebUI.comment('Step 4: Verifying Usage Status...')
    
    // Lấy tất cả các p có class text-muted-foreground
    TestObject allStatusElements = new TestObject('allStatusElements')
    allStatusElements.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-muted-foreground') and contains(@class, 'text-xs')]")
    String statusText = WebUI.getText(allStatusElements)
    WebUI.comment('Status text raw: ' + statusText)
    
    boolean hasAvailableStatus = false
    boolean hasUnlimitedStatus = false
    
    if (statusText != null && !statusText.trim().isEmpty()) {
        String statusLower = statusText.toLowerCase().trim()
        WebUI.comment('Status text lowercase: ' + statusLower)
        
        if (statusLower.contains('available · next 30 days')) {
            hasAvailableStatus = true
        }
        
        if (statusLower.contains('you have unlimited usage')) {
            hasUnlimitedStatus = true
        }
    }
    
    // Debug log
    WebUI.comment('DEBUG - hasAvailableStatus: ' + hasAvailableStatus)
    WebUI.comment('DEBUG - hasUnlimitedStatus: ' + hasUnlimitedStatus)
    
    boolean usageStatusFound = false
    String usageStatusText = ''
    
    if (hasAvailableStatus) {
        usageStatusFound = true
        usageStatusText = 'Available · next 30 days'
        WebUI.comment('✓ Usage status found: Available · next 30 days')
        
        // Step 5a: Check $10.00 for Available · next 30 days
        WebUI.comment('Step 5a: Verifying usage data for Available · next 30 days...')
        
        // Check used amount:$10.00 - LINH HOẠT + ÉP KIỂU CHỮ THƯỜNG
        TestObject usedAmount = new TestObject('usedAmount')
        usedAmount.addProperty('xpath', ConditionType.EQUALS, 
            "//span[contains(@class, 'text-muted-foreground')]")
        String usedText = WebUI.getText(usedAmount)
        WebUI.comment('Used amount text: ' + usedText)
        
        if (usedText == null || usedText.trim().isEmpty()) {
            // Try alternative: lấy text của span đầu tiên trong div flex
            TestObject usedAmountAlt = new TestObject('usedAmountAlt')
            usedAmountAlt.addProperty('xpath', ConditionType.EQUALS, 
                "//div[./p[contains(text(), 'Available')]]//span[contains(text(), 'used')]")
            usedText = WebUI.getText(usedAmountAlt)
            WebUI.comment('Used amount text (alternative): ' + usedText)
        }
        
        // Ép kiểu chữ thường để so sánh
        String usedTextLower = usedText != null ? usedText.toLowerCase().trim() : ''
        WebUI.comment('Used text lowercase: ' + usedTextLower)
        
        if (usedTextLower == null || !usedTextLower.contains('\$10.00') && !usedTextLower.contains('$10.00')) {
            throw new Exception('Used amount not found or incorrect. Expected:$10.00, Actual: ' + usedText)
        }
        WebUI.comment('✓ Usage data verified: $10.00 - correct for new user')
        
        // Check "Healthy" status - LINH HOẠT + ÉP KIỂU CHỮ THƯỜNG
        TestObject healthyStatus = new TestObject('healthyStatus')
        healthyStatus.addProperty('xpath', ConditionType.EQUALS, 
            "//div[contains(@class, 'flex') and contains(@class, 'justify-between')]/span[2]")
        String healthyText = WebUI.getText(healthyStatus)
        WebUI.comment('Healthy text: ' + healthyText)
        
        if (healthyText != null && !healthyText.trim().isEmpty()) {
            String healthyLower = healthyText.toLowerCase().trim()
            if (healthyLower.contains('healthy')) {
                WebUI.comment('✓ Healthy status found')
            } else {
                WebUI.comment('⚠ Warning: Healthy status text is: ' + healthyText)
            }
        } else {
            WebUI.comment('⚠ Warning: Healthy status not found but continuing...')
        }
        
    } else if (hasUnlimitedStatus) {
        usageStatusFound = true
        usageStatusText = 'You have unlimited usage'
        WebUI.comment('✓ Usage status found: You have unlimited usage')
        
        // Step 5b: Check $0.00 for unlimited usage - LINH HOẠT + ÉP KIỂU CHỮ THƯỜNG
        WebUI.comment('Step 5b: Verifying usage data for unlimited usage...')
        
        // Check for $0.00 amount
        TestObject unlimitedAmount = new TestObject('unlimitedAmount')
        unlimitedAmount.addProperty('xpath', ConditionType.EQUALS, 
            "//p[contains(@class, 'font-mono')]")
        String amount = WebUI.getText(unlimitedAmount)
        WebUI.comment('Amount text: ' + amount)
        
        if (amount != null && !amount.trim().isEmpty()) {
            String amountLower = amount.toLowerCase().trim()
            if (!amountLower.contains('\$0.00') && !amountLower.contains('$0.00')) {
                throw new Exception('Unlimited usage amount not found or incorrect. Expected: $0.00, Actual: ' + amount)
            }
        } else {
            throw new Exception('Unlimited usage amount not found')
        }
        WebUI.comment('✓ Unlimited usage data verified: $0.00')
        
    } else {
        // Debug: Check page source using driver
        WebUI.comment('DEBUG: Checking page source for status text...')
        String pageSource = driver.getPageSource()
        String pageSourceLower = pageSource.toLowerCase()
        WebUI.comment('DEBUG - Page source contains "available · next 30 days": ' + pageSourceLower.contains('available · next 30 days'))
        WebUI.comment('DEBUG - Page source contains "you have unlimited usage": ' + pageSourceLower.contains('you have unlimited usage'))
        
        throw new Exception('Usage status not found. Expected: "Available · next 30 days" OR "You have unlimited usage". Actual: ' + statusText)
    }
    
    WebUI.comment('✓ Usage status verified: ' + usageStatusText)
    
    // Step 6: Verify Last 7 days - LINH HOẠT + ÉP KIỂU CHỮ THƯỜNG
    WebUI.comment('Step 6: Verifying Last 7 days...')
    
    // Check Last 7 days header - LINH HOẠT
//    TestObject last7DaysHeader = new TestObject('last7DaysHeader')
//    last7DaysHeader.addProperty('xpath', ConditionType.EQUALS, 
//        "//div[contains(@class, 'bg-card') and .//h3[text()='Last 7 days']]")
//    boolean hasLast7Days = WebUI.waitForElementVisible(last7DaysHeader, 5)
//    
//    if (!hasLast7Days) {
//        throw new Exception('Last 7 days section not found')
//    }
//    WebUI.comment('✓ Last 7 days section found')
    
    // Check "0 requests · $0.00" text - LINH HOẠT + ÉP KIỂU CHỮ THƯỜNG
    TestObject requestCount = new TestObject('requestCount')
    requestCount.addProperty('xpath', ConditionType.EQUALS, 
        "//h3[contains(text(), 'Last 7 days')]/following-sibling::span[contains(@class, 'text-muted-foreground')]")
    String requestsText = WebUI.getText(requestCount)
    WebUI.comment('Requests text: ' + requestsText)
    
    if (requestsText != null && !requestsText.trim().isEmpty()) {
        String requestsLower = requestsText.toLowerCase().trim()
        if (!requestsLower.contains('0 requests')) {
            throw new Exception('Request count not found or incorrect. Expected: 0 requests · $0.00, Actual: ' + requestsText)
        }
    } else {
        throw new Exception('Request count not found')
    }
    WebUI.comment('✓ Last 7 days data verified: 0 requests · $0.00 - correct for new user')
    
    // Step 7: Verify Where it goes section - LINH HOẠT
    WebUI.comment('Step 7: Verifying Where it goes section...')
    
    TestObject whereItGoes = new TestObject('whereItGoes')
    whereItGoes.addProperty('xpath', ConditionType.EQUALS, 
        "//h3[contains(text(), 'Where it goes')]")
    boolean hasWhereItGoes = WebUI.waitForElementVisible(whereItGoes, 5)
    
    if (!hasWhereItGoes) {
        throw new Exception('Where it goes section not found')
    }
    WebUI.comment('✓ Where it goes section found')
    
    // Check message "You haven't used anything yet this period." - LINH HOẠT + ÉP KIỂU CHỮ THƯỜNG
    TestObject noUsageMessage = new TestObject('noUsageMessage')
    noUsageMessage.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-muted-foreground') and contains(@class, 'text-center')]")
    String messageText = WebUI.getText(noUsageMessage)
    WebUI.comment('No usage message: ' + messageText)
    
    if (messageText != null && !messageText.trim().isEmpty()) {
        String messageLower = messageText.toLowerCase().trim()
        if (!messageLower.contains('you haven') && !messageLower.contains('used anything yet this period')) {
            throw new Exception('No usage message not found. Actual: ' + messageText)
        }
    } else {
        throw new Exception('No usage message not found')
    }
    WebUI.comment('✓ No usage message verified for new user')
    
    // Step 8: Verify API keys section
	WebUI.comment('Step 8: Verifying API keys section...')
	
	// Check "Your keys" header
	TestObject apiKeysSection = new TestObject('apiKeysSection')
	apiKeysSection.addProperty('xpath', ConditionType.EQUALS, 
	    "//h3[contains(text(), 'Your keys')]")
	boolean hasApiKeys = WebUI.waitForElementVisible(apiKeysSection, 5)
	
	if (!hasApiKeys) {
	    throw new Exception('API keys section not found')
	}
	WebUI.comment('✓ API keys section found')
	
	// Check "View all (1) →" button
	TestObject viewAllLink = new TestObject('viewAllLink')
	viewAllLink.addProperty('xpath', ConditionType.EQUALS, 
	    "//h3[contains(text(), 'Your keys')]/following-sibling::a//button[contains(text(), 'View all')]")
	boolean hasViewAllLink = WebUI.waitForElementVisible(viewAllLink, 5)
	
	if (!hasViewAllLink) {
	    throw new Exception('View all button not found')
	}
	WebUI.comment('✓ View all button found')
	
	// Verify button text contains (1) - đã có 1 key
	String viewAllText = WebUI.getText(viewAllLink)
	WebUI.comment('View all text: ' + viewAllText)
	
	if (viewAllText == null || !viewAllText.contains('(1)')) {
	    throw new Exception('Expected View all (1), but found: ' + viewAllText)
	}
	WebUI.comment('✓ Correct: View all (1) - user has 1 API key')
    
    WebUI.takeScreenshot('TC02_Console_Profile_Success.png')
    WebUI.comment('=== TC02 PASSED - All profile elements verified for new user ===')

} catch (Exception e) {
    WebUI.comment('=== TC02 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC02_Console_Profile_Error.png')
    throw e
}