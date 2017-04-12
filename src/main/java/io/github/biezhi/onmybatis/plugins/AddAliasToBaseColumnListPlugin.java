package io.github.biezhi.onmybatis.plugins;


import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.ibatis2.sqlmap.elements.BaseColumnListElementGenerator;

import java.util.Iterator;
import java.util.List;

public class AddAliasToBaseColumnListPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document,
                                           IntrospectedTable introspectedTable) {

        BaseColumnListElementGenerator baseColumnListElementGenerator = new BaseColumnListElementGenerator();
        baseColumnListElementGenerator.setContext(getContext());
        baseColumnListElementGenerator.setIntrospectedTable(introspectedTable);
        baseColumnListElementGenerator.addElements(document.getRootElement());

        int lastIndex = document.getRootElement().getElements().size() - 1;
        Element element = document.getRootElement().getElements().get(lastIndex);
        Attribute attrAliased = null;
        Iterator<Attribute> attributes = ((XmlElement) element).getAttributes().iterator();
        while (attributes.hasNext()) {
            Attribute attribute = attributes.next();
            if (attribute.getName().equalsIgnoreCase("id")) {
                attrAliased = new Attribute(attribute.getName(), attribute.getValue());
                attributes.remove();
                break;
            }
        }
        if (attrAliased != null) {
            ((XmlElement) element).addAttribute(attrAliased);
        }
        return true;
    }
}