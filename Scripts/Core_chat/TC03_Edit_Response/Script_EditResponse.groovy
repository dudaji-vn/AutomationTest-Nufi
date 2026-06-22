import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable
import keywords.ChatKeywords

// Core Chat Setup - Login + Navigate + Select Gemini
ChatKeywords.coreChateSetup()
WebUI.delay(2)

// Send a prompt to generate a response to edit
String testMessage = 'Test message for edit response'
WebUI.click(findTestObject('Core Chat/chat_input'))
WebUI.delay(1)
WebUI.clearText(findTestObject('Core Chat/chat_input'))
WebUI.setText(findTestObject('Core Chat/chat_input'), testMessage)
WebUI.delay(1)
WebUI.click(findTestObject('Core Chat/button__send-button'))
WebUI.delay(3)

// Wait for edit button and open editor
WebUI.waitForElementPresent(findTestObject('Core Chat/Page_AIs Nature And Offer/button_Edit'), 30, FailureHandling.STOP_ON_FAILURE)
WebUI.click(findTestObject('Core Chat/Page_AIs Nature And Offer/button_Edit'))
WebUI.delay(1)

// Wait for textarea in edit modal
WebUI.waitForElementPresent(findTestObject('Core Chat/Page_T C Gio/textarea_Response_text'), 10, FailureHandling.STOP_ON_FAILURE)
String original = WebUI.getAttribute(findTestObject('Core Chat/Page_T C Gio/textarea_Response_text'), 'value')
WebUI.comment('Original response length: ' + (original == null ? 0 : original.length()))

String edited = ''
if (original != null) {
    edited = original + ' [edited by automated test]'
} else {
    edited = ' [edited by automated test]'
}
WebUI.clearText(findTestObject('Core Chat/Page_T C Gio/textarea_Response_text'))
WebUI.setText(findTestObject('Core Chat/Page_T C Gio/textarea_Response_text'), edited)
WebUI.delay(1)

// Click Save & Submit
WebUI.click(findTestObject('Core Chat/Page_T C Gio/button_Save  Submit'))
WebUI.delay(3)

// Verify edited text appears in page (basic presence check)
boolean found = WebUI.verifyTextPresent('edited by automated test', false, FailureHandling.OPTIONAL)
if (found) {
    WebUI.comment('Edit saved and visible in the chat')
    WebUI.takeScreenshot('EditResponse_Success.png')
} else {
    WebUI.comment('Edited text not found after save')
    WebUI.takeScreenshot('EditResponse_Failure.png')
    WebUI.markFailed('Edited text not visible')
}

WebUI.comment('Edit-response test completed')
