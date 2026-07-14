import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC01B: Add File Search to Existing Agent
 */

WebUI.comment('=== TC01B: Add File Search to Agent ===')

try {
    WebUI.comment('Step 1: Open existing Agent...')
    // Click vào agent vừa tạo từ TC19
    WebUI.click(findTestObject('Object Repository/Core Chat/nav/RAG/button_Open_Recent_Agent')) // chỉnh theo object thật

    WebUI.comment('Step 2: Enable File Search...')
    TestObject checkbox = findTestObject('Object Repository/nav/RAG/button_File Search_file-search-checkbox')
    WebUI.click(checkbox)

    WebUI.comment('Step 3: Fill optional support contact...')
    WebUI.setText(findTestObject('Object Repository/nav/RAG/input_Name_support-contact-name'), "Support Team")
    WebUI.setText(findTestObject('Object Repository/nav/RAG/input_Email_support-contact-email'), "support@company.com")

    WebUI.comment('Step 4: Upload files for long-term memory...')
    WebUI.click(findTestObject('Object Repository/nav/RAG/button_Upload for File Search'))
    WebUI.delay(2)
    // Thực hiện upload file nếu cần

    WebUI.takeScreenshot('TC01B_Add_FileSearch_Success.png')
    WebUI.comment('=== TC01B Completed Successfully ===')

} catch (Exception e) {
    WebUI.comment('TC01B FAILED: ' + e.getMessage())
    KeywordUtil.markFailedAndStop(e.getMessage())
}