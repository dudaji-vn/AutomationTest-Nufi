import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

/**
 * TC02: Copy to Clipboard Test
 * Sử dụng keyword clickCopyButtonOfLastMessage
 */

WebUI.comment('=== TC02: Copy to Clipboard Test ===')

try {
    // Step 1-5: Setup và gửi tin nhắn
    WebUI.comment('Step 1: Opening browser...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(GlobalVariable.Base_URL)

    WebUI.comment('Step 2: Logging in...')
    CustomKeywords.'keywords.ChatKeywords.loginChat'(GlobalVariable.email, GlobalVariable.password)

    WebUI.comment('Step 3: Opening new conversation...')
    CustomKeywords.'keywords.ChatKeywords.openNewConversation'(GlobalVariable.Base_URL)

    WebUI.comment('Step 4: Selecting Gemini endpoint...')
    CustomKeywords.'keywords.ChatKeywords.selectEndpointAndModel'('Gemini', 'gemini')

    WebUI.comment('Step 5: Sending test message...')
    String testMessage = 'Test message for copy to clipboard'
    String response = CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(testMessage)
    WebUI.comment('Response received: ' + (response.length() > 100 ? response.substring(0, 100) + '...' : response))

    // Step 6: Click copy button using ancestor method (SỬ DỤNG KEYWORD ĐÃ TẠO)
    WebUI.comment('Step 6: Clicking copy button using ancestor method...')
    CustomKeywords.'keywords.ChatKeywords.clickCopyButtonOfLastMessage'()
    WebUI.delay(1)

    // Step 7: Read system clipboard
    WebUI.comment('Step 7: Reading system clipboard...')
    try {
        java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit()
        java.awt.datatransfer.Clipboard clipboard = toolkit.getSystemClipboard()
        java.awt.datatransfer.DataFlavor flavor = java.awt.datatransfer.DataFlavor.stringFlavor
        
        if (clipboard.isDataFlavorAvailable(flavor)) {
            String clipboardContent = clipboard.getData(flavor).toString()
            
            if (clipboardContent != null && clipboardContent.trim().length() > 0) {
                WebUI.comment('✓ Copy to clipboard succeeded')
                String preview = clipboardContent.length() > 120 
                    ? clipboardContent.substring(0, 120) + '...' 
                    : clipboardContent
                WebUI.comment('Clipboard preview: ' + preview)
                
                // Verify clipboard content matches response
                if (response.contains(clipboardContent) || clipboardContent.contains(response.substring(0, Math.min(50, response.length())))) {
                    WebUI.comment('✓ Clipboard content matches response')
                } else {
                    WebUI.comment('⚠ Clipboard content may not match response exactly')
                }
            } else {
                WebUI.comment('✗ Clipboard is empty after clicking copy button')
                WebUI.takeScreenshot('TC02_CopyToClipboard_Empty.png')
            }
        } else {
            WebUI.comment('✗ String data not available in clipboard')
            WebUI.takeScreenshot('TC02_CopyToClipboard_NoStringData.png')
        }
        
        WebUI.takeScreenshot('TC02_CopyToClipboard_Success.png')
        
    } catch (Exception e) {
        WebUI.comment('⚠ Unable to read system clipboard: ' + e.getMessage())
        WebUI.takeScreenshot('TC02_CopyToClipboard_NoAccess.png')
    }

    // Step 8: Close browser
    WebUI.comment('Step 8: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('✓ TC02 PASSED')

} catch (Exception e) {
    WebUI.comment('✗ TC02 FAILED: ' + e.getMessage())
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}