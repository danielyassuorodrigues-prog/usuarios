package com.daniels.usuarios.business;


import com.daniels.usuarios.business.converter.UsuarioConverter;
import com.daniels.usuarios.business.dto.EnderecoDTO;
import com.daniels.usuarios.business.dto.TelefoneDTO;
import com.daniels.usuarios.business.dto.UsuarioDTO;
import com.daniels.usuarios.infrastructure.Repository.EnderecoRepository;
import com.daniels.usuarios.infrastructure.Repository.TelefoneRepository;
import com.daniels.usuarios.infrastructure.Repository.UsuarioRepository;
import com.daniels.usuarios.infrastructure.entity.Endereco;
import com.daniels.usuarios.infrastructure.entity.Telefone;
import com.daniels.usuarios.infrastructure.entity.Usuario;
import com.daniels.usuarios.infrastructure.exceptions.ConflictException;
import com.daniels.usuarios.infrastructure.exceptions.ResourceNotFoundException;
import com.daniels.usuarios.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;


     //metodo que salva usuario
    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        //verificamos se o email existe
        emailExists(usuarioDTO.getEmail());
        //encriptamos a senha
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        //convertemos o usuarioDTO para usuario, para podermos salvar no banco de dados
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        //salvamos no banco de dados como usuario e retornamos um DTO
        return usuarioConverter.paraUsuarioDTO(repository.save(usuario));
    }


    //verifica se o email existe
    public void emailExists(String email) {
        //tenta procurar o email e armazena em uma variavel
        try{
            boolean existe = verificaEmailExistente(email);

            //caso o email exista, irei lancar uma excessao
            if(existe) {
                throw new ConflictException("Email ja cadastrado " + email);
            }
            //possivel erro, lanço uma ecessão e motro a causa do problema
        } catch (ConflictException e) {
            throw new ConflictException("Email ja cadastrado " + e.getCause());
        }
    }
    //CHAMA O metodo pronto do JPA que está na repository
    public boolean verificaEmailExistente(String email) {
        return repository.existsByEmail(email);
    }

    //busca o usuario atraves do email, metodo pronto do JPA tambem
    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        //como pode não encontrar o email , lançamos uma ecessão caso não encontre nada
        try{
            return  usuarioConverter.paraUsuarioDTO(repository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Email não encontrado " + email)));
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException("Email não Encontrado " + e.getCause());
        }



    }

    //deleta o email
    public void deletaUsuarioPorEmail(String email) {
        repository.deleteByEmail(email);
    }


    //metodo para atualizar os dados do usuario, como não é obrigatório ele nos passar o email , então temos que extrair do token
    public UsuarioDTO atualizaDadosUsuario(String token , UsuarioDTO dto) {
        //extraimos o email do token , ja que o usuario não é obrigado a informar
        String email = jwtUtil.extraitEmailToken(token.substring(7));
        //encriptamos a senha novamente
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);
        //procuramos esse email no banco de dados , caso não encontre lançamos uma excessão, porém muito dificil não encontrar , ja que pegamos o token de um usuario
        Usuario usuarioEntity = repository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("Email não encontrado : " +email));

        //Mesclamos os dados do DTO com o do banco de dados, para caso o usuário não preencha algum campo. utilizaremos os dados anteriores
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);


        //encriptamos a senha novamente

        //convertemos de DTO para usuario, para podermos salvar no banco de dados e retornamos um DTO ao usuario
        return usuarioConverter.paraUsuarioDTO(repository.save(usuario));
    }

    public EnderecoDTO atualizaEndereco (Long idEndereco, EnderecoDTO enderecoDTO) {
        Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(() -> new ResourceNotFoundException("Id não encontrado"));

        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO , entity);

        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO telefoneDTO) {
        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(() -> new ResourceNotFoundException("Id não Encontrado"));

        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO, entity);

        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }





}
