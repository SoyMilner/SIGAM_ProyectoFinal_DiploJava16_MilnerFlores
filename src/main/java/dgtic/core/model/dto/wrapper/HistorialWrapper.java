package dgtic.core.model.dto.wrapper;

import dgtic.core.model.HistorialCalificaciones;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistorialWrapper {
    private List<HistorialCalificaciones> historiales;
}

