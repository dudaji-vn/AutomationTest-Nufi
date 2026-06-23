import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable

WebUI.comment('=== TC01: Chat Gemini Normal Flow ===')

// 1. Open browser
CustomKeywords.'keywords.ChatKeywords.openBrowser'(
    GlobalVariable.Base_URL
)

// 2. Login
CustomKeywords.'keywords.ChatKeywords.loginChat'(
    GlobalVariable.email,
    GlobalVariable.password
)

// 3. Open new conversation
CustomKeywords.'keywords.ChatKeywords.openNewConversation'(
    GlobalVariable.Base_URL
)

// 4. Select endpoint + model
CustomKeywords.'keywords.ChatKeywords.selectEndpointAndModel'(
    'Gemini',
    'gemini'
)

// 5. Send message
String testMessage = 'Hello Gemini, how are you?'

String response =
CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(
    testMessage
)

// 6. Verify no error
//CustomKeywords.'keywords.ChatKeywords.verifyNoError'()

// 7. Screenshot
WebUI.takeScreenshot(
    'TC01_Chat_Gemini_Success.png'
)

// 8. Close browser
CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

WebUI.comment(
    '✓ TC01 PASSED'
)