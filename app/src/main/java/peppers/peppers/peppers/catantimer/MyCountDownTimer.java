package peppers.peppers.peppers.catantimer;

import android.os.CountDownTimer;

// Improves android.os.CountDownTimer by implementing pause/unpause, restart, reset,
// and increase/decrease time functionality on top of the existing start/stop behavior.
// Also allows you to specify the maximum interval the timer should update,
// as well as the maximum countdown duration.
public abstract class MyCountDownTimer {

    // Force implementations to define the countdown duration, update interval,
    // and maximums for both values

    public MyCountDownTimer(long initialCountDownTimeMs, long intervalTimeMs,
                            boolean usesMaxCountDown, long maxCountDownTimeMs,
                            boolean usesMaxInterval, long maxIntervalTimeMs) {
        setUsesMaxCountDown(usesMaxCountDown);
        setMaxCountDownTimeMs(maxCountDownTimeMs);
        setUsesMaxInterval(usesMaxInterval);
        setMaxIntervalTimeMs(maxIntervalTimeMs);
        setInitialCountDownTimeMs(initialCountDownTimeMs);
        setCurrentCountDownTimeMs(initialCountDownTimeMs);
        setIntervalTimeMs(intervalTimeMs);
        updateCountDownTimer();
        setIsPaused(false);
    }

    // Template out the abstract methods //////////////////////////////////////

    public abstract void onTick();
    public abstract void onFinish();

    // This callback is used by the system, e.g. not called by instance owner
    private final void _onTick(long timeUntilFinishedMs) {
        setCurrentCountDownTimeMs(timeUntilFinishedMs);
        onTick();
    }

    // This callback is used by the system, e.g. not called by instance owner
    // _onFinish has to exist so the anonymous CountDownTimer definition compiles
    private final void _onFinish() {
        onFinish();
    }

    // Define improved countdown timer behavior ///////////////////////////////

    public final void start() {
        getCountDownTimer().cancel();
        updateCountDownTimer();
        getCountDownTimer().start();
    }

    public final void pause() {
        getCountDownTimer().cancel();
        updateCountDownTimer();
        setIsPaused(true);
    }

    public final void unpause() {
        getCountDownTimer().cancel();
        updateCountDownTimer();
        getCountDownTimer().start();
        setIsPaused(false);
    }

    public final void reset() {
        getCountDownTimer().cancel();
        setCurrentCountDownTimeMs(getInitialCountDownTimeMs());
        updateCountDownTimer();
        setIsPaused(false);
    }

    public final void reset(long newCountDownTimeMs) {
        getCountDownTimer().cancel();
        setInitialCountDownTimeMs(newCountDownTimeMs);
        setCurrentCountDownTimeMs(newCountDownTimeMs);
        updateCountDownTimer();
        setIsPaused(false);
    }

    public final void restart() {
        getCountDownTimer().cancel();
        setCurrentCountDownTimeMs(getInitialCountDownTimeMs());
        updateCountDownTimer();
        setIsPaused(false);
        getCountDownTimer().start();
    }

    public final void restart(long newCountDownTimeMs) {
        getCountDownTimer().cancel();
        setInitialCountDownTimeMs(newCountDownTimeMs);
        setCurrentCountDownTimeMs(newCountDownTimeMs);
        updateCountDownTimer();
        setIsPaused(false);
        getCountDownTimer().start();
    }

    public final void increaseCurrentCountDownTimeMs(long deltaMs) {
        getCountDownTimer().cancel();
        setCurrentCountDownTimeMs(getCurrentCountDownTimeMs() + deltaMs);
        updateCountDownTimer();
        if (!getIsPaused()) {
            getCountDownTimer().start();
        }
    }

    public final void decreaseCurrentCountDownTimeMs(long deltaMs) {
        getCountDownTimer().cancel();
        setCurrentCountDownTimeMs(getCurrentCountDownTimeMs() - deltaMs);
        updateCountDownTimer();
        if (!getIsPaused()) {
            getCountDownTimer().start();
        }
    }

