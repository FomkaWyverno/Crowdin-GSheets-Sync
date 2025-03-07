package ua.wyverno.crowdin.api.stringtranslation.queries;

import com.crowdin.client.core.http.HttpClient;
import com.crowdin.client.core.http.HttpRequestConfig;
import ua.wyverno.crowdin.api.Query;

import java.util.Map;
import java.util.Optional;

public class StringTranslationRemoveApprovalsByStringIDQuery implements Query<Void> {
    private final HttpClient crowdinHttpClient;
    private final String crowdinBaseApiURL;
    private final long projectID;
    private Integer stringId;

    public StringTranslationRemoveApprovalsByStringIDQuery(HttpClient crowdinHttpClient, String crowdinBaseApiURL, long projectID) {
        this.crowdinHttpClient = crowdinHttpClient;
        this.crowdinBaseApiURL = crowdinBaseApiURL;
        this.projectID = projectID;
    }

    /**
     * @param stringId Example: stringId=2<br/>
     * String Identifier. Get via <a href="https://support.crowdin.com/developer/api/v2/#operation/api.projects.strings.getMany">List Strings</a><br/>
     *                 Айді вихідного рядка (Source String)
     * @return {@link StringTranslationRemoveApprovalsByStringIDQuery}
     */
    public StringTranslationRemoveApprovalsByStringIDQuery stringId(Integer stringId) {
        this.stringId = stringId;
        return this;
    }

    @Override
    public Void execute() {
        String buildURL = String.format("%s/projects/%s/approvals", this.crowdinBaseApiURL, this.projectID);
        Map<String, Optional<Object>> queryParams = HttpRequestConfig.buildUrlParams(
                "stringId", Optional.ofNullable(this.stringId));

        this.crowdinHttpClient.delete(buildURL, new HttpRequestConfig(queryParams), Void.class);
        return null;
    }
}
