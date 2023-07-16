

//package ru.practicum.shareit.user.dto;
//
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.json.JsonTest;
//import org.springframework.boot.test.json.JacksonTester;
//import org.springframework.boot.test.json.JsonContent;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@JsonTest
//class UserDTOTest {
//
//    @Autowired
//    private JacksonTester<UserDTO> json;
//
//    private String name;
//    private String email;
//
//    @BeforeEach
//    void setup() {
//        name = "Ivan";
//        email = "ivan@mail.ru";
//    }
//
//    @Test
//    void userDtoSerializeTest() throws Exception {
//        UserDTO userDTO = new UserDTO(1, name, email);
//        JsonContent<UserDTO> result = json.write(userDTO);
//
//        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(name);
//        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(email);
//    }
//
//    @Test
//    void userDTODeserializeTest() throws Exception {
//        String jsonContent = String.format("{\"name\":\"%s\", \"email\": \"%s\"}", name, email);
//        UserDTO result = this.json.parse(jsonContent).getObject();
//
//        assertThat(result.getName()).isEqualTo(name);
//        assertThat(result.getEmail()).isEqualTo(email);
//    }
//}