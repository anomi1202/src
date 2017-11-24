package uspn.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class DocNum {
    final static Logger logger = LogManager.getLogger(Solution.class);
    private static ArrayList<String> docNumList;
    private static HashMap<String, String> docMap;

    public DocNum() {
        insertDocList();
    }

    private void insertDocList() {
        BufferedReader reader = null;
        try {
            Path docListFilePath = Paths.get("D:\\MyJavaProject\\upsn.test\\src\\main\\java\\uspn\\test\\docList.txt");
            logger.info("Read file documents number: {}", docListFilePath);
            docNumList = new ArrayList<>();
            docMap = new HashMap<>();

            reader = new BufferedReader(new FileReader(docListFilePath.toFile()));
            while (reader.ready()) {
                String[] lineText = reader.readLine().split("\\s");
                String docNum = lineText[0];
                String docDate = lineText[1];
                docMap.put(docNum, docDate);
                docNumList.add(docNum);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public ArrayList<String> getDocNumList() {
        return docNumList;
    }

    public static HashMap<String, String> getDocMap() {
        return docMap;
    }
}
