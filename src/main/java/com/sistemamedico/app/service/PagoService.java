package com.sistemamedico.app.service;

import com.sistemamedico.app.dto.PagoRequest;
import com.sistemamedico.app.dto.PagoResponse;
import com.sistemamedico.app.exception.RecursoNoEncontradoException;
import com.sistemamedico.app.model.*;
import com.sistemamedico.app.repository.*;
import com.sistemamedico.app.model.OrdenLaboratorio;
import com.sistemamedico.app.repository.OrdenLaboratorioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final CitaRepository citaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SucursalRepository sucursalRepository;
    private final NotificacionService notificacionService;
    private final OrdenLaboratorioRepository ordenLaboratorioRepository;

    public PagoService(PagoRepository pagoRepository,
                       CitaRepository citaRepository,
                       UsuarioRepository usuarioRepository,
                       SucursalRepository sucursalRepository,
                       NotificacionService notificacionService,
                       OrdenLaboratorioRepository ordenLaboratorioRepository) {
        this.pagoRepository = pagoRepository;
        this.citaRepository = citaRepository;
        this.usuarioRepository = usuarioRepository;
        this.sucursalRepository = sucursalRepository;
        this.notificacionService = notificacionService;
        this.ordenLaboratorioRepository = ordenLaboratorioRepository;
    }

    @Transactional
    public PagoResponse crear(PagoRequest request) {
        Pago.TipoOrigenPago tipoOrigen = Pago.TipoOrigenPago.valueOf(request.getTipoOrigen());
        Pago.MetodoPago metodoPago = Pago.MetodoPago.valueOf(request.getMetodoPago());

        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Sucursal no encontrada."));

        Usuario cajero = null;
        if (request.getCajeroId() != null) {
            cajero = usuarioRepository.findById(request.getCajeroId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Cajero no encontrado."));
        }

        Cita cita = null;
        OrdenLaboratorio orden = null;

        if (tipoOrigen == Pago.TipoOrigenPago.CITA) {
            cita = citaRepository.findById(request.getReferenciaId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Cita no encontrada."));
            if (cita.getEstado() == Cita.EstadoCita.PAGADA) {
                throw new IllegalArgumentException("Esta cita ya fue pagada.");
            }
        } else if (tipoOrigen == Pago.TipoOrigenPago.LABORATORIO) {
            orden = ordenLaboratorioRepository.findById(request.getReferenciaId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Orden de laboratorio no encontrada."));
            if (orden.getEstado() != OrdenLaboratorio.EstadoOrden.PENDIENTE) {
                throw new IllegalArgumentException("Esta orden ya fue pagada o no está pendiente de pago.");
            }
        }

        Pago pago = new Pago();
        pago.setTipoOrigen(tipoOrigen);
        pago.setReferenciaId(request.getReferenciaId());
        pago.setMonto(request.getMonto());
        pago.setMetodoPago(metodoPago);
        pago.setSucursal(sucursal);
        pago.setCajero(cajero);
        pago.setFechaHora(LocalDateTime.now());
        pago.setNumeroTransaccion("TXN-" + UUID.randomUUID());

        if (metodoPago == Pago.MetodoPago.EFECTIVO) {
            if (request.getMontoRecibido() == null || request.getMontoRecibido().compareTo(request.getMonto()) < 0) {
                throw new IllegalArgumentException("El monto recibido debe ser mayor o igual al monto a pagar.");
            }
            pago.setMontoRecibido(request.getMontoRecibido());
            pago.setCambio(request.getMontoRecibido().subtract(request.getMonto()));
        } else {
            if (request.getUltimosDigitosTarjeta() == null || request.getUltimosDigitosTarjeta().length() != 4) {
                throw new IllegalArgumentException("Debe indicar los últimos 4 dígitos de la tarjeta.");
            }
            pago.setUltimosDigitosTarjeta(request.getUltimosDigitosTarjeta());
        }

        Pago guardado = pagoRepository.save(pago);

        if (cita != null) {
            cita.setEstado(Cita.EstadoCita.PAGADA);
            citaRepository.save(cita);

            notificacionService.enviar(
                    "Comprobante de Pago",
                    cita.getPaciente().getCorreo(),
                    "Comprobante de pago - Sistema Medico",
                    "Hola " + cita.getPaciente().getNombreCompleto() + ",\n\n" +
                            "Hemos recibido tu pago exitosamente.\n" +
                            "Monto: Q" + request.getMonto() + "\n" +
                            "Metodo de pago: " + metodoPago + "\n" +
                            "Numero de transaccion: " + guardado.getNumeroTransaccion() + "\n\n" +
                            "Sistema Medico 2026"
            );
        } else if (orden != null) {
            orden.setEstado(OrdenLaboratorio.EstadoOrden.EN_PROCESO);
            ordenLaboratorioRepository.save(orden);

            notificacionService.enviar(
                    "Comprobante de Pago",
                    orden.getPaciente().getCorreo(),
                    "Comprobante de pago de laboratorio - Sistema Medico",
                    "Hola " + orden.getPaciente().getNombreCompleto() + ",\n\n" +
                            "Hemos recibido el pago de tu orden de laboratorio.\n" +
                            "Monto: Q" + request.getMonto() + "\n" +
                            "Metodo de pago: " + metodoPago + "\n" +
                            "Numero de transaccion: " + guardado.getNumeroTransaccion() + "\n\n" +
                            "Tu orden ahora está en proceso.\n\n" +
                            "Sistema Medico 2026"
            );
        }

        return mapearAResponse(guardado);
    }

    public PagoResponse buscarPorId(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pago no encontrado."));
        return mapearAResponse(pago);
    }

    private PagoResponse mapearAResponse(Pago pago) {
        PagoResponse dto = new PagoResponse();
        dto.setId(pago.getId());
        dto.setTipoOrigen(pago.getTipoOrigen().name());
        dto.setReferenciaId(pago.getReferenciaId());
        dto.setMonto(pago.getMonto());
        dto.setMetodoPago(pago.getMetodoPago().name());
        dto.setMontoRecibido(pago.getMontoRecibido());
        dto.setCambio(pago.getCambio());
        dto.setNumeroTransaccion(pago.getNumeroTransaccion());
        dto.setCajeroNombre(pago.getCajero() != null ? pago.getCajero().getNombreCompleto() : "Pago en línea");
        dto.setSucursalNombre(pago.getSucursal().getNombre());
        dto.setFechaHora(pago.getFechaHora());
        return dto;
    }
}