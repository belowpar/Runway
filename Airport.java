import java.util.Scanner;
import java.util.Random;
import java.io.*;

/**
 * Airport will simulate an airport with only one runway. It will have a queue 
 * of planes waiting to take off and a queue of planes waiting to land. Only one
 * plane can use the runway at a time. Every runway use will take same amount
 * of time based on input but a landing and take off can be different. Planes 
 * will arrive randomly and take off randomly, but each will have a probability.
 * Landings have higher priority than take offs. A clock will track the time 
 * passed in minutes.
 */

public class Airport
{
    // time it takes plan to land or take off
    private static int landTime = 0;
    private static int takeOffTime = 0;
    // probability of a plane arriving in each queue
    private static double landingProb = 0.0;
    private static double takeOffProb = 0.0;
    // time a plane can be ing landing queue without crashing
    private static int fuelTime = 0;
    // time to run simulation for
    private static int simulationTime = 0;
    public static void main(String[] args)
    throws IOException
    {
        inputData();
        runSimulation();

    }
    /**
     * prompts the user to input data for the class level fields
     * Postconditions: class level fields have valid inputs
     */
    private static void inputData()
    {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("How long do planes take to land?");
        do
        {
            landTime = keyboard.nextInt();
            keyboard.nextLine();
        } while(!isGreaterThanZero(landTime));
        System.out.println("How long do planes take to take off?");
         do
        {
            takeOffTime = keyboard.nextInt();
            keyboard.nextLine();
        } while(!isGreaterThanZero(takeOffTime));
        System.out.println("How likely is a plane arrive in the landing" + 
                        " queue?(0 to 1)");
        do
        {
            landingProb = keyboard.nextDouble();
            keyboard.nextLine();
            if (landingProb < 0 || landingProb > 1)
            {
                System.out.println("Please input a value between 0 and 1.");
            }
        } while(landingProb < 0 || landingProb > 1);
        System.out.println("How likely is a plane arrive in the take off" + 
                        " queue?(0 to 1)");
        do
        {
            takeOffProb = keyboard.nextDouble();
            keyboard.nextLine();
            if (takeOffProb < 0 || takeOffProb > 1)
            {
                System.out.println("Please input a value between 0 and 1.");
            }
        } while(takeOffProb < 0 || takeOffProb > 1);
        System.out.println("How long can a plane wait to land before running"
                        + " out of fuel?");
         do
        {
            fuelTime = keyboard.nextInt();
            keyboard.nextLine();
        } while(!isGreaterThanZero(fuelTime));
        System.out.println("How long should the simulation run?");
        do
        {
            simulationTime = keyboard.nextInt();
            keyboard.nextLine();
        } while(!isGreaterThanZero(simulationTime));
    }

    /**
     * Checks if the value is greater than 0
     * @param input     value to validate
     * @return          true if greater than 0, false if not
     */
    private static boolean isGreaterThanZero(int input)
    {
         if (input <= 0)
         {
            System.out.println("Please input a value greater than 0");
            return false;
         }
         else
         {
            return true;
         }
    }

