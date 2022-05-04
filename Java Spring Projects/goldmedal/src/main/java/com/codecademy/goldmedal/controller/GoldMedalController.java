package com.codecademy.goldmedal.controller;

import com.codecademy.goldmedal.model.*;
import com.codecademy.goldmedal.repo.CountryRepository;

import com.codecademy.goldmedal.repo.GoldMedalRepository;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/countries")
public class GoldMedalController {
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    GoldMedalRepository goldMedalRepository;

    /*public GoldMedalController(CountryRepository countryRepository, GoldMedalRepository goldMedalRepository) {
        this.countryRepository = countryRepository;
        this.goldMedalRepository = goldMedalRepository;
    }*/

    @GetMapping
    public CountriesResponse getCountries(@RequestParam String sort_by, @RequestParam String ascending) {
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return new CountriesResponse(getCountrySummaries(sort_by.toLowerCase(), ascendingOrder));
    }

    @GetMapping("/{country}")
    public CountryDetailsResponse getCountryDetails(@PathVariable String country) {
        String countryName = WordUtils.capitalizeFully(country);
        return getCountryDetailsResponse(countryName);
    }

    @GetMapping("/{country}/medals")
    public CountryMedalsListResponse getCountryMedalsList(@PathVariable String country, @RequestParam String sort_by, @RequestParam String ascending) {
        String countryName = WordUtils.capitalizeFully(country);
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return getCountryMedalsListResponse(countryName, sort_by.toLowerCase(), ascendingOrder);
    }

    private CountryMedalsListResponse getCountryMedalsListResponse(String countryName, String sortBy, boolean ascendingOrder) {
        List<GoldMedal> medalsList = null;
        switch (sortBy) {
            case "year":
                if(countryName != null && ascendingOrder){
                    medalsList = goldMedalRepository.getByCountryOrderByYearAsc(countryName);
                }
                else {
                    medalsList = goldMedalRepository.getByCountryOrderByYearDesc(countryName);
                }
                break;
            case "season":
                if (countryName!=null && ascendingOrder){
                    medalsList = goldMedalRepository.getByCountryOrderBySeasonAsc(countryName);
                }
                else {
                    medalsList = goldMedalRepository.getByCountryOrderBySeasonDesc(countryName);
                }

                break;
            case "city":
                if (countryName !=null && ascendingOrder) {
                    medalsList = goldMedalRepository.getByCountryOrderByCityAsc(countryName);
                }
                else  {
                    medalsList = goldMedalRepository.getByCountryOrderByCityDesc(countryName);
                }
                break;
            case "name":
                if (countryName !=null && ascendingOrder) {
                    medalsList = goldMedalRepository.getByCountryOrderByNameAsc(countryName);
                }
                else  {
                    medalsList = goldMedalRepository.getByCountryOrderByNameDesc(countryName);
                }
                break;
            case "event":
                if (countryName !=null && ascendingOrder) {
                    medalsList = goldMedalRepository.getByCountryOrderByEventAsc(countryName);
                }
                else {
                    medalsList = goldMedalRepository.getByCountryOrderByEventDesc(countryName);
                }
                
                break;
            default:
                medalsList = new ArrayList<>();
                break;
        }

        return new CountryMedalsListResponse(medalsList);
    }

