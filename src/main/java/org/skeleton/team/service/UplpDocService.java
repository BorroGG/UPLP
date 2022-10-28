package org.skeleton.team.service;

import org.skeleton.team.entity.UplpDoc;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UplpDocService {

    UplpDoc getUplpDocById(Integer id);

    UplpDoc deleteUplpDocById(Integer id);

    UplpDoc createUplpDoc(List<MultipartFile> files, String docType);

    UplpDoc updateUplpDoc(MultipartFile file, Integer id);
}
