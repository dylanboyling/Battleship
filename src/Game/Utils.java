package Game;

import java.util.Map;

/**
 * This class contains methods to perform useful operations or actions used by multiple classes in the project
 */
public class Utils {

    /**
     * Maps integers to their letter equivalent
     */
    private final static Map<Integer, String> coordinates = Map.of(
            1, "A", 2, "B", 3, "C", 4, "D", 5, "E",
            6, "F", 7, "G", 8, "H", 9, "I", 10, "J");

    /**
     * Returns the letter equivalent of a numeric coordinate
     * @param numericCoordinate Numeric coordinate
     * @return Letter coordinate
     */
    public static String getLetterCoordinate(final Integer numericCoordinate){
        return coordinates.get(numericCoordinate);
    }
}
