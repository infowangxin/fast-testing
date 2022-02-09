package com.nutcracker.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

/**
 * @author 胡桃夹子
 * @date 2021-11-19 10:07
 */
public class DateUtilTest {

    private static final Logger LOG = LoggerFactory.getLogger(DateUtilTest.class);

    @Test
    public void format() {
        try {
            Date date = DateUtil.parse(Calendar.getInstance().getTime());
            System.out.println(DateUtil.dateToString(date,DateUtil.fm_yyyy_MM_dd_HHmmss));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
