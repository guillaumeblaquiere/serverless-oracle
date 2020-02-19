package dev.gblaquiere.serverlessoracle.java;

import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

public class Helpers {

    public static HttpResponse createHttpResponse(HttpServletResponse response) {
        return new HttpResponse() {
            @Override
            public void setStatusCode(int i) {
                response.setStatus(i);
            }

            @Override
            public void setStatusCode(int i, String s) {
                response.setStatus(i);
                try {
                    response.getWriter().println(s);
                } catch (IOException e) {
                    //FIXME handle correctly the error
                    e.printStackTrace();
                }
            }

            @Override
            public void setContentType(String s) {
                response.setContentType(s);
            }

            @Override
            public Optional<String> getContentType() {
                return Optional.of(response.getContentType());
            }

            @Override
            public void appendHeader(String k, String v) {
                if(response.containsHeader(k)){
                    response.getHeaders(k).add(v);
                } else {
                    response.addHeader(k,v);
                }
            }

            @Override
            public Map<String, List<String>> getHeaders() {
                var m = new HashMap<String,List<String>>();
                response.getHeaderNames().forEach(s ->
                        m.put(s,(List)response.getHeaders(s)));
                return m;
            }

            @Override
            public OutputStream getOutputStream() throws IOException {
                return response.getOutputStream();
            }

            //FIXME usage of the write write nothing in the output stream of the servlet response
            BufferedWriter writer = null;
            @Override
            public BufferedWriter getWriter() throws IOException {
                if (writer == null)
                    writer = new BufferedWriter(new OutputStreamWriter(getOutputStream()));
                return writer;
            }
        };
    }

    public static HttpRequest createHttpRequest(HttpServletRequest request) {
        return new HttpRequest() {
            @Override
            public String getMethod() {
                return request.getMethod();
            }

            @Override
            public String getUri() {
                return request.getRequestURI();
            }

            @Override
            public String getPath() {
                return request.getPathInfo();
            }

            @Override
            public Optional<String> getQuery() {
                return Optional.of(request.getQueryString());
            }

            @Override
            public Map<String, List<String>> getQueryParameters() {
                var m = new HashMap<String, List<String>>();
                request.getParameterMap().forEach((k, v) -> m.put(k, Arrays.asList(v)));
                return m;
            }

            @Override
            public Map<String, HttpPart> getParts() {
                var m = new HashMap<String,HttpPart>();
                try {
                    request.getParts().forEach(part -> m.put(part.getName(), new HttpPart() {
                                @Override
                                public Optional<String> getFileName() {
                                    return Optional.of(part.getSubmittedFileName());
                                }

                                @Override
                                public Optional<String> getContentType() {
                                    return Optional.of(part.getContentType());
                                }

                                @Override
                                public long getContentLength() {
                                    return part.getSize();
                                }

                                @Override
                                public Optional<String> getCharacterEncoding() {
                                    //FIXME in the headers??
                                    return Optional.empty();
                                }

                                @Override
                                public InputStream getInputStream() throws IOException {
                                    return part.getInputStream();
                                }

                                @Override
                                public BufferedReader getReader() throws IOException {
                                    return new BufferedReader(new InputStreamReader(part.getInputStream()));
                                }

                                @Override
                                public Map<String, List<String>> getHeaders() {
                                    var m = new HashMap<String,List<String>>();
                                    part.getHeaderNames().forEach(s ->
                                            m.put(s,(List)part.getHeaders(s)));
                                    return m;
                                }
                            })
                    );
                } catch (IOException e) {
                    //FIXME handle correctly the error
                    e.printStackTrace();
                } catch (ServletException e) {
                    //FIXME handle correctly the error
                    e.printStackTrace();
                }
                return m;
            }

            @Override
            public Optional<String> getContentType() {
                return Optional.of(request.getContentType());
            }

            @Override
            public long getContentLength() {
                return request.getContentLength();
            }

            @Override
            public Optional<String> getCharacterEncoding() {
                return Optional.of(request.getCharacterEncoding());
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return request.getInputStream();
            }

            @Override
            public BufferedReader getReader() throws IOException {
                return request.getReader();
            }

            @Override
            public Map<String, List<String>> getHeaders() {
                var m = new HashMap<String,List<String>>();
                Collections.list(request.getHeaderNames()).forEach(s ->
                        m.put(s,Collections.list(request.getHeaders(s))));
                return m;
            }
        };
    }

}
