package com.sistemamedico.app.repository;

import com.sistemamedico.app.model.Cita;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByPacienteDpi(String dpi);
    List<Cita> findByMedicoIdAndEstado(Long medicoId, Cita.EstadoCita estado);
    List<Cita> findByEstado(Cita.EstadoCita estado);

    // Nuevo método para buscar citas de un médico en un rango de fechas (un día)
    List<Cita> findByMedicoIdAndFechaHoraBetween(Long medicoId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    // Nuevo método para buscar la cola de enfermería
    @EntityGraph(attributePaths = {"paciente", "medico", "especialidad", "sucursal"})
    List<Cita> findByEstadoInAndFechaHoraBetweenOrderByFechaHoraAsc(List<Cita.EstadoCita> estados, java.time.LocalDateTime inicio, java.time.LocalDateTime fin);

    // Nuevo método para buscar citas por DPI y una lista de estados, ordenadas por ID ascendente
    @EntityGraph(attributePaths = {"paciente", "medico", "especialidad", "sucursal"})
    List<Cita> findByPacienteDpiAndEstadoInOrderByIdAsc(String dpi, List<Cita.EstadoCita> estados);

    // Nuevo método para buscar citas por DPI y una lista de estados, ordenadas por ID descendente
    @EntityGraph(attributePaths = {"paciente", "medico", "especialidad", "sucursal"})
    List<Cita> findByPacienteDpiAndEstadoInOrderByIdDesc(String dpi, List<Cita.EstadoCita> estados);

    // Nuevo método para buscar citas de un médico en un rango de fechas, excluyendo una cita específica
    List<Cita> findByMedicoIdAndFechaHoraBetweenAndIdNot(Long medicoId, LocalDateTime startOfDay, LocalDateTime endOfDay, Long id);

    // Nuevo método para buscar citas por DPI y una lista de estados, ordenadas por fecha y hora ascendente
    @EntityGraph(attributePaths = {"paciente", "medico", "especialidad", "sucursal"})
    List<Cita> findByPacienteDpiAndEstadoInOrderByFechaHoraAsc(String dpi, List<Cita.EstadoCita> estados);

    // Método para listar todas las citas ordenadas por ID descendente
    List<Cita> findAllByOrderByIdDesc();

    // Método para buscar citas por estado y fechaHora de la cita anterior a un punto en el tiempo
    @EntityGraph(attributePaths = {"paciente", "medico", "especialidad", "sucursal"})
    List<Cita> findByEstadoAndFechaHoraBefore(Cita.EstadoCita estado, LocalDateTime fechaHora);

    // Nuevo método para buscar citas por estado y reservaExpiraEn anterior a un punto en el tiempo
    @EntityGraph(attributePaths = {"paciente", "medico", "especialidad", "sucursal"})
    List<Cita> findByEstadoAndReservaExpiraEnBefore(Cita.EstadoCita estado, LocalDateTime reservaExpiraEn);
}