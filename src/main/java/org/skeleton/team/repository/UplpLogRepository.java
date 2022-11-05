package org.skeleton.team.repository;

import org.skeleton.team.entity.UplpLog;
import org.springframework.data.jpa.repository.JpaRepository;

//Интерфейс для взаимодействия с таблицей логов в БД
public interface UplpLogRepository extends JpaRepository<UplpLog, Long> {
}
