package com.project.splitwise.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.splitwise.convertor.ExpenseMetaDataConverter;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "expense")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Expense extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY) // Uni-directional mapping
    @JoinColumn(name="paid_by")
    @JsonBackReference
    User paidBy;

    Double amount;

    @OneToMany(mappedBy = "expense", fetch = FetchType.LAZY) // Bi-directional mapping
    @JsonManagedReference
    List<Split> splits;

    @Convert(converter = ExpenseMetaDataConverter.class) // MetaData Conversion
    @Column(columnDefinition = "jsonb")
    ExpenseMetaData metaData;

    @ManyToOne(fetch = FetchType.LAZY) // Uni-directional mapping
    @JoinColumn(name="group_id")
    @JsonBackReference
    SplitGroup group;

}
