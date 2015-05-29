package net.mobindustry.telegram.utils;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ListCountryObject implements Serializable {


    private List<CountryObject> listCountries = new ArrayList<>();
    private List<Integer> listHeaderPositions = new ArrayList<>();
    private List<Integer> listRowsQuantity = new ArrayList<>();
    private int countRows = 0;


    public List<CountryObject> getListCountries() {
        return listCountries;
    }


    public ListCountryObject(String text) {
        List<String> listSplitRow = splitRow(text);
        for (int i = 0; i < listSplitRow.size(); i++) {
            String[] parts = listSplitRow.get(i).split(";");
            CountryObject country = new CountryObject();
            country.setCountryCode("+" + parts[0]);
            country.setCountryStringCode(parts[1]);
            country.setCountryName(parts[2]);
            String firstLetter = String.valueOf(parts[2].charAt(0));
            country.setInitialLetter(firstLetter);
            listCountries.add(country);
        }
        Collections.sort(listCountries, new Comparator<CountryObject>() {
            @Override
            public int compare(CountryObject lhs, CountryObject rhs) {
                return lhs.getCountryName().compareTo(rhs.getCountryName());
            }
        });
        headerPosition();
        quantityCountriesInSections();
    }

    private List<String> splitRow(String text) {
        //Split string row
        String line = text;
        String[] parts = line.split("\n");
        List<String> list = new ArrayList<>(Arrays.asList(parts));
        Collections.sort(list);
        return list;
    }

    public void quantityCountriesInSections() {
        int quantityRows = 0;
        for (int i = 0; i < listCountries.size() - 1; i++) {
            if (listCountries.get(i).getInitialLetter().equals(listCountries.get(i + 1).getInitialLetter())) {
                quantityRows++;
            } else {
                listRowsQuantity.add(quantityRows);
                quantityRows = 0;
            }
        }
    }

    @Override
    public String toString() {
        return "ListCountryObject{" +
                "listHeaderPositions=" + listHeaderPositions +
                '}';
    }

    public void headerPosition() {
        listHeaderPositions.add(0);
        for (int i = 0; i < listCountries.size() - 1; i++) {
            if (!listCountries.get(i).getInitialLetter().equals(listCountries.get(i + 1).getInitialLetter())) {
                Log.e(" log ", " first " + listCountries.get(i).getInitialLetter());
                Log.e(" log ", " second " + listCountries.get(i + 1).getInitialLetter());
                listHeaderPositions.add(i + 1);
            }
        }
    }

    public int getRowsQuantity() {
        int position = listRowsQuantity.get(countRows);
        if (countRows != listHeaderPositions.size() - 1) {
            countRows++;
        } else {
            countRows = 0;
        }
        return position;
    }

}

