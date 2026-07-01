package com.daniels.usuarios.infrastructure.business;


import com.daniels.usuarios.infrastructure.Repository.UsuarioRepository;
import com.daniels.usuarios.infrastructure.business.converter.UsuarioConverter;
import com.daniels.usuarios.infrastructure.business.dto.UsuarioDTO;
import com.daniels.usuarios.infrastructure.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(repository.save(usuario));
    }



}
