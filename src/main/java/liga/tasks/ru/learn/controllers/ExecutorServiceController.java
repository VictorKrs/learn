package liga.tasks.ru.learn.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import liga.tasks.ru.learn.services.ExecutorServiceService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/executor")
@RequiredArgsConstructor
@Tag(name = "Параллельная обработка", description = "API для запуска ExecutorService")
public class ExecutorServiceController {

    private final ExecutorServiceService service;

    @GetMapping("/any")
    @Operation(summary = "InvokeAny", description = "Запуск InvokeAny")
    public String invokeAny() {
        return service.runInvokeAny();
    }

}
