package com.system.roll.oss;

import com.aliyun.oss.model.OSSObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@SuppressWarnings("all")
public class OssResource implements Resource {

    private OSSObject ossObject;


    @Override
    public boolean exists() {
        return ossObject!=null;
    }

    @Override
    public URL getURL() throws IOException {
        return null;
    }

    @Override
    public URI getURI() throws IOException {
        return null;
    }

    @Override
    public File getFile() throws IOException {
        return null;
    }

    @Override
    public long contentLength() throws IOException {
        return ossObject.getObjectMetadata().getContentLength();
    }

    @Override
    public long lastModified() throws IOException {
        return ossObject.getObjectMetadata().getLastModified().getTime();
    }

    @Override
    public Resource createRelative(String s) throws IOException {
        return null;
    }

    @Override
    public String getFilename() {
        return ossObject.getKey();
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return ossObject.getObjectContent();
    }
    public String getContentType(){
        return ossObject.getObjectMetadata().getContentType();
    }
}
