package dgtic.core.model.dto.wrapper;

import dgtic.core.model.dto.CalificacionTrabajoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CalificacionesWrapper {
    private List<CalificacionTrabajoDTO> calificaciones;
}
