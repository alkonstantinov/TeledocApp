package bg.teledoc.teledocapp.Requests;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import bg.teledoc.teledocapp.MainActivity;

/**
 * Created by alkon on 18-Oct-17.
 */

public class uploadFileToServerTask extends AsyncTask<String, String, Object> {

    private String issueId;
    private byte[] bytes;

    public uploadFileToServerTask(byte[] bytes) {
        this.bytes = bytes;

    }

    public String readFullyAsString(InputStream inputStream, String encoding) throws IOException {
        return readFully(inputStream).toString(encoding);
    }

    private ByteArrayOutputStream readFully(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos;
    }

    @Override
    protected String doInBackground(String... args) {
        try {
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            @SuppressWarnings("PointlessArithmeticExpression")
            int maxBufferSize = 1 * 1024 * 1024;


            //java.net.URL url = new URL("http://18.194.18.118/uploadimage");
            java.net.URL url = new URL("http://10.0.2.2/uploadimage");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setChunkedStreamingMode(bytes.length);
            // Allow Inputs &amp; Outputs.
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            // Set HTTP method to POST.
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            FileInputStream fileInputStream;
            DataOutputStream outputStream;
            {
                outputStream = new DataOutputStream(connection.getOutputStream());

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                this.issueId = args[0];

                outputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + UUID.randomUUID().toString() + ".jpg\"" + lineEnd);
                outputStream.writeBytes(lineEnd);


                outputStream.write(bytes, 0, bytes.length);

                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            }
            outputStream.flush();
            outputStream.close();


            int serverResponseCode = connection.getResponseCode();
            String inp = readFullyAsString(connection.getInputStream(), "UTF-8");



            if (serverResponseCode == 200) {
                return inp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false";
    }

    @Override
    protected void onPostExecute(Object result) {
        JSONObject jo = new JSONObject();
        try {
            jo.put("room", "issue_" + issueId);
            jo.put("userid", MainActivity.getMain().getUserId());
            jo.put("name", MainActivity.getMain().getUserName());
            jo.put("issueId", issueId);
            jo.put("fnm", new JSONObject(result.toString()).getString("imageId"));
        } catch (JSONException ex) {

        }
        MainActivity.getMain().getSocket().emit("sendimage", jo);
    }
}