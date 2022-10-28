create table if not exists uplp_doc
(
    /*TODO Названия на английском берешь из переводчика, если не знаешь как назвать*/
    /*TODO Уже готовые 4 поля: */
    uplp_doc_id                                    bigint       not null, /* TODO Первичный ключ, только он not null*/
    record_no                                      varchar(128) null,   /*TODO Строка/Текст, 128 - максимальное количество символов это и остальные поля должны быть null */
    plot_square                                    bigint       null,   /*TODO Числовое значение, без числел после запятой (5) */
    non_residential_development_square             decimal      null,   /*TODO Числовое значение с запятой (5,424)*/

    /*TODO Примеры данных, либо меняешь на нужное, либо удаляешь*/

--     create_dttm                                    timestamptz  not null,
--     modify_dttm                                    timestamptz  not null,
--     action_ind                                     varchar(1)   not null,
--     region_id                                      bigint       not null,
--     region_code                                    varchar(128) null,
--     region_name                                    varchar(256) null,
--     forestry_name                                  varchar(256) not null,
--     egrn_no                                        varchar(128) null,
--     request_type_ival                              bigint       not null,
--     change_request_id                              bigint       not null,
--     cadastr_no                                     varchar(128) null,
--     original_registration_no                       varchar(128) null,
--     registration_no                                varchar(128) not null,
--     category_land_forestry_id                      bigint       not null,
--     category_land_forestry_name                    varchar(256) null,
--     confirm_taxation_dttm                          timestamptz  null,
--     egrn_assignment_dt                             date         null,
--     include_dttm                                   timestamptz  null,
--     change_dttm                                    timestamptz  null,
--     change_boundary_dttm                           timestamptz  null,
--     termination_dttm                               timestamptz  null,
--     status_ind                                     varchar(1)   not null,
--     accept_decision_create_organization_name       varchar(256) null,
--     decision_create_identity_no                    varchar(128) null,
--     accept_decision_termination_organization_name  varchar(256) null,
--     decision_termination_identity_no               varchar(128) null,
--     accept_decision_boundary_organization_name     varchar(256) null,
--     decision_boundary_identity_no                  varchar(128) null,
--     square_nval                                    decimal      null,
--     other_info_cval                                text         null,
--     forestry_order_identity_no                     varchar(128) null,
--     forestry_usage_record_identity_no              varchar(128) null,
--     forest_pathology_inspection_act_identity_no    varchar(128) null,
--     forestry_stead_registration_no                 varchar(128) null,
--     forestry_no_providing_usage_record_identity_no varchar(128) null,
--     last_forest_manage_dt                          date         null,
--     assignment_registry_number_dt                  date         null,
--     forest_zone_no                                 varchar(128) null,
--     forest_area_no                                 varchar(128) null,
--     square_forest_plantation_land_nval             decimal      null,
--     assignment_registration_number_dt              date         null,
--     order_termination_dttm                         timestamptz  null,
--     popd_object_id_no                              varchar(128) null,
    constraint pk_uplp_doc primary key (uplp_doc_id)
    );

/*TODO Описание комментариев берешь из приложения 2, оптимизируешь по смыслу, если нужно*/
/*TODO Уже готовые комментарии на таблицу и колонки*/
comment on table uplp_doc is
    'Документ ГПЗУ';

comment on column uplp_doc.uplp_doc_id is
    'ИД документа';

comment on column uplp_doc.record_no is
    'Уникальный номер записи';

comment on column uplp_doc.plot_square is
    'Площадь земельного  участка (ЗУ), кв.м';

comment on column uplp_doc.non_residential_development_square is
    'Площадь нежилой застройки';