    private CountryDetailsResponse getCountryDetailsResponse(String countryName) {
        var countryOptional = countryRepository.findCountriesByName(countryName);
        if (countryOptional.isEmpty()) {
            return new CountryDetailsResponse(countryName);
        }

        var country = countryOptional.get();
        var goldMedalCount = goldMedalRepository.countByCountry(countryName);// TODO: get the medal count

        var summerWins = goldMedalRepository.getByCountryAndSeasonOrderByYearAsc(countryName,"Summer");// TODO: get the collection of wins at the Summer Olympics, sorted by year in ascending order
        var numberSummerWins = summerWins.size() > 0 ? summerWins.size() : null;
        var totalSummerEvents = goldMedalRepository.countBySeason("Summer");// TODO: get the total number of events at the Summer Olympics
        var percentageTotalSummerWins = totalSummerEvents != 0 && numberSummerWins != null ? (float) summerWins.size() / totalSummerEvents : null;
        var yearFirstSummerWin = summerWins.size() > 0 ? summerWins.get(0).getYear() : null;

        var winterWins = goldMedalRepository.getByCountryAndSeasonOrderByYearAsc(countryName,"Winter");// TODO: get the collection of wins at the Winter Olympics
        var numberWinterWins = winterWins.size() > 0 ? winterWins.size() : null;
        var totalWinterEvents = goldMedalRepository.countBySeason("Winter");// TODO: get the total number of events at the Winter Olympics, sorted by year in ascending order
        var percentageTotalWinterWins = totalWinterEvents != 0 && numberWinterWins != null ? (float) winterWins.size() / totalWinterEvents : null;
        var yearFirstWinterWin = winterWins.size() > 0 ? winterWins.get(0).getYear() : null;

        var numberEventsWonByFemaleAthletes = goldMedalRepository.countByCountryAndGender(countryName,"Women");// TODO: get the number of wins by female athletes
        var numberEventsWonByMaleAthletes = goldMedalRepository.countByCountryAndGender(countryName,"Men");// TODO: get the number of wins by male athletes

        return new CountryDetailsResponse(
                countryName,
                country.getGdp(),
                country.getPopulation(),
                goldMedalCount,
                numberSummerWins,
                percentageTotalSummerWins,
                yearFirstSummerWin,
                numberWinterWins,
                percentageTotalWinterWins,
                yearFirstWinterWin,
                numberEventsWonByFemaleAthletes,
                numberEventsWonByMaleAthletes);
    }

    private List<CountrySummary> getCountrySummaries(String sortBy, boolean ascendingOrder) {
        List<Country> countries = null;
        switch (sortBy) {
            case "name":
                if (ascendingOrder) {
                    countries = countryRepository.getAllByOrderByNameAsc();// TODO: list of countries sorted by name in the given order
                }
                else {
                    countries = countryRepository.getAllByOrderByNameDesc();// TODO: list of countries sorted by name in the given order
                }

                break;
            case "gdp":
                if (ascendingOrder){
                    countries = countryRepository.getAllByOrderByGdpAsc();// TODO: list of countries sorted by gdp in the given order
                }
                else  {
                    countries = countryRepository.getAllByOrderByGdpDesc();// TODO: list of countries sorted by gdp in the given order
                }

                break;
            case "population":
                if (ascendingOrder) {
                    countries = countryRepository.getAllByOrderByPopulationAsc();// TODO: list of countries sorted by population in the given order
                }
                else  {
                    countries = countryRepository.getAllByOrderByPopulationDesc();// TODO: list of countries sorted by population in the given order
                }

                break;
            case "medals":
            default:
                countries = countryRepository.getAllByOrderByNameAsc();// TODO: list of countries in any order you choose; for sorting by medal count, additional logic below will handle that
                break;
        }

        var countrySummaries = getCountrySummariesWithMedalCount(countries);

        if (sortBy.equalsIgnoreCase("medals")) {
            countrySummaries = sortByMedalCount(countrySummaries, ascendingOrder);
        }

        return countrySummaries;
    }

    private List<CountrySummary> sortByMedalCount(List<CountrySummary> countrySummaries, boolean ascendingOrder) {
        return countrySummaries.stream()
                .sorted((t1, t2) -> ascendingOrder ?
                        t1.getMedals() - t2.getMedals() :
                        t2.getMedals() - t1.getMedals())
                .collect(Collectors.toList());
    }

    private List<CountrySummary> getCountrySummariesWithMedalCount(List<Country> countries) {
        List<CountrySummary> countrySummaries = new ArrayList<>();
        for (var country : countries) {
            var goldMedalCount = goldMedalRepository.countByCountry(country.getName());
            countrySummaries.add(new CountrySummary(country, goldMedalCount));
        }
        return countrySummaries;
    }
}
