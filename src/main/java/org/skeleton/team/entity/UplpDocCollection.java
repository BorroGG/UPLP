package org.skeleton.team.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

//Класс для представления нескольких документов ГПЗУ
@XmlRootElement(name = "uplpDocCollection")
@XmlType(propOrder = {"uplpDoc"})
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UplpDocCollection {

    private List<UplpSimpleDoc> uplpDoc;
}
