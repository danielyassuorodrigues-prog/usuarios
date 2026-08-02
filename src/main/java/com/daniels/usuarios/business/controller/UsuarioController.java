package com.daniels.usuarios.business.controller;

import com.daniels.usuarios.business.UsuarioService;
import com.daniels.usuarios.business.dto.EnderecoDTO;
import com.daniels.usuarios.business.dto.TelefoneDTO;
import com.daniels.usuarios.business.dto.UsuarioDTO;
import com.daniels.usuarios.infrastructure.security.JwtUtil;
import com.daniels.usuarios.infrastructure.security.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Cadastra e faz login de usuários")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    @PostMapping
    @Operation(summary = "Salvar Usuários", description = "Cria um novo Usuário")
    @ApiResponse(responseCode = "200", description = "Usuário cadastrado com sucesso")
    @ApiResponse(responseCode = "409", description = "Usuário já cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    public ResponseEntity<UsuarioDTO>salvaUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));
    }

    @PostMapping("/login")
    @Operation(summary = "Login de Usuários", description = "Realiza o login dos usuários")
    @ApiResponse(responseCode = "200", description = "Usuário logado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    public String login(@RequestBody UsuarioDTO usuarioDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(),
                        usuarioDTO.getSenha()
                )
        );
        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }


    @GetMapping
    @Operation(summary = "Busca de usuários por email", description = "Realiza a busca de usuários por email")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    @ApiResponse(responseCode = "403", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }

    @DeleteMapping("/{email}")
    @Operation(summary = "Deleta usuário por email", description = "Faz a deleção de um usuário através do email")
    @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    @ApiResponse(responseCode = "403", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email){
        usuarioService.deletaUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Operation(summary = "Atualiza dados de usuário ja cadastrado", description = "faz a atualização de dados/informações de usuário ja cadastrados na plataforma")
    @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    @ApiResponse(responseCode = "403", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    public ResponseEntity<UsuarioDTO> atualizaDadosUsuario(@RequestBody UsuarioDTO dto , @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.atualizaDadosUsuario(token, dto));
    }

    @PutMapping("/endereco")
    @Operation(summary = "Atualiza os endereços", description = "Realiza a alteração do endereço de usuário")
    @ApiResponse(responseCode = "200", description = "Endereço alterado com sucesso")
    @ApiResponse(responseCode = "401", description = "ID inválido")
    @ApiResponse(responseCode = "403", description = "Endereço não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    public ResponseEntity<EnderecoDTO> atualizaEndereco(@RequestBody EnderecoDTO dto, @RequestParam("id") Long id) {
        return ResponseEntity.ok(usuarioService.atualizaEndereco(id, dto));
    }

    @PutMapping("/telefone")
    @Operation(summary = "Atualiza os telefones", description = "Realiza a alteração do telefone de usuário")
    @ApiResponse(responseCode = "200", description = "Telefone alterado com sucesso")
    @ApiResponse(responseCode = "401", description = "ID inválido")
    @ApiResponse(responseCode = "403", description = "Telefone não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    public ResponseEntity<TelefoneDTO> atualizaTelefone(@RequestBody TelefoneDTO dto, @RequestParam("id") Long id){
        return ResponseEntity.ok(usuarioService.atualizaTelefone(id, dto));
    }


    @PostMapping("/endereco")
    @Operation(summary = "Salva Endereço de usuário", description = "Salva endereço do usuário ja cadastrado")
    @ApiResponse(responseCode = "200", description = "Endereço cadastrado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credencias inválidas")
    @ApiResponse(responseCode = "403", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    public ResponseEntity<EnderecoDTO> cadastraEndereco(@RequestBody EnderecoDTO dto, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.cadastraEndereco(token , dto));
    }

    @PostMapping("/telefone")
    @Operation(summary = "Salva telefone de usuário", description = "Salva  telefone de usuário já cadastrado")
    @ApiResponse(responseCode = "200", description = "Telefone salvo com sucesso")
    @ApiResponse(responseCode = "401", description = "Credencias inválidas")
    @ApiResponse(responseCode = "403", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro no servido")
    public ResponseEntity<TelefoneDTO> cadastraTelefone(@RequestBody TelefoneDTO dto, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.cadastraTelefone(token, dto));
    }










}
