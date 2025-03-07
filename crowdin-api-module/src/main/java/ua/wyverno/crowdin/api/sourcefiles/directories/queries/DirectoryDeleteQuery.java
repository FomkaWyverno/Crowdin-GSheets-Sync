package ua.wyverno.crowdin.api.sourcefiles.directories.queries;

import com.crowdin.client.core.http.exceptions.HttpException;
import com.crowdin.client.sourcefiles.SourceFilesApi;
import ua.wyverno.crowdin.api.Query;

public class DirectoryDeleteQuery implements Query<Boolean> {
    private final SourceFilesApi sourceFilesApi;
    private final long projectID;
    private long directoryID;

    public DirectoryDeleteQuery(SourceFilesApi sourceFilesApi, long projectID) {
        this.sourceFilesApi = sourceFilesApi;
        this.projectID = projectID;
    }

    /**
     * @param directoryID айді директорії яку потрібно видалити
     * @return {@link DirectoryDeleteQuery}
     */
    public DirectoryDeleteQuery directoryID(long directoryID) {
        this.directoryID = directoryID;
        return this;
    }
    /**
     * Виконує запит до Crowdin API - Delete Directories
     * @return Якщо успішно видалено директорію поверне true, якщо директорію не було знайдено - false
     */
    @Override
    public Boolean execute() {
        try {
            this.sourceFilesApi.deleteDirectory(this.projectID, this.directoryID);
            return true;
        } catch (HttpException e) {
            if (e.getError().getCode().equals("404") && e.getError().getMessage().equals("Directory Not Found")) return false;
            throw e;
        }
    }
}
