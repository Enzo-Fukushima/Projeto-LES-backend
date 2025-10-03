package com.enzo.les.les.service;

import com.enzo.les.les.dtos.CupomUseDTO;
import org.springframework.stereotype.Service;

@Service
public class CupomService {
    public CupomUseDTO validarCupom(String codigo) {
        // TODO: Verificar validade do cupom (existência, expiração, regras de uso)
        return new CupomUseDTO();
    }
}
