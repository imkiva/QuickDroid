package com.imkiva.quickdroid.database;

import com.imkiva.quickdroid.database.statement.Statement;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author kiva
 */

public class StatementTest {
    @Test
    public void parseTableData() {
        TableMetaInfo tableMetaInfo = TableMetaInfo.get(Person.class);
        Assert.assertFalse(tableMetaInfo.hasDeclaredPrimaryKey);
    }

    @Test
    public void parseTableDataPrimaryKey() {
        TableMetaInfo tableMetaInfo = TableMetaInfo.get(PersonWithPrimaryKey.class);
        Assert.assertTrue(tableMetaInfo.hasDeclaredPrimaryKey);
        try {
            Assert.assertEquals(PersonWithPrimaryKey.class.getField("id"),
                    tableMetaInfo.primaryKeyField);
        } catch (NoSuchFieldException e) {
            assert false;
        }
    }

    @Test
    public void createTable() {
        TableMetaInfo tableMetaInfo = TableMetaInfo.get(Person.class);
        Statement statement = Statement.begin(tableMetaInfo)
                .createTable()
                .end();
        System.out.println(statement.getCode());
    }

    @Test(expected = SQLMalformedException.class)
    public void callDeleteInCreateTable() {
        TableMetaInfo tableMetaInfo = TableMetaInfo.get(Person.class);
        Statement statement = Statement.begin(tableMetaInfo)
                .createTable()
                .delete()
                .end();
        System.out.println(statement.getCode());
    }

    @Test(expected = SQLMalformedException.class)
    public void callWhereInCreateTable() {
        TableMetaInfo tableMetaInfo = TableMetaInfo.get(Person.class);
        Statement statement = Statement.begin(tableMetaInfo)
                .createTable()
                .where("1=1")
                .end();
        System.out.println(statement.getCode());
    }

    @Test
    public void callWhereInSelect() {
        TableMetaInfo tableMetaInfo = TableMetaInfo.get(Person.class);
        Statement statement = Statement.begin(tableMetaInfo)
                .select()
                .where("id = {0}, name = {1}", 1, "Bob")
                .end();
        System.out.println(statement.getCode());
    }
}
