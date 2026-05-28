// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.service;

import io.github.sundenjaeger.amylensbackend.dto.VarietyRequest;
import io.github.sundenjaeger.amylensbackend.dto.VarietyResponse;
import io.github.sundenjaeger.amylensbackend.exception.VarietyNotFoundException;
import io.github.sundenjaeger.amylensbackend.model.Variety;
import io.github.sundenjaeger.amylensbackend.repository.VarietyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VarietyService {
    private final VarietyRepository varietyRepository;

    @Transactional
    @CacheEvict(value = "varieties", allEntries = true)
    public VarietyResponse create(VarietyRequest request) {
        Variety variety = new Variety();
        variety.setName(request.name());
        variety.setDescription(request.description());

        Variety saved = varietyRepository.save(variety);

        return new VarietyResponse(saved.getId(), saved.getName(), saved.getDescription());
    }

    @Transactional
    @CacheEvict(value = "varieties", allEntries = true)
    public VarietyResponse update(Long id, VarietyRequest request) {
        Variety variety = varietyRepository.findById(id)
                .orElseThrow(() -> new VarietyNotFoundException("Variety does not exist: " + id));
        variety.setName(request.name());
        variety.setDescription(request.description());

        Variety updated = varietyRepository.save(variety);

        return new VarietyResponse(updated.getId(), updated.getName(), updated.getDescription());
    }

    @Transactional(readOnly = true)
    @Cacheable("varieties")
    public List<VarietyResponse> findAll() {
        return varietyRepository.findAll()
                .stream()
                .map(variety -> new VarietyResponse(variety.getId(), variety.getName(),
                        variety.getDescription()))
                .toList();
    }


    @Transactional
    @CacheEvict(value = "varieties", allEntries = true)
    public void delete(Long id) {
        varietyRepository.findById(id)
                .orElseThrow(() -> new VarietyNotFoundException("Variety does not exist: " + id));

        varietyRepository.deleteById(id);
    }
}
