package Common;

import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.*;

public class FileUtils {

    private static byte buf[] = new byte[10240];

    public FileUtils() {
    }

    public static String loadFile(String filename, String encoding) throws IOException {
        return loadFile(new File(filename), encoding);
    }

    public static String loadFile(File file, String encoding) throws IOException {
        InputStream in = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        do {
            int count = in.read(buf);
            if (count != -1) {
                out.write(buf, 0, count);
            } else {
                in.close();
                return out.toString(encoding);
            }
        } while (true);
    }

    public static byte[] loadFileData(File file) throws IOException {
        InputStream in = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        do {
            int count = in.read(buf);
            if (count != -1) {
                out.write(buf, 0, count);
            } else {
                in.close();
                return out.toByteArray();
            }
        } while (true);
    }

    public static void saveFile(String filename, Message message, boolean body) throws Exception {
        OutputStream out = new FileOutputStream(filename);

        if (body)
            if (message instanceof TextMessage) {
                String text = ((TextMessage) message).getText();
                out.write(text.getBytes((new String(text.getBytes("Windows-1251"), "Windows-1251")).equals(text) ? "Windows-1251" : "UTF-8"));
            } else {
                int numRead = 0;
                int totalRead = 0;
                while ((numRead = ((BytesMessage) message).readBytes(buf)) >= 0) {
                    out.write(buf, 0, numRead);
                    totalRead = totalRead + numRead;
                }
            }
        else
            out.write(message.toString().getBytes("Windows-1251"));
        out.close();
    }

    public static String normalizeDir(String dir) {
        dir = dir.replace('\\', '/');
        if (!dir.endsWith("/"))
            dir = (new StringBuilder()).append(dir).append("/").toString();
        return dir;
    }

    public static void createDirs(String dirs) {
        File file = new File(dirs);
        file.mkdirs();
    }

}
