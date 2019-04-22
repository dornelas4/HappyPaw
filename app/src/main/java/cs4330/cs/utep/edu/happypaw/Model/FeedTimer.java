package cs4330.cs.utep.edu.happypaw.Model;


public class FeedTimer {

    /** The end time of this timer in milliseconds. */
    private long endTime;

    /** Create a nesw timer. Initially it isn't running. */
    public FeedTimer(long endTime) {
        this.endTime = endTime;
    }

    /**
     * Is this timer running?
     *
     * @return true if this timer is running; false otherwise.
     */
    public boolean isRunning() {
        return System.currentTimeMillis() > endTime;
    }

    /**
     * Return the elapsed time since this timer has started; return 0 if this
     * timer is not running. The elapsed time is given in milliseconds.
     *
     * @return elapsed time in milliseconds; 0 if this timer is not running.
     */
    public long elapsedTime() {
        return isRunning() ? endTime - System.currentTimeMillis() : 0;
    }
}