// Source code is decompiled from a .class file using FernFlower decompiler.
package ltu;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class PaymentImplErrorCode implements IPayment {
   private static final String DEFAULT_RULES = "student100loan=7088\nstudent100subsidy=2816\nstudent50loan=4564\nstudent50subsidy=1396\nstudent0loan=0\nstudent0subsidy=0\nfulltimeIncome=85813\nparttimeIncome=128722\n";
   private final int FULL_LOAN;
   private final int HALF_LOAN;
   private final int ZERO_LOAN;
   private final int FULL_SUBSIDY;
   private final int HALF_SUBSIDY;
   private final int ZERO_SUBSIDY;
   private final int FULLTIME_INCOME;
   private final int PARTTIME_INCOME;
   private final ICalendar calendar;
   private final Properties props;
   private final boolean debug;

   public PaymentImplErrorCode(ICalendar var1) throws IOException {
      this(var1, "student100loan=7088\nstudent100subsidy=2816\nstudent50loan=4564\nstudent50subsidy=1396\nstudent0loan=0\nstudent0subsidy=0\nfulltimeIncome=85813\nparttimeIncome=128722\n");
   }

   public PaymentImplErrorCode(ICalendar var1, String var2) throws IOException {
      this.debug = true;
      this.calendar = var1;
      this.props = new Properties();

      try {
         this.props.load(new StringReader(var2));
      } catch (IOException var4) {
         var4.printStackTrace();
         throw var4;
      }

      this.FULL_LOAN = Integer.parseInt((String)this.props.get("student100loan"));
      this.HALF_LOAN = Integer.parseInt((String)this.props.get("student50loan"));
      this.ZERO_LOAN = Integer.parseInt((String)this.props.get("student0loan"));
      this.FULL_SUBSIDY = Integer.parseInt((String)this.props.get("student100subsidy"));
      this.HALF_SUBSIDY = Integer.parseInt((String)this.props.get("student50subsidy"));
      this.ZERO_SUBSIDY = Integer.parseInt((String)this.props.get("student0subsidy"));
      this.FULLTIME_INCOME = Integer.parseInt((String)this.props.get("fulltimeIncome"));
      this.PARTTIME_INCOME = Integer.parseInt((String)this.props.get("parttimeIncome"));
   }

   public String getNextPaymentDay() {
      SimpleDateFormat var1 = new SimpleDateFormat("yyyyMMdd");
      Calendar var2 = Calendar.getInstance();
      var2.setTime(this.calendar.getDate());
      if (var2.get(2) == 1) {
         var2.set(5, var2.getLeastMaximum(5));
      } else {
         var2.set(5, var2.getActualMaximum(5));
      }

      int var3 = var2.get(7);
      if (var3 == 1) {
         var2.add(5, -2);
      } else if (var3 == 7) {
         var2.add(5, -1);
      }

      return var1.format(var2.getTime());
   }

   public int getMonthlyAmount(String var1, int var2, int var3, int var4) throws IllegalArgumentException {
      if (var1 != null && var2 >= 0 && var3 >= 0 && var4 >= 0) {
         int var5 = this.getAge(var1);
         int var6 = this.getLoan(var5, var2, var3, var4);
         var6 += this.getSubsidy(var5, var2, var3, var4);
         return var6;
      } else {
         throw new IllegalArgumentException("Invalid input.");
      }
   }

   private int getAge(String var1) {
      if (var1 != null && var1.length() == 13) {
         int var2 = Integer.parseInt(var1.substring(0, 4));
         int var3 = Integer.parseInt((new SimpleDateFormat("yyyy")).format(this.calendar.getDate()));
         int var4 = var3 - var2;
         return var4;
      } else {
         throw new IllegalArgumentException("Invalid personId: " + var1);
      }
   }

   private int getLoan(int var1, int var2, int var3, int var4) {
      if (var3 >= 50 && var1 >= 20 && var1 <= 47 && var4 >= 50) {
         if (var3 >= 100 && var2 > this.FULLTIME_INCOME) {
            return this.ZERO_LOAN;
         } else if (var3 < 100 && var2 > this.PARTTIME_INCOME) {
            return this.ZERO_LOAN;
         } else {
            return var3 < 100 ? this.HALF_LOAN : this.FULL_LOAN;
         }
      } else {
         return this.ZERO_LOAN;
      }
   }

   private int getSubsidy(int var1, int var2, int var3, int var4) {
      if (var3 == 100 && var1 > 56 && var4 >= 50 && var2 <= this.FULLTIME_INCOME) {
         return Integer.MAX_VALUE;
      } else if (var3 >= 50 && var1 > 20 && var1 <= 56 && var4 >= 50) {
         if (var3 >= 100 && var2 > this.FULLTIME_INCOME) {
            return this.ZERO_SUBSIDY;
         } else if (var3 < 100 && var2 > this.PARTTIME_INCOME) {
            return this.HALF_SUBSIDY;
         } else {
            return var3 < 100 ? this.HALF_SUBSIDY : this.FULL_SUBSIDY;
         }
      } else {
         return this.ZERO_SUBSIDY;
      }
   }
}
