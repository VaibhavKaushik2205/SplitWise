package com.project.splitwise.convertor;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.splitwise.entity.ExpenseMetaData;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import lombok.RequiredArgsConstructor;

@Converter
@RequiredArgsConstructor
public class ExpenseMetaDataConverter implements AttributeConverter<ExpenseMetaData, String> {

    private static final TypeReference<ExpenseMetaData> EXPENSE_METADATA_TYPE_REFERENCE =
        new TypeReference<>() {

        };

    private final ObjectMapper mapper;

    @Override
    public String convertToDatabaseColumn(ExpenseMetaData metaData) {
        try {
            if (isNull(metaData)) {
                return null;
            }
            return mapper.writeValueAsString(metaData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                "ProfessionDetail: Failed during convertToDatabaseColumn - ", e);
        }
    }

    @Override
    public ExpenseMetaData convertToEntityAttribute(String value) {
        if (isNull(value)) {
            return null;
        }
        try {
            return mapper.readValue(value, EXPENSE_METADATA_TYPE_REFERENCE);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                "ProfessionDetail: Failed during convertToEntityAttribute - ", e);
        }
    }

}
