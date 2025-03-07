package ua.wyverno.crowdin.managers;

import com.crowdin.client.sourcefiles.model.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ua.wyverno.config.ConfigLoader;
import ua.wyverno.crowdin.CrowdinService;
import ua.wyverno.crowdin.api.sourcefiles.directories.queries.DirectoryEditQuery;
import ua.wyverno.crowdin.api.sourcefiles.directories.queries.edit.PatchDirRequestBuilder;
import ua.wyverno.utils.json.JSONCreator;

import java.util.List;
import java.util.Objects;

@Component
public class CrowdinDirectoryManager {
    private final static Logger logger = LoggerFactory.getLogger(CrowdinDirectoryManager.class);

    private final CrowdinService crowdinService;
    private final long projectId;

    private final JSONCreator jsonCreator;

    @Autowired
    public CrowdinDirectoryManager(CrowdinService crowdinService, ConfigLoader configLoader, JSONCreator jsonCreator) {
        this.crowdinService = crowdinService;
        this.projectId = configLoader.getCoreConfig().getProjectID();
        this.jsonCreator = jsonCreator;
    }

    /**
     * Збирає лист з усіма директоріями у проєкті
     * @return Лист з усіма директоріями у проєкті
     */
    public List<Directory> getAllDirectories() {
        return this.crowdinService.directories()
                .list(this.projectId)
                .execute();
    }

    /**
     * Створює директорію у Кроудіні
     * @param directoryId айді директорії де має бути розташована директорія, якщо це коренева директорія має бути null
     * @param directoryName ім'я директорії
     * @param directoryTitle заголовок директорії може бути null, щоб не встановлювати загаловок
     * @return {@link Directory} створена директорія на Кроудіні
     */
    public Directory createDirectory(@Nullable Long directoryId, String directoryName, @Nullable String directoryTitle) {
        Objects.requireNonNull(directoryName, "Directory name can't be null!");
        logger.debug("Creating directories: {}, Title: {}, in directoryId: {}", directoryName, directoryTitle, directoryId);
        return this.crowdinService.directories() // Створюємо директорію у Кроудіні
                .createDirectory(this.projectId)
                .directoryID(directoryId)
                .name(directoryName)
                .title(directoryTitle)
                .execute();
    }

    public Directory editDirectory(Long directoryId, List<PatchDirRequestBuilder> patchDirRequests) {
        logger.debug("Edit directory with request: {}", this.jsonCreator.toJSON(patchDirRequests));

        DirectoryEditQuery query = this.crowdinService.directories()
                .editDirectory(this.projectId)
                .directoryID(directoryId);
        patchDirRequests.forEach(query::addPatchRequest);
        return query.execute();
    }

    public boolean deleteDirectory(Directory directory) {
        return this.crowdinService.directories()
                .deleteDirectory(this.projectId)
                .directoryID(directory.getId())
                .execute();
    }
}
