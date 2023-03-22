package com.project.splitwise.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "users")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {

    String referenceId;

    String name;

    String email;

    String phoneNumber;

    @ManyToMany // owner side // mapping does not have owner in database so either way works
    @JoinTable(
        name = "user_group",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "split_group_id"))
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    @JsonManagedReference
    List<SplitGroup> userGroups = new ArrayList<>();

    @OneToMany(mappedBy = "id", fetch = FetchType.EAGER) // Uni directional relationship
    List<Split> owedUsers = new ArrayList<>();

    public void addGroup(SplitGroup newGroup) {
        userGroups.add(newGroup);
    }

    public void addOwesTo(User owedUser, Double amountOwed) {
        // TODO create split

//        if (userOwesTo.containsKey(owedUser.getId())) {
//            Double newAmountOwed = userOwesTo.get(owedUser.getId()) + amountOwed;
//            userOwesTo.put(owedUser.getId(), newAmountOwed);
//            return;
//        }
//        userOwesTo.put(owedUser.getId(), amountOwed);
    }

}
