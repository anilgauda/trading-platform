package ie.ncirl.tradingplatform.util;

import org.apache.commons.lang3.RandomStringUtils;

public class Util {

    /**
     * Generates a 8 character random number
     * @return
     */
    public static String generateRandomId() {
        return RandomStringUtils.randomNumeric(8);
    }
}
