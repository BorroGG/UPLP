package org.skeleton.team.repository;

import org.skeleton.team.entity.UplpDoc;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс для взаимодействия с таблицей UplpDoc в БД
 */
public interface UplpDocRepository extends JpaRepository<UplpDoc, Long> {
}
