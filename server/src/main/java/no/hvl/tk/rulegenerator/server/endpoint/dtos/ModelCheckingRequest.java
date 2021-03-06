package no.hvl.tk.rulegenerator.server.endpoint.dtos;

import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

public class ModelCheckingRequest {
    MultipartFile file;
    Set<ModelCheckingProperty> propertiesToBeChecked;

    public ModelCheckingRequest() {
        propertiesToBeChecked = new HashSet<>();
    }

    public MultipartFile getFile() {
        return file;
    }

    public Set<ModelCheckingProperty> getPropertiesToBeChecked() {
        return propertiesToBeChecked;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public void setPropertiesToBeChecked(Set<ModelCheckingProperty> propertiesToBeChecked) {
        this.propertiesToBeChecked = propertiesToBeChecked;
    }
}
