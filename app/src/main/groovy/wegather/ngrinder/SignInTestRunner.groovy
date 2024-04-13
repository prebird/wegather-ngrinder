package wegather.ngrinder

import groovy.json.JsonBuilder
import net.grinder.script.GTest
import net.grinder.scriptengine.groovy.junit.GrinderRunner
import net.grinder.scriptengine.groovy.junit.annotation.BeforeProcess
import net.grinder.scriptengine.groovy.junit.annotation.BeforeThread
import org.apache.hc.core5.http.Header
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
import static net.grinder.script.Grinder.grinder
import static org.hamcrest.Matchers.is
import static org.junit.Assert.assertThat

@RunWith(GrinderRunner)
class SignInTestRunner {
    private static final String urlPrefix = "http://127.0.0.1:8080"
    public static GTest test
    public static HTTPRequest request
    public static Map<String, String> headers = [:]
    public static Map<String, Object> params = [:]
    public static List<Cookie> cookies = []

    @BeforeProcess
    public static void beforeProcess() {
        HTTPRequestControl.setConnectionTimeout(300000)
        //headers.put("Content-Type", "application/json")
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

    public HTTPResponse signIn() {
        Map<String, Object> bodyMap = new HashMap<>()
        bodyMap.put("usernameOrEmail", "test01")
        bodyMap.put("password", "12341234")

        String jsonString = new JsonBuilder(bodyMap).toString()
        headers.put("Content-Type", "application/json")
        return request.POST(urlPrefix+ "/api/sign-in", jsonString.getBytes(), headers)
    }

    @Test
    public void test() {
        HTTPResponse response = signIn()
        String cookie = response.getHeader("Set-Cookie")
        grinder.logger.info("cookie : " + cookie)
        if (response.statusCode == 301 || response.statusCode == 302) {
            grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
        } else {
            assertThat(response.statusCode, is(200))
        }
    }
}
