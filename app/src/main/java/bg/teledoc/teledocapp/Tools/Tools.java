package bg.teledoc.teledocapp.Tools;



/**
 * Created by alkon on 25-Sep-17.
 */

public class Tools {

    public static String FormatPGDateTime(String pgDateTime) {
        return pgDateTime.substring(8, 10) + "." +pgDateTime.substring(5, 7) + "." + pgDateTime.substring(0, 4)
                + " " + pgDateTime.substring(11, 16);
    }
}
