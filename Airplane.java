/**
 * Represents an airplane in queue for landing or take off.
 */
public class Airplane 
{
    private int arrivalTime = 0;    // time the plane enters the queue
    
    /**
     * Constructor
     * @param currentTime   the time the plane enters the queue, stored in 
     * arrivalTime
     */
    public Airplane(int currentTime)
    {
        arrivalTime = currentTime;
    }
    /**
     * Getter method for arrivalTime
     * @return  arrivalTime
     */
    public int getArrivalTime()
    {
        return arrivalTime;
    }

    /**
     * Checks if the plane has crashed by adding the fuel time to the planes
     * arrival time and checking if that is less than the current time.
     * Preconditions: airplane was in landing queue
     * Postconditions: plane can be removed from queue and crashes can be 
     * incremented by 1
     * @param currentTime   the current simulation time
     * @param fuelTime      time the plane can be in queue before crashing
     * @return      true if the plane has crashed, else false
     */
    public boolean hasCrashed(int currentTime, int fuelTime)
    {
        if ((arrivalTime + fuelTime) < currentTime)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
