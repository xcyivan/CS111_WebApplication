package edu.ucla.cs.cs144;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;

public class MyComparator implements Comparator<Bid>{
   public MyComparator() {}

   @Override
   public int compare(Bid bid1, Bid bid2) {
      SimpleDateFormat format = new SimpleDateFormat("MMM-dd-yy kk:mm:ss");
      try {
         Date date1 = format.parse(bid1.time);
         Date date2 = format.parse(bid2.time);
         return date1.compareTo(date2);
      } catch (ParseException e) {
         e.printStackTrace();
      }
      return 0;
   }
}