package vinid.api.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MultipartUtility {

    private String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;

    public MultipartUtility(String requestURL, String charset) throws IOException {
        this.charset = charset;
        this.boundary = "===" + System.currentTimeMillis() + "===";
        URL url = new URL(requestURL);
        this.httpConn = (HttpURLConnection)url.openConnection();
        this.httpConn.setUseCaches(false);
        this.httpConn.setDoOutput(true);
        this.httpConn.setRequestMethod("POST");
        this.httpConn.setDoInput(true);
        this.httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + this.boundary);
        this.httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
        this.httpConn.setRequestProperty("Test", "Bonjour");
        this.outputStream = this.httpConn.getOutputStream();
        this.writer = new PrintWriter(new OutputStreamWriter(this.outputStream, charset), true);
    }

    public void addFormField(String name, String value) {
        this.writer.append("--" + this.boundary).append("\r\n").append("Content-Disposition: form-data; name=\"" + name + "\"").append("\r\n").append("Content-Type: text/plain; charset=" + this.charset).append("\r\n").append("\r\n").append(value).append("\r\n").flush();
    }

    public void addFilePart(String fieldName, File uploadFile) throws IOException {
        String fileName = uploadFile.getName();
        this.writer.append("--" + this.boundary).append("\r\n").append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"").append("\r\n").append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append("\r\n").append("Content-Transfer-Encoding: binary").append("\r\n").append("\r\n").flush();
        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
//        int bytesRead = true;

        int bytesRead;
        while((bytesRead = inputStream.read(buffer)) != -1) {
            this.outputStream.write(buffer, 0, bytesRead);
        }

        this.outputStream.flush();
        inputStream.close();
        this.writer.append("\r\n");
        this.writer.flush();
    }

    public void addHeaderField(String name, String value) {
        this.writer.append(name + ": " + value).append("\r\n").flush();
    }

    public List<String> finish() throws IOException {
        List<String> response = new ArrayList();
        this.writer.append("\r\n").flush();
        this.writer.append("--" + this.boundary + "--").append("\r\n");
        this.writer.close();
        int status = this.httpConn.getResponseCode();
        if (status != 200) {
            throw new IOException("Server returned non-OK status: " + status);
        } else {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.httpConn.getInputStream()));
            String line = null;

            while((line = reader.readLine()) != null) {
                response.add(line);
            }

            reader.close();
            this.httpConn.disconnect();
            return response;
        }
    }

    public String Print_FormData() {
        StringBuilder result = new StringBuilder();
        result.getClass().getConstructors();
        return result.toString();
    }
}