    public final void increaseInitialCountDownTimeMs(long deltaMs) {
        setInitialCountDownTimeMs(getInitialCountDownTimeMs() + deltaMs);
        setCurrentCountDownTimeMs(getCurrentCountDownTimeMs() + deltaMs);
    }

    public final void decreaseInitialCountDownTimeMs(long deltaMs) {
        setInitialCountDownTimeMs(getInitialCountDownTimeMs() - deltaMs);
        setCurrentCountDownTimeMs(getCurrentCountDownTimeMs() - deltaMs);
    }

    // Getters, Setters ///////////////////////////////////////////////////////

    public long getInitialCountDownTimeMs() {
        return initialCountDownTimeMs;
    }

    private void setInitialCountDownTimeMs(long countDownTimeMs) {
        if (countDownTimeMs < 0) {
            this.initialCountDownTimeMs = 0;
        }
        else if (getUsesMaxCountDown() && (currentCountDownTimeMs > getMaxCountDownTimeMs())) {
            this.initialCountDownTimeMs = getMaxCountDownTimeMs();
        }
        else {
            this.initialCountDownTimeMs = countDownTimeMs;
        }
    }

    public long getCurrentCountDownTimeMs() {
        return currentCountDownTimeMs;
    }

    private void setCurrentCountDownTimeMs(long currentCountDownTimeMs) {
        if (currentCountDownTimeMs < 0) {
            this.currentCountDownTimeMs = 0;
        }
        else if (getUsesMaxCountDown() && (currentCountDownTimeMs > getMaxCountDownTimeMs())) {
            this.currentCountDownTimeMs = getMaxCountDownTimeMs();
        }
        else {
            this.currentCountDownTimeMs = currentCountDownTimeMs;
        }
    }

    public long getIntervalTimeMs() {
        return intervalTimeMs;
    }

    private void setIntervalTimeMs(long intervalTimeMs) {
        if (intervalTimeMs < 0) {
            this.intervalTimeMs = 0;
        }
        else if (getUsesMaxInterval() && (intervalTimeMs > getMaxIntervalTimeMs())) {
            this.intervalTimeMs = getMaxIntervalTimeMs();
        }
        else {
            this.intervalTimeMs = intervalTimeMs;
        }
    }

    public long getMaxCountDownTimeMs() {
        return maxCountDownTimeMs;
    }

    private void setMaxCountDownTimeMs(long maxCountDownTimeMs) {
        this.maxCountDownTimeMs = maxCountDownTimeMs;
    }

    public long getMaxIntervalTimeMs() {
        return maxIntervalTimeMs;
    }

    private void setMaxIntervalTimeMs(long maxIntervalTimeMs) {
        this.maxIntervalTimeMs = maxIntervalTimeMs;
    }

    private CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    private void updateCountDownTimer() {
        countDownTimer = new CountDownTimer(currentCountDownTimeMs, intervalTimeMs) {
            public void onTick(long timeUntilFinishedMs) {
                _onTick(timeUntilFinishedMs);
            }

            public void onFinish() {
                _onFinish();
            }
        };
    }

    public boolean getUsesMaxInterval() {
        return usesMaxInterval;
    }

    private void setUsesMaxInterval(boolean usesMaxInterval) {
        this.usesMaxInterval = usesMaxInterval;
    }

    public boolean getUsesMaxCountDown() {
        return usesMaxCountDown;
    }

    private void setUsesMaxCountDown(boolean usesMaxCountDown) {
        this.usesMaxCountDown = usesMaxCountDown;
    }

    public boolean getIsPaused() {
        return isPaused;
    }

    private void setIsPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    // Member Variables ///////////////////////////////////////////////////////

    private CountDownTimer countDownTimer;
    private long initialCountDownTimeMs;
    private long currentCountDownTimeMs;
    private long intervalTimeMs;
    private long maxCountDownTimeMs;
    private long maxIntervalTimeMs;
    private boolean usesMaxInterval;
    private boolean usesMaxCountDown;
    private boolean isPaused;
}