package tech.challenge.establishment.manager.controller;

import tech.challenge.establishment.manager.dtos.user.CreateUserDTO;
import tech.challenge.establishment.manager.dtos.user.UpdateUserDTO;
import tech.challenge.establishment.manager.dtos.user.UserResponseDTO;
import tech.challenge.establishment.manager.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(UserResponseDTO.from(adminService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> save(@RequestBody @Valid CreateUserDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.from(adminService.save(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody @Valid UpdateUserDTO dto) {
        return ResponseEntity.ok(UserResponseDTO.from(adminService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adminService.delete(id);
        return ResponseEntity.noContent().build();
    }
}