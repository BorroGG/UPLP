package org.skeleton.team.mapper;

import org.mapstruct.Mapper;
import org.skeleton.team.entity.UplpDoc;
import org.skeleton.team.entity.UplpSimpleDoc;

import java.util.List;

//Интерфейс для маппинга полей одного объекта на другой
@Mapper(componentModel = "spring")
public interface UplpDocMapper {

    UplpSimpleDoc toSimpleDoc(UplpDoc doc);

    List<UplpSimpleDoc> toSimpleDoc(List<UplpDoc> doc);
}