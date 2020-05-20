import geb.report.ScreenshotReporter
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.Proxy

import util.SpecHelper

def properties = new SpecHelper().getApplicationProperties()

// Define timing to get information from a page
waiting {
	timeout = 25
	retryInterval = 0.5
	includeCauseInMessage = true
	presets {
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
	// run via “./gradlew chromeTest”
	// See: http://code.google.com/p/selenium/wiki/ChromeDriver
	def env = System.getenv()
	System.setProperty("webdriver.chrome.driver", "c:\\opt\\chromedriver\\chromedriver.exe");

	if (env.HTTP_PROXY) {
		Proxy proxy = new Proxy();
		URL url = new URL(env.HTTP_PROXY);
		proxy.setHttpProxy("${url.getHost()}:${url.getPort()}");
		proxy.setHttpProxy()
		proxy.setNoProxy(env.NO_PROXY)
	}

	chrome {
		//if (env.HTTP_PROXY) {
		//	ChromeOptions options = new ChromeOptions();
		//	options.setCapability("proxy", proxy);
		//	driver = { new ChromeDriver(options) }
		//} else {
			driver = { new ChromeDriver() }
		//}
	}

	// run via “./gradlew chromeHeadlessTest”
	// See: http://code.google.com/p/selenium/wiki/ChromeDriver
	chromeHeadless {
		driver = {
			ChromeOptions o = new ChromeOptions()
			o.addArguments('headless')
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
reporter =  new ScreenshotReporter()
reportsDir = new File(properties."config.reports.dir")
