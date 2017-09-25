package bg.teledoc.teledocapp.Callbacks;

/**
 * Created by alkon on 22-Sep-17.
 */

public interface ServerAPICallback {
    public void onResult(String result);
    public void onError(Object error);
}

