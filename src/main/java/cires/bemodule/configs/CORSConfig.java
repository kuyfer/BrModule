package cires.bemodule.configs;

public class CORSConfig {
    public static final String[] ALLOWED_ORIGINS = {"http://localhost:3000"};
    public static final String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
    public static final String[] ALLOWED_HEADERS = {"Content-Type", "Authorization"};
    public static final long MAX_AGE = 3600;
    public static final boolean ALLOW_CREDENTIALS = true;
    public static final String ALLOW_ORIGIN = "*";
    public static final String ALLOW_METHODS_ = "*";
    public static final String ALLOW_HEADERS_ = "*";
    public static final long MAX_AGE_ = 3600;
    public static final boolean ALLOW_CREDENTIALS_ = true;
    public static final String[] EXPOSE_HEADERS = {"Authorization"};
    public static final String[] ORIGIN = {"*"};
    public static final String[] METHODS = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
    public static final String[] HEADERS = {"Content-Type", "Authorization"};

}
