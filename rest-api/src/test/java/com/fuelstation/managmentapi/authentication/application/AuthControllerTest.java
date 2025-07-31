package com.fuelstation.managmentapi.authentication.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.fuelstation.managmentapi.administrator.application.usecases.CreateAdministrator;
import com.fuelstation.managmentapi.administrator.domain.Administrator;
import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.manager.application.usecases.CreateManager;
import com.fuelstation.managmentapi.manager.domain.Manager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
public class AuthControllerTest {

    @Autowired
    private CreateAdministrator createAdministrator;

    @Autowired
    private CreateManager createManager;

    @Autowired
    private AuthTestClient authTestClient;

    private final String testAdminEmail = "test-admin@gmail.com";
    private final String testAdminPassword = "test-admin-password";
    private final String testManagerEmail = "test-manager@gmail.com";
    private final String testManagerPassword = "test-manager-password";

    @Nested
    class LoginAdminTests {

        @Test
        @DisplayName("Should login admin")
        void shouldLoginAdmin() throws Exception {
            createAdministrator.process(testAdminEmail, testAdminPassword);

            authTestClient.loginAdmin(new AuthRequest(testAdminEmail, testAdminPassword))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should return Unauthorized for invalid credentials")
        void shouldReturnUnauthorizedForInvalidCredentials() throws Exception {
            createAdministrator.process(testAdminEmail, testAdminPassword);

            authTestClient.loginAdmin(new AuthRequest(testAdminEmail, "wrongPassword"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Should return Unauthorized for non-existent admin")
        void shouldReturnUnauthorizedForNonExistentAdmin() throws Exception {
            authTestClient.loginAdmin(new AuthRequest("nonexistent@example.com", "password"))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @MethodSource("invalidLoginRequests")
        @DisplayName("Should return Bad Request for invalid requests")
        void shouldReturnBadRequestForInvalidRequests(AuthRequest request) throws Exception {
            authTestClient.loginAdmin(request).andExpect(status().isBadRequest());
        }

        private static Stream<Arguments> invalidLoginRequests() {
            return Stream.of(
                    Arguments.of(new AuthRequest(null, "password"), "null email"),
                    Arguments.of(new AuthRequest("", "password"), "empty email"),
                    Arguments.of(new AuthRequest("   ", "password"), "blank email"),
                    Arguments.of(new AuthRequest("invalid-email", "password"), "invalid email format"),
                    Arguments.of(new AuthRequest("test@example.com", null), "null password"),
                    Arguments.of(new AuthRequest("test@example.com", ""), "empty password"),
                    Arguments.of(new AuthRequest("test@example.com", "   "), "blank password"),
                    Arguments.of(new AuthRequest("A".repeat(256) + "@example.com", "password"), "email too long"),
                    Arguments.of(new AuthRequest("test@example.com", "A".repeat(256)), "password too long"),
                    Arguments.of(new AuthRequest(null, null), "both fields null")
            );
        }

    }

    @Nested
    class LoginManagerTests {

        @Test
        @DisplayName("Should login manager")
        void shouldLoginManager() throws Exception {
            createManager.process("John", "Doe", testManagerEmail, testManagerPassword);

            authTestClient.loginManager(new AuthRequest(testManagerEmail, testManagerPassword))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should return Unauthorized for invalid credentials")
        void shouldReturnUnauthorizedForInvalidCredentials() throws Exception {
            createManager.process("John", "Doe", testAdminEmail, testAdminPassword);

            authTestClient.loginManager(new AuthRequest(testAdminEmail, "wrongPassword"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Should return Unauthorized for non-existent admin")
        void shouldReturnUnauthorizedForNonExistentAdmin() throws Exception {
            authTestClient.loginManager(new AuthRequest("nonexistent@example.com", "password"))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @MethodSource("invalidLoginRequests")
        @DisplayName("Should return Bad Request for invalid requests")
        void shouldReturnBadRequestForInvalidRequests(AuthRequest request) throws Exception {
            authTestClient.loginManager(request).andExpect(status().isBadRequest());
        }

        private static Stream<Arguments> invalidLoginRequests() {
            return Stream.of(
                    Arguments.of(new AuthRequest(null, "password"), "null email"),
                    Arguments.of(new AuthRequest("", "password"), "empty email"),
                    Arguments.of(new AuthRequest("   ", "password"), "blank email"),
                    Arguments.of(new AuthRequest("invalid-email", "password"), "invalid email format"),
                    Arguments.of(new AuthRequest("test@example.com", null), "null password"),
                    Arguments.of(new AuthRequest("test@example.com", ""), "empty password"),
                    Arguments.of(new AuthRequest("test@example.com", "   "), "blank password"),
                    Arguments.of(new AuthRequest("A".repeat(256) + "@example.com", "password"), "email too long"),
                    Arguments.of(new AuthRequest("test@example.com", "A".repeat(256)), "password too long"),
                    Arguments.of(new AuthRequest(null, null), "both fields null")
            );
        }

    }

    @Nested
    class GetMeTests {

        @Test
        @DisplayName("Should get admin")
        void shouldGetAdmin() throws Exception {
            Administrator administrator = createAdministrator.process(testAdminEmail, testAdminPassword);

            String token = authTestClient.loginAdminAndGetToken(new AuthRequest(testAdminEmail, testAdminPassword));
            Me me = authTestClient.getMeAndReturnResponse(token);

            assertThat(me.userId()).isEqualTo(administrator.getId());
            assertThat(me.role()).isEqualTo(UserRole.ADMINISTRATOR.toString());
            assertThat(me.email()).isEqualTo(testAdminEmail);
        }

        @Test
        @DisplayName("Should get manager")
        void shouldGetManager() throws Exception {
            Manager manager = createManager.process("John", "Doe", testManagerEmail, testManagerPassword);

            String token = authTestClient.loginManagerAndGetToken(new AuthRequest(testManagerEmail, testManagerPassword));
            Me me = authTestClient.getMeAndReturnResponse(token);

            assertThat(me.userId()).isEqualTo(manager.getId());
            assertThat(me.role()).isEqualTo(UserRole.MANAGER.toString());
            assertThat(me.email()).isEqualTo(testManagerEmail);
        }


    }

    @Nested
    class SameEmailTests {

        private final String sharedEmail = "shared@gmail.com";
        private final String adminPassword = "admin-password";
        private final String managerPassword = "manager-password";

        @Test
        @DisplayName("Should handle same email for admin and manager - admin login")
        void shouldHandleSameEmailAdminLogin() throws Exception {
            Administrator administrator = createAdministrator.process(sharedEmail, adminPassword);
            createManager.process("John", "Doe", sharedEmail, managerPassword);

            // Login as admin
            String adminToken = authTestClient.loginAdminAndGetToken(new AuthRequest(sharedEmail, adminPassword));
            Me adminMe = authTestClient.getMeAndReturnResponse(adminToken);

            // Verify admin identity
            assertThat(adminMe.userId()).isEqualTo(administrator.getId());
            assertThat(adminMe.role()).isEqualTo(UserRole.ADMINISTRATOR.toString());
            assertThat(adminMe.email()).isEqualTo(sharedEmail);
        }

        @Test
        @DisplayName("Should handle same email for admin and manager - manager login")
        void shouldHandleSameEmailManagerLogin() throws Exception {
            createAdministrator.process(sharedEmail, adminPassword);
            Manager manager = createManager.process("John", "Doe", sharedEmail, managerPassword);

            // Login as manager
            String managerToken = authTestClient.loginManagerAndGetToken(new AuthRequest(sharedEmail, managerPassword));
            Me managerMe = authTestClient.getMeAndReturnResponse(managerToken);

            // Verify manager identity
            assertThat(managerMe.userId()).isEqualTo(manager.getId());
            assertThat(managerMe.role()).isEqualTo(UserRole.MANAGER.toString());
            assertThat(managerMe.email()).isEqualTo(sharedEmail);
        }

    }
}
