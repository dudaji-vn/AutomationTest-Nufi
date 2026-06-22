package keywords

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import internal.GlobalVariable

/**
 * Custom Keywords for NuFi Chat Automation Testing
 * Supports common chat operations: login, endpoint selection, messaging, and response verification
 */
public class ChatKeywords {

	// ============= TIMEOUT CONSTANTS =============
	static final int SHORT_WAIT = 5
	static final int MEDIUM_WAIT = 15
	static final int LONG_WAIT = 90

	// ============= KEYWORD 1: LOGIN =============
	/**
	 * Logs into NuFi Chat with provided credentials.
	 * Verifies successful login by checking URL contains '/c/' or '/c/new'
	 *
	 * @param email - Email address to login
	 * @param password - Password for the account
	 */
	@Keyword
	void loginChat(String email, String password) {
		try {
			WebUI.comment('=== KEYWORD: loginChat ===')
			WebUI.comment("Logging in with email: ${email}")

			// Step 1: Enter email
			TestObject emailField = new TestObject('dynamic_login_email')
			emailField.addProperty('xpath', ConditionType.EQUALS, "//input[@data-testid='name']")
			WebUI.waitForElementVisible(emailField, SHORT_WAIT)
			WebUI.setText(emailField, email)
			WebUI.comment('Email entered')

			// Step 2: Enter password
			TestObject passwordField = new TestObject('dynamic_login_password')
			passwordField.addProperty('xpath', ConditionType.EQUALS, "//input[@type='password']")
			WebUI.waitForElementVisible(passwordField, SHORT_WAIT)
			WebUI.setText(passwordField, password)
			WebUI.comment('Password entered')

			// Step 3: Click Continue button
			TestObject continueButton = new TestObject('dynamic_login_button')
			continueButton.addProperty('xpath', ConditionType.EQUALS, "//button[contains(text(), 'Continue')]")
			WebUI.waitForElementClickable(continueButton, SHORT_WAIT)
			WebUI.click(continueButton)
			WebUI.comment('Continue button clicked')

			// Step 4: Verify login success
			WebUI.waitForPageLoad(LONG_WAIT)
			String currentUrl = WebUI.getUrl()
			WebUI.comment("Current URL after login: ${currentUrl}")

			if (currentUrl.contains('/c/')) {
				WebUI.comment('✓ Login successful - URL contains /c/')
			} else {
				throw new Exception("Login verification failed - URL does not contain '/c/' : ${currentUrl}")
			}
		} catch (Exception e) {
			WebUI.comment("✗ ERROR in loginChat: ${e.getMessage()}")
			throw e
		}
	}

	// ============= KEYWORD 2: OPEN NEW CONVERSATION =============
	/**
	 * Opens a new conversation by navigating to /c/new endpoint
	 * Ensures clean state for new test case
	 */
	@Keyword
	void openNewConversation() {
		try {
			WebUI.comment('=== KEYWORD: openNewConversation ===')
			String newConversationUrl = GlobalVariable.Base_URL + '/c/new'
			WebUI.navigateToUrl(newConversationUrl)
			WebUI.comment("Navigated to: ${newConversationUrl}")

			// Wait for page load
			WebUI.waitForPageLoad(MEDIUM_WAIT)

			// Verify text input is visible
			TestObject textInput = new TestObject('dynamic_chat_input')
			textInput.addProperty('css', ConditionType.EQUALS, "[data-testid='text-input']")
			WebUI.waitForElementVisible(textInput, MEDIUM_WAIT)
			WebUI.comment('✓ New conversation opened - chat input visible')
		} catch (Exception e) {
			WebUI.comment("✗ ERROR in openNewConversation: ${e.getMessage()}")
			throw e
		}
	}

