package run;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import ladders.Ladder;
import monkeys.Monkey;
import monkeys.Monkey.Direction;
import monkeys.MonkeyGenerator;
import strategy.StrategyOne;
import visual.Cartoon;


/**
 * The main method will be executed in this class and its fields are all public and static, because
 * we want all the class can have access to this fields ( the list of Ladders and the logger).
 */
public class Client extends Thread {
  public static Logger logger = Logger.getLogger(Client.class);
  public static List<Ladder> manyLadders = Collections.synchronizedList(new ArrayList<Ladder>());
  public static List<Monkey> monkeys = Collections.synchronizedList(new ArrayList<Monkey>());
  public static CountDownLatch downLatch;
  public static int totalNumber;
  private int timeConsumption;
  private Map<Integer, List<Monkey>> startMonkeys = new TreeMap<Integer, List<Monkey>>();
  public static void main(String[] args) {
    new Thread(new Client()).start();

  }

  @Override
  public void run() {
    manyLadders.clear();
    String encoding = "UTF-8";
    try {
      int timeMax = Integer.MIN_VALUE;
      File file = new File("src/files/Competition_3.txt");
      if (file.isFile() && file.exists()) {
        InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
        BufferedReader bufferedReader = new BufferedReader(read);
        String value = null;
        int n = 0;
        int N = 0; // the total number of
        long startTime = System.currentTimeMillis();
        String nRegex = "n=(.*)";
        String monkeyPattern = "monkey=<(.*),(.*),(.*),(.*)>";
        Pattern pMonkey = Pattern.compile(monkeyPattern);
        Pattern nPattern = Pattern.compile(nRegex);
        new Thread(new Cartoon()).start();
        while ((value = bufferedReader.readLine()) != null) {
          Matcher mn = pMonkey.matcher(value);
          Matcher nladderMatcher = nPattern.matcher(value);
          while (nladderMatcher.find()) {
            System.out.println(nladderMatcher.group(0));
            n = Integer.valueOf(nladderMatcher.group(1));
            for (int i = 1; i <= n; i++) {
              manyLadders.add(new Ladder(i));
            }
          }
          
          while (mn.find()) {
            N++;
            int sleepTime = Integer.valueOf(mn.group(1));
            Direction direction = null;
            if (mn.group(3).equals("L->R")) {
              direction = Direction.LtoR;
            } else {
              direction = direction.RtoL;
            }
            int ID = Integer.valueOf(mn.group(2));
            timeMax = Integer.max(sleepTime, timeMax);
            int velocity = Integer.valueOf(mn.group(4));
            Monkey iMonkey = new Monkey(ID, direction, velocity);
            monkeys.add(iMonkey);
            if (!startMonkeys.containsKey(sleepTime)) {
              List<Monkey> thisList = new ArrayList<Monkey>();
              thisList.add(iMonkey);
              startMonkeys.put(sleepTime, thisList);
            }else {
              startMonkeys.get(sleepTime).add(iMonkey);
            }
          }
        }
        downLatch = new CountDownLatch(N);
        int time1 = 0;
        for (int time = 0; time <= timeMax; time++) {
          
          if(startMonkeys.containsKey(time)) {
            System.out.println(time);
            try {
              Thread.sleep((time-time1)*1000);
            } catch (Exception e) {
              e.printStackTrace();
            }
            for(Monkey monkey : startMonkeys.get(time)) {
              new Thread(monkey).start();
            }
            time1 = time;
          }
        }
        
        /* Produce many monkeys */
        totalNumber = N;         
        
           
        System.out.println("All the monkeys are there. Please wait for them!");
        bufferedReader.close();
        try {
          downLatch.await();
        } catch (Exception e) {
        }

        long endTime = System.currentTimeMillis();
        timeConsumption = (int) ((endTime - startTime) / 1000);
        logger.info("All cross the river.");
        logger.info("Time Consumption " + (endTime - startTime) / 1000 + "s");
        System.out.println(
            "All the monkeys cross the river successfully. \nTime consumption :" + timeConsumption);

        System.out.println("###########");
        System.out.println("Please read the log for details. log -> src/log/info.log");
        System.out.println("-----------");
        System.out.println("Total number of the monkeys " + N);
        System.out.println("-----------");
        System.out.println("There are " + manyLadders.size() + " ladders");
        System.out.println("-----------");
        System.out.println("Their information are as follows:");
        for (Monkey iMonkey : monkeys) {
          System.out.println(iMonkey.toString());
        }
        System.out.println("###########");
        System.out.println("FOR OTHER DETAILS:");
        System.out.println("-----------");
        /* Throughput */
        logger.info("-----------");
        double Th = calculateTh(N, timeConsumption);
        System.out.println("Throughput: " + Th);
        System.out.println("-----------");
        logger.info("Throughput: " + Th);
        logger.info("-----------");
        /* Fairness */
        double f = calculateFairness();
        System.out.println("Fairness index: " + f);
        System.out.println("-----------");
        logger.info("-----------");
        logger.info("Fairness index: " + f);
      }
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  /**
   * Calculate the fairness of the whole crossing-river process of monkeys. If there are two
   * monkeys, m1 and m2. We call it fair if m1 is born earlier than m2 and cross the river
   * earlier(or m2 is born earlier and cross the river earlier). There are N monkeys, we calculate
   * the fairness between each two monkeys. If it's fair, the total number + 1, otherwise -1. And
   * the fairness = total number / (n * (n - 1) / 2 ).
   * 
   * @return fairness among the monkeys.
   */
  private double calculateFairness() {
    double fairness = 0;
    double sum = 0;
    for (int i = 0; i < monkeys.size() - 1; i++) {
      Monkey m1 = monkeys.get(i);
      for (int j = i + 1; j < monkeys.size(); j++) {
        Monkey m2 = monkeys.get(j);
        if ((m1.getBornTime() - m2.getBornTime()) * (m1.getEndTime() - m2.getEndTime()) >= 0) {
          sum++;
        } else {
          sum--;
        }
      }
    }
    int n = monkeys.size();
    fairness = sum / (n * (n - 1) / 2);
    BigDecimal b = new BigDecimal(fairness);
    fairness = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
    assert (fairness >= -1) && (fairness <= 1); // the value should be in the interval [-1,1]
    return fairness;
  }

  /**
   * Calculate the Throughput.
   * 
   * @param N - the total number of the monkeys.
   * @param endTime - the time consumption, from the first monkey born to the last monkey which
   *        cross the river.
   * @return Throuput ( N / timeConsumption) - a double number (3 digits preserved).
   */
  private double calculateTh(int N, long timeConsumption) {
    /* Calculate the Throughput. */
    double monkeyNumber = N;
    double totalNumber = timeConsumption;
    double Th = monkeyNumber / totalNumber;
    BigDecimal b = new BigDecimal(Th);
    Th = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
    return Th;
  }

}


