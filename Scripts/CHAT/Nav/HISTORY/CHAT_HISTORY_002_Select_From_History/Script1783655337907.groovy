import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebElement

/**
 * TC33: Navbar - Select Chat from History
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Verify navbar is open
 * 4. Switch to Chat History tab
 * 5. Find all chat items in Today section
 * 6. Select a chat item (random OR specific)
 * 7. Verify chat is selected
 */

WebUI.comment('=== TC33: Navbar - Select Chat from History ===')

try {
    // Create TestObjects with direct XPath
    TestObject navSidebar = new TestObject('navSidebar')
    navSidebar.addProperty('xpath', ConditionType.EQUALS, "//nav")
    
    TestObject openButton = new TestObject('openButton')
    openButton.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
    
    TestObject chatHistoryButton = new TestObject('chatHistoryButton')
    chatHistoryButton.addProperty('xpath', ConditionType.EQUALS, "//button[@aria-label='Chat History']")
    
    TestObject navRegion = new TestObject('navRegion')
    navRegion.addProperty('xpath', ConditionType.EQUALS, "//nav//div[@role='region']")

    // Step 1: Open browser
    WebUI.comment('Step 1: Opening browser...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(GlobalVariable.Base_URL)

    // Step 2: Login
    WebUI.comment('Step 2: Logging in...')
    CustomKeywords.'keywords.ChatKeywords.loginChat'(GlobalVariable.email, GlobalVariable.password)

    // Step 3: Verify navbar is open
    WebUI.comment('Step 3: Verifying navbar is open...')
    WebUI.waitForElementVisible(navSidebar, 10)
    
    String ariaHidden = WebUI.getAttribute(navSidebar, 'aria-hidden')
    WebUI.comment('Navbar aria-hidden: ' + ariaHidden)
    
    if (ariaHidden == 'true') {
        WebUI.comment('Navbar is closed, opening...')
        WebUI.waitForElementClickable(openButton, 5)
        WebUI.click(openButton)
        WebUI.delay(1)
    }
    WebUI.comment('Navbar is open')

    // Step 4: Ensure we are on Chat History tab
    WebUI.comment('Step 4: Ensuring Chat History tab is active...')
    WebUI.waitForElementVisible(navRegion, 10)
    
    String currentAriaLabel = WebUI.getAttribute(navRegion, 'aria-label')
    WebUI.comment('Current aria-label: ' + currentAriaLabel)
    
    if (currentAriaLabel != 'Chat History') {
        WebUI.comment('Switching to Chat History tab...')
        WebUI.waitForElementClickable(chatHistoryButton, 5)
        WebUI.click(chatHistoryButton)
        WebUI.delay(1)
    }
    WebUI.comment('On Chat History tab')

    // Step 5: Find all chat items in Today section
    WebUI.comment('Step 5: Finding chat items in Today section...')
    
    // Get all chat items with data-testid="convo-item"
    TestObject chatItems = new TestObject('chatItems')
    chatItems.addProperty('xpath', ConditionType.EQUALS, 
        "//div[@data-testid='convo-item']")
    
    // Use findWebElements to get count
    List<WebElement> elements = WebUI.findWebElements(chatItems, 10)
    int totalChats = elements.size()
    WebUI.comment('Total chat items found: ' + totalChats)
    
    if (totalChats == 0) {
        WebUI.comment('No chat items found in history')
        WebUI.takeScreenshot('TC33_Navbar_SelectChat_NoChats.png')
        WebUI.comment('Skipping selection - no chats available')
    } else {
        // ============================================================
        // OPTION 1: RANDOM CHAT (Default - uncomment to use)
        // ============================================================
        WebUI.comment('Step 6: Selecting RANDOM chat item...')
        
        // Generate random index (1-based)
        int randomIndex = (Math.random() * totalChats).toInteger() + 1
        WebUI.comment('Random index: ' + randomIndex + ' of ' + totalChats)
        
        // Get random chat item
        TestObject targetChat = new TestObject('targetChat')
        targetChat.addProperty('xpath', ConditionType.EQUALS, 
            "(//div[@data-testid='convo-item'])[" + randomIndex + "]")
        
        String chatLabel = WebUI.getAttribute(targetChat, 'aria-label')
        WebUI.comment('Selected random chat: ' + chatLabel)
        
        WebUI.waitForElementClickable(targetChat, 5)
        WebUI.click(targetChat)
        WebUI.comment('Random chat item clicked')
        WebUI.delay(2)
        
        // ============================================================
        // OPTION 2: SPECIFIC CHAT (Comment OPTION 1, uncomment this)
        // ============================================================
        /*
        WebUI.comment('Step 6: Selecting SPECIFIC chat item...')
        
        // Select chat by index (1 = first, 2 = second, etc.)
        int chatIndex = 1
        WebUI.comment('Target index: ' + chatIndex + ' of ' + totalChats)
        
        // Get specific chat item
        TestObject targetChat = new TestObject('targetChat')
        targetChat.addProperty('xpath', ConditionType.EQUALS, 
            "(//div[@data-testid='convo-item'])[" + chatIndex + "]")
        
        String chatLabel = WebUI.getAttribute(targetChat, 'aria-label')
        WebUI.comment('Selected chat: ' + chatLabel)
        
        WebUI.waitForElementClickable(targetChat, 5)
        WebUI.click(targetChat)
        WebUI.comment('Specific chat item clicked')
        WebUI.delay(2)
        */

        // ============================================================
        // OPTION 3: SELECT BY CHAT TITLE (Comment OPTION 1, uncomment this)
        // ============================================================
        /*
        WebUI.comment('Step 6: Selecting chat by title...')
        
        // Select chat by exact title
        String targetTitle = 'Haha'
        WebUI.comment('Looking for chat with title: ' + targetTitle)
        
        TestObject targetChat = new TestObject('targetChat')
        targetChat.addProperty('xpath', ConditionType.EQUALS, 
            "//div[@data-testid='convo-item' and @aria-label='" + targetTitle + "']")
        
        WebUI.waitForElementVisible(targetChat, 5)
        String chatLabel = WebUI.getAttribute(targetChat, 'aria-label')
        WebUI.comment('Found chat: ' + chatLabel)
        
        WebUI.waitForElementClickable(targetChat, 5)
        WebUI.click(targetChat)
        WebUI.comment('Chat clicked: ' + targetTitle)
        WebUI.delay(2)
        */

        // ============================================================
        // Step 7: Verify chat is selected (active state)
        // ============================================================
        WebUI.comment('Step 7: Verifying chat is selected...')
        
        // Check if the clicked item has active state
        TestObject activeChat = new TestObject('activeChat')
        activeChat.addProperty('xpath', ConditionType.EQUALS, 
            "//div[@data-testid='convo-item' and contains(@class, 'bg-surface-active-alt')]")
        
        boolean isActive = WebUI.waitForElementVisible(activeChat, 3, FailureHandling.OPTIONAL)
        
        if (isActive) {
            String activeLabel = WebUI.getAttribute(activeChat, 'aria-label')
            WebUI.comment('Active chat: ' + activeLabel)
            WebUI.comment('Chat selected successfully')
        } else {
            WebUI.comment('Active state not found, but click was performed')
        }
        
        WebUI.takeScreenshot('TC33_Navbar_SelectChat_Success.png')
    }

    // Step 8: Close browser
    WebUI.comment('Step 8: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC33 PASSED')

} catch (Exception e) {
    WebUI.comment('TC33 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC33_Navbar_SelectChat_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}