import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable

// 1. Setup - Login
WebUI.callTestCase(findTestCase('Test Cases/Setup/Login'), [:])
WebUI.delay(2)

// 2. Navigate to Base URL - system will auto redirect to /c/new if token exists
WebUI.navigateToUrl(GlobalVariable.Base_URL)
WebUI.delay(3)

// 3. Check current agent in span_Agents
WebUI.waitForElementPresent(
    findTestObject('Core Chat/span_Agents'), 
    10, FailureHandling.STOP_ON_FAILURE)

String currentAgent = WebUI.getText(findTestObject('Core Chat/span_Agents')).toLowerCase()
WebUI.comment('Current Agent: ' + currentAgent)

// 4. If not Gemini, select Gemini
if (!currentAgent.contains('gemini')) {
    WebUI.comment('Current agent is not Gemini, selecting Gemini...')
    
    // Click on "My Agents" button
    WebUI.click(findTestObject('Core Chat/button_My Agents'))
    WebUI.delay(2)
    
    // Select Gemini option
    WebUI.click(findTestObject('Core Chat/Sellect_Gemini'))
    WebUI.delay(2)
    
    // Select Gemini (1)
    WebUI.click(findTestObject('Core Chat/sellect_gemini (1)'))
    WebUI.delay(2)
    
    WebUI.comment('Gemini endpoint selected')
} else {
    WebUI.comment('Gemini endpoint is already selected')
}

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
