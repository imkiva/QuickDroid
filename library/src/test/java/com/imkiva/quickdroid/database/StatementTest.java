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
        TableData tableData = TableData.get(Person.class);
        Assert.assertFalse(tableData.hasDeclaredPrimaryKey);
    }

    @Test
    public void parseTableDataPrimaryKey() {
        TableData tableData = TableData.get(PersonWithPrimaryKey.class);
        Assert.assertTrue(tableData.hasDeclaredPrimaryKey);
        try {
            Assert.assertEquals(PersonWithPrimaryKey.class.getField("id"),
                    tableData.primaryKeyField);
        } catch (NoSuchFieldException e) {
            assert false;
        }
    }

    @Test
    public void createTable() {
        TableData tableData = TableData.get(Person.class);
        Statement statement = Statement.begin(tableData)
                .createTable()
                .end();
        System.out.println(statement.getCode());
    }

    @Test(expected = DatabaseMalformedException.class)
    public void callDeleteInCreateTable() {
        TableData tableData = TableData.get(Person.class);
        Statement statement = Statement.begin(tableData)
                .createTable()
                .delete()
                .end();
        System.out.println(statement.getCode());
    }

    @Test(expected = DatabaseMalformedException.class)
    public void callWhereInCreateTable() {
        TableData tableData = TableData.get(Person.class);
        Statement statement = Statement.begin(tableData)
                .createTable()
                .where("1=1")
                .end();
        System.out.println(statement.getCode());
    }

    @Test
    public void callWhereInSelect() {
        TableData tableData = TableData.get(Person.class);
        Statement statement = Statement.begin(tableData)
                .select()
                .where("id = {0}, name = {1}", 1, "Bob")
                .end();
        System.out.println(statement.getCode());
    }
}
