package aeropresscipe.divinelink.aeropress.savedrecipes;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import aeropresscipe.divinelink.aeropress.base.HomeDatabase;
import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain;
import aeropresscipe.divinelink.aeropress.generaterecipe.RecipeDao;
import androidx.annotation.RequiresApi;

public class SavedRecipesInteractorImpl implements SavedRecipesInteractor {


    @Override
    public void getListsFromDB(final OnGetSavedListsFromDBFinishListener listener, final Context ctx) {


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                final SavedRecipeDao savedRecipeDao = HomeDatabase.getDatabase(ctx).savedRecipeDao();
                final List<SavedRecipeDomain> myData = savedRecipeDao.getSavedRecipes();

                listener.onSuccess(myData);
            }

        });

    }

    @Override
    public void deleteRecipeFromDB(final OnGetSavedListsFromDBFinishListener listener, final SavedRecipeDomain recipeDomain, final Context ctx) {

        AsyncTask.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                final SavedRecipeDao savedRecipeDao = HomeDatabase.getDatabase(ctx).savedRecipeDao();

                savedRecipeDao.deleteCurrent(recipeDomain.getDiceTemperature(),
                        recipeDomain.getGroundSize(),
                        recipeDomain.getBrewTime(),
                        recipeDomain.getBrewingMethod(),
                        recipeDomain.getBloomTime(),
                        recipeDomain.getBloomWater(),
                        recipeDomain.getBrewWaterAmount(),
                        recipeDomain.getCoffeeAmount());

                final List<SavedRecipeDomain> myData = savedRecipeDao.getSavedRecipes();
                listener.onSuccess(myData);
            }

        });


    }
}