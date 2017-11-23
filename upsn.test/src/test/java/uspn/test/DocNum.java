package uspn.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class DocNum {
    private static ArrayList<String> docNumList;
    private static HashMap<String, String> docMap;

    public DocNum() {
        insertDocList();
    }

    private void insertDocList() {
        Path docListFilePath = Paths.get("D:\\MyJavaProject\\upsn.test\\src\\test\\java\\uspn\\test\\docList.txt");
        docNumList = new ArrayList<>();
        docMap = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(docListFilePath.toFile()));
            while (reader.ready()) {
                String[] lineText = reader.readLine().split("\\s");
                String docNum = lineText[0];
                String docDate = lineText[1];
                docMap.put(docNum, docDate);
                docNumList.add(docNum);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getDocNumList() {
        return docNumList;
    }

    public static HashMap<String, String> getDocMap() {
        return docMap;
    }
}
