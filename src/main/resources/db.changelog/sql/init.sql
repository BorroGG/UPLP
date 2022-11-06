create table if not exists uplp_doc
(
    uplp_doc_id                                    serial       not null,
    record_no                                      varchar(128) null,
    uplp_no                                        varchar(128) null,
    date_of_issue                                  date         null,
    uplp_status                                    varchar(128) null,
    expiry_date                                    date         null,
    uplp_recipient                                 varchar(256) null,
    recipient_type                                 varchar(128) null,
    administrative_area                            varchar(128) null,
    district                                       varchar(128) null,
    building_address                               varchar(128) null,
    cadastral_no                                   varchar(128) null,
    tlp_project_availability                       varchar(128) null,
    tlp_document_details                           varchar(128) null,
    surveying_project_availability                 varchar(128) null,
    surveying_project_details                      varchar(128) null,
    sut_group_name                                 varchar(128) null,
    sut_codes                                      varchar(128) null,
    plot_area                                      bigint       null,
    subzones_availability                          varchar(128) null,
    subzones_area                                  bigint       null,
    building_max_height                            decimal      null,
    building_max_floors                            bigint       null,
    built_up_area_percentage                       varchar(128) null,
    building_density                               varchar(128) null,
    cbo_purpose                                    varchar(128) null,
    cbo_description                                varchar(256) null,
    objects_not_under_construction_standarts       varchar(128) null,
    total_floor_area                               decimal      null,
    residential_development_floor_area             decimal      null,
    non_residential_development_floor_area         decimal      null,
    residential_premises_floor_area                decimal      null,
    non_residential_premises_floor_area            decimal      null,
    total_building_area                            decimal      null,
    total_residential_development_area             decimal      null,
    total_non_residential_development_area         decimal      null,
    total_residential_premises_area                decimal      null,
    total_non_residential_premises_area            decimal      null,
    total_underground_space_area                   decimal      null,
    subzones_total_floor_area                      decimal      null,
    subzones_residential_objects_floor_area        decimal      null,
    subzones_non_residential_objects_floor_area    decimal      null,
    subzones_residential_premises_floor_area       decimal      null,
    subzones_non_residential_premises_floor_area   decimal      null,
    subzones_total_objects_area                    decimal      null,
    subzones_total_residential_objects_area        decimal      null,
    subzones_total_non_residential_objects_area    decimal      null,
    subzones_total_residential_premises_area       decimal      null,
    subzones_total_non_residential_premises_area   decimal      null,
    subzones_total_underground_space_area          decimal      null,
    existing_cbo_availability                      varchar(128) null,
    existing_cbo_total_count                       varchar(128) null,
    existing_cbo_purpose                           varchar(128) null,
    existing_cbo_description                       varchar(128) null,
    existing_cbo_max_floor_count                   bigint       null,
    existing_cbo_total_area                        decimal      null,
    existing_cbo_residential_objects_area          decimal      null,
    existing_cbo_non_residential_objects_area      decimal      null,
    clo_availability                               varchar(128) null,
    clo_total_count                                varchar(128) null,
    clo_description                                varchar(128) null,
    clo_identification_no                          varchar(128) null,
    clo_registration_no                            varchar(128) null,
    operator_name                                  varchar(128) null,
    actualisation_date                             date         null,
    file_reference                                 varchar(128) not null,
    object_zone_no                                 varchar(128) null,
    uplp_log_id                                    bigint       not null,
    constraint pk_uplp_doc primary key (uplp_doc_id)
    );

comment on table uplp_doc is
    'Документ ГПЗУ';
comment on column uplp_doc.uplp_doc_id is
    'ИД документа';
comment on column uplp_doc.record_no is
    'Уникальный номер записи';
comment on column uplp_doc.uplp_no is
    'Номер ГПЗУ';
comment on column uplp_doc.date_of_issue is
    'Дата выдачи ГПЗУ';
comment on column uplp_doc.uplp_status is
    'Статус ГПЗУ';
comment on column uplp_doc.expiry_date is
    'Срок действия ГПЗУ';
comment on column uplp_doc.uplp_recipient is
    'Правообладатель или иной получатель ГПЗУ';
