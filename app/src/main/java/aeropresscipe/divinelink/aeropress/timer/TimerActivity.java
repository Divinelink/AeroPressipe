package aeropresscipe.divinelink.aeropress.timer;

import aeropresscipe.divinelink.aeropress.R;
import aeropresscipe.divinelink.aeropress.base.HomeActivity;
import aeropresscipe.divinelink.aeropress.base.HomeDatabase;
import aeropresscipe.divinelink.aeropress.generaterecipe.DiceUI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

public class TimerActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        DiceUI diceUI = getIntent().getParcelableExtra("timer");

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.timerRoot, TimerFragment.newInstance(diceUI))

                .commit();
    }


}