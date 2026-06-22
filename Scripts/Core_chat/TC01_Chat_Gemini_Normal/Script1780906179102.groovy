import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable
import keywords.ChatKeywords

// Core Chat Setup - Login + Navigate + Select Gemini
ChatKeywords.coreChateSetup()
WebUI.delay(2)

// 5. Start normal chat - prepare test message
String testMessage = 'Hello Gemini, how are you?'
WebUI.comment('Sending message: ' + testMessage)

// 6. Click on chat input field
WebUI.click(findTestObject('Core Chat/chat_input'))
WebUI.delay(1)

// 7. Clear any existing text and type new message
WebUI.clearText(findTestObject('Core Chat/chat_input'))
WebUI.setText(findTestObject('Core Chat/chat_input'), testMessage)
WebUI.delay(1)

// 8. Send message by clicking send button
WebUI.click(findTestObject('Core Chat/button__send-button'))
WebUI.delay(2)

WebUI.comment('Message sent successfully')
WebUI.takeScreenshot('Chat_Gemini_Message_Sent.png')

// 9. Check for thinking indicator
WebUI.comment('Waiting for thinking indicator...')
WebUI.waitForElementPresent(
    findTestObject('Core Chat/thinking'), 
    15, FailureHandling.OPTIONAL)

if (WebUI.verifyElementVisible(findTestObject('Core Chat/thinking'), FailureHandling.OPTIONAL)) {
    WebUI.comment('Thinking indicator appeared - Gemini is processing the message')
    WebUI.takeScreenshot('Chat_Gemini_Thinking.png')
    WebUI.delay(3)
} else {
    WebUI.comment('No thinking indicator found - Gemini may have already processed')
}

// 10. Wait for response (wait for thinking to complete or response to appear)
WebUI.delay(3)

WebUI.comment('Chat test completed')
