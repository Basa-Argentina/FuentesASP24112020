package com.aconcaguasf.basa.digitalize.controller;

import com.aconcaguasf.basa.digitalize.Application;
import com.aconcaguasf.basa.digitalize.dto.ctrl.ReportDTO;
import com.aconcaguasf.basa.digitalize.dto.reporte.HojaRutaReporte;
import com.aconcaguasf.basa.digitalize.dto.reporte.OperacionElementoReporte;
import com.aconcaguasf.basa.digitalize.dto.reporte.RequerimientoReporte;
import com.aconcaguasf.basa.digitalize.model.*;
import com.aconcaguasf.basa.digitalize.repository.*;
import com.aconcaguasf.basa.digitalize.util.Commons;
import com.google.gson.internal.LinkedTreeMap;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.Principal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.jdbc.datasource.DataSourceUtils;

@Controller
public class ReportesController {

    @Autowired
    OperacionesRepository operacionesRepository;
    @Autowired
    ElementosRepository elementosRepository;
    @Autowired
    HojaRutaRepository hojaRutaRepository;
    @Autowired
    HdRxOperacionRepository hdRxOperacionRepository;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ConfigReportRepository configReportRepository;

    @Autowired
    private ConfigReportParamRepository configReportParamRepository;

    @ResponseBody
    @RequestMapping(value = "/reporte/imprimir", method = RequestMethod.POST, produces = "application/pdf")
    public void imprimirReporte(@AuthenticationPrincipal Principal principal,
                                //@PathVariable("numReq") Integer numReq,
                                @RequestBody String reporteJson,
                                HttpServletResponse response)
    {
        // -- http://localhost:8090/reporte/remito/7488/imprimir
        //C:\Users\ecaceres\Documents\Jasper\rpt_ImpresionOperacion.jasper
        //C:\Users\ecaceres\Documents\Jasper\rpt_ImpresionOperacion.jrxml

        ReportDTO reportDTO = Commons.INSTANCE.getCommonGson().fromJson(reporteJson, ReportDTO.class);
        ConfigReport configReport = configReportRepository.findByReportName(reportDTO.getReportName());

        if (configReport != null){
            try{
                Connection connection          = jdbcTemplate.getDataSource().getConnection();
                JasperReport jasperReport      = JasperCompileManager.compileReport(configReport.getFileJrxml());
                Map<String, Object> parameters = new HashMap<>();

                configReport.getParams().forEach( param -> {
                    parameters.put(param.getParamName(), this.castVariable(reportDTO.getParams().get(param.getParamOrder()), param.getParamType()));
                });

                parameters.put("copia","ORIGINAL");
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
                parameters.put("copia","DUPLICADO");
                JasperPrint jasperPrint2 = JasperFillManager.fillReport(jasperReport, parameters, connection);

                //JRSaver.saveObject(jasperReport, "employeeReport.jasper");
                List pages = jasperPrint2 .getPages();
                for (int j = 0; j < pages.size(); j++) {
                    JRPrintPage object = (JRPrintPage)pages.get(j);
                    jasperPrint.addPage(object);

                }

                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=report.pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
                response.getOutputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/reporte/imprimirOpe", method = RequestMethod.POST, produces = "application/pdf")
    public void imprimirReporteOpe(@AuthenticationPrincipal Principal principal,
                                //@PathVariable("numReq") Integer numReq,
                                @RequestBody String reporteJson,
                                HttpServletResponse response)
    {
        // -- http://localhost:8090/reporte/remito/7488/imprimir
        //C:\Users\ecaceres\Documents\Jasper\rpt_ImpresionOperacion.jasper
        //C:\Users\ecaceres\Documents\Jasper\rpt_ImpresionOperacion.jrxml

        ReportDTO reportDTO = Commons.INSTANCE.getCommonGson().fromJson(reporteJson, ReportDTO.class);
        ConfigReport configReport = configReportRepository.findByReportName(reportDTO.getReportName());

        if (configReport != null){
            try{
                Connection connection          = jdbcTemplate.getDataSource().getConnection();
                JasperReport jasperReport      = JasperCompileManager.compileReport(configReport.getFileJrxml());
                Map<String, Object> parameters = new HashMap<>();

                configReport.getParams().forEach( param -> {
                    parameters.put(param.getParamName(), this.castVariable(reportDTO.getParams().get(param.getParamOrder()), param.getParamType()));
                });
             //   parameters.put("copia","ORIGINAL");
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
              //  parameters.put("copia","DUPLICADO");
              //  JasperPrint jasperPrint2 = JasperFillManager.fillReport(jasperReport, parameters, connection);

                //JRSaver.saveObject(jasperReport, "employeeReport.jasper");
               /* List pages = jasperPrint2 .getPages();
                for (int j = 0; j < pages.size(); j++) {
                    JRPrintPage object = (JRPrintPage)pages.get(j);
                    jasperPrint.addPage(object);
                }*/

                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=report.pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
                response.getOutputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Object castVariable(Object variable, String typeVariable){
        try {
            switch (typeVariable){
                case "java.lang.Integer":
                    if (variable instanceof Double)
                        return ((Double) variable).intValue();
                    else if (variable instanceof String)
                        return Integer.parseInt(variable.toString());
                    break;
                case "java.lang.String":
                    variable.toString();
                    break;
                case "java.lang.Double":
                    if (variable instanceof String)
                        return Double.parseDouble(variable.toString());
                    else if (variable instanceof Integer)
                        return (Double) variable;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void exportReport(JasperPrint jasperPrint, String pathFile){

        try {
            JRPdfExporter exporter = new JRPdfExporter();

            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("C:\\Users\\ecaceres\\Documents\\Jasper\\rpt_ImpresionOperacion.pdf"));

            SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
            reportConfig.setSizePageToContent(true);
            reportConfig.setForceLineBreakPolicy(false);

            SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
            exportConfig.setMetadataAuthor("ECaceres");
            exportConfig.setEncrypted(true);
            exportConfig.setAllowedPermissionsHint("PRINTING");

            exporter.setConfiguration(reportConfig);
            exporter.setConfiguration(exportConfig);

            exporter.exportReport();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @ResponseBody
    @RequestMapping(value = "/reportes/operacion_web/{id}/imprimir", method = RequestMethod.GET, produces = "application/pdf")
    public void imprimirOperacion(@AuthenticationPrincipal Principal principal, @PathVariable("id") Long id,
                                               HttpServletResponse response){
        try{
            Operaciones operacion = operacionesRepository.getOne(id);
            List<String> elementos_id= operacion.getRelacionOpEl().stream().map(RelacionOpEl::getElemento_id).collect(Collectors.toList());
            List<Elementos> elementosList = elementosRepository.findByElementosIds(elementos_id.stream().map(Long::parseLong).collect(Collectors.toList()));

            String requerimiento = operacion.getRequerimiento().getNumero();
            String tipoOperacion = operacion.getTipoOperaciones().getDescripcion();
            String numero =operacion.getId().toString();
            String deposito = operacion.getDepositos().getDescripcion();
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String fechaAlta = df.format(operacion.getFechaAlta());
            String fechaEntrega = operacion.getFechaEntregaString();
            String estado = operacion.getEstado();
            String cliente = operacion.getClienteEmp().getPersonasJuridicas().getRazonSocial();
            String solicitante = operacion.getRequerimiento().getEmpSolicitante().getPersonasFisicas().getNombreCompleto();
            String autorizante = operacion.getRequerimiento().getEmpAutorizante().getPersonasFisicas().getNombreCompleto();
            String observaciones = operacion.getRequerimiento().getObservaciones();
            String rearchivoDe = "";
            ArrayList<OperacionElementoReporte> opElementos=new ArrayList<>();
            for(Elementos elemento : elementosList){

                OperacionElementoReporte opElementoReporte=new OperacionElementoReporte();

                if(elemento.getId()!=null){
                    opElementoReporte.setTipoElemento("cajas");
                    opElementoReporte.setCodigo(elemento.getCodigo());
                    opElementoReporte.setDeposito(operacion.getDepositos().getDescripcion());
                    if (elemento.getPosicion() != null) {
                        opElementoReporte.setSeccion(elemento.getPosicion().getEstante_id().toString());
                        opElementoReporte.setModulo(elemento.getPosicion().getModulo_id().toString());
                        opElementoReporte.setPosicion(elemento.getPosicion().getEstado());
                    }
                    //opElementoReporte.setRearchivoDe(opElemento.getRearchivoDe().getCodigoAlternativo());
                    opElementoReporte.setCodigoBarras((elemento.getId().intValue()));

                    opElementoReporte.setOrigen("Lectura");
                    opElementoReporte.setEstado(elemento.getEstado().toString());
                    opElementos.add(opElementoReporte);
            }}
            JasperReport jasperReport = JasperCompileManager.compileReport("C:/Users/acorrea/Documents/Projects/BASA/basa-migracion/src/main/resources/jasper/reporteImpresionOperacion.jrxml");
            Map<String,Object> parametros=new HashMap<String,Object>();
            parametros.put("requerimiento", requerimiento);
            parametros.put("tipoOperacion", tipoOperacion);
            parametros.put("numero", numero);
            parametros.put("deposito", deposito);
            parametros.put("fechaAlta", fechaAlta);
            parametros.put("fechaEntrega", fechaEntrega);
            parametros.put("estado", estado);
            parametros.put("cliente", cliente);
            parametros.put("solicitante", solicitante);
            parametros.put("autorizante", autorizante);
            parametros.put("observaciones", observaciones);

            response.setContentType("application/pdf");
            //response.addHeader("Content-Disposition", "attachment; filename=lista_operaciones.pdf");

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(opElementos);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
            response.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @ResponseBody
    @RequestMapping(value = "/reportes/hoja_ruta/{numero}/impimir", method = RequestMethod.GET, produces = "application/pdf")
    public void imprimirHojaRuta(@AuthenticationPrincipal Principal principal, @PathVariable("numero") String numero,
                                  HttpServletResponse response) {

        try{
            List<String> idRelOpEl = hdRxOperacionRepository.findByNumero(numero);
            List<Long> rel = idRelOpEl.stream().map(Long::parseLong).collect(Collectors.toList());
            List<Operaciones> operacionesList = operacionesRepository.findByRelOpId(rel);

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            LocalDate date = LocalDate.now();
            java.sql.Date sqlDate = java.sql.Date.valueOf(date);
            String fechaHoraSalida = df.format(sqlDate);
            String nroSerieHr = numero;
            String responsable = "Propio";

            List<HojaRutaReporte> listOperaciones = new ArrayList<>();
            for(Operaciones operacion : operacionesList) {
                HojaRutaReporte hojaRutaReporte = new HojaRutaReporte();
                hojaRutaReporte.setSerie(operacion.getRequerimiento().getNumero());
                hojaRutaReporte.setFechaEntrega(operacion.getFechaEntregaString());
                hojaRutaReporte.setIdHojaRutaOpElemnt(operacion.getId());
                hojaRutaReporte.setTipoRequerimiento(operacion.getRequerimiento().getTipoRequerimiento().getDescripcion());
                hojaRutaReporte.setSolicitante(operacion.getRequerimiento().getEmpSolicitante().getPersonasFisicas().getNombreCompleto());
                hojaRutaReporte.setDireccionEntrega(operacion.getRequerimiento().getClientesDirecciones().getDirecciones().getDireccionCompleta());
                hojaRutaReporte.setCliente(operacion.getClienteEmp().getPersonasJuridicas().getRazonSocial());
                hojaRutaReporte.setObservaciones(operacion.getRequerimiento().getObservaciones());
                hojaRutaReporte.setCantidadElemento(operacion.getCantidadPendientes());

                listOperaciones.add(hojaRutaReporte);
            }

            JasperReport jasperReport = JasperCompileManager.compileReport("C:/Users/acorrea/Documents/Projects/BASA/basa-migracion/src/main/resources/jasper/informeHojaRuta.jrxml");
            Map<String,Object> parametros=new HashMap<String,Object>();
            parametros.put("fechaHoraSalida", fechaHoraSalida);
            parametros.put("nroSerieHr", nroSerieHr);
            parametros.put("responsable", responsable);
            response.setContentType("application/pdf");

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listOperaciones);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros,dataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
            response.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*@ResponseBody
    @RequestMapping(value = "/reportes/requerimientos/impimir", method = RequestMethod.GET, produces = "application/pdf")
    public void imprimirRequerimientos(@AuthenticationPrincipal Principal principal,
                                 HttpServletResponse response) {
        // Sort descending and Search the first #### elements
        Pageable topTen = new PageRequest(0, 5, new Sort(Sort.Direction.DESC, "id"));
//sets filter?s date by substracting {dia} from {today}
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1000);
        java.util.Date date = calendar.getTime();

        Page<Operaciones> operaciones = operacionesRepository.findByFilters((long)1,"1", "%%", "%%", date, topTen);



        try{

            Map<String,Object> parametros=new HashMap<String,Object>();
            JasperReport jasperReport = JasperCompileManager.compileReport("C:/Users/acorrea/Documents/Projects/BASA/basa-migracion/src/main/resources/jasper/reporteImpresionRequerimientos.jrxml");
            response.setContentType("application/pdf");
            List<RequerimientoReporte> req = new ArrayList<>();

            for(Operaciones operacion:operaciones.getContent()){
                RequerimientoReporte reqVo = new RequerimientoReporte();
                reqVo.setTipo(operacion.getRequerimiento().getTipoRequerimiento().getDescripcion());
                reqVo.setNumero(operacion.getRequerimiento().getNumero());
                reqVo.setCliente(operacion.getClienteEmp().getPersonasJuridicas().getRazonSocial());
                reqVo.setSolicitante(operacion.getRequerimiento().getEmpSolicitante().getPersonasFisicas().getNombreCompleto());
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                reqVo.setFechaAlta(df.format(operacion.getFechaAlta()));
                reqVo.setFechaEntrega(operacion.getFechaEntregaString());
                reqVo.setEstado(operacion.getEstado());
                req.add(reqVo);
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros,new JRBeanCollectionDataSource(req));
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
            response.getOutputStream().close();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @ResponseBody
    @RequestMapping(value = "/reportes/requerimientos_por_dia/{dia}", method = RequestMethod.GET)
    public List<String> getCurrentUser2(@AuthenticationPrincipal Principal principal, @PathVariable("dia") Integer dia) {

        Pageable topTen = new PageRequest(0, 5, new Sort(Sort.Direction.DESC, "id"));
        //sets filter?s date by substracting {dia} from {today}
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -dia);
        java.util.Date date = calendar.getTime();

        Page<Operaciones> reqid = operacionesRepository.findByFilters((long)1,"1", "%%", "%%", date, topTen);
        List<Requerimiento> idList = reqid.getContent().stream().map(Operaciones::getRequerimiento).collect(Collectors.toList());
        List<String> idList2 = idList.stream().map(Requerimiento::getNumero).collect(Collectors.toList());


        return idList2;
    }*/
}
