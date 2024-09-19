package ltu;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

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
    public PaymentImpl pImp;
    // System-under-test class
    
    
    @Before
    public void Sut() {
        mockCalendar = new TestCalendar();
        try {
            pImp = new PaymentImpl(mockCalendar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Age requirements

    @Test //1
    public void testAgeFullLoanInRange()
    {
        // Test the age requirements In Range adn On Border Range For:
        //[ID: 101] 
        //[ID: 103] 

        // Define the ages to be tested in range and on border range
        String[] ages = {"19700101-1111","19960101-1111","19960101-1111","19810101-1111","19860101-1111","19920101-1111"};
        
        // Check each age
        for (String age : ages) {
            assertEquals(LOAN_FULL + SUBSIDY_FULL, pImp.getMonthlyAmount(age, 0, 100, 51));
        }
    }

    @Test //2
    public void testAgeSubsidyInRange(){
        // Test the age requirements In Range and On Border Range For:
        //[ID: 102]
        //[ID: 103]

        String[] ages = {"19690101-1111","19660101-1111","19640101-1111","19620101-1111","19600101-1111"};

        for (String age : ages) {
            assertEquals(SUBSIDY_FULL, pImp.getMonthlyAmount(age, 0, 100, 51));
        }
    }

    @Test //3
    public void testAgeNoLoanOutofRange(){
        // Test the age requirements Out of Range For:
        //[ID: 101]
        //[ID: 102]
        //[ID: 103]

        String[] ages = {"20000101-1111","19990101-1111","19520101-1111","19510101-1111","19590101-1111"};

        for (String age : ages) {
            assertEquals(0, pImp.getMonthlyAmount(age, 0, 100, 51));
        }
    }

    // Study pace requirements

    @Test //4
    public void testStudyPaceInRange(){
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
    public void testStudyPaceOutOfRange(){
        // Test the study pace requirements Out of Range For:
        //[ID: 201]
        //[ID: 202]
        //[ID: 203]

        int[] studyRate = {0,20,30,40,49};

        for (int study : studyRate) {
            assertEquals(0, pImp.getMonthlyAmount("19960101-1111", 0, study, 51));
        }
    }
}
