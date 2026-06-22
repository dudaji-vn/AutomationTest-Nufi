import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import keywords.ChatKeywords

// Core Chat Setup - Login + Navigate + Select Gemini
ChatKeywords.coreChateSetup()
WebUI.delay(2)

// Send a prompt to get a response
String testMessage = 'Test message for copy to clipboard'
WebUI.click(findTestObject('Core Chat/chat_input'))
WebUI.delay(1)
WebUI.clearText(findTestObject('Core Chat/chat_input'))
WebUI.setText(findTestObject('Core Chat/chat_input'), testMessage)
WebUI.delay(1)
WebUI.click(findTestObject('Core Chat/button__send-button'))
WebUI.delay(3)

// Wait for copy button to appear and click it
WebUI.waitForElementPresent(findTestObject('Core Chat/Page_AIs Nature And Offer/button_Copy-to-clipboard'), 30, FailureHandling.STOP_ON_FAILURE)
WebUI.click(findTestObject('Core Chat/Page_AIs Nature And Offer/button_Copy-to-clipboard'))
WebUI.delay(1)

// Read system clipboard (may require non-headless environment)
try {

    String clipboard = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor)
    WebUI.comment('Clipboard contents length: ' + (clipboard == null ? 0 : clipboard.length()))
    if (clipboard == null || clipboard.trim().length() == 0) {
        WebUI.comment('Copy to clipboard may have failed - clipboard empty')
        WebUI.takeScreenshot('CopyToClipboard_Empty.png')
        WebUI.markFailed('Clipboard empty after clicking copy')
    } else {
        WebUI.comment('Copy to clipboard succeeded - snippet: ' + clipboard.substring(0, Math.min(120, clipboard.length())))
        WebUI.takeScreenshot('CopyToClipboard_Success.png')
    }
} catch (Exception e) {
    WebUI.comment('Unable to read system clipboard: ' + e.getMessage())
    WebUI.takeScreenshot('CopyToClipboard_Exception.png')
}

WebUI.comment('Copy-to-clipboard test completed')
