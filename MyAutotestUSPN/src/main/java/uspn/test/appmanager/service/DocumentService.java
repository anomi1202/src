package uspn.test.appmanager.service;

import java.io.File;

public interface DocumentService {
    /**
     * �������� ����� � ���-��������
     *
     * @param file ����������� ����
     * @return ������������� ������������ �����
     */
    String upload(File file) throws Exception;
}
