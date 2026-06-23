# NuFi Chat Keywords - Complete Reference Guide

## Overview

All NuFi Chat automation keywords are centralized in `Keywords/keywords/ChatKeywords.groovy`. This document provides complete reference for all available functions.

---

## Core Chat Setup (Composite)

### coreChateSetup()
Performs complete setup for core chat testing in one call.

**What it does:**
1. Logs in with GlobalVariable.email and GlobalVariable.password
2. Opens a new conversation
3. Selects Gemini endpoint
4. Waits for page to load

**Usage:**
```groovy
import keywords.ChatKeywords
ChatKeywords.coreChateSetup()
WebUI.delay(2)
// Now ready to send messages
```

**Used by:**
- TC01_Chat_Gemini_Normal
- TC02_CopyToClipboard
- TC03_Edit_Response
- TC04_Fork_VisibleMessagesOnly

---

## Authentication

### loginChat(String email, String password)
Logs into NuFi Chat with provided credentials.

**Parameters:**
- `email` - Email address for login
- `password` - Password for account

**Verification:**
- Waits for page load after clicking Continue
- Verifies URL contains '/c/' to confirm login success
- Throws exception if login fails

**Usage:**
```groovy
ChatKeywords.loginChat('user@example.com', 'password123')
```

**Timeout:** 5-90 seconds
**Logs:** Step-by-step progress with ✓/✗ indicators

---

## Navigation

### openNewConversation()
Opens a new conversation by navigating to `/c/new`.

**What it does:**
1. Constructs URL: GlobalVariable.Base_URL + '/c/new'
2. Navigates to URL
3. Waits for page load
4. Verifies chat input is visible

**Usage:**
```groovy
ChatKeywords.openNewConversation()
```

**Timeout:** 15 seconds
**Verification:** Chat input field must be visible

---

## Endpoint & Model Selection

### selectEndpoint(String endpointName)
Selects an endpoint from the dropdown menu.

**Parameters:**
- `endpointName` - Name of endpoint (e.g., "Gemini", "Nufi", "Claude", "Qwen2.5-0.5B")

**What it does:**
1. Clicks endpoint selector button
2. Waits for dropdown to appear
3. Clicks the specified endpoint
4. Returns after 1 second delay for submenu loading

**Usage:**
```groovy
ChatKeywords.selectEndpoint('Gemini')
```

**Timeout:** 15 seconds

---

### selectModel(String modelName)
Selects a model from the model submenu (appears after endpoint selection).

**Parameters:**
- `modelName` - Name of model (e.g., "gemini", "gpt-4o", "qwen2.5-0.5b")

**What it does:**
1. Waits for model submenu to appear
2. Finds model by text matching
3. Clicks the model option
4. Verifies dropdown closes

**Usage:**
```groovy
ChatKeywords.selectModel('gemini')
```

**Timeout:** 15 seconds

---

### selectEndpointAndModel(String endpointName, String modelName)
Convenience wrapper that selects both endpoint and model in sequence.

**Parameters:**
- `endpointName` - Endpoint name
- `modelName` - Model name

**Usage:**
```groovy
ChatKeywords.selectEndpointAndModel('Gemini', 'gemini')
```

**Equivalent to:**
```groovy
ChatKeywords.selectEndpoint('Gemini')
WebUI.delay(2)
ChatKeywords.selectModel('gemini')
```

---

## Endpoint Convenience Functions

### selectGeminiEndpoint()
Select Gemini endpoint in one call.
```groovy
ChatKeywords.selectGeminiEndpoint()
// Equivalent to: selectEndpointAndModel('Gemini', 'gemini')
```

### selectNufiEndpoint()
Select Nufi endpoint in one call.
```groovy
ChatKeywords.selectNufiEndpoint()
// Equivalent to: selectEndpointAndModel('Nufi', 'nufi')
```

### selectClaudeEndpoint()
Select Claude endpoint in one call.
```groovy
ChatKeywords.selectClaudeEndpoint()
// Equivalent to: selectEndpointAndModel('Claude', 'claude')
```

### selectQwenEndpoint()
Select Qwen2.5-0.5B endpoint in one call.
```groovy
ChatKeywords.selectQwenEndpoint()
// Equivalent to: selectEndpointAndModel('Qwen2.5-0.5B', 'qwen2.5-0.5b')
```

---

## Messaging

### sendMessage(String message)
Sends a message in the chat.

**Parameters:**
- `message` - Message text to send

**What it does:**
1. Waits for text input to be visible
2. Clears any existing text
3. Types the new message
4. Clicks send button
5. Verifies send button disappears (generation started)

**Usage:**
```groovy
ChatKeywords.sendMessage('Hello, how are you?')
```

**Timeout:** 5-15 seconds
**Logs:** Message text and confirmation

---

### waitForResponse(int timeoutSeconds)
Waits for AI model to complete response generation.

**Parameters:**
- `timeoutSeconds` - Maximum seconds to wait (default: 90)

**What it does:**
1. Waits for thinking indicator to disappear
2. Waits for send button to reappear
3. Logs elapsed time (seconds and milliseconds)

**Usage:**
```groovy
ChatKeywords.waitForResponse(90)
// Or use default:
ChatKeywords.waitForResponse()
```

**Timeout:** 90 seconds (configurable)
**Logs:** Response time metrics

---

### verifyResponseSuccess()
Verifies AI response was successful and retrieves response text.

**Returns:** String - The AI response content

**What it does:**
1. Checks for error banners (throws exception if found)
2. Finds last message bubble
3. Extracts text content
4. Verifies response is not empty
5. Returns response text

**Usage:**
```groovy
String response = ChatKeywords.verifyResponseSuccess()
WebUI.comment("Response: " + response)
```

