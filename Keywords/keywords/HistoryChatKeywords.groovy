package keywords

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import org.openqa.selenium.WebElement

/**
 * HistoryChatKeywords.groovy
 *
 * Custom Keywords for Chat History operations.
 * Reusable across all test cases that need to interact with chat history.
 */
public class HistoryChatKeywords {

    // ============================================================
    // TIMEOUT CONSTANTS
    // ============================================================
    static final int SHORT_WAIT  = 2
    static final int MEDIUM_WAIT = 4
    static final int LONG_WAIT   = 5

    // ============================================================
    // PRIVATE HELPER: ENSURE NAVBAR READY
    // ============================================================
    /**
     * Private helper to ensure navbar is open and on Chat History tab.
     */
    private void ensureNavbarReady(int timeoutSeconds = MEDIUM_WAIT) {
        // Step 1: Make sure navbar is open
        TestObject navSidebar = new TestObject('navSidebar')
        navSidebar.addProperty('xpath', ConditionType.EQUALS, "//nav")
        WebUI.waitForElementVisible(navSidebar, timeoutSeconds)
        
        String ariaHidden = WebUI.getAttribute(navSidebar, 'aria-hidden')
        WebUI.comment('Navbar aria-hidden: ' + ariaHidden)
        
        if (ariaHidden == 'true') {
            WebUI.comment('Navbar is closed, opening...')
            TestObject openButton = new TestObject('openButton')
            openButton.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
            WebUI.waitForElementClickable(openButton, SHORT_WAIT)
            WebUI.click(openButton)
        }
        
        // Step 2: Ensure Chat History tab is active
        TestObject navRegion = new TestObject('navRegion')
        navRegion.addProperty('xpath', ConditionType.EQUALS, "//nav//div[@role='region']")
        WebUI.waitForElementVisible(navRegion, timeoutSeconds)
        
        String currentAriaLabel = WebUI.getAttribute(navRegion, 'aria-label')
        WebUI.comment('Current aria-label: ' + currentAriaLabel)
        
        if (currentAriaLabel != 'Chat History') {
            WebUI.comment('Switching to Chat History tab...')
            TestObject chatHistoryButton = new TestObject('chatHistoryButton')
            chatHistoryButton.addProperty('xpath', ConditionType.EQUALS, "//button[@aria-label='Chat History']")
            WebUI.waitForElementClickable(chatHistoryButton, SHORT_WAIT)
            WebUI.click(chatHistoryButton)
        }
    }

    /**
     * Private helper to create a new chat for test.
     */
    private String createNewChatForTest() {
        String testMessage = 'Auto-created chat for test'
        WebUI.comment('No chats found, creating new conversation...')
        WebUI.comment('Sending test message: ' + testMessage)
        
        // Use ChatKeywords to send message
        String response = CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(testMessage)
        WebUI.comment('New chat created')
        return 'New conversation'
    }

    // ============================================================
    // KEYWORD: GET TOTAL CHAT COUNT
    // ============================================================
    /**
     * Gets the total number of chats in history.
     * 
     * @param timeoutSeconds - Max wait time for elements (default: 10)
     * @return int - Total number of chats
     */
    @Keyword
    int getTotalChatCount(int timeoutSeconds = MEDIUM_WAIT) {
        try {
            WebUI.comment('=== KEYWORD: getTotalChatCount ===')
            
            ensureNavbarReady(timeoutSeconds)
            
            TestObject chatItems = new TestObject('chatItems')
            chatItems.addProperty('xpath', ConditionType.EQUALS, "//div[@data-testid='convo-item']")
            
            List<WebElement> chatElements = WebUI.findWebElements(chatItems, timeoutSeconds)
            int totalChats = chatElements.size()
            WebUI.comment('Total chats found: ' + totalChats)
            
            return totalChats
            
        } catch (Exception e) {
            WebUI.comment('✗ ERROR in getTotalChatCount: ' + e.getMessage())
            throw e
        }
    }

