package util;


import java.io.Serializable;

public final class R implements Serializable{

    public static final class PageAddresses {

        public final static String TEMP_PAGE = "/financial/professor/MyAccount.xhtml";
        //public final static String START_PAGE = "/financial/basic/PricingAndGreeks.xhtml";
        public final static String ADMIN = "/financial/admin/Registration.xhtml";
        public final static String PROF_START_PAGE = "/financial/professor/Trading.xhtml";
        public final static String STUDENT_START_PAGE = "/financial/student/MyAccount.xhtml";

        public final static String BASIC = "/financial/basic/";
        public final static String OTHERS = "/financial/others/";
        public final static String SIGN = "/financial/sign/";

        public final static String HOME_PAGE = "/financial/homepage/index.jsp";
        public final static String ERROR_PAGE = "/errors/error.jsp";
        public final static String INDEX = "/index.xhtml";

        public final static String FORMATED_BASIC = "/financial/basic/%s.xhtml";
        public final static String FORMATED_OTHERS = "/financial/%s/%s.xhtml";
        public final static String FORMATED_PROFESSOR = "/financial/%s/%s.xhtml";
        public final static String FORMATED_SIGN = "/financial/sign/%s.xhtml";

        public final static String XHTML_EXTENTION = ".xhtml";
    }

    public static final class PublicText {
        public static final String EMPTY = "";
        public static final String SPACE = " ";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String FILLED = "filled";
        public static final String UNKNOWN = "unknown";
        public static final String CANCELED = "canceled";
        public static final String BUY = "buy";
        public static final String SELL = "sell";
        public static final String STUDENT = "student";
        public static final String PROFESSOR = "professor";
        public static final String ADMIN = "admin";
        public static final String NO_ROLE_NAME = "NoRoleName";


    }

    public static final class Messages {

        public static final class Error {
            public static final String ERROR = "Error:";
            public static final String ALREADY_AUTHENTICATED = "This request has already been authenticated ...";
            public static final String ALREADY_REGISTERED = ", Already has been registered ...";
            public static final String INVALID_EXCEL_FILE_DATA = "Invalid Excel file data for (x) or (y) axis ...";
            public static final String INVALID_EXCEL_FILE_FORMAT = "Invalid Excel file format ...";
            public static final String INVALID_TIME_DISTANCE = "Please select a valid time distance ...";
            public static final String INVALID_PASS_COMPARISON = "Password and confirmation is not same ...";

            public static final String PURCHASE_DENIED = "Your request to purchase is denied due to insufficient funds in your account ...";
            public static final String SELL_DENIED = "Your request to sell is declined as you don’t own enough shares of this company for this transaction ...";
        }

        public static final class Warning {
            public static final String WARN = "Warning:";
            public static final String BUY_REQUEST_DENIED = "Your request to purchase is denied as you have reached the stock purchase limit for this company";
            public static final String DELETE_USER_DENIED = "Removing online user is denied ...";
            public static final String RESET_USER_DENIED = "Resetting online user is denied ...";
            public static final String CHANGE_PASS_DENIED = "Changing the password for online user is denied ...";
        }

        public static final class Information {
            public static final String INFO = "INFO";
            public static final String SUMMARY = "Information:";
            public static final String THE_END = "The End:";
            public static final String BUY_REQUEST_ACCEPTED = "Your purchase request has been accepted ...";
            public static final String SELL_REQUEST_ACCEPTED = "Your sale request has been accepted ...";
            public static final String ALL_POINTS_SHOWED = "All points are showed ...";
            public static final String ALL_GRAPHS_SHOWED = "All Graph are showed ...";
            public static final String UPLOAD_SUCCESSFULLY = " is uploaded successfully ...";
            public static final String USER_DEL_SUCCESSFULLY = "User Deleted successfully ...";
            public static final String USER_RESET_SUCCESSFULLY = "User reset action is over successfully ...";
            public static final String ALL_USER_RESET_SUCCESSFULLY = "All users reset successfully ...";
            public static final String PASS_CHANGE_SUCCESSFULLY = "Password changed successfully ...";
            public static final String USER_NOT_EXIT = "User not exist or not a valid user (Professor or Admin) ...";
            public static final String USER_NOT_EXIT_OR_NOT_ADMIN = "User not exist or is not a admin ...";
        }
    }

    public final class ActionNames {
        public static final String LOG_IN = "Login";
        public static final String LOG_OUT = "Logout";
        public static final String BUY_STOCK = "Buying stock";
        public static final String SELL_STOCK = "Selling stock";
    }

}
