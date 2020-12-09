package aeropresscipe.divinelink.aeropress.timer;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceUI;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import aeropresscipe.divinelink.aeropress.R;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;


public class TimerFragment extends Fragment implements TimerView {

    TextView timerTextView;
    TextView notificationTextView;
    MaterialProgressBar progressBar;
    ImageButton likeRecipeBtn;

    private TimerPresenter presenter;

    private int secondsRemaining = 0;

    DiceUI diceUI;

    GetPhaseFactory getPhaseFactory = new GetPhaseFactory();


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_timer, container, false);

        diceUI = getArguments().getParcelable("timer");

        timerTextView = (TextView) v.findViewById(R.id.savedTimeTV);
        progressBar = (MaterialProgressBar) v.findViewById(R.id.progressBar);
        notificationTextView = (TextView) v.findViewById(R.id.notificationTV);
        likeRecipeBtn = (ImageButton) v.findViewById(R.id.likeRecipeButton);

        likeRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.saveLikedRecipeOnDB(getContext());
            }
        });

        //FIXME move it somewhere else
        likeRecipeBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    AnimatorSet reducer = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.reduce_size);
                    reducer.setTarget(view);
                    reducer.start();

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    AnimatorSet regainer = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.regain_size);
                    regainer.setTarget(view);
                    regainer.start();
                }
                return false;
            }
        });


        presenter = new TimerPresenterImpl(this);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static TimerFragment newInstance(DiceUI diceUI) {

        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putParcelable("timer", diceUI);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void showTimer(final int time, boolean bloomPhase) {
        secondsRemaining = time;

        if (bloomPhase) {
            timerHandler.postDelayed(bloomRunnable, 1000);
            updateCountdownUI();
            notificationTextView.setText(String.format("%s\n%s", getString(R.string.bloomPhase), getString(R.string.bloomPhaseWaterText, diceUI.getBloomWater())));

        } else {
            timerHandler.postDelayed(brewRunnable, 1000);
            // Checks if there was a bloom or not, and set corresponding text on textView.
            updateCountdownUI();
            if (getPhaseFactory.findPhase(diceUI.getBloomTime(), diceUI.getBrewTime()).getPhase())
            //FIXME make notificationTextView fade in and fade out constantly!
            {
                notificationTextView.setText(String.format("%s\n%s", getString(R.string.brewPhase), getString(R.string.brewPhaseWithBloom, diceUI.getRemainingBrewWater())));
            } else
                notificationTextView.setText(String.format("%s\n%s", getString(R.string.brewPhase), getString(R.string.brewPhaseNoBloom, diceUI.getRemainingBrewWater())));
            diceUI.setBloomTime(0);
        }
        // Set max progress bar to be either the max BloomTime or BrewTime.
        // Avoid if statements by using Factory
        progressBar.setMax(getPhaseFactory.getMaxTime(diceUI.getBloomTime(), diceUI.getBrewTime()));

        ObjectAnimator.ofInt(progressBar, "progress", time)
                .setDuration(300)
                .start();
    }

    Handler timerHandler = new Handler();
    Runnable bloomRunnable = new Runnable() {
        @Override
        public void run() {
            if (secondsRemaining == 1 || secondsRemaining == 0) {
                presenter.getNumbersForTimer(
                        getPhaseFactory.findPhase(0, diceUI.getBrewTime()).getTime(),
                        false,
                        getContext());
                timerHandler.removeCallbacks(bloomRunnable);
            } else {
                timerHandler.postDelayed(this, 1000);
                secondsRemaining -= 1;
                updateCountdownUI();
            }
        }
    };

    Runnable brewRunnable = new Runnable() {
        @Override
        public void run() {
            if (secondsRemaining == 1 || secondsRemaining == 0) {
                timerHandler.removeCallbacks(brewRunnable);
                //TODO ADD ANIMATION
                presenter.showMessage();

            } else {
                timerHandler.postDelayed(this, 1000);
                secondsRemaining -= 1;
                updateCountdownUI();
            }
        }
    };

    private void updateCountdownUI() {
        int minutesUntilFinished = secondsRemaining / 60;
        int secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60;
        timerTextView.setText(String.format("%d:%02d", minutesUntilFinished, secondsInMinuteUntilFinished));
        progressBar.setProgress(secondsInMinuteUntilFinished + minutesUntilFinished * 60);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Basically when both getBloomTime and getBrewTime are equal to 0, then don't proceed.
        if (getPhaseFactory.findPhase(diceUI.getBloomTime(), diceUI.getBrewTime()).getTime() != 0) {
            final boolean isBloomPhase = getPhaseFactory.findPhase(diceUI.getBloomTime(), diceUI.getBrewTime()).getPhase();

            timerHandler.removeCallbacks(bloomRunnable);
            timerHandler.removeCallbacks(brewRunnable);

            presenter.saveValuesOnPause(getContext(), secondsRemaining, diceUI.getBrewTime(), isBloomPhase);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // FIXME after the brew is finished, make it unable to resume brew.
        if (diceUI.isNewRecipe()) // if it's a new recipe, dont call returnValuesOnResume
            presenter.getNumbersForTimer(
                    getPhaseFactory.findPhase(diceUI.getBloomTime(), diceUI.getBrewTime()).getTime(),
                    getPhaseFactory.findPhase(diceUI.getBloomTime(), diceUI.getBrewTime()).getPhase(),
                    getContext());
        else
            //When resuming, we need to pass the old recipe, not the new one.
            presenter.returnValuesOnResume(getContext());

    }

    @Override
    public void showMessage() {
        //TODO make it show a button that returns to starting screen
        timerTextView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        notificationTextView.setText(R.string.enjoyCoffee);

        // Temporary Fix?
        diceUI.setBloomTime(0);
        diceUI.setBrewTime(0);

    }

    @Override
    public void addToLiked(final boolean isLiked) {

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (isLiked)
                        likeRecipeBtn.setImageResource(R.drawable.ic_heart_on);
                    else
                        likeRecipeBtn.setImageResource(R.drawable.ic_heart_off);
                }
            });
        }
    }
}
