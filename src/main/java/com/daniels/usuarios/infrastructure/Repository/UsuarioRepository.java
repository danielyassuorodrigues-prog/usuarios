package com.daniels.usuarios.infrastructure.Repository;


import com.daniels.usuarios.infrastructure.entity.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);

    //classe que utilizamos caso ele não ache uma informação no banco de dados, pois em vez de ele retornar um nullPointException, ele trata melhor este erro
    Optional<Usuario> findByEmail(String email);

    @Transactional
    void deleteByEmail(String email);
}