    // ============================================================
    // KEYWORD: GET ALL CHAT LABELS
    // ============================================================
    /**
     * Gets all chat labels/titles from history.
     * 
     * @param timeoutSeconds - Max wait time for elements (default: 10)
     * @return List<String> - List of chat labels
     */
    @Keyword
    List<String> getAllChatLabels(int timeoutSeconds = MEDIUM_WAIT) {
        try {
            WebUI.comment('=== KEYWORD: getAllChatLabels ===')
            
            ensureNavbarReady(timeoutSeconds)
            
            TestObject chatItems = new TestObject('chatItems')
            chatItems.addProperty('xpath', ConditionType.EQUALS, "//div[@data-testid='convo-item']")
            
            List<WebElement> chatElements = WebUI.findWebElements(chatItems, timeoutSeconds)
            List<String> chatLabels = new ArrayList<String>()
            
            for (int i = 1; i <= chatElements.size(); i++) {
                TestObject chatItem = new TestObject('chatItem_' + i)
                chatItem.addProperty('xpath', ConditionType.EQUALS, 
                    "(//div[@data-testid='convo-item'])[" + i + "]")
                String label = WebUI.getAttribute(chatItem, 'aria-label')
                chatLabels.add(label)
                WebUI.comment('Chat ' + i + ': ' + label)
            }
            
            return chatLabels
            
        } catch (Exception e) {
            WebUI.comment('✗ ERROR in getAllChatLabels: ' + e.getMessage())
            throw e
        }
    }

    // ============================================================
    // KEYWORD: OPEN CHAT BY INDEX
    // ============================================================
    /**
     * Opens a chat from Chat History by index (1-based).
     * 
     * @param chatIndex - Index of chat to open (1 = first, 2 = second, etc.)
     * @param timeoutSeconds - Max wait time for elements (default: 10)
     * @return String - The chat label/title that was opened
     */
    @Keyword
    String openChatByIndex(int chatIndex, int timeoutSeconds = MEDIUM_WAIT) {
        try {
            WebUI.comment('=== KEYWORD: openChatByIndex ===')
            WebUI.comment('Opening chat at index: ' + chatIndex)
            
            ensureNavbarReady(timeoutSeconds)
            
            // Get total chats to validate index
            int totalChats = getTotalChatCount(timeoutSeconds)
            
            if (totalChats == 0) {
                WebUI.comment('No chats found, creating new conversation...')
                return createNewChatForTest()
            }
            
            if (chatIndex > totalChats) {
                WebUI.comment('Index ' + chatIndex + ' exceeds total chats (' + totalChats + '), using last chat')
                chatIndex = totalChats
            }
            
            // Find and click chat by index
            TestObject targetChat = new TestObject('targetChat')
            targetChat.addProperty('xpath', ConditionType.EQUALS, 
                "(//div[@data-testid='convo-item'])[" + chatIndex + "]")
            
            WebUI.waitForElementVisible(targetChat, timeoutSeconds)
            String chatLabel = WebUI.getAttribute(targetChat, 'aria-label')
            WebUI.comment('Opening chat: ' + chatLabel)
            
            WebUI.waitForElementClickable(targetChat, SHORT_WAIT)
            WebUI.click(targetChat)
            WebUI.comment('Chat opened successfully')
            
            return chatLabel
            
        } catch (Exception e) {
            WebUI.comment('✗ ERROR in openChatByIndex: ' + e.getMessage())
            throw e
        }
    }

    // ============================================================
    // KEYWORD: OPEN FIRST CHAT FROM HISTORY
    // ============================================================
    /**
     * Opens the first chat from Chat History.
     * 
     * @param timeoutSeconds - Max wait time for elements (default: 10)
     * @return String - The chat label/title that was opened
     */
    @Keyword
    String openFirstChatFromHistory(int timeoutSeconds = MEDIUM_WAIT) {
        try {
            WebUI.comment('=== KEYWORD: openFirstChatFromHistory ===')
            return openChatByIndex(1, timeoutSeconds)
        } catch (Exception e) {
            WebUI.comment('✗ ERROR in openFirstChatFromHistory: ' + e.getMessage())
            throw e
        }
    }

