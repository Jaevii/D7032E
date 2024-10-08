package ltu;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.Calendar;

/* 
Age requirements
    [ID: 101] The student must be at least 20 years old to receive subsidiary and student loans.
    [ID: 102] The student may receive subsidiary until the year they turn 56.
    [ID: 103] The student may not receive any student loans from the year they turn 47.

Study pace requirements
    [ID: 201] The student must be studying at least half time to receive any subsidiary.
    [ID: 202] A student studying less than full time is entitled to 50% subsidiary.
    [ID: 203] A student studying full time is entitled to 100% subsidiary.

Income while studying requirements
    [ID: 301] A student who is studying full time or more is permitted to earn a maximum of 85 813SEK per year 
    in order to receive any subsidiary or student loans.
    [ID: 302] A student who is studying less than full time is allowed to earn a maximum of 128 722SEK per year 
    in order to receive any subsidiary or student loans.

Completion ratio requirement
    [ID: 401] A student must have completed at least 50% of previous studies in order to receive any subsidiary or student loans.

When and amount paid requirements
    Full time students are entitled to:
        [ID: 501] Student loan: 7088 SEK / month
        [ID: 502] Subsidiary: 2816 SEK / month

    Less than full time students are entitled to:
        [ID: 503] Student loan: 3564 SEK / month
        [ID: 504] Subsidiary: 1396 SEK / month

    [ID: 505] A person who is entitled to receive a student loan will always receive the full amount.
    [ID: 506] Student loans and subsidiary is paid on the last weekday (Monday to Friday) every month.

Your task is to run the system under test and verify that these requirements are correctly implemented. 
The implementation you have been given uses the current month to calculate payment date, but you have been 
asked to test the system for the spring-term of 2016 (2016-01-01 to 2016-06-30).
*/

public class PaymentTest
{

    public static final int LOAN_FULL = 7088;
    public static final int SUBSIDY_FULL = 2816;
    public static final int MAX_INCOME_FULL = 85813;

