package Documents.forJson.interfaces;

import Documents.forJson.common.Type;

public interface IDocuments {
    long getId();
    String getNumber();
    String getStatusRUS();
    String getStatusENG();
    Type getType();
}
