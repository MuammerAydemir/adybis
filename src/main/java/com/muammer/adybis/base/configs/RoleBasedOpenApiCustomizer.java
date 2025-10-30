// FIXME: Swagger-ui is throwing an error on the first connection. This is
// because there is no anonymous user access. It will be fixed in the future.

// package com.muammer.adybis.base.configs;

// import
// org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
// import org.springframework.security.core.Authentication;
// import org.springframework.stereotype.Component;

// import com.muammer.adybis.base.auth.AuthUtils;

// import io.swagger.v3.oas.models.OpenAPI;
// import lombok.extern.slf4j.Slf4j;

// import java.util.Arrays;
// import java.util.List;

// import org.springdoc.core.customizers.OpenApiCustomizer;

// import org.springdoc.core.customizers.OpenApiCustomizer;
// import io.swagger.v3.oas.models.OpenAPI;
// import io.swagger.v3.oas.models.Operation;
// import io.swagger.v3.oas.models.PathItem;
// import
// org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

// import java.util.Arrays;
// import java.util.List;
// import java.util.Map;
// import java.util.Objects;

// // @Component
// // @Slf4j
// // public class RoleBasedOpenApiCustomizer implements OpenApiCustomizer {

// @Override
// public void customise(OpenAPI openApi) {
// Authentication auth = AuthUtils.currentAuth();

// // Anonymous kullanıcılar için tüm endpoint'leri göster
// if (auth == null || !auth.isAuthenticated()) {
// // Sadece public endpoint'leri göster veya tümünü göster
// openApi.getPaths().entrySet()
// .removeIf(entry -> !isPublicEndpoint(entry.getValue()));
// return;
// }

// List<String> currentUserRoles = AuthUtils.getCurrentUserRoles();

// openApi.getPaths().entrySet()
// .removeIf(entry -> !hasAccessibleOperation(entry.getValue(),
// currentUserRoles));
// }

// private boolean isPublicEndpoint(PathItem pathItem) {
// // Public endpoint kontrolü - extensions'da public işaretli olanlar
// return List.of(
// pathItem.getGet(), pathItem.getPost(), pathItem.getPut(),
// pathItem.getDelete(), pathItem.getPatch(), pathItem.getHead(),
// pathItem.getOptions(), pathItem.getTrace()).stream()
// .filter(Objects::nonNull)
// .anyMatch(operation -> {
// Map<String, Object> extensions = operation.getExtensions();
// return extensions != null && "public".equals(extensions.get("access"));
// });
// }

// private boolean hasAccessibleOperation(PathItem pathItem, List<String>
// currentUserRoles) {
// // Tüm HTTP metodları için operasyonları kontrol et
// return List.of(
// pathItem.getGet(), pathItem.getPost(), pathItem.getPut(),
// pathItem.getDelete(), pathItem.getPatch(), pathItem.getHead(),
// pathItem.getOptions(), pathItem.getTrace()).stream()
// .filter(Objects::nonNull) // null olmayan operasyonları filtrele
// .anyMatch(operation -> hasRoleAccess(operation, currentUserRoles));
// }

// private boolean hasRoleAccess(Operation operation, List<String>
// currentUserRoles) {
// // Extensions null olabilir, bu durumu kontrol et
// Map<String, Object> extensions = operation.getExtensions();
// if (extensions == null) {
// return false; // Extension yoksa erişim yok
// }

// Object rolesObj = extensions.get("roles");
// if (rolesObj == null) {
// return false; // Roles extension yoksa erişim yok
// }

// List<String> opRoles = parseRoles(rolesObj.toString());
// if (opRoles.isEmpty()) {
// return false; // Tanımlı rol yoksa erişim yok
// }

// // Kullanıcının en az bir required role sahip olup olmadığını kontrol et
// return opRoles.stream().anyMatch(currentUserRoles::contains);
// }

// private List<String> parseRoles(String rolesString) {
// return Arrays.stream(rolesString.split(","))
// .map(String::trim)
// .filter(s -> !s.isEmpty())
// .toList();
// }
// }