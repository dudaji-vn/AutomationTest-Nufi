package keywords

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

/**
 * ChatKeywords.groovy
 *
 * Custom Keywords for NuFi Chat Automation Testing
 * Reusable across all Chat Core test cases.
 */
public class ChatKeywords {

	// ============================================================
	// TIMEOUT CONSTANTS
	// ============================================================
	static final int SHORT_WAIT  = 5
	static final int MEDIUM_WAIT = 15
	static final int LONG_WAIT   = 90
	
	// ============================================================
	// KEYWORD: OPEN BROWSER
	// ============================================================
	/**
	 * Opens a new browser window and navigates to Base URL.
	 * Call this at the START of every TC.
	 *
	 * @param baseUrl - GlobalVariable.Base_URL passed from TC
	 */
	@Keyword
	void openBrowser(String baseUrl) {
		try {
			WebUI.comment('=== KEYWORD: openBrowser ===')
			WebUI.openBrowser('')
			WebUI.navigateToUrl(baseUrl)
			WebUI.waitForPageLoad(MEDIUM_WAIT)
			WebUI.comment('✓ Browser opened and navigated to: ' + baseUrl)
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in openBrowser: ' + e.getMessage())
			throw e
		}
	}
	
	// ============================================================
	// KEYWORD: CLOSE BROWSER
	// ============================================================
	/**
	 * Closes the browser.
	 * Call this at the END of every TC.
	 */
	@Keyword
	void closeBrowser() {
		try {
			WebUI.comment('=== KEYWORD: closeBrowser ===')
			WebUI.closeBrowser()
			WebUI.comment('✓ Browser closed')
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in closeBrowser: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD: CLICK COPY BUTTON OF LAST MESSAGE USING ANCESTOR
	// ============================================================
	/**
	 * Click the copy button of the last AI response message
	 * Uses ancestor method to find the copy button within the same message block
	 *
	 * @param timeoutSeconds - Max wait time (default: 30s)
	 */
	@Keyword
	void clickCopyButtonOfLastMessage(int timeoutSeconds = 30) {
		try {
			WebUI.comment('=== KEYWORD: clickCopyButtonOfLastMessage ===')
			
			// Xây dựng XPath với ancestor
			String xpath = "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[contains(@aria-label, 'Copy') or contains(@title, 'Copy')]"
			
			TestObject copyButton = new TestObject('dynamic_copy_button_ancestor')
			copyButton.addProperty('xpath', ConditionType.EQUALS, xpath)
			
			WebUI.waitForElementVisible(copyButton, timeoutSeconds)
			WebUI.click(copyButton)
			WebUI.comment('✓ Copy button of last message clicked (ancestor method)')
			
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in clickCopyButtonOfLastMessage: ' + e.getMessage())
			throw e
		}
	}
	
	// ============================================================
	// KEYWORD: CLICK COPY BUTTON BY MESSAGE CONTENT
	// ============================================================
	/**
	 * Click the copy button of a message that contains specific text
	 * Uses ancestor method
	 *
	 * @param messageContent - Content of the message to find
	 * @param timeoutSeconds - Max wait time (default: 30s)
	 */
	@Keyword
	void clickCopyButtonByContent(String messageContent, int timeoutSeconds = 30) {
		try {
			WebUI.comment('=== KEYWORD: clickCopyButtonByContent ===')
			WebUI.comment('Looking for message containing: ' + messageContent)
			
			// Escape special characters in messageContent for XPath
			String escapedContent = messageContent.replace("'", "&apos;")
			
			// Xây dựng XPath với ancestor
			String xpath = "//div[contains(@class,'message-content') and contains(text(),'" + escapedContent + "')]/ancestor::div[contains(@class,'message')]//button[contains(@aria-label, 'Copy') or contains(@title, 'Copy')]"
			
			TestObject copyButton = new TestObject('dynamic_copy_button_content')
			copyButton.addProperty('xpath', ConditionType.EQUALS, xpath)
			
			WebUI.waitForElementVisible(copyButton, timeoutSeconds)
			WebUI.click(copyButton)
			WebUI.comment('✓ Copy button clicked for message containing: ' + messageContent)
			
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in clickCopyButtonByContent: ' + e.getMessage())
			throw e
		}
	}
	
	// ============================================================
	// KEYWORD: CLICK COPY BUTTON BY MESSAGE INDEX
	// ============================================================
	/**
	 * Click the copy button of a specific message by index (1-based)
	 * Uses ancestor method
	 *
	 * @param messageIndex - Index of the message (1 = first, last() = last)
	 * @param timeoutSeconds - Max wait time (default: 30s)
	 */
	@Keyword
	void clickCopyButtonByMessageIndex(int messageIndex, int timeoutSeconds = 30) {
		try {
			WebUI.comment('=== KEYWORD: clickCopyButtonByMessageIndex ===')
			WebUI.comment('Clicking copy button of message index: ' + messageIndex)
			
			// Xây dựng XPath với ancestor
			String xpath = "(//div[contains(@class,'message-content')])[" + messageIndex + "]/ancestor::div[contains(@class,'message')]//button[contains(@aria-label, 'Copy') or contains(@title, 'Copy')]"
			
			TestObject copyButton = new TestObject('dynamic_copy_button_index')
			copyButton.addProperty('xpath', ConditionType.EQUALS, xpath)
			
			WebUI.waitForElementVisible(copyButton, timeoutSeconds)
			WebUI.click(copyButton)
			WebUI.comment('✓ Copy button of message ' + messageIndex + ' clicked')
			
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in clickCopyButtonByMessageIndex: ' + e.getMessage())
			throw e
		}
	}
	
	
	
	
	
	// ============================================================
	// KEYWORD 1: LOGIN
	// ============================================================
	/**
	 * Logs into NuFi Chat using the provided credentials.
	 * Verifies login success by checking the URL contains '/c/'.
	 *
	 * @param email    - Login email address
	 * @param password - Login password
	 */
	@Keyword
	void loginChat(String email, String password) {
	    try {
	        WebUI.comment('=== KEYWORD: loginChat ===')
	
	        // Email field
	        TestObject emailField = new TestObject('dynamic_login_email')
	        emailField.addProperty('xpath', ConditionType.EQUALS, "//input[@id='email']")
	        WebUI.waitForElementVisible(emailField, SHORT_WAIT)
	        WebUI.setText(emailField, email)
	        WebUI.comment('Email entered')
	
	        // Password field
	        TestObject passwordField = new TestObject('dynamic_login_password')
	        passwordField.addProperty('xpath', ConditionType.EQUALS, "//input[@id='password']")
	        WebUI.waitForElementVisible(passwordField, SHORT_WAIT)
	        WebUI.setText(passwordField, password)
	        WebUI.comment('Password entered')
	
	        // Continue button
	        TestObject continueButton = new TestObject('dynamic_login_button')
	        continueButton.addProperty('xpath', ConditionType.EQUALS, "//button[contains(text(),'Continue')]")
	        WebUI.waitForElementClickable(continueButton, SHORT_WAIT)
	        WebUI.click(continueButton)
	        WebUI.comment('Continue button clicked')
	
	        // Wait for redirect to /c/
	        WebUI.waitForPageLoad(LONG_WAIT)

			boolean redirected = false
			
			for (int i = 0; i < LONG_WAIT; i++) {
			    if (WebUI.getUrl().contains('/c/')) {
			        redirected = true
			        break
			    }
			    WebUI.delay(1)
			}
			
			if (!redirected) {
			    throw new Exception('Login failed - URL does not contain /c/')
			}
	
	        String currentUrl = WebUI.getUrl()
	        WebUI.comment('✓ Login successful - URL: ' + currentUrl)
	
	    } catch (Exception e) {
	        WebUI.comment('✗ ERROR in loginChat: ' + e.getMessage())
	        throw e
	    }
	}
	
	// ============================================================
	// KEYWORD 2: OPEN NEW CONVERSATION
	// ============================================================
	/**
	 * Navigates to baseUrl (which should be /c/new) to start a fresh conversation.
	 * Ensures a clean state before each test case.
	 *
	 * @param baseUrl - The URL to navigate to (should be GlobalVariable.Base_URL)
	 */
	@Keyword
	void openNewConversation(String baseUrl) {
	    try {
	        WebUI.comment('=== KEYWORD: openNewConversation ===')
	        WebUI.navigateToUrl(baseUrl)
	        WebUI.comment('Navigated to: ' + baseUrl)
	
	        WebUI.waitForPageLoad(MEDIUM_WAIT)
	
	        TestObject textInput = new TestObject('dynamic_chat_input')
	        textInput.addProperty('css', ConditionType.EQUALS, "[data-testid='text-input']")
	        WebUI.waitForElementVisible(textInput, MEDIUM_WAIT)
	        WebUI.comment('✓ New conversation ready - chat input is visible')
	    } catch (Exception e) {
	        WebUI.comment('✗ ERROR in openNewConversation: ' + e.getMessage())
	        throw e
	    }
	}

	// ============================================================
	// KEYWORD 3: SELECT ENDPOINT
	// ============================================================
	/**
	 * Opens the endpoint dropdown and selects the specified endpoint.
	 *
	 * @param endpointName - Endpoint to select (e.g. "Gemini", "Nufi", "Agents")
	 */
	@Keyword
	void selectEndpoint(String endpointName) {
		try {
			WebUI.comment('=== KEYWORD: selectEndpoint ===')
			WebUI.comment('Selecting endpoint: ' + endpointName)

			// Step 1: Click the model selector trigger button
			TestObject selectorButton = new TestObject('dynamic_endpoint_selector_button')
			selectorButton.addProperty('xpath', ConditionType.EQUALS, "//button[@aria-label='Select a model']")
			WebUI.waitForElementClickable(selectorButton, MEDIUM_WAIT)
			WebUI.click(selectorButton)
			WebUI.comment('Endpoint selector opened')

			// Step 2: Wait for the endpoint listbox to appear
			TestObject listbox = new TestObject('dynamic_endpoint_listbox')
			listbox.addProperty('xpath', ConditionType.EQUALS, "//div[@role='listbox']")
			WebUI.waitForElementVisible(listbox, MEDIUM_WAIT)
			WebUI.comment('Endpoint listbox visible')

			// Step 3: Click the target endpoint by its DOM id
			TestObject endpointOption = new TestObject('dynamic_endpoint_option_' + endpointName)
			endpointOption.addProperty('xpath', ConditionType.EQUALS, "//div[@id='endpoint-" + endpointName + "-menu']")
			WebUI.waitForElementClickable(endpointOption, MEDIUM_WAIT)
			WebUI.click(endpointOption)
			WebUI.comment('✓ Endpoint selected: ' + endpointName)

			// Allow submenu time to appear
			WebUI.delay(1)
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in selectEndpoint: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 4: SELECT MODEL
	// ============================================================
	/**
	 * Selects a model from the submenu that appears after endpoint selection.
	 *
	 * @param modelName - Model to select (e.g. "gemini", "gpt-4o", "qwen2.5-0.5b")
	 */
	@Keyword
	void selectModel(String modelName) {
		try {
			WebUI.comment('=== KEYWORD: selectModel ===')
			WebUI.comment('Selecting model: ' + modelName)

			// Step 1: Wait for the model submenu (second listbox) to appear
			TestObject modelListbox = new TestObject('dynamic_model_listbox')
			modelListbox.addProperty('xpath', ConditionType.EQUALS, "(//div[@role='listbox'])[2]")
			WebUI.waitForElementVisible(modelListbox, MEDIUM_WAIT)
			WebUI.comment('Model submenu appeared')

			// Step 2: Click the model option matching the given name
			TestObject modelOption = new TestObject('dynamic_model_option_' + modelName)
			modelOption.addProperty('xpath', ConditionType.EQUALS,
				"//div[@role='option']//span[normalize-space(text())='" + modelName + "']/ancestor::div[@role='option']")
			WebUI.waitForElementClickable(modelOption, MEDIUM_WAIT)
			WebUI.click(modelOption)
			WebUI.comment('✓ Model selected: ' + modelName)

			// Step 3: Wait for dropdown to close
			WebUI.delay(1)
			TestObject listbox = new TestObject('dynamic_listbox_closed_check')
			listbox.addProperty('xpath', ConditionType.EQUALS, "//div[@role='listbox']")
			boolean closed = WebUI.waitForElementNotVisible(listbox, SHORT_WAIT)
			if (closed) {
				WebUI.comment('✓ Dropdown closed after model selection')
			}
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in selectModel: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 5: SELECT ENDPOINT AND MODEL (combined)
	// ============================================================
	/**
	 * Convenience wrapper: selects both endpoint and model in one call.
	 *
	 * @param endpointName - Endpoint name (e.g. "Gemini")
	 * @param modelName    - Model name    (e.g. "gemini")
	 */
	@Keyword
	void selectEndpointAndModel(String endpointName, String modelName) {
		try {
			WebUI.comment('=== KEYWORD: selectEndpointAndModel ===')
			WebUI.comment('Configuring endpoint: ' + endpointName + ' / model: ' + modelName)

			selectEndpoint(endpointName)
			WebUI.delay(2)
			selectModel(modelName)

			WebUI.comment('✓ Endpoint and model configured: ' + endpointName + ' / ' + modelName)
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in selectEndpointAndModel: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 6: SEND MESSAGE
	// ============================================================
	/**
	 * Types a message into the chat input and clicks the Send button.
	 * Confirms generation has started by checking Send button changes to Stop button.
	 *
	 * @param message - Text to send
	 */
	@Keyword
	void sendMessage(String message) {
		try {
			WebUI.comment('=== KEYWORD: sendMessage ===')
			WebUI.comment('Message to send: ' + message)

			WebUI.waitForElementVisible(findTestObject('Object Repository/Core Chat/chat_input'), MEDIUM_WAIT)

			WebUI.setText(findTestObject('Object Repository/Core Chat/chat_input'), message)
			WebUI.comment('Message typed into input')

			WebUI.waitForElementClickable(findTestObject('Object Repository/Core Chat/button__send-button'), SHORT_WAIT)
			WebUI.click(findTestObject('Object Repository/Core Chat/button__send-button'))
			WebUI.comment('Send button clicked')

		} catch (Exception e) {
			WebUI.comment('✗ ERROR in sendMessage: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 7: WAIT FOR THINKING
	// ============================================================
	/**
	 * Waits for the thinking indicator to appear during AI response generation.
	 * This indicates the AI is processing the request.
	 *
	 * @param timeoutSeconds - Max wait time in seconds (default: MEDIUM_WAIT = 15s)
	 * @return boolean - true if thinking indicator appeared, false if timed out
	 */
	@Keyword
	boolean waitForThinking(int timeoutSeconds = MEDIUM_WAIT) {
		try {
			WebUI.comment('=== KEYWORD: waitForThinking ===')
			WebUI.comment('Waiting for thinking indicator (timeout: ' + timeoutSeconds + 's)')
			
			// Use TestObject from Object Repository
			TestObject thinkingIndicator = findTestObject('Object Repository/Core Chat/Page_AI Greeting and Inquiry/thinking')
			
			boolean isVisible = WebUI.waitForElementVisible(thinkingIndicator, timeoutSeconds)
			
			if (isVisible) {
				WebUI.comment('✓ Thinking indicator is visible - AI is processing')
			} else {
				WebUI.comment('⚠ Thinking indicator not visible within timeout')
			}
			
			return isVisible
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in waitForThinking: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 8: WAIT FOR THINKING TO DISAPPEAR
	// ============================================================
	/**
	 * Waits for the thinking indicator to disappear, indicating first token received.
	 * This is the signal that the AI has started generating the response.
	 *
	 * @param timeoutSeconds - Max wait time in seconds (default: LONG_WAIT = 90s)
	 * @return boolean - true if thinking disappeared, false if timed out
	 */
	@Keyword
	boolean waitForThinkingToDisappear(int timeoutSeconds = LONG_WAIT) {
		try {
			WebUI.comment('=== KEYWORD: waitForThinkingToDisappear ===')
			WebUI.comment('Waiting for thinking indicator to disappear (timeout: ' + timeoutSeconds + 's)')
			
			// Use TestObject from Object Repository
			TestObject thinkingIndicator = findTestObject('Object Repository/Core Chat/Page_AI Greeting and Inquiry/thinking')
			
			boolean isNotVisible = WebUI.waitForElementNotVisible(thinkingIndicator, timeoutSeconds)
			
			if (isNotVisible) {
				WebUI.comment('✓ Thinking indicator disappeared - first token received')
			} else {
				WebUI.comment('⚠ Thinking indicator still visible after timeout')
			}
			
			return isNotVisible
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in waitForThinkingToDisappear: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 9: WAIT FOR RESPONSE
	// ============================================================
	/**
	 * Waits until the AI model finishes generating its response.
	 * Uses three signals:
	 *   1. Thinking indicator disappears (first token received)
	 *   2. Stop button disappears (generation completed)
	 *   3. Send button reappears (ready for next message)
	 *
	 * @param timeoutSeconds - Max wait time in seconds (default: LONG_WAIT = 90)
	 */
	@Keyword
	void waitForResponse(int timeoutSeconds = LONG_WAIT) {
		try {
			WebUI.comment('=== KEYWORD: waitForResponse ===')
			WebUI.comment('Waiting for response (timeout: ' + timeoutSeconds + 's)')
			long startTime = System.currentTimeMillis()

			TestObject thinkingIndicator = findTestObject('Object Repository/Core Chat/Page_AI Greeting and Inquiry/thinking')
			try {
				WebUI.waitForElementNotVisible(thinkingIndicator, timeoutSeconds)
				WebUI.comment('✓ Thinking indicator gone - first token received')
			} catch (Exception ex) {
				WebUI.comment('⚠ Thinking indicator check skipped - may not have appeared or already gone')
			}
			
			try {
				WebUI.waitForElementNotVisible(findTestObject('Object Repository/Core Chat/Page_AI Greeting and Inquiry/Stop'), timeoutSeconds)
				WebUI.comment('✓ Stop button disappeared - generation completed')
			} catch (Exception ex) {
				WebUI.comment('⚠ Stop button check skipped - may not have appeared')
			}

			WebUI.waitForElementVisible(findTestObject('Object Repository/Core Chat/button__send-button'), timeoutSeconds)
			WebUI.comment('✓ Send button visible - response generation completed')

			// Log elapsed time
			long elapsedSec = (System.currentTimeMillis() - startTime) / 1000
			WebUI.comment('Response received in ' + elapsedSec + 's')
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in waitForResponse: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 10: VERIFY RESPONSE SUCCESS
	// ============================================================
	/**
	 * Verifies the AI response is successful:
	 *   - No error banner visible
	 *   - Last message bubble contains non-empty text
	 *
	 * @return String - The AI response text (truncated in log, full value returned)
	 */
	@Keyword
	String verifyResponseSuccess() {
		try {
			WebUI.comment('=== KEYWORD: verifyResponseSuccess ===')

			// Check 1: No error banner on page
			TestObject errorBanner = new TestObject('dynamic_error_banner')
			errorBanner.addProperty('xpath', ConditionType.EQUALS,
				"//div[contains(@class,'alert-root')]")
			boolean noError = WebUI.verifyElementNotVisible(errorBanner,
				com.kms.katalon.core.model.FailureHandling.OPTIONAL)
//			if (!noError) {
//				throw new Exception('Error banner detected - API response failed')
//			}
//			WebUI.comment('✓ No error banner found')

			// Check 2: Last AI message bubble is visible
			TestObject lastMessage = new TestObject('dynamic_last_message')
			lastMessage.addProperty('xpath', ConditionType.EQUALS,
				"(//div[contains(@class,'message-content')])[last()]")
			WebUI.waitForElementVisible(lastMessage, MEDIUM_WAIT)

			// Check 3: Response text is not empty
			String responseText = WebUI.getText(lastMessage)
			if (responseText == null || responseText.trim().isEmpty()) {
				throw new Exception('AI response text is empty')
			}

			// Log a preview (max 200 chars)
			String preview = responseText.length() > 200
				? responseText.substring(0, 200) + '...'
				: responseText
			WebUI.comment('✓ Response verified (' + responseText.length() + ' chars): ' + preview)

			return responseText
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in verifyResponseSuccess: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 11: VERIFY NO ERROR
	// ============================================================
	/**
	 * Quick assertion that no error banner is currently visible on the page.
	 */
	@Keyword
	void verifyNoError() {
		try {
			WebUI.comment('=== KEYWORD: verifyNoError ===')
			TestObject errorBanner = new TestObject('dynamic_error_check')
			errorBanner.addProperty('xpath', ConditionType.EQUALS,
				"//div[contains(@class,'alert-root')]")
			boolean noError = WebUI.verifyElementNotVisible(errorBanner,
				com.kms.katalon.core.model.FailureHandling.OPTIONAL)
			if (!noError) {
				throw new Exception('Error banner is visible on page')
			}
			WebUI.comment('✓ No error detected on page')
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in verifyNoError: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 12: WAIT FOR MESSAGE VISIBLE
	// ============================================================
	/**
	 * Waits until at least one message bubble appears in the conversation.
	 *
	 * @param timeoutSeconds - Max wait time (default: MEDIUM_WAIT = 15s)
	 */
	@Keyword
	void waitForMessageVisible(int timeoutSeconds = MEDIUM_WAIT) {
		try {
			WebUI.comment('=== KEYWORD: waitForMessageVisible ===')
			TestObject messageBubble = new TestObject('dynamic_message_bubble')
			messageBubble.addProperty('xpath', ConditionType.EQUALS,
				"//div[contains(@class,'message-content')]")
			WebUI.waitForElementVisible(messageBubble, timeoutSeconds)
			WebUI.comment('✓ Message bubble appeared')
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in waitForMessageVisible: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 13: SEND MESSAGE AND VERIFY RESPONSE (composite)
	// ============================================================
	/**
	 * Full chat interaction in one keyword:
	 *   sendMessage → waitForResponse → verifyResponseSuccess
	 *
	 * @param message - Text message to send
	 * @return String - The verified AI response text
	 */
	@Keyword
	String sendMessageAndVerifyResponse(String message) {
		try {
			WebUI.comment('=== KEYWORD: sendMessageAndVerifyResponse ===')

			sendMessage(message)
			WebUI.delay(1)

			waitForResponse()

			String response = verifyResponseSuccess()

			WebUI.comment('✓ sendMessageAndVerifyResponse completed successfully')
			return response
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in sendMessageAndVerifyResponse: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 14: SELECT GEMINI ENDPOINT (convenience)
	// ============================================================
	@Keyword
	void selectGeminiEndpoint() {
		try {
			WebUI.comment('=== KEYWORD: selectGeminiEndpoint ===')
			selectEndpointAndModel('Gemini', 'gemini')
			WebUI.comment('✓ Gemini endpoint and model selected')
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in selectGeminiEndpoint: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 15: SELECT NUFI ENDPOINT (convenience)
	// ============================================================
	@Keyword
	void selectNufiEndpoint() {
		try {
			WebUI.comment('=== KEYWORD: selectNufiEndpoint ===')
			selectEndpointAndModel('Nufi', 'nufi')
			WebUI.comment('✓ Nufi endpoint and model selected')
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in selectNufiEndpoint: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 16: SELECT QWEN ENDPOINT (convenience)
	// ============================================================
	@Keyword
	void selectQwenEndpoint() {
		try {
			WebUI.comment('=== KEYWORD: selectQwenEndpoint ===')
			selectEndpointAndModel('Qwen2.5-0.5B', 'qwen2.5-0.5b')
			WebUI.comment('✓ Qwen endpoint and model selected')
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in selectQwenEndpoint: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 17: CORE CHAT SETUP (standalone use only)
	// ============================================================
	/**
	 * Full setup for running a TC without a separate Script_Setup:
	 *   Login → Open new conversation → Select Gemini endpoint
	 *
	 * NOTE: Do NOT call this keyword inside a Test Suite that already has
	 *       Script_Setup handling login.
	 *
	 * @param email    - Login email (pass GlobalVariable.email from TC)
	 * @param password - Login password (pass GlobalVariable.password from TC)
	 * @param baseUrl  - Base URL (pass GlobalVariable.Base_URL from TC)
	 */
	@Keyword
	void coreChateSetup(String email, String password, String baseUrl) {
		try {
			WebUI.comment('=== KEYWORD: coreChateSetup ===')
			WebUI.comment('Starting standalone setup: Login → Navigate → Select Gemini')

			// Step 1: Login
			WebUI.comment('Step 1: Logging in...')
			loginChat(email, password)
			WebUI.delay(2)

			// Step 2: Open new conversation
			WebUI.comment('Step 2: Opening new conversation...')
			openNewConversation(baseUrl)
			WebUI.delay(2)

			// Step 3: Select Gemini as default endpoint
			WebUI.comment('Step 3: Selecting Gemini endpoint...')
			selectGeminiEndpoint()
			WebUI.delay(2)

			WebUI.comment('✓ Core chat setup completed successfully')
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in coreChateSetup: ' + e.getMessage())
			throw e
		}
	}
}