package com.project.splitwise.entity;

import com.project.splitwise.convertor.ExpenseMetaDataConverter;
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
    @JoinColumn(name="user_id")
    User paidBy;

    Long amount;

    @OneToMany(mappedBy = "expense", fetch = FetchType.LAZY) // Bi-directional mapping
    List<Split> splits;

    @Convert(converter = ExpenseMetaDataConverter.class) // customer conversion
    @Column(columnDefinition = "jsonb")
    ExpenseMetaData metaData;

}