	// ============= KEYWORD 3: SELECT ENDPOINT =============
	/**
	 * Selects an endpoint from the endpoint dropdown menu
	 *
	 * @param endpointName - Name of endpoint (e.g., "Gemini", "Nufi", "Agents", "Qwen2.5")
	 */
	@Keyword
	void selectEndpoint(String endpointName) {
		try {
			WebUI.comment('=== KEYWORD: selectEndpoint ===')
			WebUI.comment("Selecting endpoint: ${endpointName}")

			// Step 1: Click endpoint selector button
			TestObject endpointButton = new TestObject('dynamic_endpoint_button')
			endpointButton.addProperty('xpath', ConditionType.EQUALS, "//button[@aria-label='Select a model']")
			WebUI.waitForElementClickable(endpointButton, MEDIUM_WAIT)
			WebUI.click(endpointButton)
			WebUI.comment('Endpoint selector button clicked')

			// Step 2: Wait for dropdown listbox to appear
			TestObject listbox = new TestObject('dynamic_endpoint_listbox')
			listbox.addProperty('xpath', ConditionType.EQUALS, "//div[@role='listbox']")
			WebUI.waitForElementVisible(listbox, MEDIUM_WAIT)
			WebUI.comment('Endpoint dropdown opened')

			// Step 3: Click the specific endpoint option
			TestObject endpointOption = new TestObject("dynamic_endpoint_${endpointName}")
			endpointOption.addProperty('xpath', ConditionType.EQUALS, "//div[@id='endpoint-${endpointName}-menu']")
			WebUI.waitForElementClickable(endpointOption, MEDIUM_WAIT)
			WebUI.click(endpointOption)
			WebUI.comment("✓ Endpoint '${endpointName}' selected")

			// Small delay for submenu to appear if needed
			WebUI.delay(1)
		} catch (Exception e) {
			WebUI.comment("✗ ERROR in selectEndpoint: ${e.getMessage()}")
			throw e
		}
	}

	// ============= KEYWORD 4: SELECT MODEL =============
	/**
	 * Selects a model from the model submenu (appears after endpoint selection)
	 *
	 * @param modelName - Name of model (e.g., "gemini", "gpt-4o", "qwen2.5-0.5b")
	 */
	@Keyword
	void selectModel(String modelName) {
		try {
			WebUI.comment('=== KEYWORD: selectModel ===')
			WebUI.comment("Selecting model: ${modelName}")

			// Step 1: Wait for model submenu (second listbox) to appear
			TestObject modelListbox = new TestObject('dynamic_model_listbox')
			modelListbox.addProperty('xpath', ConditionType.EQUALS, "//div[@role='listbox'][2]")
			WebUI.waitForElementVisible(modelListbox, MEDIUM_WAIT)
			WebUI.comment('Model submenu appeared')

			// Step 2: Find and click the model option by text match
			TestObject modelOption = new TestObject("dynamic_model_${modelName}")
			modelOption.addProperty('xpath', ConditionType.EQUALS, 
				"//div[@role='option']//span[normalize-space(text())='${modelName}']/ancestor::div[@role='option']")
			WebUI.waitForElementClickable(modelOption, MEDIUM_WAIT)
			WebUI.click(modelOption)
			WebUI.comment("✓ Model '${modelName}' selected")

			// Step 3: Verify dropdown closed
			WebUI.delay(1)
			TestObject listbox = new TestObject('dynamic_listbox_check')
			listbox.addProperty('xpath', ConditionType.EQUALS, "//div[@role='listbox']")
			boolean isListboxGone = WebUI.waitForElementNotVisible(listbox, SHORT_WAIT)
			if (isListboxGone) {
				WebUI.comment('✓ Dropdown closed after model selection')
			}
		} catch (Exception e) {
			WebUI.comment("✗ ERROR in selectModel: ${e.getMessage()}")
			throw e
		}
	}

