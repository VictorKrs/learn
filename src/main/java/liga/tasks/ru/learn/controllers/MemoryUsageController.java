package liga.tasks.ru.learn.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import liga.tasks.ru.learn.dto.memory.MemoryTestInfo;
import liga.tasks.ru.learn.services.MemoryUsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/memory")
@RequiredArgsConstructor
@Tag(name = "Тестирование работы GC", description = "API для тестирования работы GC")
public class MemoryUsageController {

    private final MemoryUsageService memoryUsageService;

    @PostMapping("/start")
    public MemoryTestInfo postMethodName(@RequestBody MemoryTestInfo memoryTestInfo) {
        memoryUsageService.generatedMapsAsync(memoryTestInfo);
        
        return memoryTestInfo;
    }

    @PostMapping("stop")
    public String stop() {
        memoryUsageService.stop();
        
        return "Stopped";
    }
    
    
}
