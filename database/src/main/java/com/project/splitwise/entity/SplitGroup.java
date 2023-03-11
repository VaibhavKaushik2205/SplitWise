package com.project.splitwise.entity;


import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "split_group")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SplitGroup extends BaseEntity {

    String nameOfGroup;

    @ManyToMany(mappedBy = "user_groups") // Bi-directional mapping
    Set<User> members;

    @OneToMany
    List<Expense> expense; // Uni-directional mapping

    @OneToMany
    List<Split> userSplits; // Uni-directional mapping

}
