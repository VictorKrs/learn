package liga.tasks.ru.learn.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ExecutorServiceService {

    private final ExecutorService executorService;

    @SneakyThrows
    public String runInvokeAny() {
        List<Callable<String>> tasks = new ArrayList<>();

        for(long i = 1L; i <= 1000; i++) {
            final long id = i;
            tasks.add(() -> workTask(id));
        }

        return executorService.invokeAny(tasks);
    }

    @SneakyThrows
    private String workTask(long taskId) {
        log.info("Run task: {}; ThreadId: {}", taskId, Thread.currentThread().getId());
        Random random = new Random();

        TimeUnit.MILLISECONDS.sleep(random.nextLong(15000));

        log.info("End task: {}; ThreadId: {}", taskId, Thread.currentThread().getId());

        return "taskId: " + taskId;
    }
}
