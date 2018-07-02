package peppers.peppers.peppers.catantimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize member variables ////////////////////////////////////////

        timer = new CatanCountDownTimerBuilder()
            .countDownTimeMs(CATAN_INITIAL_COUNTDOWN_TIME_MS)
            .buildMyCatanCountDownTimer();
        normalCountDownTimeMs = CATAN_INITIAL_COUNTDOWN_TIME_MS;
        robberCountDownTimeMs = CATAN_INITIAL_COUNTDOWN_TIME_MS * 2;
        currCountDownTimeMs = normalCountDownTimeMs;
        isRobberCountDown = false;
        notYetStarted = true;

        // get widgets, update as needed //////////////////////////////////////

        timeTextView = (TextView)findViewById(R.id.time_text);

        // not-yet-started buttons
        notYetStartedLayout = (GridLayout)findViewById(R.id.not_yet_started_layout);
        startButton = (Button)findViewById(R.id.start_button);
        startWithRobberButton = (Button)findViewById(R.id.robber_start_button);
        addTimeToStartButton = (Button)findViewById(R.id.add_to_start_time_button);
        subTimeFromStartButton = (Button)findViewById(R.id.sub_from_start_time_button);

        // currently-started buttons
        currentlyStartedLayout = (GridLayout)findViewById(R.id.currently_started_layout);
        pauseUnpauseButton = (Button)findViewById(R.id.pause_unpause_button);
        restartButton = (Button)findViewById(R.id.restart_button);
        restartWithRobberButton = (Button)findViewById(R.id.robber_restart_button);
        resetButton = (Button)findViewById(R.id.reset_button);
        addTimeButton = (Button)findViewById(R.id.add_time_button);
        subTimeButton = (Button)findViewById(R.id.sub_time_button);

        // update buttons to have correct on click handlers
        startButton.setOnClickListener(new View.OnClickListener()               { public void onClick(View v) { start();                    } });
        startWithRobberButton.setOnClickListener(new View.OnClickListener()     { public void onClick(View v) { robberStart();              } });
        addTimeToStartButton.setOnClickListener(new View.OnClickListener()      { public void onClick(View v) { increaseInitialTime();      } });
        subTimeFromStartButton.setOnClickListener(new View.OnClickListener()    { public void onClick(View v) { decreaseInitialTime();      } });
        pauseUnpauseButton.setOnClickListener(new View.OnClickListener()        { public void onClick(View v) { pauseUnpause();             } });
        restartButton.setOnClickListener(new View.OnClickListener()             { public void onClick(View v) { restart();                  } });
        restartWithRobberButton.setOnClickListener(new View.OnClickListener()   { public void onClick(View v) { robberRestart();            } });
        resetButton.setOnClickListener(new View.OnClickListener()               { public void onClick(View v) { reset();                    } });
        addTimeButton.setOnClickListener(new View.OnClickListener()             { public void onClick(View v) { increaseCurrentTime();      } });
        subTimeButton.setOnClickListener(new View.OnClickListener()             { public void onClick(View v) { decreaseCurrentTime();      } });

        // update buttons with text requiring formatting
        addTimeToStartButton.setText(String.format(getResources().getString(    R.string.add_start_time_text),  (CATAN_DELTA_MS / 1000)     ));
        subTimeFromStartButton.setText(String.format(getResources().getString(  R.string.sub_start_time_text),  (CATAN_DELTA_MS / 1000)     ));
        addTimeButton.setText(String.format(getResources().getString(           R.string.add_time_text),        (CATAN_DELTA_MS / 1000)     ));
        subTimeButton.setText(String.format(getResources().getString(           R.string.sub_time_text),        (CATAN_DELTA_MS / 1000)     ));

        updateTimeMs();
    }

    protected void activateFinishSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.finish_sound);
        mediaPlayer.start();
    }

    protected void showNotYetStartedUI() {
        notYetStarted = true;
        notYetStartedLayout.setVisibility(View.VISIBLE);
        currentlyStartedLayout.setVisibility(View.GONE);
    }

    protected void showCurrentlyStartedUI() {
        notYetStarted = false;
        notYetStartedLayout.setVisibility(View.GONE);
        currentlyStartedLayout.setVisibility(View.VISIBLE);
    }

    protected void updateTimeMs() {
        long time = (currCountDownTimeMs / 1000);
        if (!notYetStarted) {
            time += 1;
        }

        long seconds = time % 60;
        long minutes = time / 60;
        String timeStr = String.format(getResources().getString(R.string.time_text), minutes, seconds);

        timeTextView.setText(timeStr);
    }

    // Define UI behavior /////////////////////////////////////////////////////

    protected void start() {
        timer.start();
        isRobberCountDown = false;

        showCurrentlyStartedUI();
    }

    protected void robberStart() {
        timer.restart(robberCountDownTimeMs);
        isRobberCountDown = true;

        showCurrentlyStartedUI();
    }

    protected void increaseInitialTime() {
        timer.increaseInitialCountDownTimeMs(CATAN_DELTA_MS);
        currCountDownTimeMs = timer.getCurrentCountDownTimeMs();
        normalCountDownTimeMs += CATAN_DELTA_MS;
        robberCountDownTimeMs = normalCountDownTimeMs * 2;
        updateTimeMs();
    }

    protected void decreaseInitialTime() {
        timer.decreaseInitialCountDownTimeMs(CATAN_DELTA_MS);
        currCountDownTimeMs = timer.getCurrentCountDownTimeMs();
        normalCountDownTimeMs -= CATAN_DELTA_MS;
        robberCountDownTimeMs = normalCountDownTimeMs * 2;
        updateTimeMs();
    }

    protected void pauseUnpause() {
        if (timer.getIsPaused()) {
            unpause();
        }
        else {
            pause();
        }
    }

    protected void pause() {
        timer.pause();
        pauseUnpauseButton.setText(getResources().getString(R.string.unpause_button_text));
    }

    protected void unpause() {
        timer.unpause();
        pauseUnpauseButton.setText(getResources().getString(R.string.pause_button_text));
    }

    protected void reset() {
        if (isRobberCountDown) {
            timer.reset(normalCountDownTimeMs);
            isRobberCountDown = false;
        }
        else {
            timer.reset();
        }

        currCountDownTimeMs = timer.getCurrentCountDownTimeMs();
        showNotYetStartedUI();
        pauseUnpauseButton.setText(getResources().getString(R.string.pause_button_text));
        updateTimeMs();
    }

    protected void restart() {
        if (isRobberCountDown) {
            timer.restart(normalCountDownTimeMs);
            isRobberCountDown = false;
        }
        else {
            timer.restart();
        }

        if (timer.getIsPaused()) {
            pauseUnpauseButton.setText(getResources().getString(R.string.unpause_button_text));
        }
        else {
            pauseUnpauseButton.setText(getResources().getString(R.string.pause_button_text));
        }

        updateTimeMs();
    }

    protected void robberRestart() {
        timer.restart(robberCountDownTimeMs);
        isRobberCountDown = true;

        if (timer.getIsPaused()) {
            pauseUnpauseButton.setText(getResources().getString(R.string.unpause_button_text));
        }
        else {
            pauseUnpauseButton.setText(getResources().getString(R.string.pause_button_text));
        }

        updateTimeMs();
    }

    protected void increaseCurrentTime() {
        timer.increaseCurrentCountDownTimeMs(CATAN_DELTA_MS);
        currCountDownTimeMs = timer.getCurrentCountDownTimeMs();
        updateTimeMs();
    }

    protected void decreaseCurrentTime() {
        if (timer.getCurrentCountDownTimeMs() - CATAN_DELTA_MS < 0) {
            timer.decreaseCurrentCountDownTimeMs(CATAN_DELTA_MS);
            currCountDownTimeMs = timer.getCurrentCountDownTimeMs();
            timeTextView.setText(getResources().getString(R.string.finished_text));
            activateFinishSound();
        }
        else {
            timer.decreaseCurrentCountDownTimeMs(CATAN_DELTA_MS);
            currCountDownTimeMs = timer.getCurrentCountDownTimeMs();
            updateTimeMs();
        }
    }

    // Member Variables ///////////////////////////////////////////////////////

    protected TextView timeTextView;

    protected GridLayout notYetStartedLayout;
    protected Button startButton;
    protected Button startWithRobberButton;
    protected Button addTimeToStartButton;
    protected Button subTimeFromStartButton;

    protected GridLayout currentlyStartedLayout;
    protected Button pauseUnpauseButton;
    protected Button restartButton;
    protected Button restartWithRobberButton;
    protected Button resetButton;
    protected Button addTimeButton;
    protected Button subTimeButton;

    public static final long CATAN_INITIAL_COUNTDOWN_TIME_MS = 60 * 1000;
    public static final long CATAN_COUNTDOWN_INTERVAL_MS = 1 * 1000;
    public static final long CATAN_DELTA_MS = 15 * 1000;
    protected CatanCountDownTimer timer;

    protected long normalCountDownTimeMs;
    protected long robberCountDownTimeMs;
    protected long currCountDownTimeMs;
    protected boolean isRobberCountDown;
    protected boolean notYetStarted;


    // Single-Use Catan Classes ///////////////////////////////////////////////

    private class CatanCountDownTimerBuilder {
        public CatanCountDownTimer buildMyCatanCountDownTimer() {
            return new CatanCountDownTimer(countDownTimeMs, countDownIntervalMs,
                usesMaxCountDown, maxCountDownTimeMs, usesMaxInterval, maxIntervalTimeMs);
        }

        public CatanCountDownTimerBuilder countDownTimeMs(long countDownTimeMs) {
            this.countDownTimeMs = countDownTimeMs;
            return this;
        }

        public CatanCountDownTimerBuilder countDownIntervalMs(long countDownIntervalMs) {
            this.countDownIntervalMs = countDownIntervalMs;
            return this;
        }

        public CatanCountDownTimerBuilder usesMaxCountDown(boolean usesMaxCountDown) {
            this.usesMaxCountDown = usesMaxCountDown;
            return this;
        }

        public CatanCountDownTimerBuilder maxCountDownTimeMs(long maxCountDownTimeMs) {
            this.maxCountDownTimeMs = maxCountDownTimeMs;
            return this;
        }

        public CatanCountDownTimerBuilder usesMaxInterval(boolean usesMaxInterval) {
            this.usesMaxInterval = usesMaxInterval;
            return this;
        }

        public CatanCountDownTimerBuilder maxIntervalTimeMs(long maxIntervalTimeMs) {
            this.maxIntervalTimeMs = maxIntervalTimeMs;
            return this;
        }

        // default to 1 minute countdown with 1 second intervals, max 10 minutes, 15 second intervals
        private long countDownTimeMs = 60 * 1000;
        private long countDownIntervalMs = 1 * 1000;
        private long maxCountDownTimeMs = 10 * (60 * 1000);
        private long maxIntervalTimeMs = 15 * 1000;
        private boolean usesMaxCountDown = true;
        private boolean usesMaxInterval = true;
    }

    private class CatanCountDownTimer extends MyCountDownTimer {
        public CatanCountDownTimer(long initialCountDownTimeMs, long intervalTimeMs,
                                   boolean usesMaxCountDown, long maxCountDownTimeMs,
                                   boolean usesMaxInterval, long maxIntervalTimeMs) {
            super(initialCountDownTimeMs, intervalTimeMs, usesMaxCountDown, maxCountDownTimeMs,
                usesMaxInterval, maxIntervalTimeMs);
        }

        @Override
        public void onFinish() {
            timeTextView.setText(getResources().getString(R.string.finished_text));
            activateFinishSound();
        }

        @Override
        public void onTick() {
            currCountDownTimeMs = getCurrentCountDownTimeMs();
            updateTimeMs();
        }
    }
}