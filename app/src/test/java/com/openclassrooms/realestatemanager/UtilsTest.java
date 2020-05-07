package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.tools.Utils;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UtilsTest {
    private final static int MONTANT_EURO1 = 48233;
    private final static int MONTANT_DOLLARS1 = 59400;

    private final static int MONTANT_EURO2 = 48720;
    private final static int MONTANT_DOLLARS2 = 60000;

    @Test
    public void testConvertDollarToEuro(){
        assertEquals(Utils.convertDollarToEuro(MONTANT_DOLLARS1), MONTANT_EURO1);
        assertEquals(Utils.convertDollarToEuro(MONTANT_DOLLARS2), MONTANT_EURO2);

        assertNotEquals(Utils.convertDollarToEuro(MONTANT_DOLLARS1), MONTANT_DOLLARS1);
        assertNotEquals(Utils.convertDollarToEuro(MONTANT_DOLLARS2), MONTANT_DOLLARS2);
    }

    @Test
    public void testConvertEuroToDollars(){
        assertEquals(Utils.convertEuroToDollars(MONTANT_EURO1), MONTANT_DOLLARS1);
        assertEquals(Utils.convertEuroToDollars(MONTANT_EURO2), MONTANT_DOLLARS2);

        assertNotEquals(Utils.convertEuroToDollars(MONTANT_EURO1), MONTANT_EURO1);
        assertNotEquals(Utils.convertEuroToDollars(MONTANT_EURO2), MONTANT_EURO1);
    }

    @Test
    public void tesGetTodayDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar dateFromNow = Calendar.getInstance(Locale.FRANCE);
        assertEquals(sdf.format(dateFromNow.getTime()), Utils.getTodayDate());

        assertNotEquals("12/03/1998", Utils.getTodayDate());
        assertNotEquals("23/06/2010", Utils.getTodayDate());

        assertNotEquals(null, Utils.getTodayDate());
    }


}