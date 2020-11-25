package com.aconcaguasf.basa.digitalize.bussiness;

import com.aconcaguasf.basa.digitalize.dto.ctrl.ClienteEmpDTO;
import com.aconcaguasf.basa.digitalize.dto.ctrl.HojaRutaDTO;
import com.aconcaguasf.basa.digitalize.repository.ClienteEmpRepository;
import com.aconcaguasf.basa.digitalize.repository.HojaRutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("filtersBussiness")
public class FiltersBussiness {

    @Autowired
    private HojaRutaRepository hojaRutaRepository;
    @Autowired
    private ClienteEmpRepository clienteEmpRepository;

    public List<HojaRutaDTO> findHojaRuta() throws Exception{
        List<HojaRutaDTO> hojaRutaDTOList = new ArrayList<>();
        hojaRutaRepository.findDistinctByNumeroOrderByNumeroDesc()
                .forEach(s-> hojaRutaDTOList.add(new HojaRutaDTO(s)));
        return hojaRutaDTOList;
    }

    public List<ClienteEmpDTO> findClienteEmp() throws Exception{
        List<ClienteEmpDTO> clienteEmpDTOList = new ArrayList<>();
        clienteEmpRepository.findAll(new Sort(Sort.Direction.ASC, "personasJuridicas.razonSocial"))
            .forEach(clienteEmp ->
                clienteEmpDTOList.add(
                    new ClienteEmpDTO(
                        clienteEmp.getPersonasJuridicas().getId(),
                        clienteEmp.getPersonasJuridicas().getRazonSocial() == null ? "-- Cliente sin descripción --" : clienteEmp.getPersonasJuridicas().getRazonSocial(),
                        clienteEmp.getCodigo()
                    )
                )
            );
        return clienteEmpDTOList;
    }

}
