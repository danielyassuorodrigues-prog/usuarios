package com.daniels.usuarios.business;


import com.daniels.usuarios.business.dto.ViaCepDTO;
import com.daniels.usuarios.infrastructure.client.ViaCepClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ViaCepService {
    private final ViaCepClient client;

    public ViaCepDTO buscarDadosEndereco(String cep){
        return client.buscaDadosEndereco(processarCep(cep));
    }

    private String processarCep(String cep){
        String cepFormatado = cep.replace(" ", "").replace("-", "");

        if(!cepFormatado.matches("\\d+") || !Objects.equals(cepFormatado.length(), 8)){
            throw new IllegalArgumentException("O CEP possui caracteres especiais, favor verificar");
        }
        return cepFormatado;


    }




}
