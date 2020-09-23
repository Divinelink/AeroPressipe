package aeropresscipe.divinelink.aeropress.generaterecipe;


import java.util.ArrayList;
import java.util.Collections;

public class GenerateRecipeInteractorImpl implements GenerateRecipeInteractor{
            //We only want to edit out data in the interactor, before we pass them into the Presenter
            // otherwise we break the MVP rules
            @Override
            public void getRecipe(OnGenerateRecipeFinishListener listener) {

                ArrayList<DiceDomain> temperature = addTemperatureDiceProperties();
                ArrayList<DiceDomain> groundSize = addGroundSizeDiceProperties();
                ArrayList<DiceDomain> brewingMethod = addBrewingMethodProperties();
                ArrayList<DiceDomain> waterAmount = addBrewingWaterAmountProperties();

                int randomTempIndex = (int) (Math.random() * temperature.size());
                int randomGroundSizeIndex = (int) (Math.random() * groundSize.size());
                int randombrewingMethodIndex = (int) (Math.random() * brewingMethod.size());
                int randomWaterAmountIndex = (int) (Math.random() * waterAmount.size());

                // need to get specific details from each array list. From waterAmount for example, get coffee Amount and water Amount etc.
                int temp = temperature.get(randomTempIndex).getDiceTemperature();
                String groundS = groundSize.get(randomGroundSizeIndex).getGroundSize();
                int brewTime = groundSize.get(randomGroundSizeIndex).getBrewTime();





                listener.onSuccess(temperature, groundSize, brewingMethod, waterAmount);
            }

            private ArrayList<DiceDomain> addTemperatureDiceProperties() {

                ArrayList<DiceDomain> TemperatureDice = new ArrayList<>();

                DiceDomain tempDice1 = new DiceDomain(1, 80);
                DiceDomain tempDice2 = new DiceDomain(1, 85);
                DiceDomain tempDice3 = new DiceDomain(1, 90);
                DiceDomain tempDice4 = new DiceDomain(1, 95);
                DiceDomain tempDice5 = new DiceDomain(1, 100);

                TemperatureDice.add(tempDice1);
                TemperatureDice.add(tempDice2);
                TemperatureDice.add(tempDice3);
                TemperatureDice.add(tempDice4);
                TemperatureDice.add(tempDice5);

                return TemperatureDice;
            }

            private ArrayList<DiceDomain> addGroundSizeDiceProperties(){

                ArrayList<DiceDomain> GroundSizeDice = new ArrayList<>();
                DiceDomain groundSizeDice1 = new DiceDomain(2,"Fine", 60);
                DiceDomain groundSizeDice2 = new DiceDomain(2,"MediumFine", 90);
                DiceDomain groundSizeDice3 = new DiceDomain(2,"Medium", 120);
                DiceDomain groundSizeDice4 = new DiceDomain(2,"Coarse", 240);

                GroundSizeDice.add(groundSizeDice1);
                GroundSizeDice.add(groundSizeDice2);
                GroundSizeDice.add(groundSizeDice3);
                GroundSizeDice.add(groundSizeDice4);

                return GroundSizeDice;
            }

            private ArrayList<DiceDomain> addBrewingMethodProperties(){

                ArrayList<DiceDomain> BrewingMethodDice = new ArrayList<>();

                DiceDomain brewingMethodDice1 = new DiceDomain(3, "Standard", 0, 0);
                DiceDomain brewingMethodDice2 = new DiceDomain(3, "Standard", 30, 30);
                DiceDomain brewingMethodDice3 = new DiceDomain(3, "Standard", 30, 60);
                DiceDomain brewingMethodDice4 = new DiceDomain(3, "Inverted", 0, 0);
                DiceDomain brewingMethodDice5 = new DiceDomain(3, "Inverted", 30, 30);
                DiceDomain brewingMethodDice6 = new DiceDomain(3, "Inverted", 30, 60);

                BrewingMethodDice.add(brewingMethodDice1);
                BrewingMethodDice.add(brewingMethodDice2);
                BrewingMethodDice.add(brewingMethodDice3);
                BrewingMethodDice.add(brewingMethodDice4);
                BrewingMethodDice.add(brewingMethodDice5);
                BrewingMethodDice.add(brewingMethodDice6);

                return BrewingMethodDice;
            }

            private ArrayList<DiceDomain> addBrewingWaterAmountProperties() {
                ArrayList<DiceDomain> BrewingWaterAmountDice = new ArrayList<>();

                DiceDomain brewingAmountDice1 = new DiceDomain(4, 12, 200);
                DiceDomain brewingAmountDice2 = new DiceDomain(4, 15, 200);
                DiceDomain brewingAmountDice3 = new DiceDomain(4, 15, 250);
                DiceDomain brewingAmountDice4 = new DiceDomain(4, 20, 200);
                DiceDomain brewingAmountDice5 = new DiceDomain(4, 20, 250);
                DiceDomain brewingAmountDice6 = new DiceDomain(4, 23, 250);

                BrewingWaterAmountDice.add(brewingAmountDice1);
                BrewingWaterAmountDice.add(brewingAmountDice2);
                BrewingWaterAmountDice.add(brewingAmountDice3);
                BrewingWaterAmountDice.add(brewingAmountDice4);
                BrewingWaterAmountDice.add(brewingAmountDice5);
                BrewingWaterAmountDice.add(brewingAmountDice6);

                return BrewingWaterAmountDice;
            }
    
}
