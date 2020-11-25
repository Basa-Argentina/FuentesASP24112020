package com.aconcaguasf.basa.digitalize.controller;

import com.aconcaguasf.basa.digitalize.bussiness.FiltersBussiness;
import com.aconcaguasf.basa.digitalize.dto.ctrl.ClienteEmpDTO;
import com.aconcaguasf.basa.digitalize.dto.ctrl.ResponseDTO;
import com.aconcaguasf.basa.digitalize.exceptions.CustomException;
import com.aconcaguasf.basa.digitalize.model.*;
import com.aconcaguasf.basa.digitalize.repository.*;
import com.aconcaguasf.basa.digitalize.util.Commons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
public class FiltersController {
	private final static Logger LOGGER = Logger.getLogger("bitacora.subnivel.under.InternalSys");


	@Autowired
	private FiltersBussiness  filtersBussiness;

	@Autowired
	private ClientesAspRepository clientesAspRepository;

	@Autowired
	private PersonasJuridicasRepository personasJuridicasRepository;

	@Autowired
	private HojaRutaRepository hojaRutaRepository;

	@Autowired
	private TipoOperacionesRepository tipoOperacionesRepository;

	@Autowired
	private TipoRequerimientoRepository tipoRequerimientoRepository;

	@Autowired
	private DepositosRepository depositosRepository;

	@Autowired
	private ClienteEmpRepository clienteEmpRepository;

	@Autowired
	private ConceptosFacturablesRepository conceptosFacturablesRepository;


	/**
	 * Service to get every Cliente for select textbox in Requerimiento view
	 *
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/clientes_asp/read", method = RequestMethod.GET)
	public List<ClientesAsp> readArticulos() {

		return  clientesAspRepository.findAll();
	}
	/**
	 * Service to get every PersonaJuridica for select textbox in Requerimiento view
	 *
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/personas_juridicas/read", method = RequestMethod.GET)
	public List<Object> personasJuridicas(@AuthenticationPrincipal Principal principal) {

		return  clienteEmpRepository.findPersonsaJuridicas();
	}

	@ResponseBody
	@RequestMapping(value = "clientes_emp/get_all")
	public ResponseEntity getClientesEmp(@AuthenticationPrincipal Principal principal){
		try {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(Commons.INSTANCE.getCommonGson()
							.toJson(filtersBussiness.findClienteEmp()));
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Commons.INSTANCE.getCommonGson()
							.toJson(new ResponseDTO(CustomException.getInstance().getCode(), CustomException.getInstance().getMessage())));
		}
	}

	/**
	 *
	 *Service for "Select textbox"
	 * from hoja_ruta.html
	 * to show possible Hojas de Ruta
	 *
	 */

	@ResponseBody
	@RequestMapping(value = "/hoja_de_ruta/distinct", method = RequestMethod.GET)
	public ResponseEntity hojaRutas(@AuthenticationPrincipal Principal principal){
		try {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(Commons.INSTANCE.getCommonGson()
							.toJson(filtersBussiness.findHojaRuta()));
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Commons.INSTANCE.getCommonGson()
							.toJson(new ResponseDTO(CustomException.getInstance().getCode(), CustomException.getInstance().getMessage())));
		}
	}


	/**
	 * Search and list disctinc Tipo de Operaciones for
	 * tipo_operaciones�s filter from main view
	 *
	 * @param principal
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/operaciones/tipos", method = RequestMethod.GET)
	public List<TipoOperaciones> tipoOps(@AuthenticationPrincipal Principal principal) {

		return tipoOperacionesRepository.findDistinctByTipoOperacion();
	}

	/**
	 * Service to get every tipoRequerimiento for select textbox in Requerimiento view
	 *
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/requerimiento/tipo_view", method = RequestMethod.GET)
	public List<TipoRequerimiento> tipoRequerimiento (@AuthenticationPrincipal Principal principal) {
		return tipoRequerimientoRepository.findTipoRequerimientoByClienteasp().stream().sorted(Comparator.comparing(TipoRequerimiento::getDescripcion)).collect(Collectors.toList());
	}

	/**
	 * Service to get every Depositos for select textbox in Requerimiento view
	 *
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/sucursales/depositos", method = RequestMethod.GET)
	public List<Depositos> despositos(@AuthenticationPrincipal Principal principal){

		return depositosRepository.findAll();
	}
	@ResponseBody
	@RequestMapping(value = "/conceptos/tipos", method = RequestMethod.GET)
	public List<ConceptosFacturables> conceptos(@AuthenticationPrincipal Principal principal){

		return conceptosFacturablesRepository.findAll();
	}
}

