package Game.Util;

import java.util.Map;

/**
 * This class contains methods to perform useful operations or actions used by multiple classes in the project
 */
public class Utils {
    /**
     * Returns the capital letter equivalent of a numeric coordinate
     * @param numericCoordinate Numeric coordinate
     * @return Capital letter coordinate, '?' if invalid argument
     */
    public static char getLetterCoordinate(final int numericCoordinate) {
        return numericCoordinate<0 || numericCoordinate>25 ? '?' : (char)('A' + numericCoordinate);
    }
}