    // ============================================================
    // KEYWORD: OPEN LAST CHAT FROM HISTORY
    // ============================================================
    /**
     * Opens the last chat from Chat History.
     * 
     * @param timeoutSeconds - Max wait time for elements (default: 10)
     * @return String - The chat label/title that was opened
     */
    @Keyword
    String openLastChatFromHistory(int timeoutSeconds = MEDIUM_WAIT) {
        try {
            WebUI.comment('=== KEYWORD: openLastChatFromHistory ===')
            
            int totalChats = getTotalChatCount(timeoutSeconds)
            
            if (totalChats == 0) {
                WebUI.comment('No chats found, creating new conversation...')
                return createNewChatForTest()
            }
            
            return openChatByIndex(totalChats, timeoutSeconds)
            
        } catch (Exception e) {
            WebUI.comment('✗ ERROR in openLastChatFromHistory: ' + e.getMessage())
            throw e
        }
    }

    // ============================================================
    // KEYWORD: OPEN RANDOM CHAT FROM HISTORY
    // ============================================================
    /**
     * Opens a random chat from Chat History.
     * 
     * @param timeoutSeconds - Max wait time for elements (default: 10)
     * @return String - The chat label/title that was opened
     */
    @Keyword
    String openRandomChatFromHistory(int timeoutSeconds = MEDIUM_WAIT) {
        try {
            WebUI.comment('=== KEYWORD: openRandomChatFromHistory ===')
            
            ensureNavbarReady(timeoutSeconds)
            
            int totalChats = getTotalChatCount(timeoutSeconds)
            
            if (totalChats == 0) {
                WebUI.comment('No chats found, creating new conversation...')
                return createNewChatForTest()
            }
            
            // Generate random index (1-based)
            int randomIndex = (Math.random() * totalChats).toInteger() + 1
            WebUI.comment('Random index: ' + randomIndex + ' of ' + totalChats)
            
            return openChatByIndex(randomIndex, timeoutSeconds)
            
        } catch (Exception e) {
            WebUI.comment('✗ ERROR in openRandomChatFromHistory: ' + e.getMessage())
            throw e
        }
    }

    // ============================================================
    // KEYWORD: OPEN CHAT BY LABEL
    // ============================================================
    /**
     * Opens a chat from Chat History by its label/title.
     * 
     * @param chatLabel - The label/title of the chat to open
     * @param timeoutSeconds - Max wait time for elements (default: 10)
     * @return String - The chat label/title that was opened
     */
    @Keyword
    String openChatByLabel(String chatLabel, int timeoutSeconds = MEDIUM_WAIT) {
        try {
            WebUI.comment('=== KEYWORD: openChatByLabel ===')
            WebUI.comment('Searching for chat with label: ' + chatLabel)
            
            ensureNavbarReady(timeoutSeconds)
            
            TestObject targetChat = new TestObject('targetChat')
            targetChat.addProperty('xpath', ConditionType.EQUALS, 
                "//div[@data-testid='convo-item' and @aria-label='" + chatLabel + "']")
            
            WebUI.waitForElementVisible(targetChat, timeoutSeconds)
            String foundLabel = WebUI.getAttribute(targetChat, 'aria-label')
            WebUI.comment('Found chat: ' + foundLabel)
            
            WebUI.waitForElementClickable(targetChat, SHORT_WAIT)
            WebUI.click(targetChat)
            WebUI.comment('Chat opened successfully')
            
            return foundLabel
            
        } catch (Exception e) {
            WebUI.comment('✗ ERROR in openChatByLabel: ' + e.getMessage())
            throw e
        }
    }

