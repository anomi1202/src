package uspn.test.service;

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
