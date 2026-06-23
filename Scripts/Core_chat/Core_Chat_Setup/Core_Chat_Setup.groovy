import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import keywords.ChatKeywords

/**
 * Core Chat Setup - Common setup for all Core Chat test cases
 * Handles:
 * 1. Login
 * 2. Navigation to Base URL
 * 3. Gemini endpoint selection
 */

// Execute core chat setup using the composite keyword
ChatKeywords.coreChateSetup()

WebUI.comment('Core Chat Setup completed successfully')
