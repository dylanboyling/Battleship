package Game.Util;

import java.util.Locale;

/**
 * This class constants shared Constants that are used in multiple classes or packages
 */
public interface Constants {
    /**
     * Initial dimension of one side of the square grid
     */
    int DEFAULT_GRID_DIMENSION = 10;

    Locale[] supportedLocales = {new Locale("en", "CA"), new Locale("fr", "CA")};
}
