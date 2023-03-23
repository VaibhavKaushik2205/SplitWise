package com.project.splitwise.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "split_group")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SplitGroup extends BaseEntity {

    String groupReferenceId;

    String nameOfGroup;

    @ManyToMany(mappedBy = "userGroups") // Bi-directional mapping
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    @JsonBackReference
    List<User> members = new ArrayList<>();

    @Column(columnDefinition = "jsonb")
    String userSplits;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true) // Uni-directional mapping
    @JoinColumn(name = "id")
    List<Expense> expense;


}
