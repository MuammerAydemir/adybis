package com.muammer.adybis.base.seeder;

import java.time.ZoneId;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.github.javafaker.Faker;
import com.muammer.adybis.role.models.Role;
import com.muammer.adybis.user.enums.BloodTypesEnums;
import com.muammer.adybis.user.models.User;

public class UserFactory {
    private static Faker faker = new Faker();

    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public static User.UserBuilder userBuilder() {
        return User.builder()
                .username(faker.harryPotter().character().toLowerCase())
                .email(faker.internet().emailAddress())
                .password(encoder.encode("user123?"))
                .phone("+90" + faker.numerify("5#########"))
                .birthdayDate(faker.date().birthday(15, 100).toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .bloodType(faker.options().option(BloodTypesEnums.class).label)
                .specialCaseDescription(faker.lorem().paragraph(7));

    }

    public static User createUser() {
        return userBuilder().build();
    }

    private static User.UserBuilder baseUserBuilder(List<Role> roles, String password, String name) {
        return userBuilder()
                .username(name.toLowerCase())
                .password(encoder.encode(password))
                .twoFAEnabled(true)
                .verified(true)
                .roles(roles);
    }

    public static User createAdmin(List<Role> roles, String password, String name) {
        return baseUserBuilder(roles, password, name)
                .build();
    }

    public static User createRescueTeam(List<Role> roles, String password, String name) {
        return baseUserBuilder(roles, password, name).build();
    }

    public static User createDispatcher(List<Role> roles, String password, String name) {
        return baseUserBuilder(roles, password, name).build();
    }

    public static User createViewer(List<Role> roles, String password, String name) {
        return baseUserBuilder(roles, password, name).build();
    }

    public static User createVictim(List<Role> roles, String password, String name) {
        return baseUserBuilder(roles, password, name).build();
    }

}
