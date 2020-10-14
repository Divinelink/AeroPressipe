package aeropresscipe.divinelink.aeropress.timer;

import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeInteractor;

public class TimerPresenterImpl implements TimerPresenter, TimerInteractor.OnStartTimerFinishListener {

    private TimerView timerView;
    private TimerInteractor interactor;

    public TimerPresenterImpl(TimerView timerView) {
        this.timerView = timerView;
        interactor = new TimerInteractorImpl();
    }

    @Override
    public void onSuccess(int time, boolean bloomPhase) {

        timerView.showTimer(time, bloomPhase);

        // The logic behind the progress bar will go here probably
        // Presenter knows WHEN to display certain states and triggers updates
        // When a user clicks a button,
        // presenter tells the view to display progress bar, ask the domain layer (interactor) for a data/update

    }

    @Override
    public void onError() {

    }
    /*

    @Override
    public void getNumbersForTimer(TimerNumbers time) {
        timerView.showTimer(time.timeForPhase, time.isBloomPhase);
    }*/

    @Override
    public void getNumbersForTimer(int time, boolean bloomPhase) {
        timerView.showTimer(time, bloomPhase);
    }

    /*
        @Override
        public void getNumbersForTimer(int time, boolean bloomPhase) {
            // Interactor is not needed since we don't interact with any data on the model layer
            interactor.startTimer(this, time, bloomPhase);
        }*/
    @Override
    public void showMessage(String message) {
        timerView.showMessage(message);
    }
}
