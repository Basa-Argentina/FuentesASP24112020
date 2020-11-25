package com.aconcaguasf.basa.digitalize.controller;

import com.aconcaguasf.basa.digitalize.dto.ConceptosFilter;
import com.aconcaguasf.basa.digitalize.model.RelacionReqConF;
import com.aconcaguasf.basa.digitalize.repository.ElementosRepository;
import com.aconcaguasf.basa.digitalize.repository.RelacionReqConRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;


@Controller
public class ConceptosController {

    @Autowired
    private ElementosRepository elementosRepository;
    @Autowired
    private RelacionReqConRepository relacionReqConRepository;

    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/conceptos/filter", method = RequestMethod.POST)
    public  Page<RelacionReqConF> conceptosFilters(@RequestBody final ConceptosFilter filtro, @AuthenticationPrincipal Principal principal) throws Exception {
        Page<RelacionReqConF> relacionReqConFPage = null;

        if (principal == null)
            throw new Exception("Sesi�n expirada.");

        if (filtro.getCliente() == null) filtro.setCliente("%%");

        filtro.setConcepto_id((filtro.getConcepto_id() == null || filtro.getConcepto_id().equals("0")) ? ("%%") : (filtro.getConcepto_id()));

        Pageable topTen = new PageRequest(filtro.getPage(), filtro.getSize(), new Sort(Sort.Direction.DESC, "id"));

        relacionReqConFPage = relacionReqConRepository.findByFilters(filtro.getSucursal(), filtro.getConcepto_id(), filtro.getCliente(), filtro.getFechaDesde(), filtro.getFechaHasta(), topTen);


        return relacionReqConFPage;
    }

    @PreAuthorize("hasAuthority('2')")
    @ResponseBody
    @RequestMapping(value = "/conceptos/update_state", method = RequestMethod.POST)
    public void operacionesFilters(@RequestBody final List<RelacionReqConF> relacionReqConFList, @AuthenticationPrincipal Principal principal) {


        relacionReqConRepository.save(relacionReqConFList);
    }

}
