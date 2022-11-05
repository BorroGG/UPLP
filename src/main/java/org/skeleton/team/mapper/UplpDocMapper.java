package org.skeleton.team.mapper;

import org.mapstruct.Mapper;
import org.skeleton.team.entity.UplpDoc;
import org.skeleton.team.entity.UplpSimpleDoc;

import java.util.List;

/**
 * Интерфейс для маппинга полей одного объекта на другой
 */
@Mapper(componentModel = "spring")
public interface UplpDocMapper {

    /**
     * Маппинг из одного документа
     * @param doc документ ГПЗУ
     * @return документ ГПЗУ в простом формате
     */
    UplpSimpleDoc toSimpleDoc(UplpDoc doc);

    /**
     * Маппинг из нескольких документов
     * @param doc документы ГПЗУ
     * @return документы ГПЗУ в простом формате
     */
    List<UplpSimpleDoc> toSimpleDoc(List<UplpDoc> doc);
}