	// ============= KEYWORD 5: SELECT ENDPOINT AND MODEL =============
	/**
	 * Convenience wrapper that selects both endpoint and model in sequence
	 *
	 * @param endpointName - Name of endpoint (e.g., "Gemini")
	 * @param modelName - Name of model (e.g., "gemini")
	 */
	@Keyword
	void selectEndpointAndModel(String endpointName, String modelName) {
		try {
			WebUI.comment('=== KEYWORD: selectEndpointAndModel ===')
			WebUI.comment("Selecting endpoint: ${endpointName}, model: ${modelName}")

			// Select endpoint
			selectEndpoint(endpointName)

			// Wait for submenu to appear
			WebUI.delay(2)

			// Select model
			selectModel(modelName)

			WebUI.comment("✓ Endpoint '${endpointName}' and model '${modelName}' configured")
		} catch (Exception e) {
			WebUI.comment("✗ ERROR in selectEndpointAndModel: ${e.getMessage()}")
			throw e
		}
	}

	// ============= KEYWORD 6: SEND MESSAGE =============
	/**
	 * Sends a message in the chat
	 * Verifies message was sent by checking send button disappears
	 *
	 * @param message - Message text to send
	 */
	@Keyword
	void sendMessage(String message) {
		try {
			WebUI.comment('=== KEYWORD: sendMessage ===')
			WebUI.comment("Sending message: '${message}'")

			// Step 1: Wait for text input visible
			TestObject textInput = new TestObject('dynamic_text_input')
			textInput.addProperty('css', ConditionType.EQUALS, "[data-testid='text-input']")
			WebUI.waitForElementVisible(textInput, MEDIUM_WAIT)
			WebUI.comment('Text input is visible')

			// Step 2: Clear and set text
			WebUI.clearText(textInput)
			WebUI.setText(textInput, message)
			WebUI.comment('Message text entered')

			// Step 3: Click send button
			TestObject sendButton = new TestObject('dynamic_send_button')
			sendButton.addProperty('css', ConditionType.EQUALS, "[data-testid='send-button']")
			WebUI.waitForElementClickable(sendButton, SHORT_WAIT)
			WebUI.click(sendButton)
			WebUI.comment('Send button clicked')

			// Step 4: Verify send button disappears (generation started)
			boolean buttonGone = WebUI.waitForElementNotVisible(sendButton, SHORT_WAIT)
			if (buttonGone) {
				WebUI.comment('✓ Message sent - send button disappeared (generation in progress)')
			} else {
				WebUI.comment('⚠ Send button still visible - generation may be delayed')
			}
		} catch (Exception e) {
			WebUI.comment("✗ ERROR in sendMessage: ${e.getMessage()}")
			throw e
		}
	}

	// ============= KEYWORD 7: WAIT FOR RESPONSE =============
	/**
	 * Waits for AI model to complete response generation
	 * Monitors thinking indicator and send button visibility
	 *
	 * @param timeoutSeconds - Maximum seconds to wait for response (default 90)
	 */
	@Keyword
	void waitForResponse(int timeoutSeconds = LONG_WAIT) {
		try {
			WebUI.comment('=== KEYWORD: waitForResponse ===')
			WebUI.comment("Waiting for response (timeout: ${timeoutSeconds}s)")
			long startTime = System.currentTimeMillis()

			// Step 1: Wait for thinking indicator to disappear (first token received)
			TestObject thinkingIndicator = new TestObject('dynamic_thinking_indicator')
			thinkingIndicator.addProperty('xpath', ConditionType.EQUALS, "//span[contains(@class,'result-thinking')]")
			try {
				WebUI.waitForElementNotVisible(thinkingIndicator, timeoutSeconds)
				WebUI.comment('Thinking indicator gone - first token received')
			} catch (Exception thinkingError) {
				WebUI.comment('⚠ Thinking indicator not found or did not disappear - stream may have started immediately')
			}

			// Step 2: Wait for send button to reappear (stream completed)
			TestObject sendButton = new TestObject('dynamic_send_button_wait')
			sendButton.addProperty('css', ConditionType.EQUALS, "[data-testid='send-button']")
			WebUI.waitForElementVisible(sendButton, timeoutSeconds)
			WebUI.comment('✓ Send button visible - response generation completed')

			// Step 3: Log elapsed time
			long elapsedMs = System.currentTimeMillis() - startTime
			long elapsedSec = elapsedMs / 1000
			WebUI.comment("Response time: ${elapsedSec}s (${elapsedMs}ms)")
		} catch (Exception e) {
			WebUI.comment("✗ ERROR in waitForResponse: ${e.getMessage()}")
			throw e
		}
	}

