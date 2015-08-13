package net.mobindustry.telegram.utils;

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
    private List<CountryObject> listConst = new ArrayList<>();
    private List<CountryObject> listTmp = new ArrayList<>();

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
            listConst.add(country);
            listTmp.add(country);
        }
        Collections.sort(listCountries, new Comparator<CountryObject>() {
            @Override
            public int compare(CountryObject lhs, CountryObject rhs) {
                return lhs.getCountryName().compareTo(rhs.getCountryName());
            }
        });
        Collections.sort(listConst, new Comparator<CountryObject>() {
            @Override
            public int compare(CountryObject lhs, CountryObject rhs) {
                return lhs.getCountryName().compareTo(rhs.getCountryName());
            }
        });
        Collections.sort(listTmp, new Comparator<CountryObject>() {
            @Override
            public int compare(CountryObject lhs, CountryObject rhs) {
                return lhs.getCountryName().compareTo(rhs.getCountryName());
            }
        });
        headerPosition();
        quantityCountriesInSections();
    }

    public void updateListTmp(String text) {
        List<String> listSplitRow = splitRow(text);
        for (int i = 0; i < listSplitRow.size(); i++) {
            String[] parts = listSplitRow.get(i).split(";");
            CountryObject country = new CountryObject();
            country.setCountryCode("+" + parts[0]);
            country.setCountryStringCode(parts[1]);
            country.setCountryName(parts[2]);
            String firstLetter = String.valueOf(parts[2].charAt(0));
            country.setInitialLetter(firstLetter);
            listTmp.add(country);
        }
        Collections.sort(listTmp, new Comparator<CountryObject>() {
            @Override
            public int compare(CountryObject lhs, CountryObject rhs) {
                return lhs.getCountryName().compareTo(rhs.getCountryName());
            }
        });
    }

    public List<CountryObject> getListConst() {
        return listConst;
    }

    public void setListConst(List<CountryObject> listConst) {
        this.listConst = listConst;
    }

    public List<CountryObject> getListTmp() {
        return listTmp;
    }

    public void setListTmp(List<CountryObject> listTmp) {
        this.listTmp = listTmp;
    }

    public void setListCountries(List<CountryObject> listCountries) {
        this.listCountries = listCountries;
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
                listHeaderPositions.add(i + 1);
            }
        }
    }
}

