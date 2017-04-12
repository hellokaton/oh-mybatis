package io.github.biezhi.onmybatis.utils;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.internal.DefaultCommentGenerator;

public class QnloftCommentGenerator extends DefaultCommentGenerator {

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        // 添加字段注释
        StringBuffer sb = new StringBuffer();
        field.addJavaDocLine("/**");
        field.addJavaDocLine(" * <pre>");
        if (introspectedColumn.getRemarks() != null)
            field.addJavaDocLine(" * " + introspectedColumn.getRemarks());
        sb.append(" * 表字段 : ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        sb.append('.');
        sb.append(introspectedColumn.getActualColumnName());
        field.addJavaDocLine(sb.toString());
        field.addJavaDocLine(" * </pre>");
        field.addJavaDocLine(" * ");
        // addJavadocTag(field, false);
        field.addJavaDocLine(" */");
    }

}