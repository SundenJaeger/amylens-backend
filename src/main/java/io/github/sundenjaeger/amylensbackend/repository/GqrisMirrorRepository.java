// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.repository;

import io.github.sundenjaeger.amylensbackend.model.GqrisMirror;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GqrisMirrorRepository extends JpaRepository<GqrisMirror, Long> {
    List<GqrisMirror> findByVariety(String variety);
}
