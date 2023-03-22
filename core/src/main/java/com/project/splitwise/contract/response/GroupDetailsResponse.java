package com.project.splitwise.contract.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.splitwise.entity.Expense;
import com.project.splitwise.entity.Split;
import com.project.splitwise.entity.User;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class GroupDetailsResponse {


    String nameOfGroup;

    @ManyToMany(mappedBy = "user_groups") // Bi-directional mapping
    Set<User> members;

    @OneToMany // Uni-directional mapping
    Map<String, Split> userSplits;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true) // Uni-directional mapping
    @JoinColumn(name = "expense_id")
    List<Expense> expense;
}
