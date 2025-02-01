package liga.tasks.ru.learn.dto.memory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "Настройки для тестирования GC")
@Data
@AllArgsConstructor
public class MemoryTestInfo {
    @Positive
    @Schema(description = "Количество словарей", example = "100")
    private Long mapsCount;
    @Positive
    @Schema(description = "Количество объектов в словаре", example = "1000")
    private Long objectsCount;
    @Positive
    @Schema(description = "Количество списков в объекте", example = "100")
    private Long fieldsCount;
    @Positive
    @Schema(description = "Количество элементов в списке; Каждый элемент является GUID строкой", example = "1000")
    private Long listLength;
}
