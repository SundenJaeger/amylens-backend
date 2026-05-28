package io.github.sundenjaeger.amylensbackend.util;

import io.github.sundenjaeger.amylensbackend.model.GqrisMirror;
import io.github.sundenjaeger.amylensbackend.model.Variety;
import io.github.sundenjaeger.amylensbackend.repository.GqrisMirrorRepository;
import io.github.sundenjaeger.amylensbackend.repository.VarietyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final VarietyRepository varietyRepository;
    private final GqrisMirrorRepository gqrisMirrorRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initVarieties();
        initGqrisMirror();
    }

    private void initVarieties() {
        if (varietyRepository.count() == 0) {
            List<Variety> varieties = List.of(
                    createVariety("IR64", "Popular indica variety with intermediate amylose content"),
                    createVariety("IR72", "High-yielding indica variety"),
                    createVariety("PSB Rc18", "Philippine lowland variety, waxy type"),
                    createVariety("NSIC Rc222", "Malagkit Sungsong, waxy glutinous variety"),
                    createVariety("NSIC Rc160", "InnaRice, low amylose content"),
                    createVariety("NSIC Rc272", "High amylose content variety"),
                    createVariety("IR841", "Intermediate amylose content"),
                    createVariety("Mestiso 1", "PhilRice hybrid variety"),
                    createVariety("Mestiso 7", "PhilRice hybrid, high yield"),
                    createVariety("Dinorado", "Premium aromatic variety")
            );
            varietyRepository.saveAll(varieties);
            System.out.println("Varieties seeded: " + varieties.size() + " records.");
        }
    }

    private void initGqrisMirror() {
        if (gqrisMirrorRepository.count() == 0) {
            List<GqrisMirror> records = List.of(
                    // IR64 — Intermediate (ordinal 3)
                    createGqris("IR64", 3, 2021),
                    createGqris("IR64", 3, 2022),
                    createGqris("IR64", 3, 2023),
                    // IR72 — Intermediate (ordinal 3)
                    createGqris("IR72", 3, 2021),
                    createGqris("IR72", 3, 2022),
                    createGqris("IR72", 3, 2023),
                    // PSB Rc18 — Waxy (ordinal 1)
                    createGqris("PSB Rc18", 1, 2021),
                    createGqris("PSB Rc18", 1, 2022),
                    createGqris("PSB Rc18", 1, 2023),
                    // NSIC Rc222 — Waxy (ordinal 1)
                    createGqris("NSIC Rc222", 1, 2021),
                    createGqris("NSIC Rc222", 1, 2022),
                    createGqris("NSIC Rc222", 1, 2023),
                    // NSIC Rc160 — Low (ordinal 2)
                    createGqris("NSIC Rc160", 2, 2021),
                    createGqris("NSIC Rc160", 2, 2022),
                    createGqris("NSIC Rc160", 2, 2023),
                    // NSIC Rc272 — High (ordinal 4)
                    createGqris("NSIC Rc272", 4, 2021),
                    createGqris("NSIC Rc272", 4, 2022),
                    createGqris("NSIC Rc272", 4, 2023),
                    // IR841 — Intermediate (ordinal 3)
                    createGqris("IR841", 3, 2021),
                    createGqris("IR841", 3, 2022),
                    createGqris("IR841", 3, 2023),
                    // Mestiso 1 — Intermediate (ordinal 3)
                    createGqris("Mestiso 1", 3, 2021),
                    createGqris("Mestiso 1", 3, 2022),
                    createGqris("Mestiso 1", 3, 2023),
                    // Mestiso 7 — Intermediate (ordinal 3)
                    createGqris("Mestiso 7", 3, 2021),
                    createGqris("Mestiso 7", 3, 2022),
                    createGqris("Mestiso 7", 3, 2023),
                    // Dinorado — Low (ordinal 2)
                    createGqris("Dinorado", 2, 2021),
                    createGqris("Dinorado", 2, 2022),
                    createGqris("Dinorado", 2, 2023)
            );
            gqrisMirrorRepository.saveAll(records);
        }
    }

    private Variety createVariety(String name, String description) {
        Variety variety = new Variety();
        variety.setName(name);
        variety.setDescription(description);
        return variety;
    }

    private GqrisMirror createGqris(String variety, int ordinal, int year) {
        GqrisMirror record = new GqrisMirror();
        record.setVariety(variety);
        record.setAmyloseOrdinal(ordinal);
        record.setYear(year);
        return record;
    }
}
