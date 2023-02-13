package apple.discord.clover.util;


import apple.utilities.threading.service.queue.TaskHandlerQueue;

public class FileIOService {

    private final static TaskHandlerQueue instance = new TaskHandlerQueue(10, 0, 0);

    public static TaskHandlerQueue get() {
        return instance;
    }
}