    public static final int LOAN_PART = 3564;
    public static final int SUBSIDY_PART = 1396;
    public static final int MAX_INCOME_PART = 128722;
    public TestCalendar mockCalendar;
    //public PaymentImpl pImp;
    public PaymentImplErrorCode pImp;

    
    // System-under-test class
    @Before
    public void Sut() {
        mockCalendar = new TestCalendar();
        try {
            //pImp = new PaymentImpl(mockCalendar);
            pImp = new PaymentImplErrorCode(mockCalendar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //#region Age requirements

    @Test //1
    public void personAged19()
    {
        String age = "19970101-1111";
        
        assertEquals(0, pImp.getMonthlyAmount(age, 0, 100, 51));
        
    }

    @Test //1.1
    public void personAged20()
    {
        String age = "19960101-1111";
        
        assertEquals(LOAN_FULL + SUBSIDY_FULL, pImp.getMonthlyAmount(age, 0, 100, 51));
        
    }

    @Test //1.2
    public void personAged46()
    {
        String age = "19700101-1111";
        
        assertEquals(LOAN_FULL + SUBSIDY_FULL, pImp.getMonthlyAmount(age, 0, 100, 51));
        
    }

    @Test //1.3
    public void personAged47()
    {
        String age = "19690101-1111";
        
        assertEquals(SUBSIDY_FULL, pImp.getMonthlyAmount(age, 0, 100, 51));
        
    }

    @Test //1.4
    public void personAged56()
    {
        String age = "19600101-1111";
        
        assertEquals(SUBSIDY_FULL, pImp.getMonthlyAmount(age, 0, 100, 51));
        
    }

    @Test //1.5
    public void personAged57()
    {
        String age = "19590101-1111";
        
        assertEquals(0, pImp.getMonthlyAmount(age, 0, 100, 51));
        
    }

    @Test //1.6
    public void personAged33()
    {
        String age = "19830101-1111";
        
        assertEquals(LOAN_FULL + SUBSIDY_FULL, pImp.getMonthlyAmount(age, 0, 100, 51));
        
    }

    @Test //2
    public void ageSubsidyInRange(){
        // Test the age requirements In Range and On Border Range For:
        //[ID: 102]
        //[ID: 103]

        String[] ages = {"19690101-1111","19660101-1111","19640101-1111","19620101-1111","19600101-1111"};

        for (String age : ages) {
            assertEquals(SUBSIDY_FULL, pImp.getMonthlyAmount(age, 0, 100, 51));
        }
    }

    @Test //3
    public void ageNoLoanOutofRange(){
        // Test the age requirements Out of Range For:
        //[ID: 101]
        //[ID: 102]
        //[ID: 103]

        String[] ages = {"20000101-1111","19990101-1111","19520101-1111","19510101-1111","19590101-1111"};

        for (String age : ages) {
            assertEquals(0, pImp.getMonthlyAmount(age, 0, 100, 51));
        }
    }

    //#endregion Age requirements

    //#region Study pace requirements

    @Test //4
    public void studyPaceInRange(){
        // Test the study pace requirements In Range and On Border Range For:
        //[ID: 201]
        //[ID: 202]
        //[ID: 203]

        int[] studyRate = {50, 51,67,70, 99};

        for (int study : studyRate) {
            assertEquals(LOAN_PART + SUBSIDY_PART, pImp.getMonthlyAmount("19960101-1111", 0, study, 51));
        }

        assertEquals(LOAN_FULL + SUBSIDY_FULL, pImp.getMonthlyAmount("19960101-1111", 0, 100, 51));
    }

    @Test //5
    public void studyPaceOutOfRange(){
        // Test the study pace requirements Out of Range For:
        //[ID: 201]
        //[ID: 202]
        //[ID: 203]

        int[] studyRate = {0,20,30,40,49};

        for (int study : studyRate) {
            assertEquals(0, pImp.getMonthlyAmount("19960101-1111", 0, study, 51));
        }
    }

    //#endregion Study pace requirements

    //#region Income requirements

    @Test //6
    public void incomeFullInRange(){
        // Test the income requirements In Range and On Border Range For:
        //[ID: 301]
        //[ID: 302]

        int[] income = {85813, 85812, 80000, 70000, 50000};

        for (int inc : income) {
            assertEquals(LOAN_FULL + SUBSIDY_FULL, pImp.getMonthlyAmount("19960101-1111", inc, 100, 51));
        }

        int[] income2 = {128722, 128721, 120000, 100000, 80000};
        for (int inc : income2) {
            assertEquals(LOAN_PART + SUBSIDY_PART, pImp.getMonthlyAmount("19960101-1111", inc, 85, 51));
        }
    }

    @Test //7
    public void incomeFullOutOfRange(){
        // Test the income requirements Out of Range For:
        //[ID: 301]
        //[ID: 302]

        int[] income = {85814, 90000, 100000, 200000, 300000};

        for (int inc : income) {
            assertEquals(0, pImp.getMonthlyAmount("19960101-1111", inc, 100, 51));
        }

        int[] income2 = {128723, 130000, 150000, 200000, 300000};
        for (int inc : income2) {
            assertEquals(0, pImp.getMonthlyAmount("19960101-1111", inc, 85, 51));
        }
    }

    //#endregion Income requirements

    //#region Competion requirements

    @Test //8
    public void completionRationInRange(){
        // Test the completion ratio requirements In Range and On Border Range For:
        //[ID: 401]

        int[] completion = {50, 51, 60, 70, 80, 90, 100};

        for (int comp : completion) {
            assertEquals(LOAN_FULL + SUBSIDY_FULL, pImp.getMonthlyAmount("19960101-1111", 0, 100, comp));
        }
    }

    @Test //9
    public void completionRationOutOfRange(){
        // Test the completion ratio requirements Out of Range For:
        //[ID: 401]

        int[] completion = {0, 10, 20, 30, 40, 49};

        for (int comp : completion) {
            assertEquals(0, pImp.getMonthlyAmount("19960101-1111", 0, 100, comp));
        }
    }

    //#endregion Competion requirements

    @Test //11
    public void paymentDate()
    {
        // Test the payment date requirements For:
        // [ID: 506] Student loans and subsidiary is paid on the last weekday (Monday to Friday) every month.

        Calendar cal = Calendar.getInstance();

        // Weekday (February)
        cal.set(2016, Calendar.FEBRUARY, 13);
        mockCalendar.setDate(cal.getTime());

        assertEquals("20160229", pImp.getNextPaymentDay());

        // Saturday (April)
        cal.set(2016, Calendar.APRIL, 13);
        mockCalendar.setDate(cal.getTime());

        assertEquals("20160429", pImp.getNextPaymentDay());

        // Sunday (January)
        cal.set(2016, Calendar.JANUARY, 13);
        mockCalendar.setDate(cal.getTime());

        assertEquals("20160129", pImp.getNextPaymentDay());
    }

    //#region Invalid input

    @Test //11
    public void invalidIDInputInRange(){
        // Test the invalid input requirements In Range and On Border Range For:
        
        String[] personID = {"119960101-1111","1990101-1111","19960101-1111 "," 19960101-1111","hejjag heter-1111","199601011111"};

        for (String id : personID) {
            try {
                pImp.getMonthlyAmount(id, 0, 100, 51);
            } catch (IllegalArgumentException e) {
                assertEquals("Invalid personId: "+id, e.getMessage());
                return; 
            }
            fail("Expected IllegalArgumentException was not thrown");
        }
    }


    @Test //12
    public void invalidStudyRateInput() {
        // Test invalid study rate inputs
        int[] invalidStudyRates = {-10, -1, -4, -40};

        for (int studyRate : invalidStudyRates) {
            try {
                pImp.getMonthlyAmount("19960101-1111", 0, studyRate, 51);
            } catch (IllegalArgumentException e) {
                assertEquals("Invalid input.", e.getMessage());
                continue; 
            }
            fail("Expected IllegalArgumentException was not thrown for study rate: " + studyRate);
        }
    }

    @Test //13
    public void invalidIncomeInput() {
        // Test invalid income inputs
        int[] invalidIncomes = {-1000, -1, -11};

        for (int income : invalidIncomes) {
            try {
                pImp.getMonthlyAmount("19960101-1111", income, 100, 51);
            } catch (IllegalArgumentException e) {
                
                assertEquals("Invalid input.", e.getMessage());
                continue; 
            }
            fail("Expected IllegalArgumentException was not thrown for income: " + income);
        }
    }

    @Test //14
    public void invalidCompletionRatioInput() {
        // Test invalid completion ratio inputs
        int[] invalidCompletionRatios = {-10, -1, -4, -50};

        for (int completionRatio : invalidCompletionRatios) {
            try {
                pImp.getMonthlyAmount("19960101-1111", 0, 100, completionRatio);
            } catch (IllegalArgumentException e) {
                
                assertEquals("Invalid input.", e.getMessage());
                continue; 
            }
            fail("Expected IllegalArgumentException was not thrown for completion ratio: " + completionRatio);
        }
    }

    @Test //15
    public void invalidIDInput() {
        // Test invalid ID inputs
        
        try {
            pImp.getMonthlyAmount(null, 0, 100, 51);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input.", e.getMessage());
            return; 
        }
        fail("Expected IllegalArgumentException was not thrown for date: " );
        
    }

    //#endregion Invalid input
}
