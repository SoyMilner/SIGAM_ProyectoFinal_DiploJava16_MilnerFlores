package dgtic.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReporteFormDTO {
    // Datos del período académico
    private String nombrePeriodo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String descripcion;

    // Lista de ponderaciones asociadas a cada tipo de trabajo
    private List<TipoTrabajoPonderacionDTO> ponderaciones;

}

