package com.appverlag.kf.kftools.other;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 25.10.17.
 */
public final class KFContentProvider {

    private static final String BASE_URL = "https://content-appverlag.com/static";


    /*
    weather
     */

    public static String moonPhaseSymbolURL(float moonPhase) {
        int phaseDescriptor = (int) (Math.abs(moonPhase) * 10);

        return BASE_URL + "/images/weather/moon/" + phaseDescriptor + ".png";
    }

    public static String windDirectionSymbolURL(String windDirectionSymbolCode) {
        return BASE_URL + "/images/weather/wind/" + windDirectionSymbolCode + ".png";
    }

    public static String weatherSymbolURL(int weatherSymbolCode) {
        return BASE_URL+ "/images/weather/weather/" + weatherSymbolCode + ".png";
    }


    /*
    hunt-map
     */

    public static String facilityImageURL(int type) {
        return BASE_URL + "/images/jagd/facility/facility_round" + type + ".png";
    }


    /*
    flags
     */

    public static String flagImageURL(String state) {
        state = state.toLowerCase();
        String stateDescriptor = "";
        switch (state) {
            case "deutschland":
            case "germany":
                stateDescriptor = "germany";
                break;
            case "österreich":
            case "oesterreich":
            case "austria":
                stateDescriptor = "austria";
                break;
            case "baden-württemberg":
                stateDescriptor = "bw";
                break;
            case "bayern":
                stateDescriptor = "bayern";
                break;
            case "berlin":
                stateDescriptor = "berlin";
                break;
            case "brandenburg":
                stateDescriptor = "brandenburg";
                break;
            case "bremen":
                stateDescriptor = "bremen";
                break;
            case "hamburg":
                stateDescriptor = "hamburg";
                break;
            case "hessen":
                stateDescriptor = "hessen";
                break;
            case "mecklenburg-vorpommern":
                stateDescriptor = "mv";
                break;
            case "niedersachsen":
                stateDescriptor = "niedersachsen";
                break;
            case "nordrhein-westfalen":
                stateDescriptor = "nrw";
                break;
            case "rheinland-pfalz":
                stateDescriptor = "rpfalz";
                break;
            case "saarland":
                stateDescriptor = "saarland";
                break;
            case "sachsen-anhalt":
                stateDescriptor = "sachsen_anhalt";
                break;
            case "sachsen":
                stateDescriptor = "sachsen";
                break;
            case "schleswig-holstein":
                stateDescriptor = "schleswig";
                break;
            case "thüringen":
                stateDescriptor = "thueringen";
                break;
            case "burgenland":
                stateDescriptor = "burgenland";
                break;
            case "kärnten":
                stateDescriptor = "kaernten";
                break;
            case "niederösterreich":
                stateDescriptor = "niederoesterreich";
                break;
            case "oberösterreich":
                stateDescriptor = "oberoesterreich";
                break;
            case "salzburg":
                stateDescriptor = "salzburg";
                break;
            case "tirol":
                stateDescriptor = "tirol";
                break;
            case "vorarlberg":
                stateDescriptor = "vorarlberg";
                break;
            case "wien":
                stateDescriptor = "oberoesterreich";
                break;
        }
        return BASE_URL + "/images/flags/" + stateDescriptor + ".png";
    }

    /*
    animals
     */

    public static String animalImageURL(String animal) {
        animal = animal.toLowerCase();
        String descriptor = "";
        switch (animal) {
            case "blässhühner":
            case "blaesshühner":
            case "blässhuhn":
                descriptor = "33_Blaesshuehner";
                break;
            case "dachs":
            case "dachse":
                descriptor = "32_Dachs";
                break;
            case "damwild":
            case "sikawild":
                descriptor = "31_DamSikawild";
                break;
            case "fasanen":
                descriptor = "30_Fasan";
                break;
            case "feldhasen":
                descriptor = "29_Feldhase";
                break;
            case "füchse":
                descriptor = "05_Fuchs";
                break;
            case "gamswild":
                descriptor = "28_Gamswild";
                break;
            case "gänse":
                descriptor = "26_Gans";
                break;
            case "graureiher":
                descriptor = "27_Graureiher";
                break;
            case "hermeline":
                descriptor = "25_Hermeline";
                break;
            case "höckerschwäne":
                descriptor = "24_Schwaene";
                break;
            case "iltisse":
                descriptor = "23_Iltisse";
                break;
            case "kormoran":
                descriptor = "22_Kormoran";
                break;
            case "marder":
                descriptor = "10_Marder";
                break;
            case "marderhunde":
                descriptor = "03_Marderhunde";
                break;
            case "mauswiesel":
                descriptor = "21_Mauswiesel";
                break;
            case "minke":
                descriptor = "20_Minke";
                break;
            case "möwen":
                descriptor = "18_Moewe";
                break;
            case "muffelwild":
                descriptor = "19_Muffelwild";
                break;
            case "nutria":
                descriptor = "04_Nutria";
                break;
            case "rabenvögel":
                descriptor = "17_Rabenvögel";
                break;
            case "rebhühner":
                descriptor = "15_Rebhuhn";
                break;
            case "rehwild":
                descriptor = "14_Rehwild";
                break;
            case "rotwild":
                descriptor = "13_Rotwild";
                break;
            case "schwarzwild":
                descriptor = "12_Schwarzwild";
                break;
            case "auerwild":
                descriptor = "35_Auerwild";
                break;
            case "birkwild":
                descriptor = "34_Birkwild";
                break;
            case "wildenten":
                descriptor = "06_Wildente";
                break;
            case "waschbären":
                descriptor = "02_Waschbaeren";
                break;
            case "wildkaninchen":
                descriptor = "29_Feldhase";
                break;
            case "waldschnepfen":
                descriptor = "07_Schnepfe";
                break;
            case "trutwild":
                descriptor = "08_Trutwild";
                break;
            case "tauben":
                descriptor = "09_Taube";
                break;
            case "rackelwild":
                descriptor = "16_Rackelwild";
                break;
            case "seehunde":
                descriptor = "11_Seehund";
                break;
        }
        return BASE_URL + "/images/jagd/animal/" + descriptor + ".png";
    }


    /*
    valid
     */

    public static String validHuntImageURL(boolean valid) {
        String descriptor = valid ? "valid" : "invalid";
        return BASE_URL + "/images/jagd/" + descriptor + ".png";
    }

}