/*TODO Примеры комментариев, либо меняешь на нужное, либо удаляешь*/
-- comment on column uplp_doc.create_dttm is
--     'Дата время вставки записи';
--
-- comment on column uplp_doc.modify_dttm is
--     'Дата время последнего изменения записи';
--
-- comment on column uplp_doc.action_ind is
--     'Индикатор операции';
--
-- comment on column uplp_doc.region_id is
--     'Регион ИД справочник constituentEntity';
--
-- comment on column uplp_doc.region_code is
--     'Код региона';
--
-- comment on column uplp_doc.region_name is
--     'Наименование региона';
--
-- comment on column uplp_doc.forestry_name is
--     'Наименование';
--
-- comment on column uplp_doc.egrn_no is
--     'ЕГРН';
--
-- comment on column uplp_doc.request_type_ival is
--     'Тип запроса на изменение';
--
-- comment on column uplp_doc.change_request_id is
--     'ИД Заявки';
--
-- comment on column uplp_doc.cadastr_no is
--     'Кадастровый номер';
--
-- comment on column uplp_doc.original_registration_no is
--     'Учетный номер оринигальный';
--
-- comment on column uplp_doc.registration_no is
--     'Учетный номер';
--
-- comment on column uplp_doc.category_land_forestry_id is
--     'Категория земель справочник НСИ categoryLandForestry';
--
-- comment on column uplp_doc.category_land_forestry_name is
--     'Наименование категории земель НСИ categoryLandForestry';
--
-- comment on column uplp_doc.confirm_taxation_dttm is
--     'Дата утверждения документации в рамках таксационного';
--
-- comment on column uplp_doc.egrn_assignment_dt is
--     'Дата присвоения ЕГРН';
--
-- comment on column uplp_doc.include_dttm is
--     'Дата внесения';
--
-- comment on column uplp_doc.change_dttm is
--     'Дата изменения';
--
-- comment on column uplp_doc.change_boundary_dttm is
--     'Дата изменения границ';
--
-- comment on column uplp_doc.termination_dttm is
--     'Дата упразденения лесничества';
--
-- comment on column uplp_doc.status_ind is
--     'Статус объекта';
--
-- comment on column uplp_doc.accept_decision_create_organization_name is
--     'Наименование ОГВ принявшего решение о создании';
--
-- comment on column uplp_doc.decision_create_identity_no is
--     'ИД номер решения ОГВ о создании';
--
-- comment on column uplp_doc.accept_decision_termination_organization_name is
--     'Наименование ОГВ принявшего решение об упразднении';
--
-- comment on column uplp_doc.decision_termination_identity_no is
--     'ИД номер решения ОГВ об упраздении';
--
-- comment on column uplp_doc.accept_decision_boundary_organization_name is
--     'Наименование ОГВ принявшего решение об установлении -изменении границ';
--
-- comment on column uplp_doc.decision_boundary_identity_no is
--     'Номер решения ОГВ об установлении -изменении границ';
--
-- comment on column uplp_doc.square_nval is
--     'Площадь в гектарах';
--
-- comment on column uplp_doc.other_info_cval is
--     'Иные сведения уточняющие части границ';
--
-- comment on column uplp_doc.forestry_order_identity_no is
--     'Идентификационный номер лесохозяйственного регламента лесничества';
--
-- comment on column uplp_doc.forestry_usage_record_identity_no is
--     'Идентификационный номер записи об использовании лесов, расположенных в границе лесничества';
--
-- comment on column uplp_doc.forest_pathology_inspection_act_identity_no is
--     'Идентификационный номер акта лесопатологического обследования';
--
-- comment on column uplp_doc.forestry_stead_registration_no is
--     'Учетные номера лесных участков, расположенных в границе лесничества';
--
-- comment on column uplp_doc.forestry_no_providing_usage_record_identity_no is
--     'Идентификационные номера записей об использовании лесов без предоставления лесных участков (в отношении лесов, расположенных в границе лесничества)';
--
-- comment on column uplp_doc.last_forest_manage_dt is
--     'Дата последнего лесоустройства';
--
-- comment on column uplp_doc.assignment_registry_number_dt is
--     'Дата присвоения реестрового номера';
--
-- comment on column uplp_doc.forest_zone_no is
--     'Лесорастительная зона';
--
-- comment on column uplp_doc.forest_area_no is
--     'Лесной район';
--
-- comment on column uplp_doc.square_forest_plantation_land_nval is
--     'Площадь, покрытая лесными насаждениями, га';
--
-- comment on column uplp_doc.assignment_registration_number_dt is
--     'Дата присвоения учетного номера лесничества';
-- comment on column uplp_doc.order_termination_dttm is
--     'Дата начал действияприказа об упрозднении  из ГЛР сведений о лесничестве';