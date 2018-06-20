package handlers.xml.interfaces;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public interface IXMLHandler {
    /**
     * Поиск значения в XML документе по тегам/атрибутам_тегов
     * @param tagsPair - пара тегов в формате "ParentTag::ChildrenTag"
     *                 example 1:
     *                 <ParentTag>
     *                      <ChildrenTag>value</ChildrenTag>
     *                 </ParentTag>
     *                 return - "value"
     *
     *                 example 2:
     *                 <ParentTag ChildrenTag="value"/>
     *                 return - "value"
     * */
    String getParam(String tagsPair) throws IOException, XMLStreamException;
}
