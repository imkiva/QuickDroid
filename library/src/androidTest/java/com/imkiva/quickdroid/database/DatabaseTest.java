package com.imkiva.quickdroid.database;

import android.support.test.runner.AndroidJUnit4;

import com.imkiva.quickdroid.QuickDatabase;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * @author kiva
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    @Test
    public void testCreate() {
        DatabaseOperator operator = QuickDatabase.open();
        try {
            operator.insert(new People("Tom", 17, false));
            operator.insert(new People("Lucy", 17, true));
        } catch (Exception ignore) {
        }
    }

    @Test
    public void testSelect() {
        DatabaseOperator operator = QuickDatabase.open();
        List<People> people = operator.selectAll(People.class);
        for (People p : people) {
            System.out.println("name:   " + p.name);
            System.out.println("age:    " + p.age);
            System.out.println("female: " + p.female);
        }
    }

    @Test
    public void testSelectWhere() {
        DatabaseOperator operator = QuickDatabase.open();
        People p = operator.selectPrimary(People.class, "Tom");
        Assert.assertNotNull(p);
        System.out.println("name:   " + p.name);
        System.out.println("age:    " + p.age);
        System.out.println("female: " + p.female);
    }

    @Test
    public void testSelectWhere1() {
        DatabaseOperator operator = QuickDatabase.open();
        List<People> people = operator.selectWhere(People.class, "age = {0}", 17);
        for (People p : people) {
            System.out.println("name:   " + p.name);
            System.out.println("age:    " + p.age);
            System.out.println("female: " + p.female);
        }
    }
}
