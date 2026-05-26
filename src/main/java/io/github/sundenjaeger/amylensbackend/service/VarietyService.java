// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.service;

import io.github.sundenjaeger.amylensbackend.model.Variety;
import io.github.sundenjaeger.amylensbackend.repository.VarietyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VarietyService {
    private final VarietyRepository varietyRepository;

    @Transactional(readOnly = true)
    @Cacheable("varieties")
    public List<Variety> findAll() {
        return varietyRepository.findAll();
    }
}