**Timeout:** 15 seconds
**Returns:** Response text (up to full length)

---

## Composite Functions

### sendMessageAndVerifyResponse(String message)
Complete flow: Send message → Wait for response → Verify success.

**Parameters:**
- `message` - Message to send

**Returns:** String - The AI response text

**What it does:**
1. Calls sendMessage(message)
2. Calls waitForResponse()
3. Calls verifyResponseSuccess()
4. Returns response text

**Usage:**
```groovy
ChatKeywords.coreChateSetup()
String response = ChatKeywords.sendMessageAndVerifyResponse('Hello, Gemini!')
// Response now contains the AI's answer
```

**This function replaces:**
```groovy
ChatKeywords.sendMessage('Hello')
ChatKeywords.waitForResponse()
String response = ChatKeywords.verifyResponseSuccess()
```

---

## Utility Functions

### waitForMessageVisible(int timeoutSeconds)
Utility to wait for any message to appear.

**Parameters:**
- `timeoutSeconds` - Maximum seconds to wait (default: 15)

**Usage:**
```groovy
ChatKeywords.waitForMessageVisible(30)
```

---

### verifyNoError()
Quick check that no error banner is visible on page.

**What it does:**
1. Looks for alert/error elements
2. Throws exception if error found
3. Logs success message

**Usage:**
```groovy
ChatKeywords.verifyNoError()
```

**Throws:** Exception if error banner detected

---

## Timeout Constants

All timeouts are defined as constants:
- `SHORT_WAIT = 5` seconds
- `MEDIUM_WAIT = 15` seconds
- `LONG_WAIT = 90` seconds

These are used internally by functions for consistency.

---

## Complete Test Example

### Simple Chat Flow
```groovy
import keywords.ChatKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

// Setup
ChatKeywords.coreChateSetup()
WebUI.delay(2)

// Send message and get response
String response = ChatKeywords.sendMessageAndVerifyResponse('What is 2+2?')

// Verify and use response
if (response.contains('4')) {
    WebUI.comment('✓ Correct answer received')
} else {
    WebUI.comment('✗ Unexpected response')
}

// Take screenshot
WebUI.takeScreenshot('test_result.png')
```

### Advanced Flow with Error Handling
```groovy
import keywords.ChatKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling

try {
    // Login and setup
    ChatKeywords.coreChateSetup()
    
    // Send first message
    String response1 = ChatKeywords.sendMessageAndVerifyResponse('Hello')
    WebUI.comment("Response 1: ${response1.take(100)}")
    
    // Switch endpoint
    ChatKeywords.selectGeminiEndpoint()
    
    // Send second message
    String response2 = ChatKeywords.sendMessageAndVerifyResponse('Hello again')
    WebUI.comment("Response 2: ${response2.take(100)}")
    
    // Verify no errors
    ChatKeywords.verifyNoError()
    
    WebUI.comment('✓ All tests passed')
} catch (Exception e) {
    WebUI.comment("✗ Test failed: ${e.getMessage()}")
    WebUI.takeScreenshot('error_screenshot.png')
    throw e
}
```

---

## Global Variables Used

All functions depend on these global variables:
- `GlobalVariable.Base_URL` - Base URL (default: 'https://chat.nufi.me')
- `GlobalVariable.email` - Test email (required by coreChateSetup)
- `GlobalVariable.password` - Test password (required by coreChateSetup)

**How to update:**
1. Edit `Profiles/default.glbl`
2. Change the `<initValue>` for each variable
3. Rebuild project

**Example:**
```xml
<GlobalVariableEntity>
  <name>email</name>
  <initValue>'your.email@example.com'</initValue>
</GlobalVariableEntity>

<GlobalVariableEntity>
  <name>password</name>
  <initValue>'YourPassword123!'</initValue>
</GlobalVariableEntity>

<GlobalVariableEntity>
  <name>Base_URL</name>
  <initValue>'https://chat.nufi.me'</initValue>
</GlobalVariableEntity>
```

---

## Error Handling

All functions include try-catch blocks with detailed error logging:
- Functions log every step with ✓ (success) or ✗ (error) indicator
- Exceptions are caught and logged before re-throwing
- All errors include descriptive messages for debugging

**Example error message:**
```
✗ ERROR in loginChat: Login verification failed - URL does not contain '/c/' : https://example.com/error
```

---

## Performance Notes

- Typical chat response time: 5-30 seconds
- Timeout for responses: 90 seconds (configurable)
- Page loads: 15 seconds max
- UI interactions: 5 seconds max

**Tips:**
- Add `WebUI.delay(1-2)` between operations for reliability
- Use `OPTIONAL` FailureHandling for non-critical waits
- Check logs for actual response times and adjust timeouts if needed

---

## File Locations

- **Keywords:** `Keywords/keywords/ChatKeywords.groovy`
- **Global Variables:** `Profiles/default.glbl` (user-editable)
- **Global Variable Class:** `Libs/internal/GlobalVariable.groovy`
- **Test Scripts:** `Scripts/Core_chat/TC0X_*/Script*.groovy`

---

## Imports Required

Every test script needs:
```groovy
import keywords.ChatKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
```

---

## Support & Debugging

**If a function fails:**
1. Check the logs for ✗ ERROR messages
2. Look at the screenshot taken (if any)
3. Verify test objects exist in Object Repository
4. Ensure GlobalVariable values are correct
5. Check that xpaths match the actual DOM structure

**Common issues:**
- Element not found → Check xpath in ChatKeywords.groovy
- Timeout → Increase timeoutSeconds parameter
- Login failed → Verify email/password in GlobalVariable
- URL not matching → Check GlobalVariable.Base_URL

---

**Last Updated:** 2026-06-23  
**Version:** 1.0  
**Status:** Production Ready
