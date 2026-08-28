package com.quotehunters.quotecollection.model.service;

import com.quotehunters.quotecollection.model.dao.CountryDAO;
import com.quotehunters.quotecollection.model.dto.CountryDTO;

import java.sql.Connection;
import java.util.List;

import static com.quotehunters.quotecollection.common.JDBC.*;

public class CountryService {
    private CountryDAO countryDAO = new CountryDAO();

    public List<CountryDTO> allCountries() {
        Connection con = getConnection();
        List<CountryDTO> countries = countryDAO.allCountries(con);

        close(con);

        return countries;
    }

    public boolean existsCountryName(String countryName) {
        Connection con = getConnection();
        boolean exists = false;

        try {
            exists = countryDAO.existsCountryName(con, countryName);
        } finally {
            close(con);
        }

        return exists;
    }

    public int insertCountry(String countryName) {
        Connection con = getConnection();
        int result = countryDAO.insertCountry(con, countryName);

        if (result > 0) {
            commit(con);
        } else {
            rollback(con);
        }
        close(con);

        return result;
    }

    public int updateCountry(int countryId, String countryName) {
        Connection con = getConnection();
        int result = countryDAO.updateCountry(con, countryId, countryName);

        if (result > 0) {
            commit(con);
        } else {
            rollback(con);
        }
        close(con);

        return result;
    }
}