package edu.otib.lab_known_vuln.controller;

import org.apache.commons.fileupload.MultipartStream;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class FileController {

    private final String filename = "uploadedFile";

    @RequestMapping(method = RequestMethod.GET)
    public String main() {
        return "main";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String upload(HttpServletRequest request) {
        String boundaryHeader = "boundary=";
        int i = request.getContentType().indexOf(boundaryHeader) + boundaryHeader.length();
        String boundary = request.getContentType().substring(i);

        try {

            MultipartStream multipartStream = new MultipartStream(request.getInputStream(), boundary.getBytes());

            boolean nextPart = multipartStream.skipPreamble();
            while (nextPart) {
                String header = multipartStream.readHeaders();

                if (header.contains("filename")) {
                    try (OutputStream output = new FileOutputStream(filename)) {
                        multipartStream.readBodyData(output);
                        output.flush();
                    }
                }
                nextPart = multipartStream.readBoundary();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "main";
    }

    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public void getFile(HttpServletResponse response) throws IOException {
        if (new File(filename).exists()) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"recently_uploaded.file\"");
            InputStream is = new FileInputStream(filename);
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } else {
            response.sendRedirect("");
        }
    }
}
