package com.imkiva.quickdroid.database;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author kiva
 */

public class StatementTest {
    @Test
    public void parseTableData() {
        TableData tableData = TableData.parse(Person.class);
        Assert.assertFalse(tableData.hasPrimaryKey);
    }

    @Test
    public void parseTableDataPrimaryKey() {
        TableData tableData = TableData.parse(PersonWithPrimaryKey.class);
        Assert.assertTrue(tableData.hasPrimaryKey);
        try {
            Assert.assertEquals(PersonWithPrimaryKey.class.getField("id"),
                    tableData.primaryKeyField);
        } catch (NoSuchFieldException e) {
            assert false;
        }
    }


}
