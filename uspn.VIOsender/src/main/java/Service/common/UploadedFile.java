package Service.common;

public class UploadedFile {
    private final int id;
    private final String name;
    private final String path;
    private final String mimeType;
    private final String uploadDate;

    public UploadedFile(int id, String name, String path, String mimeType, String uploadDate) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.mimeType = mimeType;
        this.uploadDate = uploadDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUploadDate() {
        return uploadDate;
    }


}
