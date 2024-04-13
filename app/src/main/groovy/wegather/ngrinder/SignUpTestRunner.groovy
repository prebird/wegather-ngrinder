package wegather.ngrinder

import groovy.json.JsonBuilder
import net.grinder.script.GTest
import net.grinder.scriptengine.groovy.junit.GrinderRunner
import net.grinder.scriptengine.groovy.junit.annotation.BeforeProcess
import net.grinder.scriptengine.groovy.junit.annotation.BeforeThread
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ngrinder.http.HTTPRequest
import org.ngrinder.http.HTTPRequestControl
import org.ngrinder.http.HTTPResponse
import org.ngrinder.http.cookie.Cookie
import org.ngrinder.http.cookie.CookieManager

import static net.grinder.script.Grinder.grinder
import static net.grinder.script.Grinder.grinder
import static net.grinder.script.Grinder.grinder
import static net.grinder.script.Grinder.grinder
import static net.grinder.script.Grinder.grinder
import static org.hamcrest.Matchers.is
import static org.junit.Assert.assertThat

@RunWith(GrinderRunner)
class SignUpTestRunner {
    private String urlPrefix = "http://127.0.0.1:8080"
    public static GTest test
    public static HTTPRequest request
    public static Map<String, String> headers = [:]
    public static Map<String, Object> params = [:]
    public static List<Cookie> cookies = []

    @BeforeProcess
    public static void beforeProcess() {
        HTTPRequestControl.setConnectionTimeout(300000)
        headers.put("Content-Type", "application/json")
        test = new GTest(1, "127.0.0.1")
        request = new HTTPRequest()
        grinder.logger.info("before process.")
    }

    @BeforeThread
    public void beforeThread() {
        test.record(this, "test")
        grinder.statistics.delayReports = true
        grinder.logger.info("before thread.")
    }

    @Before
    public void before() {
        request.setHeaders(headers)
        CookieManager.addCookies(cookies)
        grinder.logger.info("before. init headers and cookies")
    }

    @Test
    public void test() {
        Map<String, Object> bodyMap = new HashMap<>()
        bodyMap.put("username", "test01")
        bodyMap.put("password", "12341234")
        bodyMap.put("email", "test01@example.com")
        String jsonString = new JsonBuilder(bodyMap).toString()
        grinder.logger.info("jsonString: " +  jsonString);

        headers.put("Content-Type", "application/json")

        HTTPResponse response = request.POST(urlPrefix+ "/api/sign-up", jsonString.getBytes(), headers)


        if (response.statusCode == 301 || response.statusCode == 302) {
            grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
        } else {
            assertThat(response.statusCode, is(201))
        }
    }
}