comment on column uplp_doc.recipient_type is
    'Тип правообладателя или получателя ГПЗУ';
comment on column uplp_doc.administrative_area is
    'Административный округ';
comment on column uplp_doc.district is
    'Район (поселение)';
comment on column uplp_doc.building_address is
    'Строительный адрес';
comment on column uplp_doc.cadastral_no is
    'Кадастровый номер земельного участка (ЗУ)';
comment on column uplp_doc.tlp_project_availability is
    'Наличие проекта планоровки территории (ППТ) в границах ГПЗУ';
comment on column uplp_doc.tlp_document_details is
    'Реквизиты документа ППТ';
comment on column uplp_doc.surveying_project_availability is
    'Наличие отдельного проекта межевания территории в границах ГПЗУ';
comment on column uplp_doc.surveying_project_details is
    'Реквизиты документа проекта межевания';
comment on column uplp_doc.sut_group_name is
    'Наименование условной группы использования ЗУ по ВРИ';
comment on column uplp_doc.sut_codes is
    'Коды основных видов разрешенного использования (ВРИ) зумельного участка (ЗУ)';
comment on column uplp_doc.plot_area is
    'Площадь земельного участка (ЗУ), кв.м';
comment on column uplp_doc.subzones_availability is
    'Наличие подзон ЗУ, номера';
comment on column uplp_doc.subzones_area is
    'Площади подзон ЗУ, кв.м';
comment on column uplp_doc.building_max_height is
    'Максимальная высота застройки, м';
comment on column uplp_doc.building_max_floors is
    'Максимальное количество этажей, шт';
comment on column uplp_doc.built_up_area_percentage is
    'Максимальный процент застроенности, %';
comment on column uplp_doc.building_density is
    'Максимальная плотность застройки, тыс. кв.м/га';
comment on column uplp_doc.cbo_purpose is
    'Назначение объектов капитального строительства (ОКС)';
comment on column uplp_doc.cbo_description is
    'Наименование, описание ОКС';
comment on column uplp_doc.objects_not_under_construction_standarts is
    'Наличие объектов, на которые действие градостроительного регламента не распространяется или не устанавливается';
comment on column uplp_doc.total_floor_area is
    'Общая суммарная поэтажная площадь строящихся ОКС, кв.м';
comment on column uplp_doc.residential_development_floor_area is
    'Суммарная поэтажная площадь строящихся объектов жилой застройки, кв.м';
comment on column uplp_doc.non_residential_development_floor_area is
    'Суммарная поэтажная площадь строящихся объектов нежилой застройки, кв.м';
comment on column uplp_doc.residential_premises_floor_area is
    'Суммарная поэтажная площадь строящихся жилых помещений, кв.м';
comment on column uplp_doc.non_residential_premises_floor_area is
    'Суммарная поэтажная площадь строящихся встроенно-пристроенных, отдельно стоящих нежилых помещений, кв.м';
comment on column uplp_doc.total_building_area is
    'Общая площадь строящихся ОКС, кв.м';
comment on column uplp_doc.total_residential_development_area is
    'Общая площадь строящихся объектов жилой застройки, кв.м';
comment on column uplp_doc.total_non_residential_development_area is
    'Общая площадь строящихся объектов нежилой застройки, кв.м';
comment on column uplp_doc.total_residential_premises_area is
    'Общая площадь строящихся жилых помещений, кв.м';
comment on column uplp_doc.total_non_residential_premises_area is
    'Общая площадь строящихся встроенно-пристроенных, отдельно стоящих нежилых помещений, кв.м';
comment on column uplp_doc.total_underground_space_area is
    'Общая площадь подземного пространства, кв.м';
comment on column uplp_doc.subzones_total_floor_area is
    'Общая суммарная поэтажная площадь строящихся ОКС по всем подзонам, кв.м';
comment on column uplp_doc.subzones_residential_objects_floor_area is
    'Общая суммарная поэтажная площадь строящихся жилых объектов по всем подзонам, кв.м';
comment on column uplp_doc.subzones_non_residential_objects_floor_area is
    'Общая суммарная поэтажная площадь строящихся нежилых объектов по всем подзонам, кв.м';
