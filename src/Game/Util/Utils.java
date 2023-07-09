package Game.Util;

import java.util.ResourceBundle;

/**
 * This class contains methods to perform useful operations or actions used by multiple classes in the project
 */
public class Utils {
    /**
     * Returns the capital letter equivalent of a numeric coordinate
     *
     * @param numericCoordinate Numeric coordinate
     * @return Capital letter coordinate, '?' if invalid argument
     */
    public static char getLetterCoordinate(final int numericCoordinate) {
        return numericCoordinate < 0 || numericCoordinate > 25 ? '?' : (char) ('A' + numericCoordinate);
    }

    /**
     * Returns a localized string appropriate for the current locale
     * @param message Message type that will be displayed to the user
     * @return Localized message that will be displayed to the user
     */
    public static String getLocalizedString(final String message){
        ResourceBundle bundle = ResourceBundle.getBundle("Battleship");
        System.out.printf("[DEBUG] Retrieving message of type %s from %s locale%n", message, bundle.getLocale());
        return bundle.getString(message);
    }
}
