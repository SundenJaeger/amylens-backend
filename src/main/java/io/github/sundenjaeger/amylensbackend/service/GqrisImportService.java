// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import io.github.sundenjaeger.amylensbackend.dto.GqrisImportResult;
import io.github.sundenjaeger.amylensbackend.model.GqrisMirror;
import io.github.sundenjaeger.amylensbackend.repository.GqrisMirrorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GqrisImportService {
    private final GqrisMirrorRepository gqrisRepo;

    @Transactional
    @CacheEvict(value = "gqrisMirror", allEntries = true)
    public GqrisImportResult importFromCsv(MultipartFile file) throws IOException {
        int rowsImported = 0;
        int rowsSkipped = 0;
        List<String> errors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {

            List<String[]> rows = csvReader.readAll();
            List<GqrisMirror> valid = new ArrayList<>();

            for (int i = 0; i < rows.size(); i++) {
                String[] row = rows.get(i);
                try {
                    if (row.length < 3) throw new IllegalArgumentException("Not enough columns");
                    GqrisMirror record = new GqrisMirror();
                    record.setVariety(row[0].trim());
                    record.setAmyloseOrdinal(Integer.parseInt(row[1].trim()));
                    record.setYear(Integer.parseInt(row[2].trim()));
                    valid.add(record);
                    rowsImported++;
                } catch (Exception e) {
                    errors.add("Row " + (i + 2) + ": " + e.getMessage());
                    rowsSkipped++;
                }
            }

            if (!valid.isEmpty()) {
                gqrisRepo.deleteAll();
                gqrisRepo.saveAll(valid);
            }
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }

        return new GqrisImportResult(rowsImported, rowsSkipped, errors);
    }
}
