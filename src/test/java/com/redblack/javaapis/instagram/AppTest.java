package com.redblack.javaapis.instagram;

import junit.framework.*;

import org.slf4j.*;

import com.redblack.javaapis.instagram.beans.*;
import com.redblack.javaapis.instagram.operations.Instagram;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    private static final Logger logger = LoggerFactory.getLogger(TokenRefreshingClientHttpRequestFactory.class);

    /**
     * Create the test case
     *
     * @param testName
     *            name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }

    public boolean validateInstagram(String userid, String token) {
        try {
            InstagramFactory factory = new InstagramFactory("04f3d55a7aa2440093af4095b1930274", "2265456ede0e45fb876cec7970c08ecb");
            Instagram ig = factory.getApi(token);
            InstagramUser igu = ig.getPeopleOperations().getInstagramUser(userid, token);
            logger.info(igu.toString());
            return true;
        } catch (Exception e) {
            return true;
        }
    }
}
