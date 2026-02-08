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
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
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
    class GetManagerAccessTokenTests {

        @Test
        @DisplayName("Should generate valid access token for manager")
        void shouldGenerateValidAccessTokenForManager() throws Exception {
            String adminToken = getAdminAccessToken();
            Manager manager = createManager.process("John", "Doe", testManagerEmail, testManagerPassword);

            String token = authTestClient.getManagerAccessTokenAndReturn(manager.getId(), adminToken);

            assertThat(token).isNotNull().isNotEmpty();

            Me me = authTestClient.getMeAndReturnResponse(token);
            assertThat(me.userId()).isEqualTo(manager.getId());
            assertThat(me.role()).isEqualTo(UserRole.MANAGER.toString());
            assertThat(me.email()).isEqualTo(testManagerEmail);
        }

        @Test
        @DisplayName("Should return Not Found for non-existent manager")
        void shouldReturnNotFoundForNonExistentManager() throws Exception {
            String adminToken = getAdminAccessToken();
            long nonExistentManagerId = 99999L;

            authTestClient.getManagerAccessToken(nonExistentManagerId, adminToken)
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should generate different tokens for different managers")
        void shouldGenerateDifferentTokensForDifferentManagers() throws Exception {
            String adminToken = getAdminAccessToken();
            Manager manager1 = createManager.process("John", "Doe", "manager1@gmail.com", "password1");
            Manager manager2 = createManager.process("Jane", "Smith", "manager2@gmail.com", "password2");

            String token1 = authTestClient.getManagerAccessTokenAndReturn(manager1.getId(), adminToken);
            String token2 = authTestClient.getManagerAccessTokenAndReturn(manager2.getId(), adminToken);

            assertThat(token1).isNotEqualTo(token2);

            Me me1 = authTestClient.getMeAndReturnResponse(token1);
            Me me2 = authTestClient.getMeAndReturnResponse(token2);

            assertThat(me1.userId()).isEqualTo(manager1.getId());
            assertThat(me1.email()).isEqualTo("manager1@gmail.com");
            assertThat(me2.userId()).isEqualTo(manager2.getId());
            assertThat(me2.email()).isEqualTo("manager2@gmail.com");
        }

        @Test
        @DisplayName("Should handle invalid manager ID format")
        void shouldHandleInvalidManagerIdFormat() throws Exception {
            String adminToken = getAdminAccessToken();
            authTestClient.getManagerAccessToken(-1L, adminToken)
                    .andExpect(status().isNotFound());
        }

        private String getAdminAccessToken() throws Exception {
            createAdministrator.process(testAdminEmail, testAdminPassword);
            return authTestClient.loginAdminAndGetToken(
                    new AuthRequest(testAdminEmail, testAdminPassword)
            );
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