    // ============================================================
    // KEYWORD: GET CHAT LABEL BY INDEX
    // ============================================================
    /**
     * Gets the chat label/title at a specific index.
     * 
     * @param chatIndex - Index of chat (1 = first, 2 = second, etc.)
     * @param timeoutSeconds - Max wait time for elements (default: 10)
     * @return String - The chat label
     */
    @Keyword
    String getChatLabelByIndex(int chatIndex, int timeoutSeconds = MEDIUM_WAIT) {
        try {
            WebUI.comment('=== KEYWORD: getChatLabelByIndex ===')
            WebUI.comment('Getting chat label at index: ' + chatIndex)
            
            ensureNavbarReady(timeoutSeconds)
            
            TestObject targetChat = new TestObject('targetChat')
            targetChat.addProperty('xpath', ConditionType.EQUALS, 
                "(//div[@data-testid='convo-item'])[" + chatIndex + "]")
            
            WebUI.waitForElementVisible(targetChat, timeoutSeconds)
            String chatLabel = WebUI.getAttribute(targetChat, 'aria-label')
            WebUI.comment('Chat label: ' + chatLabel)
            
            return chatLabel
            
        } catch (Exception e) {
            WebUI.comment('✗ ERROR in getChatLabelByIndex: ' + e.getMessage())
            throw e
        }
    }

    // ============================================================
    // KEYWORD: IS CHAT ACTIVE
    // ============================================================
    /**
     * Checks if a chat is currently active/selected.
     * 
     * @param chatIndex - Index of chat to check (1 = first, 2 = second, etc.)
     * @param timeoutSeconds - Max wait time for elements (default: 5)
     * @return boolean - True if chat is active, false otherwise
     */
    @Keyword
    boolean isChatActive(int chatIndex, int timeoutSeconds = SHORT_WAIT) {
        try {
            WebUI.comment('=== KEYWORD: isChatActive ===')
            WebUI.comment('Checking if chat ' + chatIndex + ' is active...')
            
            ensureNavbarReady(timeoutSeconds)
            
            TestObject activeChat = new TestObject('activeChat')
            activeChat.addProperty('xpath', ConditionType.EQUALS, 
                "(//div[@data-testid='convo-item' and contains(@class, 'bg-surface-active-alt')])[" + chatIndex + "]")
            
            boolean isActive = WebUI.waitForElementVisible(activeChat, timeoutSeconds, 
                com.kms.katalon.core.model.FailureHandling.OPTIONAL)
            
            WebUI.comment('Chat ' + chatIndex + ' active: ' + isActive)
            return isActive
            
        } catch (Exception e) {
            WebUI.comment('✗ ERROR in isChatActive: ' + e.getMessage())
            return false
        }
    }

    // ============================================================
    // KEYWORD: WAIT FOR CHAT HISTORY LOADED
    // ============================================================
    /**
     * Waits for chat history to be fully loaded.
     * 
     * @param timeoutSeconds - Max wait time for elements (default: 10)
     * @return boolean - True if loaded, false if timeout
     */
    @Keyword
    boolean waitForChatHistoryLoaded(int timeoutSeconds = MEDIUM_WAIT) {
        try {
            WebUI.comment('=== KEYWORD: waitForChatHistoryLoaded ===')
            
            ensureNavbarReady(timeoutSeconds)
            
            TestObject chatItems = new TestObject('chatItems')
            chatItems.addProperty('xpath', ConditionType.EQUALS, "//div[@data-testid='convo-item']")
            
            boolean loaded = WebUI.waitForElementVisible(chatItems, timeoutSeconds,
                com.kms.katalon.core.model.FailureHandling.OPTIONAL)
            
            WebUI.comment('Chat history loaded: ' + loaded)
            return loaded
            
        } catch (Exception e) {
            WebUI.comment('✗ ERROR in waitForChatHistoryLoaded: ' + e.getMessage())
            return false
        }
    }
}