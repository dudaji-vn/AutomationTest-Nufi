import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import com.example.CustomKeywords

/**
 * Core Chat Setup - Common setup for all Core Chat test cases
 * Handles:
 * 1. Login
 * 2. Navigation to Base URL
 * 3. Gemini endpoint selection
 */

// 1. Setup - Login
WebUI.callTestCase(findTestCase('Test Cases/Setup/Login'), [:])
WebUI.delay(2)

// 2. Navigate to Base URL
WebUI.navigateToUrl(GlobalVariable.Base_URL)
WebUI.delay(3)

// 3. Select Gemini endpoint
CustomKeywords.selectGeminiEndpoint()
WebUI.delay(2)

WebUI.comment('Core Chat Setup completed successfully')
