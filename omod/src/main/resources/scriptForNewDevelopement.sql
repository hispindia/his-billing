/*added on 07-12-2016 : sagar bele */
UPDATE billing_patient_service_bill b 
INNER JOIN person_attribute pa ON pa.person_id=b.patient_id
AND pa.person_attribute_type_id=(SELECT p.`person_attribute_type_id`
                              FROM person_attribute_type p
                              WHERE p.`name`='Patient Category')
AND (b.created_date BETWEEN pa.date_created AND pa.date_voided
                            OR (b.created_date > pa.date_created AND pa.date_voided IS NULL))
SET b.patient_category=pa.value
WHERE b.patient_category IS NULL ;

update billing_ambulance_bill b
set b.voided_by = b.creator 
where b.voided is true 
and b.voided_by is null;


update billing_miscellaneous_service_bill b
set b.voided_by = b.creator 
where b.voided is true 
and b.voided_by is null;


update billing_patient_service_bill b
set b.voided_by = b.creator 
where b.voided is true 
and b.voided_by is null;


update billing_patient_service_bill_item bi
inner join billing_patient_service_bill b on bi.patient_service_bill_id=b.patient_service_bill_id
set bi.voided_by = b.creator 
where bi.voided is true 
and bi.voided_by is null;

update billing_tender_bill b
set b.voided_by = b.creator 
where b.voided is true 
and b.voided_by is null;


update billing_tender_bill_item bi
inner join billing_tender_bill b on bi.tender_bill_id=b.tender_bill_id
set bi.voided_by = b.creator 
where bi.voided is true 
and bi.voided_by is null;

