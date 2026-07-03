package com.daniels.usuarios.business;


import com.daniels.usuarios.business.converter.UsuarioConverter;
import com.daniels.usuarios.business.dto.UsuarioDTO;
import com.daniels.usuarios.infrastructure.Repository.UsuarioRepository;
import com.daniels.usuarios.infrastructure.entity.Usuario;
import com.daniels.usuarios.infrastructure.exceptions.ConflictException;
import com.daniels.usuarios.infrastructure.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExists(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(repository.save(usuario));
    }



    public void emailExists(String email) {
        try{
            boolean existe = verificaEmailExistente(email);

            if(existe) {
                throw new ConflictException("Email ja cadastrado " + email);
            }
        } catch (ConflictException e) {
            throw new ConflictException("Email ja cadastrado " + e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return repository.existsByEmail(email);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Email não encontrado " + email));
    }

    public void deletaUsuarioPorEmail(String email) {
        repository.deleteByEmail(email);
    }





}
