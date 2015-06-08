package com.yulius.belitungtourism;

public class Constans {

    public static enum MessageType {
        MESSAGE_SUCCESS,
        MESSAGE_WARNING,
        MESSAGE_ERROR
    }
    public static class Duration {
        public static final int VERY_SHORT = (1500);
        public static final int SHORT = (2000);
        public static final int MEDIUM = (2750);
        public static final int LONG = (3500);
        public static final int EXTRA_LONG = (30000);
        public static final int INFINITE = (1000000);
    }
    public static enum MessageScreenType {
        MESSAGE_SCREEN_LOADING,
        MESSAGE_SCREEN_NO_INTERNET_CONNECTION
    }
    public static final boolean IN_DEVELOPMENT = BuildConfig.DEBUG;
}
