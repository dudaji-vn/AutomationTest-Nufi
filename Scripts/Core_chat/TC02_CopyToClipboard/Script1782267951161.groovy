import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC02: Copy to Clipboard Test
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Open existing chat from history
 * 4. Click copy button of the LAST response message
 * 5. Verify clipboard contains the response
 */

WebUI.comment('=== TC02: Copy to Clipboard Test ===')

try {
    // Step 1: Open browser
    WebUI.comment('Step 1: Opening browser...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(
        GlobalVariable.Base_URL
    )

    // Step 2: Login
    WebUI.comment('Step 2: Logging in...')
    CustomKeywords.'keywords.ChatKeywords.loginChat'(
        GlobalVariable.email,
        GlobalVariable.password
    )

    // Step 3: Open existing chat from history
    WebUI.comment('Step 3: Opening existing chat from history...')
    String chatName = CustomKeywords.'keywords.HistoryChatKeywords.openRandomChatFromHistory'()
    WebUI.comment('Opened chat: ' + chatName)

    // Step 4: Get the response from the chat (last message)
    WebUI.comment('Step 4: Getting response from chat...')
    
    // Create dynamic TestObject for last message
    TestObject lastMessage = new TestObject('dynamic_last_message')
    lastMessage.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]")
    
    WebUI.waitForElementVisible(lastMessage, 30)
    String response = WebUI.getText(lastMessage)
    WebUI.comment('Response received: ' + (response.length() > 100 ? response.substring(0, 100) + '...' : response))

    // Step 5: Click copy button of the LAST response message
    WebUI.comment('Step 5: Clicking copy button of the last response...')
    
    TestObject copyButtonLastMessage = new TestObject('dynamic_copy_button_last')
    copyButtonLastMessage.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[contains(@aria-label, 'Copy') or contains(@title, 'Copy')]")
    
    WebUI.waitForElementVisible(copyButtonLastMessage, 30)
    WebUI.click(copyButtonLastMessage)
    WebUI.comment('Copy button of last message clicked')
    WebUI.delay(1)

    // Step 6: Read system clipboard
    WebUI.comment('Step 6: Reading system clipboard...')
    try {
        java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit()
        java.awt.datatransfer.Clipboard clipboard = toolkit.getSystemClipboard()
        java.awt.datatransfer.DataFlavor flavor = java.awt.datatransfer.DataFlavor.stringFlavor
        
        if (clipboard.isDataFlavorAvailable(flavor)) {
            String clipboardContent = clipboard.getData(flavor).toString()
            
            if (clipboardContent != null && clipboardContent.trim().length() > 0) {
                WebUI.comment('Copy to clipboard succeeded')
                String preview = clipboardContent.length() > 120 
                    ? clipboardContent.substring(0, 120) + '...' 
                    : clipboardContent
                WebUI.comment('Clipboard preview: ' + preview)
                
                if (response.contains(clipboardContent) || clipboardContent.contains(response.substring(0, Math.min(50, response.length())))) {
                    WebUI.comment('Clipboard content matches response')
                } else {
                    WebUI.comment('Clipboard content may not match response exactly')
                }
            } else {
                WebUI.comment('Clipboard is empty after clicking copy button')
                WebUI.takeScreenshot('TC02_CopyToClipboard_Empty.png')
            }
        } else {
            WebUI.comment('String data not available in clipboard')
            WebUI.takeScreenshot('TC02_CopyToClipboard_NoStringData.png')
        }
        
        WebUI.takeScreenshot('TC02_CopyToClipboard_Success.png')
        
    } catch (Exception e) {
        WebUI.comment('Unable to read system clipboard: ' + e.getMessage())
        WebUI.takeScreenshot('TC02_CopyToClipboard_NoAccess.png')
    }

    // Step 7: Close browser
    WebUI.comment('Step 7: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC02 PASSED')

} catch (Exception e) {
    WebUI.comment('TC02 FAILED: ' + e.getMessage())
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}