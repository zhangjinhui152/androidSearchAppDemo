package com.example.tedy;

import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO NewTable (id, pakName, name, firstName) VALUES(");
        sql.append("randomNanoId");
        sql.append(",");
        sql.append("pakName");
        sql.append(",");
        sql.append("apkName");
        sql.append(",");
        sql.append("firstName");
        sql.append(");");
        System.out.println(sql);
        assertEquals(4, 2 + 2);
    }
}