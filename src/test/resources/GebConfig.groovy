import geb.report.ScreenshotReporter
import org.ods.e2e.util.SpecHelper
import org.openqa.selenium.Proxy
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.DesiredCapabilities

def properties = new SpecHelper().getApplicationProperties()

// Define timing to get information from a page
waiting {
    timeout = 25
    retryInterval = 0.5
    includeCauseInMessage = true
    atCheckWaiting = true

    presets {
        extremelySlow {
            timeout = 3600
            retryInterval = 2
        }
         verySlow {
            timeout = 600
            retryInterval = 5
        }
        mediumSlow {
            timeout = 300
            retryInterval = 5
        }
        slow {
            timeout = 50
            retryInterval = 1
        }
        quick {
            timeout = 10
        }
    }
}

environments {
    // TODO Add the proxy to all the drivers
    // See: http://code.google.com/p/selenium/wiki/ChromeDriver
    def env = System.getenv()

    if (env.HTTP_PROXY) {
        Proxy proxy = new Proxy()
        URL url = new URL(env.HTTP_PROXY)
        proxy.setHttpProxy("${url.getHost()}:${url.getPort()}")
        proxy.setHttpProxy()
        proxy.setNoProxy(env.NO_PROXY)
    }

    chrome {
        // if (env.HTTP_PROXY) {
        // 	ChromeOptions options = new ChromeOptions();
        // 	options.setCapability("proxy", proxy);
        // 	driver = { new ChromeDriver(options) }
        // } else {
        DesiredCapabilities handlSSLErr = DesiredCapabilities.chrome()
        handlSSLErr.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true)

        driver = {
            ChromeOptions o = new ChromeOptions()
            o.addArguments('--ignore-certificate-errors')

            o.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            o.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
            new ChromeDriver(o)
        }
        // }
    }

    // run via “./gradlew chromeHeadlessTest”
    // See: http://code.google.com/p/selenium/wiki/ChromeDriver
    chromeHeadless {
        driver = {
            ChromeOptions o = new ChromeOptions()
            o.addArguments('headless', '--ignore-certificate-errors')
            o.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            o.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
            new ChromeDriver(o)
        }
    }

    // run via “./gradlew firefoxTest”
    // See: http://code.google.com/p/selenium/wiki/FirefoxDriver
    firefox {
        atCheckWaiting = 1

        driver = {
            // For testing in local we accept untrusted certificates
            def profile = new FirefoxProfile()
            profile.acceptUntrustedCertificates = true
            FirefoxOptions options = new FirefoxOptions().setProfile(profile)
            new FirefoxDriver(options);
        }
    }
    htmlUnit {
        HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.CHROME, true)
        if (env.HTTP_PROXY) {
            driver.setProxySettings(proxy);
        }

        return driver
    }
}

autoClearWebStorage = true
autoClearCookies = true


// Report configuration
reporter = new ScreenshotReporter()
reportsDir = new File(properties."config.reports.dir")
