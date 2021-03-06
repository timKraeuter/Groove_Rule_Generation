package no.hvl.tk.rulegenerator.server;

import com.google.common.collect.Sets;
import no.hvl.tk.rulegenerator.server.endpoint.RuleGeneratorController;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RuleGeneratorControllerTests {
    public static final String BPMN_FILE = "/ruleGeneratorController/name-with-numbers.bpmn";
    @LocalServerPort
    private int port;

    @Autowired
    private RuleGeneratorController restController;

    @Test
    void testContextLoads() {
        assertNotNull(restController);
    }

    @Test
    void testGenerateGGAndZip() throws Exception {
        String URL = String.format("http://localhost:%s/%s", port, "generateGGAndZip");

        @SuppressWarnings("ConstantConditions") File bpmnModelFile =
                new File(this.getClass().getResource(BPMN_FILE).getFile());
        assertNotNull(bpmnModelFile);

        CloseableHttpResponse response;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Build request
            HttpPost uploadFile = new HttpPost(URL);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody(
                    "file",
                    new FileInputStream(bpmnModelFile),
                    ContentType.APPLICATION_OCTET_STREAM,
                    bpmnModelFile.getName()
            );
            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);

            response = httpClient.execute(uploadFile);

            // Check response
            HttpEntity responseEntity = response.getEntity();
            ZipInputStream zis = new ZipInputStream(responseEntity.getContent());
            Set<String> zipEntryNames = this.getZipEntryNames(zis);
            // We only check file names not content since the content contains timestamps etc.
            assertThat(zipEntryNames,
                       is(Sets.newHashSet("_1.gpr",
                                          "_2__345_end.gpr",
                                          "_2__345_start.gpr",
                                          "_3.gpr",
                                          "bpmn_e_model.gty",
                                          "AllTerminated.gpr",
                                          "start.gst",
                                          "system.properties",
                                          "Terminate.gpr",
                                          "Unsafe.gpr")));
        }
    }

    private Set<String> getZipEntryNames(ZipInputStream zis) throws IOException {
        Set<String> fileNames = new HashSet<>();
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            fileNames.add(entry.getName());
        }
        return fileNames;
    }

    @Test
    void testCheckBPMNSpecificProperties() throws Exception {
        String URL = String.format("http://localhost:%s/%s", port, "checkBPMNSpecificProperties");

        @SuppressWarnings("ConstantConditions") File bpmnModelFile =
                new File(this.getClass().getResource(BPMN_FILE).getFile());
        assertNotNull(bpmnModelFile);

        CloseableHttpResponse response;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Build request
            HttpPost uploadFile = new HttpPost(URL);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("propertiesToBeChecked[]", "NO_DEAD_ACTIVITIES");
            builder.addBinaryBody(
                    "file",
                    new FileInputStream(bpmnModelFile),
                    ContentType.APPLICATION_OCTET_STREAM,
                    bpmnModelFile.getName()
            );
            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);

            response = httpClient.execute(uploadFile);

            // Check response
            HttpEntity responseEntity = response.getEntity();
            String responseAsString = IOUtils.toString(responseEntity.getContent(), Charset.defaultCharset());
            assertThat(responseAsString, is("{\"propertyCheckingResults\":[{\"name\":\"No dead activities\"," +
                                            "\"holds\":true,\"additionalInfo\":\"\"}]}"));
        }

    }

}
