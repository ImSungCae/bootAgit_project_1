package com.bootagit_project_1.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
//@Document(indexName = "users")
public class User{

    @Id
//    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Field(name = "id", type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Size(min = 3, message = "Username should have at least 3 characters")
//    @Field(type = FieldType.Keyword)
    private String username;

    @NotNull
    @Size(min = 6, message = "Password should have at least 6 characters")
    private String password;

    @NotNull
    @Email(message = "Email should be valid")
    private String email;

    public void setEmail(@NotNull @Email(message = "Email should be valid") String email) {
        this.email = email;
    }

    public void setPassword(@NotNull @Size(min = 6, message = "Password should have at least 6 characters") String password) {
        this.password = password;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile profile;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Friend> friends = new ArrayList<>();


}
