import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable

/**
 * Teardown_CoreChat_Suite
 * 
 * Clean up after all test cases in Core Chat suite.
 * Performs:
 * 1. Close browser
 * 2. Clean up any remaining resources
 */

WebUI.comment('=== TEARDOWN: Core Chat Suite ===')

try {
    // Close browser
    WebUI.comment('Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    WebUI.comment('✓ Browser closed successfully')
    
    // Optional: Add any additional cleanup here
    
    WebUI.comment('✓ Teardown completed successfully')
    
} catch (Exception e) {
    WebUI.comment('⚠ Teardown error: ' + e.getMessage())
    // Don't throw exception - teardown should not fail the suite
}