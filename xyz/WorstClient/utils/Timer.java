package xyz.WorstClient.utils;

public class Timer {
    private long previousTime;
    public Timer() {
        previousTime = -1L;
    }

    public boolean check(float milliseconds) {
        return getTime() >= milliseconds;
    }

    public long getTime() {
        return getCurrentTime() - previousTime;
    }

    public void reset() {
        previousTime = getCurrentTime();
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

	public boolean delay(int i) {
		// TODO Auto-generated method stub
		return false;
	}
}
