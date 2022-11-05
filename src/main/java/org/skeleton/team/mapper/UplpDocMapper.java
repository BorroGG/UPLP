package org.skeleton.team.mapper;

import org.mapstruct.Mapper;
import org.skeleton.team.entity.UplpDoc;
import org.skeleton.team.entity.UplpSimpleDoc;

import java.util.List;

/**
 * Интерфейс для маппинга полей одного объекта на другой.
 */
@Mapper(componentModel = "spring")
public interface UplpDocMapper {

    /**
     * Маппинг из документа ГПЗУ в формат без системных полей.
     * @param doc документ ГПЗУ
     * @return документ ГПЗУ в простом формате без системных полей
     */
    UplpSimpleDoc toSimpleDoc(UplpDoc doc);

    /**
     * Маппинг нескольких документов ГПЗУ в формат без системных полей.
     * @param doc документы ГПЗУ
     * @return документы ГПЗУ в простом формате без системных полей
     */
    List<UplpSimpleDoc> toSimpleDoc(List<UplpDoc> doc);
}