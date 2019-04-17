package cs4330.cs.utep.edu.happypaw;


public class TimerModel {

    /** The start time of this timer in milliseconds. */
    private long startTime;

    /** Create a new timer. Initially it isn't running. */
    public TimerModel() {
        startTime = 0;
    }

    /**
     * Start this timer. If invoked when a timer is already running,
     * this method will restart it.
     */
    public void start() {
        startTime = System.currentTimeMillis();
    }

    /** Stop this timer. */
    public void stop() {
        startTime = 0;
    }

    /**
     * Is this timer running?
     *
     * @return true if this timer is running; false otherwise.
     */
    public boolean isRunning() {
        return startTime != 0;
    }

    /**
     * Return the elapsed time since this timer has started; return 0 if this
     * timer is not running. The elapsed time is given in milliseconds.
     *
     * @return elapsed time in milliseconds; 0 if this timer is not running.
     */
    public long elapsedTime() {
        return isRunning() ? System.currentTimeMillis() - startTime : 0;
    }
}