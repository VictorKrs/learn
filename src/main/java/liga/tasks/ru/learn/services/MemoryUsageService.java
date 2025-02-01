package liga.tasks.ru.learn.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import org.springframework.stereotype.Service;

import liga.tasks.ru.learn.dto.memory.MemoryTestInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemoryUsageService {

    private final ExecutorService executorService;

    private boolean isStarted = false;

    public void generatedMapsAsync(MemoryTestInfo memoryTestInfo) {
        executorService.submit(() -> generateMaps(memoryTestInfo));
    }

    public void generateMaps(MemoryTestInfo memoryTestInfo) {
        isStarted = true;
        for(long i = 0; i < memoryTestInfo.getMapsCount() && isStarted; i++) {
            generateMap(memoryTestInfo);
            if ((i + 1) % 100 == 0) {
                log.info("Сгенерировано словарей {} из {}", i, memoryTestInfo.getMapsCount());
            }
        }

        log.info("Генерация закончена");
    }

    public void stop() {
        isStarted = false;
    }

    private void generateMap(MemoryTestInfo memoryTestInfo) {
        Map<String, Object> obj = new HashMap<>();

        for(long i = 0; i < memoryTestInfo.getObjectsCount() && isStarted; i++) {
            obj.put("obj_" + i, generateObj(memoryTestInfo));
            if ((i + 1) % 100 == 0) {
                log.info("Сгенерировано объектов в словаре {} из {}", i,  memoryTestInfo.getObjectsCount());
            }
        }
    }
            
    private Object generateObj(MemoryTestInfo memoryTestInfo) {
        Map<String, Object> obj = new HashMap<>();

        for (long i = 0; i < memoryTestInfo.getFieldsCount() && isStarted; i++) {
            obj.put("field_" + i, generateFieldVal(memoryTestInfo));
        }

        return obj;
    }
            
    private Object generateFieldVal(MemoryTestInfo memoryTestInfo) {
        List<String> res = new ArrayList<>();

        for(long i = 0; i < memoryTestInfo.getListLength() && isStarted; i++) {
            res.add(UUID.randomUUID().toString());
        }

        return res;
    }
}
