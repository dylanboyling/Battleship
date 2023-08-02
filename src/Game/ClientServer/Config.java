package Game.ClientServer;

/**
 * This class defines various constants that is to be used by the Server and Client to standardize communication
 */
public class Config {
    /**
     * Character that is used to separate different types of data sent to the server
     */
    public static final String PROTOCOL_SEPARATOR = "#";

    /**
     * Character used to separate various fields in a protocol string
     */
    public static final String FIELD_SEPARATOR = ",";

    /**
     * Protocol identifier that signifies the client wants to terminate its connection with the server
     */
    public static final String PROTOCOL_END = "P0";

    /**
     * Protocol identifier that signifies the client wants to send their board configuration to the server
     */
    public static final String PROTOCOL_SEND_GAME = "P1";

    /**
     * Protocol identifier that signifies the client wants to terminate its connection with the server
     */
    public static final String PROTOCOL_RECEIVE_GAME = "P2";

    /**
     * Protocol identifier that signifies the client wishes to send their game data to the server
     */
    public static final String PROTOCOL_DATA = "P3";

    /**
     * Default username for the client
     */
    public static String DEFAULT_USER = "User1";

    /**
     * Default server address to connect to
     */
    public static String DEFAULT_ADDRESS = "localhost";

    /**
     * Default server port to connect to
     */
    public static int DEFAULT_PORT = 12345;
}