	// ============= KEYWORD 8: VERIFY RESPONSE SUCCESS =============
	/**
	 * Verifies that AI response was successful and retrieves the response text
	 * Checks for error messages and confirms message content is present
	 *
	 * @return String - Content of the AI response message
	 */
	@Keyword
	String verifyResponseSuccess() {
		try {
			WebUI.comment('=== KEYWORD: verifyResponseSuccess ===')

			// Step 1: Verify no error banner
			TestObject errorBanner = new TestObject('dynamic_error_banner')
			errorBanner.addProperty('xpath', ConditionType.EQUALS, "//div[contains(@class,'alert-root')]")
			boolean errorExists = WebUI.verifyElementNotVisible(errorBanner, WebUI.NOT_CONTINUE_ON_FAILURE)
			if (errorExists) {
				WebUI.comment('✗ ERROR: Error banner detected on page')
				throw new Exception('Error banner detected - API response failed')
			}
			WebUI.comment('✓ No error banner found')

			// Step 2: Get last message bubble (AI response)
			TestObject lastMessage = new TestObject('dynamic_last_message')
			lastMessage.addProperty('xpath', ConditionType.EQUALS, 
				"(//div[contains(@class,'message-content')])[last()]")
			WebUI.waitForElementVisible(lastMessage, MEDIUM_WAIT)
			WebUI.comment('✓ AI response message bubble visible')

			// Step 3: Get text content of last message
			String responseText = WebUI.getText(lastMessage)
			WebUI.comment("Response length: ${responseText.length()} characters")

			// Step 4: Verify response is not empty
			if (responseText == null || responseText.trim().isEmpty()) {
				throw new Exception('Response text is empty')
			}

			// Step 5: Log response (truncate if too long)
			String logText = responseText.length() > 200 ? 
				responseText.substring(0, 200) + '...' : responseText
			WebUI.comment("✓ Response received: ${logText}")

			return responseText
		} catch (Exception e) {
			WebUI.comment("✗ ERROR in verifyResponseSuccess: ${e.getMessage()}")
			throw e
		}
	}

	// ============= UTILITY: WAIT FOR MESSAGE VISIBILITY =============
	/**
	 * Utility function to wait for any message to appear (useful after sending)
	 *
	 * @param timeoutSeconds - Maximum seconds to wait
	 */
	@Keyword
	void waitForMessageVisible(int timeoutSeconds = MEDIUM_WAIT) {
		try {
			WebUI.comment("Waiting for message to appear (timeout: ${timeoutSeconds}s)")
			TestObject messageBubble = new TestObject('dynamic_message_bubble')
			messageBubble.addProperty('xpath', ConditionType.EQUALS, "//div[contains(@class,'message-content')]")
			WebUI.waitForElementVisible(messageBubble, timeoutSeconds)
			WebUI.comment('✓ Message bubble appeared')
		} catch (Exception e) {
			WebUI.comment("✗ ERROR in waitForMessageVisible: ${e.getMessage()}")
			throw e
		}
	}

	// ============= UTILITY: VERIFY NO ERROR =============
	/**
	 * Quick check that no error banner is visible on page
	 */
	@Keyword
	void verifyNoError() {
		try {
			TestObject errorBanner = new TestObject('dynamic_error_check')
			errorBanner.addProperty('xpath', ConditionType.EQUALS, "//div[contains(@class,'alert-root')]")
			boolean errorVisible = WebUI.verifyElementNotVisible(errorBanner, WebUI.NOT_CONTINUE_ON_FAILURE)
			if (!errorVisible) {
				throw new Exception('Error banner is visible')
			}
			WebUI.comment('✓ No error detected')
		} catch (Exception e) {
			WebUI.comment("✗ ERROR in verifyNoError: ${e.getMessage()}")
			throw e
		}
	}
}