    /**
     * Creates a loop that simulates the runway based on the given inputs using 
     * LinkedQueues, the Runway class, and the Airplane class.
     * Preconditions: inputData() has been called. landProb and takeOffProb are 
     * greater than 0 but less than 1. All parameters are positive values.
     * Postconditions: takeOffs, landings, crashes, totalTakeOffTime, and 
     * totalLAndTime are used to call outputData().
     * @throws IOException
     */
    private static void runSimulation()
    throws IOException
    {
        // queues to represent take offs and landings
        LinkedQueue<Airplane> landingQueue = new LinkedQueue<Airplane>();
        LinkedQueue<Airplane> takeOffQueue = new LinkedQueue<Airplane>();
        // to simulate the chance of a plane arriving in each queue
        Random rand = new Random();
        // for use while debugging
        File debugFile = new File("debugOutput.txt");
        FileWriter fileWriter = new FileWriter(debugFile);
        
        int takeOffs = 0;   // total successful take offs
        int landings = 0;   // total succesful landings
        int crashes = 0;    // total crashes
        int takeOffTotalTime = 0;   // total time spent in take off queue
        int landingTotalTime = 0;   // total time spent in landing queue
        int currentTime = 0;    // the current time of the simulation
        Runway myRunway = new Runway();     // represents the runway
        // begin while loop
        while (currentTime < simulationTime)
        {
            // represents the roll for each queue
            double landingChance, takeOffChance = 0.0;
            landingChance = rand.nextDouble();
            takeOffChance = rand.nextDouble();
            // for use while debugging
            String timeString = new String("Minute: " + currentTime);
            fileWriter.write("\n" + timeString + ", landing prob:" + 
                            landingChance);
            fileWriter.write("\n" + timeString + ", take off prob:" + 
                            takeOffChance);
            // check if a plane has left the runway, assuming it isnt empty
            if (currentTime != 0 && !(myRunway.isEmpty()))
            {
                myRunway.checkRunway(currentTime);
            }
            // check for new planes in landing queue
            if (landingChance < landingProb)
            {
                Airplane landingPlane = new Airplane(currentTime);
                landingQueue.enqueue(landingPlane);
            }
            // check for new planes in take off queue
            if (takeOffChance < takeOffProb)
            {
                Airplane takeOffPlane = new Airplane(currentTime);
                takeOffQueue.enqueue(takeOffPlane);
            }
            // checks if the runway is clear
            if (myRunway.isEmpty())
            {
                fileWriter.write("\n\t" + timeString + ", runway is clear");
                // checks for landing planes to give the runway
                if (!landingQueue.isEmpty())
                {
                    fileWriter.write("\n\tlanding queue is not empty");
                    // the current plane to check for landing
                    Airplane landingPlane = landingQueue.dequeue();
                    fileWriter.write("\n\tlanding is dequeued");
                    // finds a plane that hasn't crashed, or empties queue
                    while (landingPlane.hasCrashed(currentTime, fuelTime) && 
                            !landingQueue.isEmpty())
                    {
                        crashes++; 
                        fileWriter.write("\n\t\tcrash added: " + crashes);
                        fileWriter.write("\n\t\tArrival time of crasher:" + 
                                        landingPlane.getArrivalTime());
                        landingPlane = landingQueue.dequeue();
                        fileWriter.write("\n\tlanding is dequeued");
                    }
                    // if a plane is succesfully landing, adds data to totals
                    if (!landingPlane.hasCrashed(currentTime, fuelTime))
                    {
                        landingTotalTime += currentTime - 
                                            landingPlane.getArrivalTime();
                        landings++;
                        myRunway.addToRunway(landingPlane, currentTime, 
                                            landTime);
                        fileWriter.write("\n\t\ttime in L Q: " + 
                        (currentTime - landingPlane.getArrivalTime()));
                        fileWriter.write("\n\t\tL Q total: " + 
                                            landingTotalTime);
                    }

                }
                /*if runway is still empty after checking landing queue, adds 
                from take off queue if it isn't empty */ 
                if (!takeOffQueue.isEmpty() && myRunway.isEmpty())
                {
                    fileWriter.write("\n\ttakeoff queue is not empty");
                    // the plane to add to the runway
                    Airplane takeOffPlane = takeOffQueue.dequeue();
                    fileWriter.write("\n\ttake off is dequeued");
                    myRunway.addToRunway(takeOffPlane, currentTime, 
                                        takeOffTime);
                    // added the planes data to totals
                    takeOffs++;
                    takeOffTotalTime += currentTime - 
                                    takeOffPlane.getArrivalTime();
                    fileWriter.write("\n\t\ttime in TO Q: " + 
                                (currentTime - takeOffPlane.getArrivalTime()));
                    fileWriter.write("\n\t\tTO Q total: " + takeOffTotalTime);
                    fileWriter.write("\n\t\ttake offs: " + takeOffs);
                }

            }
            else
            {
                fileWriter.write("\n\t" + timeString + ", runway is not clear");
            }
            currentTime++;

        }
        // end while loop
        // check for additional crashes
        while (!landingQueue.isEmpty())
        {
            Airplane checkPlane = landingQueue.dequeue();
            if (checkPlane.hasCrashed(currentTime, fuelTime))
            {
                crashes++;
            }
        }
        // send data to user
        outputData(takeOffs, landings, crashes, takeOffTotalTime, 
                    landingTotalTime);
        fileWriter.close();
    }
    /**
     * Outputs the total number of crashes, landings, take offs, and the 
     * average time in each queue after the simulation.
     * Preconditions: the simulation has been run to completion
     * Postconditions: data has been output to the user.
     * @param takeOffs  total planes that took off sucessfully
     * @param landings  total planes that landed sucessfully
     * @param crashes   total planes that crashed in landing queue
     * @param takeOffTotalTime  total time planes spent in take off queue
     * @param landingTotalTime  total time planes spent in landing queue
     */
    private static void outputData(int takeOffs, int landings, int crashes,
                                    int takeOffTotalTime, int landingTotalTime)
    {
        System.out.println("Total Crashes: " + crashes);
        System.out.println("Total landings: " + landings);
        System.out.println("Total take offs: " + takeOffs);
        // check for divide by 0
        if (landings > 0)
        {
            System.out.println("average time in landing queue: " + 
                            (landingTotalTime / landings));
        }
        else
        {
            System.out.println("average time in landing queue: 0");
        }
        // check for divide by 0
        if (takeOffs > 0)
        {
            System.out.println("average time in take off queue: " + 
                                (takeOffTotalTime / takeOffs));
        }
        else
        {
            System.out.println("average time in take off queue: 0");
        }
    }

}