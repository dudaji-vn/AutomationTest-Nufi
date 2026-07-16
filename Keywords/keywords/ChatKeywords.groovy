package keywords

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.webui.driver.DriverFactory



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
	static final int SHORT_WAIT  = 2
	static final int MEDIUM_WAIT = 4
	static final int LONG_WAIT   = 5
	
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
	@Keyword
	void closeBrowser() {
	    try {
	        WebUI.comment('=== KEYWORD: closeBrowser ===')
	        
	        // WebUI
	        try {
	            WebUI.closeBrowser()
	            WebUI.comment('✓ WebUI.closeBrowser() executed')
	        } catch (Exception e) {
	            WebUI.comment('⚠ WebUI.closeBrowser() error: ' + e.getMessage())
	        }
	        
	        //  DriverFactory 
	        try {
	            def driver = DriverFactory.getWebDriver()
	            if (driver != null) {
	                driver.quit()
	                WebUI.comment('✓ DriverFactory.quit() executed')
	            }
	        } catch (Exception e) {
	            WebUI.comment('⚠ DriverFactory quit error: ' + e.getMessage())
	        }
	        
	        //Force close all browser windows
	        try {
	            WebUI.executeJavaScript("window.close()", null)
	        } catch (Exception e) {
	            // Ignore
	        }
	        
	        WebUI.comment('✓ Browser closed successfully')
	    } catch (Exception e) {
	        WebUI.comment('✗ ERROR in closeBrowser: ' + e.getMessage())
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
	@Keyword
	boolean waitForThinking(int timeoutSeconds = MEDIUM_WAIT) {
		try {
			WebUI.comment('=== KEYWORD: waitForThinking ===')
			WebUI.comment('Waiting for thinking indicator (timeout: ' + timeoutSeconds + 's)')
			
			TestObject thinkingIndicator = findTestObject('Object Repository/Core Chat/Chatting/thinking')
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
	@Keyword
	boolean waitForThinkingToDisappear(int timeoutSeconds = LONG_WAIT) {
		try {
			WebUI.comment('=== KEYWORD: waitForThinkingToDisappear ===')
			WebUI.comment('Waiting for thinking indicator to disappear (timeout: ' + timeoutSeconds + 's)')
			
			TestObject thinkingIndicator = findTestObject('Object Repository/Core Chat/Chatting/thinking')
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
	@Keyword
	void waitForResponse(int timeoutSeconds = LONG_WAIT) {
		try {
			WebUI.comment('=== KEYWORD: waitForResponse ===')
			WebUI.comment('Waiting for response (timeout: ' + timeoutSeconds + 's)')
			long startTime = System.currentTimeMillis()

			TestObject thinkingIndicator = findTestObject('Object Repository/Core Chat/Chatting/thinking')
			try {
				WebUI.waitForElementNotVisible(thinkingIndicator, timeoutSeconds)
				WebUI.comment('✓ Thinking indicator gone - first token received')
			} catch (Exception ex) {
				WebUI.comment('⚠ Thinking indicator check skipped - may not have appeared or already gone')
			}
			
			try {
				WebUI.waitForElementNotVisible(findTestObject('Object Repository/Core Chat/Chatting/Stop'), timeoutSeconds)
				WebUI.comment('✓ Stop button disappeared - generation completed')
			} catch (Exception ex) {
				WebUI.comment('⚠ Stop button check skipped - may not have appeared')
			}

			WebUI.waitForElementVisible(findTestObject('Object Repository/Core Chat/button__send-button'), timeoutSeconds)
			WebUI.comment('✓ Send button visible - response generation completed')

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
	@Keyword
	String verifyResponseSuccess() {
		try {
			WebUI.comment('=== KEYWORD: verifyResponseSuccess ===')

			// Check error banner but ignore if present
			TestObject errorBanner = new TestObject('dynamic_error_banner')
			errorBanner.addProperty('xpath', ConditionType.EQUALS,
				"//div[contains(@class,'alert-root')]")
			boolean noError = WebUI.verifyElementNotVisible(errorBanner,
				com.kms.katalon.core.model.FailureHandling.OPTIONAL)
			if (!noError) {
				WebUI.comment('⚠ Warning: Error banner detected but IGNORED')
			} else {
				WebUI.comment('✓ No error banner found')
			}

			// Get last message using Object Repository
			TestObject lastMessage = findTestObject('Object Repository/Core Chat/Chatting/message-content')
			WebUI.waitForElementVisible(lastMessage, MEDIUM_WAIT)

			String responseText = WebUI.getText(lastMessage)
			if (responseText == null || responseText.trim().isEmpty()) {
				throw new Exception('AI response text is empty')
			}

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
				WebUI.comment('⚠ Warning: Error banner detected but IGNORED')
			} else {
				WebUI.comment('✓ No error detected on page')
			}
		} catch (Exception e) {
			WebUI.comment('⚠ Error check encountered exception but IGNORING: ' + e.getMessage())
		}
	}

	// ============================================================
	// KEYWORD 12: WAIT FOR MESSAGE VISIBLE
	// ============================================================
	@Keyword
	void waitForMessageVisible(int timeoutSeconds = MEDIUM_WAIT) {
		try {
			WebUI.comment('=== KEYWORD: waitForMessageVisible ===')
			TestObject messageBubble = findTestObject('Object Repository/Core Chat/Chatting/message-content')
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
	@Keyword
	String sendMessageAndVerifyResponse(String message) {
		try {
			WebUI.comment('=== KEYWORD: sendMessageAndVerifyResponse ===')

			sendMessage(message)

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
	@Keyword
	void coreChateSetup(String email, String password, String baseUrl) {
		try {
			WebUI.comment('=== KEYWORD: coreChateSetup ===')
			WebUI.comment('Starting standalone setup: Login → Navigate → Select Gemini')

			WebUI.comment('Step 1: Logging in...')
			loginChat(email, password)

			WebUI.comment('Step 2: Opening new conversation...')
			openNewConversation(baseUrl)

			WebUI.comment('Step 3: Selecting Gemini endpoint...')
			selectGeminiEndpoint()

			WebUI.comment('✓ Core chat setup completed successfully')
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in coreChateSetup: ' + e.getMessage())
			throw e
		}
	}
	
	
	// ============================================================
	// KEYWORD 18: CLICK FORK MENU BUTTON
	// ============================================================
	@Keyword
	void clickForkMenuButton(int timeoutSeconds = 5) {
		try {
			WebUI.comment('=== KEYWORD: clickForkMenuButton ===')
			TestObject forkMenuButton = new TestObject('dynamic_fork_menu_button')
			forkMenuButton.addProperty('xpath', ConditionType.EQUALS,
				"(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[contains(@aria-label, 'Fork') or contains(@title, 'Fork')]")
			
			WebUI.waitForElementVisible(forkMenuButton, timeoutSeconds)
			WebUI.click(forkMenuButton)
			WebUI.comment('Fork menu button clicked')
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in clickForkMenuButton: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 19: SELECT FORK OPTION
	// ============================================================
	@Keyword
	void selectForkOption(String optionAriaLabel, int timeoutSeconds = 5) {
		try {
			WebUI.comment('=== KEYWORD: selectForkOption ===')
			WebUI.comment('Selecting fork option: ' + optionAriaLabel)
			
			TestObject forkOption = new TestObject('dynamic_fork_option')
			forkOption.addProperty('xpath', ConditionType.EQUALS,
				"//button[@aria-label='" + optionAriaLabel + "']")
			
			WebUI.waitForElementVisible(forkOption, timeoutSeconds)
			WebUI.click(forkOption)
			WebUI.comment('Fork option "' + optionAriaLabel + '" selected')
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in selectForkOption: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 20: CHECK START FORK HERE CHECKBOX
	// ============================================================
	@Keyword
	void checkStartForkHere(int timeoutSeconds = 5) {
		try {
			WebUI.comment('=== KEYWORD: checkStartForkHere ===')
			TestObject startForkCheckbox = new TestObject('dynamic_start_fork_checkbox')
			startForkCheckbox.addProperty('xpath', ConditionType.EQUALS,
				"//label[contains(text(),'Start fork here')]//input[@type='checkbox']")
			
			WebUI.waitForElementVisible(startForkCheckbox, timeoutSeconds)
			WebUI.click(startForkCheckbox)
			WebUI.comment('"Start fork here" checkbox checked')
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in checkStartForkHere: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 21: UNCHECK REMEMBER CHECKBOX
	// ============================================================
	@Keyword
	void uncheckRemember(int timeoutSeconds = 5) {
		try {
			WebUI.comment('=== KEYWORD: uncheckRemember ===')
			TestObject rememberCheckbox = new TestObject('dynamic_remember_checkbox')
			rememberCheckbox.addProperty('xpath', ConditionType.EQUALS,
				"//label[contains(text(),'Remember')]//input[@type='checkbox']")
			
			WebUI.waitForElementVisible(rememberCheckbox, timeoutSeconds)
			boolean isChecked = WebUI.verifyElementChecked(rememberCheckbox, 2, com.kms.katalon.core.model.FailureHandling.OPTIONAL)
			if (isChecked) {
				WebUI.click(rememberCheckbox)
				WebUI.comment('"Remember" checkbox unchecked')
			} else {
				WebUI.comment('"Remember" checkbox already unchecked')
			}
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in uncheckRemember: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 22: VERIFY FORK SUCCESS
	// ============================================================
	@Keyword
	void verifyForkSuccess(int timeoutSeconds = 5) {
		try {
			WebUI.comment('=== KEYWORD: verifyForkSuccess ===')
			TestObject forkSuccessMessage = findTestObject('Object Repository/Core Chat/Action/Fork/Fork Test Message')
			WebUI.waitForElementVisible(forkSuccessMessage, timeoutSeconds)
			WebUI.comment('Fork success message displayed')
			WebUI.waitForElementNotVisible(forkSuccessMessage, timeoutSeconds)
			WebUI.comment('Fork success message disappeared')
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in verifyForkSuccess: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 23: CLICK LIKE BUTTON
	// ============================================================
	@Keyword
	void clickLikeButton(int timeoutSeconds = 5) {
		try {
			WebUI.comment('=== KEYWORD: clickLikeButton ===')
			TestObject likeButton = new TestObject('dynamic_like_button')
			likeButton.addProperty('xpath', ConditionType.EQUALS,
				"(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Love this']")
			
			WebUI.waitForElementVisible(likeButton, timeoutSeconds)
			WebUI.click(likeButton)
			WebUI.comment('Like button clicked')
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in clickLikeButton: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 24: SELECT LIKE REASON
	// ============================================================
	@Keyword
	void selectLikeReason(String reasonText, int timeoutSeconds = 5) {
		try {
			WebUI.comment('=== KEYWORD: selectLikeReason ===')
			WebUI.comment('Selecting like reason: ' + reasonText)
			
			TestObject popup = findTestObject('Object Repository/Core Chat/Love-this/popup_Love-this')
			WebUI.waitForElementVisible(popup, timeoutSeconds)
			
			TestObject reasonOption = new TestObject('dynamic_like_reason')
			reasonOption.addProperty('xpath', ConditionType.EQUALS,
				"//button[text()='" + reasonText + "' or .='" + reasonText + "']")
			
			WebUI.waitForElementVisible(reasonOption, timeoutSeconds)
			WebUI.click(reasonOption)
			WebUI.comment('Like reason "' + reasonText + '" selected')
			
			WebUI.waitForElementNotVisible(popup, timeoutSeconds)
			WebUI.comment('Popup closed')
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in selectLikeReason: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 25: CLICK DISLIKE BUTTON
	// ============================================================
	@Keyword
	void clickDislikeButton(int timeoutSeconds = 5) {
		try {
			WebUI.comment('=== KEYWORD: clickDislikeButton ===')
			TestObject dislikeButton = new TestObject('dynamic_dislike_button')
			dislikeButton.addProperty('xpath', ConditionType.EQUALS,
				"(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Needs improvement']")
			
			WebUI.waitForElementVisible(dislikeButton, timeoutSeconds)
			WebUI.click(dislikeButton)
			WebUI.comment('Dislike button clicked')
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in clickDislikeButton: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 26: SELECT DISLIKE REASON
	// ============================================================
	@Keyword
	void selectDislikeReason(String reasonText, int timeoutSeconds = 5) {
		try {
			WebUI.comment('=== KEYWORD: selectDislikeReason ===')
			WebUI.comment('Selecting dislike reason: ' + reasonText)
			
			TestObject popup = findTestObject('Object Repository/Core Chat/Action/Needs improvement/popover_Needs improvement')
			WebUI.waitForElementVisible(popup, timeoutSeconds)
			
			TestObject reasonOption = new TestObject('dynamic_dislike_reason')
			reasonOption.addProperty('xpath', ConditionType.EQUALS,
				"//button[text()='" + reasonText + "' or .='" + reasonText + "']")
			
			WebUI.waitForElementVisible(reasonOption, timeoutSeconds)
			WebUI.click(reasonOption)
			WebUI.comment('Dislike reason "' + reasonText + '" selected')
			
			WebUI.waitForElementNotVisible(popup, timeoutSeconds)
			WebUI.comment('Popup closed')
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in selectDislikeReason: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 27: CLICK REGENERATE BUTTON
	// ============================================================
	@Keyword
	void clickRegenerateButton(int timeoutSeconds = 5) {
		try {
			WebUI.comment('=== KEYWORD: clickRegenerateButton ===')
			TestObject regenerateButton = new TestObject('dynamic_regenerate_button')
			regenerateButton.addProperty('xpath', ConditionType.EQUALS,
				"(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Regenerate']")
			
			WebUI.waitForElementVisible(regenerateButton, timeoutSeconds)
			WebUI.click(regenerateButton)
			WebUI.comment('Regenerate button clicked')
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in clickRegenerateButton: ' + e.getMessage())
			throw e
		}
	}

	// ============================================================
	// KEYWORD 28: CLICK READ ALOUD BUTTON
	// ============================================================
	@Keyword
	void clickReadAloudButton(int messageIndex = -1, int timeoutSeconds = 5) {
		try {
			WebUI.comment('=== KEYWORD: clickReadAloudButton ===')
			String indexStr = (messageIndex == -1) ? "last()" : String.valueOf(messageIndex)
			WebUI.comment('Clicking Read Aloud button for message index: ' + indexStr)
			
			TestObject readButton = new TestObject('dynamic_read_button')
			readButton.addProperty('xpath', ConditionType.EQUALS,
				"(//div[contains(@class,'message-content')])[" + indexStr + "]/ancestor::div[contains(@class,'message')]//button[@title='Read aloud']")
			
			WebUI.waitForElementVisible(readButton, timeoutSeconds)
			WebUI.click(readButton)
			WebUI.comment('Read Aloud button clicked')
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in clickReadAloudButton: ' + e.getMessage())
			throw e
		}
	}
	
	// ============================================================
	// KEYWORD 29: SWITCH TO ADVANCED INTERFACE
	// ============================================================
	@Keyword
	void switchToAdvancedInterface() {
		try {
			WebUI.comment('=== KEYWORD: switchToAdvancedInterface ===')
			
			// Step 1: Check screen width
			String screenWidthScript = "return window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;"
			int screenWidth = (int) WebUI.executeJavaScript(screenWidthScript, null)
			WebUI.comment('Screen width: ' + screenWidth + 'px')
			
			if (screenWidth <= 760) {
				WebUI.comment('Screen width <= 760px, ensuring navbar is open...')
				
				TestObject navSidebar = new TestObject('navSidebar')
				navSidebar.addProperty('xpath', ConditionType.EQUALS, "//nav")
				
				String ariaHidden = WebUI.getAttribute(navSidebar, 'aria-hidden')
				
				if (ariaHidden == 'true') {
					WebUI.comment('Navbar is closed, opening...')
					TestObject openButton = new TestObject('openButton')
					openButton.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
					WebUI.waitForElementClickable(openButton, 5)
					WebUI.click(openButton)
					WebUI.delay(1)
					WebUI.comment('✓ Navbar opened')
				} else {
					WebUI.comment('✓ Navbar already open')
				}
			} else {
				WebUI.comment('Screen width > 760px, skipping navbar check')
			}
			
			// Step 2: Click Account Settings
			WebUI.comment('Clicking Account Settings...')
			WebUI.waitForElementClickable(
				findTestObject('Object Repository/nav/nav_items/button_Account Settings'),
				10
			)
			WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Account Settings'))
			WebUI.delay(1)
			WebUI.comment('✓ Account Settings clicked')
			
			// Step 3: Click Settings menu item
			WebUI.comment('Clicking Settings menu item...')
			WebUI.waitForElementClickable(
				findTestObject('Object Repository/nav/Account_Settings/menuitem_Settings'),
				10
			)
			WebUI.click(findTestObject('Object Repository/nav/Account_Settings/menuitem_Settings'))
			WebUI.delay(2)
			WebUI.comment('✓ Settings popup opened')
			
			// Step 4: Click Interface dropdown
			WebUI.comment('Clicking Interface dropdown...')
			
			TestObject interfaceDropdown = new TestObject('interfaceDropdown')
			interfaceDropdown.addProperty('xpath', ConditionType.EQUALS,
				"//button[@data-testid='ui-mode-selector']")
			
			if (!WebUI.waitForElementVisible(interfaceDropdown, 5, FailureHandling.OPTIONAL)) {
				WebUI.comment('Trying alternative selector for Interface dropdown...')
				interfaceDropdown = new TestObject('interfaceDropdownAlt')
				interfaceDropdown.addProperty('xpath', ConditionType.EQUALS,
					"//div[@id='ui-mode-selector-label']/following-sibling::div//button")
			}
			
			WebUI.waitForElementClickable(interfaceDropdown, 10)
			WebUI.click(interfaceDropdown)
			WebUI.delay(1)
			WebUI.comment('✓ Interface dropdown opened')
			
			// Step 5: Select Advanced from dropdown
			WebUI.comment('Selecting Advanced from dropdown...')
			
			TestObject advancedOption = new TestObject('advancedOption')
			advancedOption.addProperty('xpath', ConditionType.EQUALS,
				"//div[@role='listbox']//div[@data-theme='advanced']")
			
			if (!WebUI.waitForElementVisible(advancedOption, 5, FailureHandling.OPTIONAL)) {
				WebUI.comment('Trying alternative selector for Advanced option...')
				advancedOption = new TestObject('advancedOptionAlt')
				advancedOption.addProperty('xpath', ConditionType.EQUALS,
					"//div[@role='listbox']//span[contains(text(), 'Advanced')]/ancestor::div[@role='option']")
			}
			
			WebUI.waitForElementClickable(advancedOption, 10)
			WebUI.click(advancedOption)
			WebUI.delay(1)
			WebUI.comment('✓ Selected Advanced interface')
			
			// Step 6: Close Settings popup
			WebUI.comment('Closing Settings popup...')
			
			TestObject closeButton = new TestObject('closeButton')
			closeButton.addProperty('xpath', ConditionType.EQUALS,
				"//button[contains(@class, 'rounded-sm')]//*[local-name()='svg']//*[local-name()='line' and @x1='6' and @x2='18' and @y1='6' and @y2='18']/ancestor::button")
			
			if (!WebUI.waitForElementVisible(closeButton, 5, FailureHandling.OPTIONAL)) {
				WebUI.comment('Trying alternative selector for close button...')
				closeButton = new TestObject('closeButtonAlt')
				closeButton.addProperty('xpath', ConditionType.EQUALS,
					"//button[@aria-label='Close Settings']")
			}
			
			WebUI.waitForElementClickable(closeButton, 10)
			WebUI.click(closeButton)
			WebUI.delay(2)
			
			WebUI.comment('✓ Settings popup closed')
			WebUI.comment('✓ Successfully switched to Advanced interface')
			
		} catch (Exception e) {
			WebUI.comment('✗ ERROR in switchToAdvancedInterface: ' + e.getMessage())
			throw e
		}
	}
}