comment on column uplp_doc.subzones_residential_premises_floor_area is
    'Общая суммарная поэтажная площадь строящихся жилых помещений по всем подзонам, кв.м';
comment on column uplp_doc.subzones_non_residential_premises_floor_area is
    'Общая суммарная поэтажная площадь строящихся встроенно-пристроенных, отдельно стоящих нежилых помещений по всем подзонам, кв.м';
comment on column uplp_doc.subzones_total_objects_area is
    'Общая площадь строящихся ОКС по всем подзонам, кв.м';
comment on column uplp_doc.subzones_total_residential_objects_area is
    'Общая площадь строящихся жилых объектов по всем подзонам, кв.м';
comment on column uplp_doc.subzones_total_non_residential_objects_area is
    'Общая площадь строящихся нежилых объектов по всем подзонам, кв.м';
comment on column uplp_doc.subzones_total_residential_premises_area is
    'Общая площадь строящихся жилых помещений по всем подзонам, кв.м';
comment on column uplp_doc.subzones_total_non_residential_premises_area is
    'Общая площадь строящихся встроенно-пристроенных, отдельно стоящих нежилых помещений по всем подзонам, кв.м';
comment on column uplp_doc.subzones_total_underground_space_area is
    'Общая площадь поздемного пространства по всем подзонам, кв.м';
comment on column uplp_doc.existing_cbo_availability is
    'Наличие или отсутствие существующих на ЗУ ОКС';
comment on column uplp_doc.existing_cbo_total_count is
    'Общее число существующих на ЗУ ОКС, единиц';
comment on column uplp_doc.existing_cbo_purpose is
    'Назначение существующих ОКС';
comment on column uplp_doc.existing_cbo_description is
    'Наименование, описание существующих ОКС';
comment on column uplp_doc.existing_cbo_max_floor_count is
    'Максимальное число наземных этажей существующих ОКС, шт';
comment on column uplp_doc.existing_cbo_total_area is
    'Общая';
comment on column uplp_doc.existing_cbo_residential_objects_area is
    'Жилых объектов';
comment on column uplp_doc.existing_cbo_non_residential_objects_area is
    'Нежилых объектов';
comment on column uplp_doc.clo_availability is
    'Наличие или отсутствие существующих на ЗУ ОКН';
comment on column uplp_doc.clo_total_count is
    'Общее число существующих на ЗУ ОКН';
comment on column uplp_doc.clo_description is
    'Наименование, описание ОКН';
comment on column uplp_doc.clo_identification_no is
    'Идентификационный номер ОКН';
comment on column uplp_doc.clo_registration_no is
    'Регистрационный номер ОКН';
comment on column uplp_doc.operator_name is
    'Исполнитель (оператор), ФИО';
comment on column uplp_doc.actualisation_date is
    'Дата актуализации документа';
comment on column uplp_doc.file_reference is
    'Ссылка на исходный файл в системе';
comment on column uplp_doc.object_zone_no is
    'Номер зоны объектов';
comment on column uplp_doc.uplp_log_id is
    'ИД лога обработки';

create table if not exists uplp_log
(
    uplp_log_id                                    serial       not null,
    file_name                                      varchar(256) not null,
    load_dttm                                      timestamptz  not null,
    process_dttm                                   timestamptz  not null,
    process_result                                 varchar(128) not null,
    errors                                         text         null,
    constraint pk_uplp_log primary key (uplp_log_id)
);

comment on table uplp_log is
    'Логи обработки документа ГПЗУ';
comment on column uplp_log.uplp_log_id is
    'ИД лога обработки';
comment on column uplp_log.file_name is
    'Наименование загруженного файла';
comment on column uplp_log.load_dttm is
    'Дата и время загрузки файла';
comment on column uplp_log.process_dttm is
    'Дата и время обработки файла';
comment on column uplp_log.process_result is
    'Результат обработки Успешно/Неуспешно';
comment on column uplp_log.errors is
    'Ошибки при обработке';

alter table uplp_doc
    add constraint fk_uplp_doc_uplp_log foreign key (uplp_log_id)
        references uplp_log (uplp_log_id)
        on delete restrict on update restrict;