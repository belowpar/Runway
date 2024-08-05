/**
 * Runway represents a single lane runway. It holds a plane object when the 
 * runway is in use, and null when it is not.
 */
public class Runway 
{
    private Airplane runwayPlane;   // plane currently on runway, null if empty
    private int runwayArrivalTime;  // time plane arrived on runway
    private int timeNeeded;     // time plane needs to leave runway

    /**
     * Constructor. Sets up runway as empty and initializes times.
     */
    public Runway()
    {
        runwayPlane = null;
        timeNeeded = 0;
        runwayArrivalTime = 0;
    }

    /**
     * Checks if the runway is empty by checking if runwayPlane is null.
     * @return true if runwayPlane is null and runway is empty, false otherwise
     */
    public boolean isEmpty()
    {
        if (runwayPlane == null)
        {
            return true;
        }
        else 
        {
            return false;
        }
    }

    /**
     * Adds a plane to the runway
     * Preconditions: Runway is empty
     * Postconditions: runwayPlane, runwayArrivalTime, timeNeeded are updated 
     * to new values
     * @param thisPlane plane store in runwayPlane
     * @param currentTime  time to store in runwayArrivalTime 
     * @param runwayTime    time to store in timeNeeded
     */
    public void addToRunway(Airplane thisPlane, int currentTime, int runwayTime)
    {
        runwayPlane = thisPlane;
        runwayArrivalTime = currentTime;
        timeNeeded = runwayTime;
    }
    /**
     * Sets runwayPlane to null.
     * precondition: checkRunway() has been called
     * postcondition: isEmpty() now true, runwayPlane is null
     */
    private void removeRunway()
    {
        runwayPlane = null;
    }

    /**
     * Checks if the plane has left the runway by subtracting the arrival time 
     * from the provided current time. If the result is equal to timeNeeded,
     * removeRunway() is called.
     * precondition: the runway isn’t empty
     * postcondition: Runway is now empty, or none
     * @param currentTime
     */
    public void checkRunway(int currentTime)
    {
        if ((currentTime - runwayArrivalTime) == timeNeeded)
        {
            this.removeRunway();
        }
    }


}
